package com.foodorder.controller;

import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserController {

    UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByToken(){
var userNamePasswordToken =
        (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok().body(
                ApiResponse.<UserResponse>builder()
                .code(200)
                .message(userService.findUserByToken(userNamePasswordToken.getCredentials().toString()))
                .build());
    }

    @PostMapping("/new-address/{userId}")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication,#userId)")
    public ResponseEntity<ApiResponse<UserResponse>> newUserAddress
            (@PathVariable Long userId,
            @Valid @RequestBody AddressRequest request
            ){
        return ResponseEntity.ok().body(
                ApiResponse.<UserResponse>builder()
                        .code(200)
                        .message(userService.addNewAddressToUser(userId,request))
                        .build());
    }

}
