package com.foodorder.controller;

import com.foodorder.dto.request.DateRange;
import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.OrderResponse;
import com.foodorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/new")
    @PreAuthorize("@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @CookieValue(name = "authToken") String token,
            @RequestBody OrderRequest request
            ){
        return ResponseEntity.ok().body(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .message(orderService.createOrder(token,request))
                        .build()
        );
        /*Updated and test completed all*/
    }

    @GetMapping("/user/{orderId}")
    @PreAuthorize(" hasRole('ROLE_USER') and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)" +
            " or hasRole('ROLE_RESTAURANT_OWNER') ")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @CookieValue(name = "authToken") String token,
            @PathVariable Long orderId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .message(orderService.getOderByOrderId(token,orderId))
                        .build()
        );
        /*Updated and test completed all*/
    }

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER') and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrderInRestaurant(
            @CookieValue(name = "authToken") String token,
            @PathVariable Long restaurantId,
            @RequestBody DateRange dateRange
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<OrderResponse>>builder()
                        .code(200)
                        .message(orderService.getAllOrderInRestaurant(token,restaurantId,dateRange.getStartDate(),dateRange.getEndDate()))
                        .build()
        );
        /*Updated and test completed all*/
    }

    @GetMapping("/user")
    @PreAuthorize("@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrderOfUser(
            @CookieValue(name = "authToken") String token
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<OrderResponse>>builder()
                        .code(200)
                        .message(orderService.getAllOrderOfUser(token))
                        .build()
        );
        /*Updated and test completed all*/
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @CookieValue(name = "authToken") String token,
            @PathVariable Long orderId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .message(orderService.deleteOrderById(token,orderId))
                        .build()
        );
        /*Updated and test completed all*/
    }
}
