package com.haot.reservation.common.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.reservation.common.response.enums.ErrorCode;
import feign.FeignException;
import java.io.IOException;

public class FeignExceptionUtils {

  public static FeignClientException convertToFeignClientException(Exception e) {
    if (e instanceof FeignException) {
      return extractErrorMessageFromFeignException((FeignException) e);
    } else {
      return new FeignClientException(ErrorCode.UNEXPECTED_ERROR, e.getMessage());
    }
  }

  private static FeignClientException extractErrorMessageFromFeignException(FeignException e) {
    String responseBody = e.contentUTF8();
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(responseBody);
      String message = jsonNode.get("message").asText();
      return new FeignClientException(ErrorCode.FEIGN_CLIENT_ERROR, message);
    } catch (IOException ex) {
      return new FeignClientException(ErrorCode.GATEWAY_ERROR, responseBody);
    }
  }
}
