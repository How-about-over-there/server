package com.haot.user.application.service;

import com.haot.user.application.req.UserLoginValidationRequest;
import com.haot.user.application.res.UserLoginValidationResponse;

public interface UserValidationService {
  UserLoginValidationResponse validateLoginInformation(UserLoginValidationRequest request);

}
