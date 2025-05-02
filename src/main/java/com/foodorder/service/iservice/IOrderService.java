package com.foodorder.service.iservice;

import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.OrderResponse;
import java.util.Date;
import java.util.List;

public interface IOrderService {
     OrderResponse createOrder(String token,OrderRequest request);
     OrderResponse getOderByOrderId(String token,Long orderId);
     List<OrderResponse> getAllOrderInRestaurant(String token,Long restaurantId, Date startDate, Date endDate);
    OrderResponse deleteOrderById(String token, Long orderId);
     List<OrderResponse> getAllOrderOfUser(String token);
}
