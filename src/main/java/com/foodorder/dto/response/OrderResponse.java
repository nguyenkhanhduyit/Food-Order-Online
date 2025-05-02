package com.foodorder.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String userName;
    private String orderStatus;
    private Date createAt;
    private String addressDelivery;
    private String numberPhoneContact;
    private List<OrderOfRestaurantResponse> orderOfRestaurant;
    private int totalItem;
    private BigDecimal totalPrice;
}
