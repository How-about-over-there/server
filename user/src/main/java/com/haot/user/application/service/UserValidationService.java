package com.haot.user.application.service;

import com.haot.user.application.dto.req.UserLoginValidationRequest;
import com.haot.user.application.dto.res.UserLoginValidationResponse;
import com.haot.user.application.dto.res.UserValidationResponse;

public interface UserValidationService {

  UserLoginValidationResponse validateLoginInformation(UserLoginValidationRequest request);

  UserValidationResponse validateUserById(String userId);
}
