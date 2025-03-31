package com.foodorder.service.iservice;

import com.foodorder.dto.request.CartItemRequest;
import com.foodorder.dto.response.CartItemResponse;
import com.foodorder.dto.response.CartResponse;

import java.util.List;

public interface ICartService {
    public CartResponse addCartItemToCart(Long cartId,CartItemRequest request);
    public CartResponse removeCartItemFromCart(Long cartId,Long cartItemId);
    public CartResponse getCartByUserId(Long userId);
    public boolean checkUserOwnerCart(String userName,Long cartId);
    public CartResponse getCartById(Long cartId);
    public List<CartItemResponse> getAllCartItemsFromCart (Long cartId);
    public boolean removeAllCartItemsFromCart(Long cartId);
}
