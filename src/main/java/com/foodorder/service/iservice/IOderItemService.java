package com.foodorder.service.iservice;

import com.foodorder.dto.request.OrderItemRequest;
import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.OrderItemResponse;
import com.foodorder.dto.response.OrderResponse;

import java.util.Date;
import java.util.List;

public interface IOderItemService {
    public OrderItemResponse createOrderItem(OrderItemRequest request);
    public OrderItemResponse getOderItemByOrderItemId(Long orderItemId);
    public List<OrderItemResponse> getAllOrderInRestaurant(Long restaurantId, Long orderItemId);
    public OrderItemResponse updateOrder(Long restaurantId, Long orderItemId);
    public boolean deleteOrderItemById(Long restaurantId,Long orderItemId);
}
