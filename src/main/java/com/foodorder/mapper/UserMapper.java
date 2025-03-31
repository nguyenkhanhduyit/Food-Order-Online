package com.foodorder.mapper;

import com.foodorder.dto.request.UserRequest;
import com.foodorder.dto.response.AuthResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {

     @Mapping(target = "password",ignore = true)
     public User toUser(UserRequest request);

     public UserResponse toUserResponse(User user);

     @Mapping(source = "roles",target = "roles")
     public AuthResponse toAuthResponse(User user);

}
