package com.foodorder.mapper;

import com.foodorder.dto.request.OrderItemRequest;
import com.foodorder.dto.response.OrderItemResponse;
import com.foodorder.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    public OrderItem toOrderItem(OrderItemRequest request);
    public OrderItemResponse toResponse(OrderItem request);
}
