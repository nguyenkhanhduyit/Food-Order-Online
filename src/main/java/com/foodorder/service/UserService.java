package com.foodorder.service;

import com.foodorder.config.JwtUtils;
import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.UserMapper;
import com.foodorder.model.Address;
import com.foodorder.model.User;
import com.foodorder.repository.AddressRepository;
import com.foodorder.repository.UserRepository;
import com.foodorder.service.iservice.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserService implements IUserService {

    UserRepository userRepository;
    JwtUtils jwtUtils;
    UserMapper userMapper;
    AddressRepository addressRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserByToken(String token) {
        if(!jwtUtils.validateToken(token))
            throw new BadCredentialsException("Token Invalid");
        String userNameFromToken = jwtUtils.retrieveEmailFromToken(token);
        User user = userRepository.findByEmail(userNameFromToken)
                .orElseThrow( () -> new ResourceNotAvailableException("User Not Found By Email"));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow( () -> new ResourceNotAvailableException("User not found by email"));
        return userMapper.toUserResponse(user);
    }

    public UserResponse addNewAddressToUser(Long userId,AddressRequest addressRequest){
        //gán value cho address
        Address newAddress = new Address();
        newAddress.setAddress(addressRequest.addressComplete());
        //find user from db
        User user =  userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotAvailableException("User Not Found"));
        //assign address to user
        user.getAddress().add(newAddress);
        //assign user to address
        newAddress.setUser(user);
        userRepository.save(user);
        addressRepository.save(newAddress);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findUserById(Long userId) {
       User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotAvailableException("User Not Found"));
        return userMapper.toUserResponse(user);
    }

}
