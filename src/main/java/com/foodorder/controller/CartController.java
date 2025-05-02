package com.foodorder.controller;

import com.foodorder.dto.request.CartItemRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.CartResponse;
import com.foodorder.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping(value = "/")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<CartResponse>> getCartFromUser(
            @CookieValue(name = "authToken",required = true) String token
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<CartResponse>builder()
                        .code(200)
                        .message(cartService.getCartFromUser(token))
                        .build()
        );
        /*Updated and test completed all*/
    }


    @GetMapping(value = "/total-quantity")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<Integer>> getNumberOfTotalFromCart(
            @CookieValue(name = "authToken",required = true) String token
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<Integer>builder()
                        .code(200)
                        .message(cartService.getTotalQuantityItemInCart(token))
                        .build()
        );
        /*Updated and test completed all*/
    }


    @PostMapping(value = "/add")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<CartResponse>> addCartItemToCart(
            @CookieValue(name = "authToken",required = true) String token,
            @ModelAttribute @Valid CartItemRequest request
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<CartResponse>builder()
                        .code(200)
                        .message(cartService.addCartItemToCart(token,request))
                        .build()
        );
        /*Updated and test completed all*/
    }

    @DeleteMapping(value = "/remove/{cartItemId}")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<CartResponse>> removeCartItemFromCart(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long cartItemId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<CartResponse>builder()
                        .code(200)
                        .message(cartService.removeCartItemFromCart(token,cartItemId))
                        .build()
        );
        /*Updated and test completed all*/
    }


    @DeleteMapping(value = "/remove")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<CartResponse>> removeAllCartItemsFromCart(
            @CookieValue(name = "authToken",required = true) String token
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<CartResponse>builder()
                        .code(200)
                        .message(cartService.removeAllCartItemsFromCart(token))
                        .build()
        );
        /*Updated and test completed all*/
    }
}
