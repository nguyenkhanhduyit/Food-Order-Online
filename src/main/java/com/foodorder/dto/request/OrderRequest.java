package com.foodorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderRequest {
    @NotNull(message = "Thiếu user id")
    private Long userId;

    @NotNull(message = "Thiếu restaurant id")
    private Long restaurantId;

    @NotBlank(message = "Thiếu trạng thái của order")
    private String orderStatus;

    @NotNull(message = "Thiếu ngày tạo order")
    private Date createAt;

    @NotNull(message = "Thiếu địa chỉ nhận hàng")
    private AddressRequest deliveryAddress;

    @NotNull(message = "Thiếu order item")
    private List<OrderItemRequest> orderItemRequests;

    @NotNull(message = "Thiếu tổng số lượng cho Order")
    @Positive(message = "Tổng số lượng phải > 0")
    private int totalItem;

    @NotNull(message = "Thiếu tổng giá tiền cho Order")
    @Positive(message = "Tổng số tiền cho Order phải > 0")
    private BigDecimal totalPrice;
}
