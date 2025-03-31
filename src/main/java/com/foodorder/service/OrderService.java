package com.foodorder.service;

import com.foodorder.dto.request.OrderItemRequest;
import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.AddressResponse;
import com.foodorder.dto.response.IngredientItemResponse;
import com.foodorder.dto.response.OrderItemResponse;
import com.foodorder.dto.response.OrderResponse;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.AddressMapper;
import com.foodorder.mapper.OrderItemMapper;
import com.foodorder.mapper.OrderMapper;
import com.foodorder.model.Address;
import com.foodorder.model.IngredientItem;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.repository.*;
import com.foodorder.service.iservice.IOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService implements IOrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;
    UserRepository userRepository;
    RestaurantRepository restaurantRepository;
    FoodRepository foodRepository;
    OrderItemMapper orderItemMapper;
    IngredientItemRepository ingredientItemRepository;
    AddressMapper addressMapper;
    OrderItemRepository orderItemRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        // map status, create At , total item , total price
        Order order = orderMapper.toOrder(request);
        //assign user to order
        order.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(()-> new ResourceNotAvailableException("User not found")));
        //assign restaurant to order
        order.setRestaurant(restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found")));
        //assign address to order
        Address address = new Address();
        address.setAddress(request.getDeliveryAddress().addressComplete());
        order.setDeliveryAddress(address);
        //assign order item to order
        order.setOrderItems(
                request.getOrderItemRequests()
                        .stream()
                        .map(
                                item -> {
                                    OrderItem orderItem = orderItemMapper.toOrderItem(item);
                                    orderItem.setOrder(order);
                                    orderItem.setFood(foodRepository.findById(item.getFoodId())
                                        .orElseThrow(()-> new ResourceNotAvailableException("Food not found")));

                                    orderItem.setIngredients(
                                            item.getIngredientItemId()
                                                    .stream()
                                                    .map(ingredientItem ->
                                                        ingredientItemRepository.findById(ingredientItem)
                                        .orElseThrow(()-> new ResourceNotAvailableException("Ingredient not found"))
                                                    )
                                                    .toList()
                                    );
                                    return orderItem;
                                }
                        )
                        .toList()
        );
        orderRepository.save(order);
        return toOrderResponse(order);
    }
    private OrderResponse toOrderResponse(Order order){
        OrderResponse orderResponse = orderMapper.toResponse(order);
        orderResponse.setUserName(order.getUser().getFullName());
        orderResponse.setRestaurantName(order.getRestaurant().getName());
        AddressResponse addressResponse = addressMapper.toResponse(order.getDeliveryAddress());
        orderResponse.setDeliveryAddress(addressResponse);
        List<OrderItemResponse> orderItemResponses = order.getOrderItems()
                .stream()
                .map(
                        orderItem -> {
                            OrderItemResponse orderItemResponse = orderItemMapper.toResponse(orderItem);
                            orderItemResponse.setFoodName(orderItem.getFood().getName());

                            List<String> ingredientItemsName =
                            orderItem.getIngredients().stream().map(
                                    IngredientItem::getName
                            ).toList();
                            orderItemResponse.setIngredientItemName(ingredientItemsName);
                            return orderItemResponse;
                        }
                )
                .toList();
        orderResponse.setOrderItemResponses(orderItemResponses);
        return orderResponse;
    }
    @Override
    public OrderResponse getOderByOrderId(Long orderId) {
        return toOrderResponse(orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotAvailableException("Order not found")));
    }

    @Override
    public List<OrderResponse> getAllOrderInRestaurant(Long restaurantId, Date startDate, Date endDate) {
        return List.of();
    }

    @Override
    public OrderResponse updateOrder(Long restaurantId, Long orderId) {
        return null;
    }

    @Override
    public boolean deleteOrderById(Long restaurantId, Long orderId) {
        return false;
    }
}
