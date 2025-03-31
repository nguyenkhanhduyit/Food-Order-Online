package com.foodorder.mapper;

import com.foodorder.dto.request.OrderItemRequest;
import com.foodorder.dto.response.OrderItemResponse;
import com.foodorder.model.OrderItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-01T00:00:40+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItem toOrderItem(OrderItemRequest request) {
        if ( request == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity( request.getQuantity() );
        orderItem.setTotalPrice( request.getTotalPrice() );

        return orderItem;
    }

    @Override
    public OrderItemResponse toResponse(OrderItem request) {
        if ( request == null ) {
            return null;
        }

        OrderItemResponse.OrderItemResponseBuilder orderItemResponse = OrderItemResponse.builder();

        orderItemResponse.quantity( request.getQuantity() );
        orderItemResponse.totalPrice( request.getTotalPrice() );

        return orderItemResponse.build();
    }
}
