package com.foodorder.dto.response;

import com.foodorder.model.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class CartResponse {
    private Long id;
    private List<CartItemResponse> cartItems = new ArrayList<>();
    private BigDecimal totalPrice;
    private int totalItem;
}
