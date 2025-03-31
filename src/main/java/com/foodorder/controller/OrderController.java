package com.foodorder.controller;

import com.foodorder.dto.request.OrderRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.OrderResponse;
import com.foodorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/new")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestBody OrderRequest request
            ){
        return ResponseEntity.ok().body(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .message(orderService.createOrder(request))
                        .build()
        );
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @RequestParam Long orderId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .message(orderService.getOderByOrderId(orderId))
                        .build()
        );
    }
}
