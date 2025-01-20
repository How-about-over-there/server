package com.haot.user.application.service;

import com.haot.user.application.dto.req.AdminUserUpdateRequest;
import com.haot.user.application.dto.res.AdminUserGetResponse;

public interface AdminUserCRUDService {

  AdminUserGetResponse getUser(String userId);
  void updateUser(String userId, AdminUserUpdateRequest adminUserUpdateRequest);
}
