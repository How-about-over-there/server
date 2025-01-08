package com.haot.gateway.client;

import com.haot.gateway.data.UserValidationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserWebClient {

  private final WebClient webClient;

  public UserWebClient(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://user-service/api/v1/users").build();
  }

  public Mono<Void> validateUser(String userIdToValidate) {
    return webClient.post()
        .uri("/{userId}/validation", userIdToValidate)
        .retrieve()
        .bodyToMono(UserValidationResponse.class)
        .flatMap(res -> {
          if (!res.getStatus().equals("ERROR") && res.getData().isValid()) {
            return Mono.empty();
          } else {
            return Mono.error(new RuntimeException("User validation failed")); // 실패 시 예외 처리
          }
        });
  }

}
