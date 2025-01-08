package com.haot.gateway.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserValidationResponse {
  @JsonProperty("statusCode")
  private String statusCode;

  @JsonProperty("status")
  private String status;

  @JsonProperty("message")
  private String message;

  @JsonProperty("data")
  private Data data;

  @Getter
  public static class Data {
    @JsonProperty("isValid")
    private boolean isValid;
  }
}
