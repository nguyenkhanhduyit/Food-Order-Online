package com.foodorder.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderOfRestaurantResponse {
    private RestaurantResponse restaurant;
    private List<OrderItemResponse> orderItems;
}
