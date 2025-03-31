package com.foodorder.controller;

import com.foodorder.dto.request.CartItemRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.CartItemResponse;
import com.foodorder.dto.response.CartResponse;
import com.foodorder.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping(value = "/")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getAllCartItemsFromCart(
            @RequestParam Long cartId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<CartItemResponse>>builder()
                        .code(200)
                        .message(cartService.getAllCartItemsFromCart(cartId))
                        .build()
        );
    }

    @GetMapping(value = "/get-cart/")
    @PreAuthorize(value = " hasRole('ROLE_USER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirOwnData(#authentication,#userId)")
    public ResponseEntity<ApiResponse<CartResponse>> getCartByUserId(
            @RequestParam Long userId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<CartResponse>builder()
                        .code(200)
                        .message(cartService.getCartByUserId(userId))
                        .build()
        );
    }

    @PostMapping(value = "/add/")
    @PreAuthorize(value = " hasRole('ROLE_USER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirCartOwnData(authentication,#cartId) ")
    public ResponseEntity<ApiResponse<CartResponse>> addCartItemToCart(
            @RequestParam Long cartId,
            @RequestBody @Valid CartItemRequest request
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<CartResponse>builder()
                        .code(200)
                        .message(cartService.addCartItemToCart(cartId,request))
                        .build()
        );
    }

    @DeleteMapping(value = "/remove/")
    @PreAuthorize(value = " hasRole('ROLE_USER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirCartOwnData(authentication,#cartId) ")
    public ResponseEntity<ApiResponse<String>> removeCartItemFromCart(
           @RequestParam Long cartId, @RequestParam Long cartItemId
    ){
        cartService.removeCartItemFromCart(cartId,cartItemId);
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
                        .message("Cart Item has been deleted !")
                        .build()
        );
    }


    @DeleteMapping(value = "/remove-all/")
    public ResponseEntity<ApiResponse<Boolean>> removeAllCartItemsFromCart(
            @RequestParam Long cartId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<Boolean>builder()
                        .code(200)
                        .message(cartService.removeAllCartItemsFromCart(cartId))
                        .build()
        );
    }
}
