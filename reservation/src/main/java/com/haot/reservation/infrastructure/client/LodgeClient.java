package com.haot.reservation.infrastructure.client;

import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.common.response.SliceResponse;
import com.haot.reservation.infrastructure.dtos.lodge.LodgeDateReadResponse;
import com.haot.reservation.infrastructure.dtos.lodge.LodgeDateUpdateStatusRequest;
import com.haot.reservation.infrastructure.dtos.lodge.LodgeReadOneResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Lodge-service")
public interface LodgeClient {

  @GetMapping("/api/v1/lodges/{lodgeId}")
  ApiResponse<LodgeReadOneResponse> readOne(@PathVariable String lodgeId);

  @GetMapping("/api/v1/lodge-dates")
  ApiResponse<SliceResponse<LodgeDateReadResponse>> read(
      @PageableDefault(size = 30)
      @SortDefault(sort = "date", direction = Direction.ASC)
      Pageable pageable,
      @RequestParam(name = "lodgeId", required = true) String lodgeId,
      @RequestParam(name = "start", required = false) LocalDate start,
      @RequestParam(name = "end", required = false) LocalDate end
  );

  @PostMapping("/api/v1/lodge-dates/status")
  ApiResponse<Void> updateStatus(
      @Valid @RequestBody LodgeDateUpdateStatusRequest request);
}
