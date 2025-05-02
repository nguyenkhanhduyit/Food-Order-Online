package com.foodorder.service.iservice;

import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.request.UserUpdateRequest;
import com.foodorder.dto.response.AddressResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.model.User;

import java.util.List;

public interface IUserService {
    UserResponse updateUser(String token, UserUpdateRequest request);
    User findUserByToken(String token);
    UserResponse findUserByEmail(String email);
    AddressResponse addNewAddressToUser(String token, AddressRequest addressRequest);
    List<AddressResponse> deleteAddressFromUser(String token, Long id);
    List<AddressResponse> getAllAddressByUser(String token);
    UserResponse findUserById(Long userId);
    UserResponse convertUserToUserResponse(User user);
    AddressResponse updateAddress(String token,Long id,AddressRequest request);
}
