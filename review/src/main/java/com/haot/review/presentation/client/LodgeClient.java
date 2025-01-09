package com.haot.review.presentation.client;

import com.haot.review.common.response.ApiResponse;
import com.haot.review.presentation.dto.LodgeReadOneResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "lodge-service")
public interface LodgeClient {

  @GetMapping("/api/v1/lodges/{lodgeId}")
  public ApiResponse<LodgeReadOneResponse> readOne(@PathVariable String lodgeId);

}
