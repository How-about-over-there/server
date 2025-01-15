package com.haot.user.application.service;

import com.haot.user.application.dto.res.AdminUserGetResponse;

public interface AdminUserCRUDService {

  AdminUserGetResponse getUser(String userId);
}
