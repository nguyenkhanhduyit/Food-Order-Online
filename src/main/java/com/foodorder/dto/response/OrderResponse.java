package com.foodorder.dto.response;

import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.request.OrderItemRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {
    private String userName;
    private String restaurantName;
    private String orderStatus;
    private Date createAt;
    private AddressResponse deliveryAddress;
    private List<OrderItemResponse> OrderItemResponses;
    private int totalItem;
    private BigDecimal totalPrice;
}
