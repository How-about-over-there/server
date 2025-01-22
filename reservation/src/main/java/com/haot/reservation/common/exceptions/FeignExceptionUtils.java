package com.haot.reservation.common.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import java.io.IOException;

public class FeignExceptionUtils {

  public static FeignClientException parseFeignException(Exception e) {
    if (e instanceof FeignException) {
      return handleFeignException((FeignException) e);
    } else {
      return new FeignClientException("7202", "ERROR", "예기치 않은 오류 발생: " + e.getMessage());
    }
  }

  private static FeignClientException handleFeignException(FeignException e) {
    String responseBody = e.contentUTF8();
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(responseBody);
      String message = jsonNode.get("message").asText();
      return new FeignClientException("7200", "ERROR", message);
    } catch (IOException ex) {
      return new FeignClientException("7201", "ERROR", responseBody);
    }
  }
}
