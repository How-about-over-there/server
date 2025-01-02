package com.haot.user.domain.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Gender {
  MALE("남성"),
  FEMALE("여성");

  private final String description;
}
