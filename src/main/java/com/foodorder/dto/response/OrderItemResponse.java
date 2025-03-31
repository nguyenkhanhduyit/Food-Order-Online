package com.foodorder.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderItemResponse {
    private String foodName;
    private int quantity;
    private BigDecimal totalPrice;
    private List<String> ingredientItemName;
}
