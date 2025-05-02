package com.foodorder.mapper;

import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.OrderResponse;
import com.foodorder.model.Order;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:01:21+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toOrder(OrderRequest request) {
        if ( request == null ) {
            return null;
        }

        Order order = new Order();

        return order;
    }

    @Override
    public OrderResponse toResponse(Order request) {
        if ( request == null ) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();

        orderResponse.orderStatus( request.getOrderStatus() );
        orderResponse.createAt( request.getCreateAt() );
        orderResponse.addressDelivery( request.getAddressDelivery() );
        orderResponse.numberPhoneContact( request.getNumberPhoneContact() );
        orderResponse.totalItem( request.getTotalItem() );
        orderResponse.totalPrice( request.getTotalPrice() );

        return orderResponse.build();
    }
}
