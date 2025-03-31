package com.foodorder.mapper;

import com.foodorder.dto.request.CartItemRequest;
import com.foodorder.dto.response.CartItemResponse;
import com.foodorder.model.CartItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-01T00:00:40+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class CartItemMapperImpl implements CartItemMapper {

    @Override
    public CartItemResponse toResponse(CartItem request) {
        if ( request == null ) {
            return null;
        }

        CartItemResponse.CartItemResponseBuilder cartItemResponse = CartItemResponse.builder();

        cartItemResponse.quantity( request.getQuantity() );
        cartItemResponse.totalPrice( request.getTotalPrice() );

        return cartItemResponse.build();
    }

    @Override
    public CartItem toCartItem(CartItemRequest request) {
        if ( request == null ) {
            return null;
        }

        CartItem cartItem = new CartItem();

        cartItem.setQuantity( request.getQuantity() );
        cartItem.setTotalPrice( request.getTotalPrice() );

        return cartItem;
    }
}
