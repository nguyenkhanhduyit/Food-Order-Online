package com.foodorder.service.iservice;

import com.foodorder.dto.request.CartItemRequest;
import com.foodorder.dto.response.CartResponse;

public interface ICartService {
    public CartResponse getCartFromUser(String token);
    public int getTotalQuantityItemInCart(String token);
    public CartResponse addCartItemToCart(String token,CartItemRequest request);
    public CartResponse removeCartItemFromCart(String token,Long cartItemId);
    public CartResponse removeAllCartItemsFromCart(String token);
}
