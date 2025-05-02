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
    private Long id;
    private String foodImageUrl;
    private String foodName;
    private int quantity;
    private List<IngredientItemResponse> ingredientItemResponses;
    private BigDecimal totalPrice;
}
