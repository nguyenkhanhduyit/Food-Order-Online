package com.foodorder.mapper;

import com.foodorder.dto.response.CartItemResponse;
import com.foodorder.dto.response.CartResponse;
import com.foodorder.model.Cart;
import com.foodorder.model.CartItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:01:21+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartResponse toResponse(Cart request) {
        if ( request == null ) {
            return null;
        }

        CartResponse.CartResponseBuilder cartResponse = CartResponse.builder();

        cartResponse.id( request.getId() );
        cartResponse.cartItems( cartItemListToCartItemResponseList( request.getCartItems() ) );
        cartResponse.totalPrice( request.getTotalPrice() );
        cartResponse.totalItem( request.getTotalItem() );

        return cartResponse.build();
    }

    protected CartItemResponse cartItemToCartItemResponse(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartItemResponse.CartItemResponseBuilder cartItemResponse = CartItemResponse.builder();

        cartItemResponse.id( cartItem.getId() );
        cartItemResponse.quantity( cartItem.getQuantity() );
        cartItemResponse.totalPrice( cartItem.getTotalPrice() );

        return cartItemResponse.build();
    }

    protected List<CartItemResponse> cartItemListToCartItemResponseList(List<CartItem> list) {
        if ( list == null ) {
            return null;
        }

        List<CartItemResponse> list1 = new ArrayList<CartItemResponse>( list.size() );
        for ( CartItem cartItem : list ) {
            list1.add( cartItemToCartItemResponse( cartItem ) );
        }

        return list1;
    }
}
