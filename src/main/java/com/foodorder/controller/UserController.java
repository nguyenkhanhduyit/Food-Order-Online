package com.foodorder.controller;

import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.request.UserUpdateRequest;
import com.foodorder.dto.response.AddressResponse;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserController {

    UserService userService;

    @PutMapping("/update")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @CookieValue(value = "authToken", required = true) String token,
            @ModelAttribute @Valid UserUpdateRequest request){
        return ResponseEntity.ok().body(
                ApiResponse.<UserResponse>builder()
                        .code(200)
                .message(userService.updateUser(token,request))
                        .build());
         /*
        Updated and test completed update User, upload Avatar, delete old image in Cloudinary
        */
    }


    @GetMapping("/profile")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByToken(
            @CookieValue(value = "authToken", required = true) String token
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<UserResponse>builder()
                .code(200)
                .message(userService.convertUserToUserResponse(userService.findUserByToken(token)))
                .build());
        /*Updated and test completed all*/
    }

    @PostMapping("/new-address")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<AddressResponse>> newUserAddress
            (
            @CookieValue(value = "authToken", required = true) String token,
            @Valid @RequestBody AddressRequest request
            ){
        return ResponseEntity.ok().body(
                ApiResponse.<AddressResponse>builder()
                        .code(200)
                        .message(userService.addNewAddressToUser(token,request))
                        .build());
        /*Updated and test completed all*/
    }

    @PutMapping("/update-address/{id}")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<AddressResponse>> updateUserAddress
            (
                    @CookieValue(value = "authToken", required = true) String token,
                    @Valid @RequestBody AddressRequest request,
                    @PathVariable Long id
            ){
        return ResponseEntity.ok().body(
                ApiResponse.<AddressResponse>builder()
                        .code(200)
                        .message(userService.updateAddress(token,id,request))
                        .build());
        /*Updated and test completed all*/
    }

    @DeleteMapping("/delete-address/{id}")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> deleteUserAddress(
            @CookieValue(value = "authToken", required = true) String token, @PathVariable Long id){
        return ResponseEntity.ok().body(
                ApiResponse.<List<AddressResponse>>builder()
                        .code(200)
                        .message(userService.deleteAddressFromUser(token,id))
                        .build());
        /*Updated and test completed all*/
    }


    @GetMapping("/get-all-address")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAllAddressFromUser(
            @CookieValue(value = "authToken", required = true) String token){
        return ResponseEntity.ok().body(
                ApiResponse.<List<AddressResponse>>builder()
                        .code(200)
                        .message(userService.getAllAddressByUser(token))
                        .build());
        /*Updated and test completed all*/
    }


}
