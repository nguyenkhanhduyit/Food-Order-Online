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
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)) " +
            " or hasRole('ROLE_ADMIN') ")
    public ResponseEntity<ApiResponse<String>> createFoodCategory(
            @CookieValue(name = "authToken", required = true) String token,
            @PathVariable Long restaurantId,
            @RequestBody @Valid FoodCategoryRequest request
            ){
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
                        .message(foodCategoryService.createFoodCategory(token,restaurantId,request))
                        .build()
                );
        /*Updated and test completed all*/
    }


    @PutMapping(value = "/update/{restaurantId}/{foodCategoryId}")
    @PreAuthorize(value = " (hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)) " +
            " or hasRole('ROLE_ADMIN') ")
    public ResponseEntity<ApiResponse<FoodCategoryResponse>> updateFoodCategory(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long foodCategoryId,
            @RequestBody @Valid FoodCategoryRequest request
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<FoodCategoryResponse>builder()
                                .code(200)
.message(foodCategoryService.updateFoodCategory(token,restaurantId,foodCategoryId,request))
                                .build()
                );
        /*Updated and test completed all*/
    }



    @DeleteMapping(value = "/remove/{restaurantId}/{foodCategoryId}")
    @PreAuthorize(value = "( hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)) " +
            " or hasRole('ROLE_ADMIN') ")
    public ResponseEntity<ApiResponse<String>> deleteFoodCategory(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long foodCategoryId
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<String>builder()
                                .code(200)
.message(foodCategoryService.deleteFoodCategory(token,restaurantId,foodCategoryId)?
        "Delete Food Category Successfully" : "Delete Food Category Failed")
                                .build()
                );
        /*Updated and test completed all*/
    }



    @GetMapping(value = "/get/{restaurantId}")
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
        /*Updated and test completed all*/
    }


    @GetMapping(value = "/get/{restaurantId}/{foodCategoryId}")
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
        /*Updated and test completed all*/
    }






}
