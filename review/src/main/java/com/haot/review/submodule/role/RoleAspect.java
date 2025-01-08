package com.haot.review.submodule.role;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAspect {

  @Around("@annotation(roleCheck)")
  public Object checkRole(ProceedingJoinPoint joinPoint, RoleCheck roleCheck) throws Throwable{
    // HttpServletRequest 가져오기
    HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
        RequestContextHolder.getRequestAttributes())).getRequest();

    // User Role 헤더에서 Role 가져오기
    String headerRole = request.getHeader("X-User-Role");

    // 헤더에 값이 없다면 예외 처리
    if (headerRole == null || headerRole.isEmpty()) {
      throw new RuntimeException("권한이 존재하지 않습니다.");
    }

    // 메소드에서 허용된 권한들을 가져오기
    Set<String> requiredRoles = Arrays.stream(roleCheck.value())
        .map(Enum::name)
        .collect(Collectors.toSet());
    // 유저가 메소드에서 허용된 권한을 가지고 있지 않다면 예외처리
    if (!requiredRoles.contains(headerRole)) {
      throw new RuntimeException("유저가 필요한 권한을 가지고 있지 않습니다.");
    }

    // 메소드 실행
    return joinPoint.proceed();
  }
}
