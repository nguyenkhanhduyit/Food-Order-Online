package com.foodorder.mapper;

import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.OrderResponse;
import com.foodorder.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    public Order toOrder (OrderRequest request);
    public OrderResponse toResponse(Order request);
}
