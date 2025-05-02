package com.foodorder.service;

import com.foodorder.dto.request.UserRequest;
import com.foodorder.dto.response.AuthResponse;
import com.foodorder.mapper.UserMapper;
import com.foodorder.model.Cart;
import com.foodorder.model.User;
import com.foodorder.repository.UserRepository;
import com.foodorder.service.iservice.IAuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodorder.exception.declare.ResourceAlreadyExistException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = {
            ResourceAlreadyExistException.class,
            Exception.class
    },isolation = Isolation.SERIALIZABLE,timeout = 20)
    public AuthResponse createUser(UserRequest request) {
        //check email already exist?
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new ResourceAlreadyExistException("Email Đã Được Đăng Kí");
        //create new instance user
        User user = userMapper.toUser(request);
        //encode password for user
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        //create cart for user
        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.valueOf(0));
        userRepository.save(user);
        return AuthResponse.builder()
                .token(null)
                .roles(null)
                .build();
    }
}
