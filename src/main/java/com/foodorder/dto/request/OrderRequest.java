package com.foodorder.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderRequest {

    @NotNull(message = "Missed deliver address")
    private Long deliveryAddressId;

    @NotNull(message = "Missed order details")
    private List<OrderOfRestaurantRequest> orderOfRestaurants;

}
