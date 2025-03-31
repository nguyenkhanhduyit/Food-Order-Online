package com.foodorder.controller;

import com.foodorder.dto.request.FoodCategoryRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.FoodCategoryResponse;
import com.foodorder.service.FoodCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/food-category")
@RequiredArgsConstructor
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @PostMapping(value = "/new/{restaurantId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId))" +
            " or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<FoodCategoryResponse>> createFoodCategory(
            @PathVariable Long restaurantId,
            @RequestBody @Valid FoodCategoryRequest request
            ){
        return ResponseEntity.ok()
                .body(
ApiResponse.<FoodCategoryResponse>builder()
        .code(200)
        .message(foodCategoryService.createFoodCategory(restaurantId,request))
        .build()
                );
    }

    @GetMapping(value = "/get-all/{restaurantId}")
    public ResponseEntity<ApiResponse<List<FoodCategoryResponse>>> getAllFoodCategoriesInRestaurant(
            @PathVariable Long restaurantId
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<FoodCategoryResponse>>builder()
                                .code(200)
                                .message(foodCategoryService.getAllFoodCategoriesInRestaurant(restaurantId))
                                .build()
                );
    }

    @GetMapping(value = "/get-one/{restaurantId}/{foodCategoryId}")
    public ResponseEntity<ApiResponse<FoodCategoryResponse>> getAllFoodCategoryById(
            @PathVariable Long restaurantId,
            @PathVariable Long foodCategoryId
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<FoodCategoryResponse>builder()
                                .code(200)
                                .message(foodCategoryService.getFoodCategoryById(restaurantId,foodCategoryId))
                                .build()
                );
    }

    @PutMapping(value = "/update/{restaurantId}/{foodCategoryId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId)) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<FoodCategoryResponse>> updateFoodCategory(
            @PathVariable Long restaurantId,
            @PathVariable Long foodCategoryId,
            @RequestBody @Valid FoodCategoryRequest request
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<FoodCategoryResponse>builder()
                                .code(200)
                                .message(foodCategoryService.updateFoodCategory(restaurantId,foodCategoryId,request))
                                .build()
                );
    }

    @DeleteMapping(value = "/remove/{restaurantId}/{foodCategoryId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId)) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> deleteFoodCategory(
            @PathVariable Long restaurantId,
            @PathVariable Long foodCategoryId
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<Boolean>builder()
                                .code(200)
                                .message(foodCategoryService.deleteFoodCategory(restaurantId,foodCategoryId))
                                .build()
                );
    }


}
