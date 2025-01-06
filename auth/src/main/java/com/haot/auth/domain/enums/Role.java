package com.haot.auth.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
  ADMIN("관리자"),
  HOST("숙소 관리자"),
  USER("일반 사용자");

  private final String description;
}
