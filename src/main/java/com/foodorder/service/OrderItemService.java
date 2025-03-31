package com.foodorder.service;

import com.foodorder.dto.request.OrderItemRequest;
import com.foodorder.dto.response.OrderItemResponse;
import com.foodorder.repository.OrderRepository;
import com.foodorder.service.iservice.IOderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService implements IOderItemService {



    @Override
    public OrderItemResponse createOrderItem(OrderItemRequest request) {

        return null;
    }

    @Override
    public OrderItemResponse getOderItemByOrderItemId(Long orderItemId) {
        return null;
    }

    @Override
    public List<OrderItemResponse> getAllOrderInRestaurant(Long restaurantId, Long orderItemId) {
        return List.of();
    }

    @Override
    public OrderItemResponse updateOrder(Long restaurantId, Long orderItemId) {
        return null;
    }

    @Override
    public boolean deleteOrderItemById(Long restaurantId, Long orderItemId) {
        return false;
    }
}
