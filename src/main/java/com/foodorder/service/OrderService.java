package com.foodorder.service;

import com.foodorder.dto.request.OrderItemRequest;
import com.foodorder.dto.request.OrderOfRestaurantRequest;
import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.*;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.AddressMapper;
import com.foodorder.model.*;
import com.foodorder.repository.*;
import com.foodorder.service.iservice.IOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderService implements IOrderService {

    OrderRepository orderRepository;
    RestaurantRepository restaurantRepository;
    FoodRepository foodRepository;
    IngredientItemRepository ingredientItemRepository;
    AddressMapper addressMapper;
    UserService userService;
    AddressRepository addressRepository;

    @Override
    @Transactional(timeout = 20,isolation = Isolation.SERIALIZABLE,rollbackFor = {Exception.class,})
    public OrderResponse createOrder(String token, OrderRequest request) {
        User user = userService.findUserByToken(token);

        var addressDeliver = addressRepository.findAddressByUser(user.getId())
                .orElseThrow(() -> new ResourceNotAvailableException("Sorry, địa chỉ giao hàng không tồn tại"));

        Order order = new Order();
        order.setAddressDelivery(addressDeliver.getAddress());
        order.setNumberPhoneContact(addressDeliver.getNumberPhoneContact());
        order.setUser(user);
        order.setTotalPrice(BigDecimal.ZERO); // Khởi tạo totalPrice
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderOfRestaurantRequest orderOfRestaurantRequest : request.getOrderOfRestaurants()) {
            Restaurant restaurant = restaurantRepository.findById(orderOfRestaurantRequest.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotAvailableException("Không tìm thấy nhà hàng với id: " + orderOfRestaurantRequest.getRestaurantId()));

            for (OrderItemRequest itemRequest : orderOfRestaurantRequest.getOrderItemRequests()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setRestaurant(restaurant);

                Food food = foodRepository.findById(itemRequest.getFoodId())
                        .orElseThrow(() -> new ResourceNotAvailableException("Không tìm thấy món ăn với id: " + itemRequest.getFoodId()));
                orderItem.setFood(food);
                orderItem.setQuantity(itemRequest.getQuantity());
                order.setTotalItem(order.getTotalItem() + orderItem.getQuantity());
                orderItem.setTotalPrice(itemRequest.getTotalPrice());
                order.setTotalPrice(order.getTotalPrice().add(orderItem.getTotalPrice()));
                List<IngredientItem> ingredients = itemRequest.getIngredientItemId().stream()
                        .map(id -> ingredientItemRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotAvailableException("Không tìm thấy nguyên liệu với id: " + id)))
                        .collect(Collectors.toList());
                orderItem.setIngredients(ingredients);

                orderItems.add(orderItem);
            }
        }
        order.setOrderItems(orderItems);
        user.getOrders().add(order);
        orderRepository.save(order);
        return toOrderResponse(order);
    }

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setUserName(order.getUser().getFullName());
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setCreateAt(order.getCreateAt());
        orderResponse.setId(order.getId());
        orderResponse.setAddressDelivery(order.getAddressDelivery());
        orderResponse.setNumberPhoneContact(order.getNumberPhoneContact());

        Map<Long, List<OrderItem>> itemsByRestaurantId = order.getOrderItems().stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getRestaurant().getId()));

        List<OrderOfRestaurantResponse> orderOfRestaurantResponses = itemsByRestaurantId.entrySet().stream()
                .map(entry -> {
                    Long restaurantId = entry.getKey();
                    List<OrderItem> orderItems = entry.getValue();
                    Restaurant restaurant = orderItems.get(0).getRestaurant();
                    RestaurantResponse restaurantResponse = new RestaurantResponse();
                    restaurantResponse.setId(restaurant.getId());
                    restaurantResponse.setName(restaurant.getName());
                    restaurantResponse.setLogoUrl(restaurant.getLogoUrl());

                    List<OrderItemResponse> orderItemResponses = orderItems.stream()
                            .map(orderItem -> {
                                OrderItemResponse itemResponse = new OrderItemResponse();
                                itemResponse.setFoodName(orderItem.getFood().getName());
                                itemResponse.setQuantity(orderItem.getQuantity());
                                itemResponse.setTotalPrice(orderItem.getTotalPrice());
                                itemResponse.setImgUrl(orderItem.getFood().getImageUrl());
                                List<String> ingredientNames = orderItem.getIngredients().stream()
                                        .map(IngredientItem::getName)
                                        .collect(Collectors.toList());
                                itemResponse.setIngredientItemName(ingredientNames);
                                return itemResponse;
                            })
                            .collect(Collectors.toList());

                    OrderOfRestaurantResponse orderOfRestaurantResponse = new OrderOfRestaurantResponse();
                    orderOfRestaurantResponse.setRestaurant(restaurantResponse);
                    orderOfRestaurantResponse.setOrderItems(orderItemResponses);
                    return orderOfRestaurantResponse;
                })
                .collect(Collectors.toList());

        orderResponse.setOrderOfRestaurant(orderOfRestaurantResponses);
        orderResponse.setTotalItem(order.getTotalItem());
        orderResponse.setTotalPrice(order.getTotalPrice());
        return orderResponse;
    }


    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOderByOrderId(String token,Long orderId) {
        User user = userService.findUserByToken(token);
        log.info("User id : {}",user.getId());
        log.info("Order id : {}",orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotAvailableException("Order not found"));
        if(!user.getOrders().contains(order))
            log.error("Order not belong to user");
        return toOrderResponse(order);
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrderInRestaurant(String token,Long restaurantId, Date startDate, Date endDate) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotAvailableException("Restaurant not found with id: " + restaurantId));
        User user = userService.findUserByToken(token);
        if(!user.getRestaurants().contains(restaurant))
            throw new ResourceNotAvailableException("Restaurant not belong to user");
        List<Order> orders = orderRepository.findAllByOrderItemsRestaurantIdAndCreateAtBetween(
                restaurant.getId(), startDate, endDate);
        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse deleteOrderById(String token, Long orderId) {
        final String STATUS_DELETE_ORDER = "Order has been cancelled";
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotAvailableException("Order not found with id: " + orderId));
        User user = userService.findUserByToken(token);
        if(!user.getOrders().contains(order))
            throw new ResourceNotAvailableException("Order not belong to user");
        order.setOrderStatus(STATUS_DELETE_ORDER);
        return this.toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrderOfUser(String token) {
        return orderRepository.findAllOrderOfUser(userService.findUserByToken(token).getId())
                .stream().map(this::toOrderResponse).toList();
    }
}
