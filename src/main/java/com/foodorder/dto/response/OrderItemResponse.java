package com.foodorder.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private String foodName;
    private String imgUrl;
    private int quantity;
    private BigDecimal totalPrice;
    private List<String> ingredientItemName;
}
