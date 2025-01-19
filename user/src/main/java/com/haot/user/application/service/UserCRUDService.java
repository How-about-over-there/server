package com.haot.user.application.service;

import com.haot.user.application.dto.req.UserCreateRequest;
import com.haot.user.application.dto.req.UserUpdateMeRequest;
import com.haot.user.application.dto.res.UserCreateResponse;
import com.haot.user.application.dto.res.UserGetMeResponse;

public interface UserCRUDService {

  UserCreateResponse createUser(UserCreateRequest userCreateRequest);

  UserGetMeResponse getMyInfo(String userId);

  void updateMyInfo(UserUpdateMeRequest userUpdateMeRequest, String userId);
}
