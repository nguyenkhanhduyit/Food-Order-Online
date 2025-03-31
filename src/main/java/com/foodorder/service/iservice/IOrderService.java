package com.foodorder.service.iservice;

import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.OrderResponse;

import java.util.Date;
import java.util.List;

public interface IOrderService {
    public OrderResponse createOrder(OrderRequest request);
    public OrderResponse getOderByOrderId(Long orderId);
    public List<OrderResponse> getAllOrderInRestaurant(Long restaurantId, Date startDate, Date endDate);
    public OrderResponse updateOrder(Long restaurantId, Long orderId);
    public boolean deleteOrderById(Long restaurantId,Long orderId);
}
