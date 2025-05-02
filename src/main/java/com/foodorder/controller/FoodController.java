package com.foodorder.controller;

import com.foodorder.dto.request.FoodRequest;
import com.foodorder.dto.request.QueryRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.FoodResponse;
import com.foodorder.service.FoodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class FoodController {

    FoodService foodService;

    @PostMapping("/new")
    @PreAuthorize(value = " (hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication) ) " +
            " or hasRole('ROLE_ADMIN') ")
    public ResponseEntity<ApiResponse<FoodResponse>> createFood(
            @CookieValue(name = "authToken",required = true) String token,
            @ModelAttribute FoodRequest request){
        return ResponseEntity.ok()
                .body(
ApiResponse.<FoodResponse>builder()
        .code(200)
        .message(foodService.createFood(token,request))
        .build()
                );
        /*Updated and test completed all*/
    }


    @PutMapping(value = "/update/{foodId}")
    @PreAuthorize(value = " (hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)) " +
            " or hasRole('ROLE_ADMIN') ")
    public ResponseEntity<ApiResponse<FoodResponse>> updateFood(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long foodId,
            @ModelAttribute FoodRequest request
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<FoodResponse>builder()
                                .code(200)
                                .message(foodService.updateFood(token,foodId,request))
                                .build()
                );
        /*Updated and test completed all*/
    }


    @DeleteMapping(value = "/remove/{restaurantId}/{foodId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER')" +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteFood(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long foodId
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<String>builder()
                                .code(200)
.message(foodService.deleteFood(token,restaurantId,foodId) ?
        "Food with id "+foodId+" in Restaurant "+restaurantId+" has been deleted" :
        "Delete food failed"
        )
                                .build()
                );
        /*Updated and test completed all*/
    }


    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> getAllFoods(){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<FoodResponse>>builder()
                                .code(200)
                            .message(foodService.getAllFoods())
                                .build()
                );
        /*Updated and test completed all*/
    }

    @GetMapping("/get-all/{restaurantId}")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> getAllFoodsInRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam Long foodCategoryId,
            @RequestParam Boolean isVegetarian,
            @RequestParam  Boolean isSeasonal
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<FoodResponse>>builder()
                                .code(200)
.message(foodService.getAllFoodsInRestaurant(restaurantId,foodCategoryId,isVegetarian,isSeasonal))
                                .build()
                );
        /*Updated and test completed all*/
    }


    @GetMapping(value = "/get/{foodId}")
    public ResponseEntity<ApiResponse<FoodResponse>> getFoodById(
            @PathVariable Long foodId
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<FoodResponse>builder()
                                .code(200)
                                .message(foodService.findFoodById(foodId))
                                .build()
                );
        /*Updated and test completed all*/
    }


    @GetMapping(value = "/get-query/")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> searchFood(
            @RequestBody QueryRequest query
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<FoodResponse>>builder()
                                .code(200)
                                .message(foodService.searchFood(query))
                                .build()
                );
        /*Updated and test completed all*/
    }


    @PutMapping(value = "/update-available/{restaurantId}/{foodId}")
    @PreAuthorize(value = " (hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)) " +
            " or hasRole('ROLE_ADMIN') ")
    public ResponseEntity<ApiResponse<FoodResponse>> updateAvailableFoodStatus(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long foodId
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<FoodResponse>builder()
                                .code(200)
                                .message(foodService.updateAvailableFoodState(token,restaurantId,foodId))
                                .build()
                );
        /*Updated and test completed all*/
    }

}
