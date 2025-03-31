package com.foodorder.mapper;

import com.foodorder.dto.request.CartItemRequest;
import com.foodorder.dto.response.CartItemResponse;
import com.foodorder.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    public CartItemResponse toResponse(CartItem request);
    public CartItem toCartItem(CartItemRequest request);
}
