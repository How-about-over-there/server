package com.haot.gateway.filter;

import com.haot.gateway.client.UserWebClient;
import com.haot.gateway.data.Role;
import com.haot.gateway.exception.ErrorCode;
import com.haot.gateway.exception.GatewayException;
import com.haot.gateway.util.JwtUtils;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

  private final JwtUtils jwtUtils;
  private final UserWebClient userWebClient;
  private final StringRedisTemplate stringRedisTemplate;

  @Value("${filter.bypass.exact-paths}")
  private List<String> exactPaths;

  @Value("${filter.bypass.prefix-paths}")
  private List<String> prefixPaths;
  /*
    1. 토큰에 대한 유효성 검증
    2. 유저 ID 의 유효성 검증
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    log.info("Request Headers: {}", exchange.getRequest().getHeaders());
    log.info("Request Path: {}", path);

    if (prefixPaths.stream().anyMatch(path::startsWith)) {
      log.info("Bypassing authentication for prefixPaths path: {}", path);
      return chain.filter(exchange);
    }

    if (exactPaths.stream().anyMatch(path::equals)) {
      log.info("Bypassing authentication for exactPaths path: {}", path);
      return chain.filter(exchange);
    }

    // JWT 토큰 추출
    String token = jwtUtils.substringToken(
        exchange.getRequest().getHeaders().getFirst(JwtUtils.AUTHORIZATION_HEADER));

    // 토큰이 유효하지 않은 경우 처리
    if (token == null || !jwtUtils.validateToken(token)) {
      throw new GatewayException(ErrorCode.TOKEN_NOT_FOUND_EXCEPTION);
    }

    // 토큰에서 사용자 정보 추출
    Claims userInfoFromToken = jwtUtils.getUserInfoFromToken(token);
    Role role = Role.valueOf((String) userInfoFromToken.get(JwtUtils.AUTHORIZATION_KEY));
    String userId = (String) userInfoFromToken.get(JwtUtils.ID_KEY);

    boolean isMember = Boolean.TRUE.equals(stringRedisTemplate.hasKey("auth:user:valid_id:" + userId));

    if (isMember) {
      // 검증 성공 시
      ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
          .header("Authorization", token)
          .header("X-User-Id", userId)
          .header("X-User-Role", role.name())
          .build();
      return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    // 유저에 ID에 대한 검증
    return userWebClient.validateUser(userId)
        .then(Mono.defer(() -> {
          // 검증 성공 시
          ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
              .header("Authorization", token)
              .header("X-User-Id", userId)
              .header("X-User-Role", role.name())
              .build();
          return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }))
        .onErrorResume(e -> {
          // 검증 실패 시
          throw new GatewayException(ErrorCode.USER_AUTHENTICATION_EXCEPTION);
        });
  }
}
