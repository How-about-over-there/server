package com.haot.reservation.common.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;

public class FeignExceptionUtils {

  public static FeignClientException parseFeignException(FeignException e)
      throws JsonProcessingException {

    String responseBody = e.contentUTF8();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(responseBody);

    String statusCode = jsonNode.get("statusCode").asText();
    String status = jsonNode.get("status").asText();
    String message = jsonNode.get("message").asText();

    return new FeignClientException(statusCode, status, message);
  }
}
