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
public class OrderItemRequest {

    @NotNull(message = "Hãy cung cấp food id")
    private Long foodId;

    @NotNull(message = "Hãy cung cấp số lượng sản phẩm")
    @Positive(message = "Hãy cung cấp số lượng > 0")
    private int quantity;

    @NotNull(message = "Hãy cung cấp giá sản phẩm")
    @Positive(message = "Giá phải > 0")
    private BigDecimal totalPrice;

    @NotNull(message = "Hãy cung cấp nguyên liệu cho sản phẩm")
    private List<Long> ingredientItemId;
}
