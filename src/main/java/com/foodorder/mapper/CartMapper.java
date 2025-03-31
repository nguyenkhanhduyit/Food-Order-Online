package com.foodorder.mapper;

import com.foodorder.dto.response.CartResponse;
import com.foodorder.model.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    public CartResponse toResponse(Cart request);
}
