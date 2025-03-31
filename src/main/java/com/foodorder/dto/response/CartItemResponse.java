package com.foodorder.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class CartItemResponse {
    private List<String> images;
    private String foodName;
    private int quantity;
    private List<String> nameIngredientItems;
    private BigDecimal totalPrice;
}
