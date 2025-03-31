package com.foodorder.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class CartItemRequest {
    @NotNull(message = "Thiếu food Id")
    private Long foodId;

    @NotNull(message = "Thiếu số lượng cho cart item")
    @Positive(message = "Số lượng cart item phải > 0")
    private int quantity;

    @NotNull(message = "Thiếu nguyên liệu cho thực phẩm")
    private List<Long> ingredientItemsId;

    @NotNull(message = "Thiếu tổng tiền cart item")
    @Positive(message = "Tổng tiền cart item phải > 0")
    private BigDecimal totalPrice;
}
