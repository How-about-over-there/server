package com.haot.user.application.service;

import com.haot.user.application.dto.req.UserCreateRequest;
import com.haot.user.application.dto.res.UserCreateResponse;

public interface UserCRUDService {

  UserCreateResponse createUser(UserCreateRequest userCreateRequest);
}
