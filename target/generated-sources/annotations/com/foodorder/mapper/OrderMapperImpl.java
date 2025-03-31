package com.foodorder.mapper;

import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.AddressResponse;
import com.foodorder.dto.response.OrderResponse;
import com.foodorder.model.Address;
import com.foodorder.model.Order;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-01T00:00:41+0700",
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

        order.setOrderStatus( request.getOrderStatus() );
        order.setCreateAt( request.getCreateAt() );
        order.setDeliveryAddress( addressRequestToAddress( request.getDeliveryAddress() ) );
        order.setTotalItem( request.getTotalItem() );
        order.setTotalPrice( request.getTotalPrice() );

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
        orderResponse.deliveryAddress( addressToAddressResponse( request.getDeliveryAddress() ) );
        orderResponse.totalItem( request.getTotalItem() );
        orderResponse.totalPrice( request.getTotalPrice() );

        return orderResponse.build();
    }

    protected Address addressRequestToAddress(AddressRequest addressRequest) {
        if ( addressRequest == null ) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        return address.build();
    }

    protected AddressResponse addressToAddressResponse(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponse.AddressResponseBuilder addressResponse = AddressResponse.builder();

        addressResponse.address( address.getAddress() );

        return addressResponse.build();
    }
}
