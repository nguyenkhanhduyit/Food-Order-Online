package com.foodorder.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderOfRestaurantRequest {
    @NotNull(message = "Missed restaurant")
    private Long restaurantId;
    @NotNull(message = "Missed order item details")
    private List<OrderItemRequest> orderItemRequests;
}
