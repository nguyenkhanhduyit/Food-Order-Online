package com.foodorder.service.iservice;

import com.foodorder.dto.request.UserRequest;
import com.foodorder.dto.response.AuthResponse;

public interface IAuthService {
    AuthResponse createUser(UserRequest request);
}
