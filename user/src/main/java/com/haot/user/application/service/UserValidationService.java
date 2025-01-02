package com.haot.user.application.service;

import com.haot.user.application.dto.req.UserLoginValidationRequest;
import com.haot.user.application.dto.res.UserLoginValidationResponse;

public interface UserValidationService {
  UserLoginValidationResponse validateLoginInformation(UserLoginValidationRequest request);

}
