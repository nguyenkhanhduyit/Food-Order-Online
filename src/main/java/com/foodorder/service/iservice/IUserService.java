package com.foodorder.service.iservice;

import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.response.UserResponse;

public interface IUserService {

    UserResponse findUserByToken(String token);
    UserResponse findUserByEmail(String email);
    UserResponse addNewAddressToUser(Long userId, AddressRequest addressRequest);
    UserResponse findUserById(Long userId);
}
