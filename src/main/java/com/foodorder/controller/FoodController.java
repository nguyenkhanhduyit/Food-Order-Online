package com.foodorder.controller;

import com.foodorder.dto.request.FoodRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.FoodResponse;
import com.foodorder.dto.response.TopMealsResponse;
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
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER')" +
            " and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#request.restaurantId)) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<FoodResponse>> createFood(
            @RequestBody FoodRequest request){
        return ResponseEntity.ok()
                .body(
ApiResponse.<FoodResponse>builder()
        .code(200)
        .message(foodService.createFood(request))
        .build()
                );
    }


    @GetMapping("/get-all/{restaurantId}")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> getAllFoodsInRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam Long foodCategoryId,
            @RequestParam boolean isAvailable,
            @RequestParam boolean isVegetarian,
            @RequestParam  boolean isSeasonal

    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<FoodResponse>>builder()
                                .code(200)
.message(foodService.getAllFoodsInRestaurant(restaurantId,foodCategoryId,isAvailable,isVegetarian,isSeasonal))
                                .build()
                );
    }


    @GetMapping(value = "/get-one/{foodId}")
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
    }


    @GetMapping(value = "/get-query/")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> searchFood(
            @RequestParam String query
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<FoodResponse>>builder()
                                .code(200)
                                .message(foodService.searchFood(query))
                                .build()
                );
    }


    @PutMapping(value = "/update-available/{restaurantId}/{foodId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId))" +
            " or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> updateAvailableFoodStatus(
            @PathVariable Long restaurantId,
            @PathVariable Long foodId
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<Boolean>builder()
                                .code(200)
                                .message(foodService.updateAvailableFoodState(foodId))
                                .build()
                );
    }


    @PutMapping(value = "/update/{foodId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#request.restaurantId))" +
            " or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<FoodResponse>> updateFood(
            @PathVariable Long foodId,
            @RequestBody FoodRequest request
    ){
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<FoodResponse>builder()
                                .code(200)
                                .message(foodService.updateFood(foodId,request))
                                .build()
                );
    }

    @DeleteMapping(value = "/remove/{restaurantId}/{foodId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER')" +
            " and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId)) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteFood(
            @PathVariable Long restaurantId,
            @PathVariable Long foodId
    ){
        foodService.deleteFood(foodId);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<String>builder()
                                .code(200)
                                .message("Food with id : "+foodId+" in Restaurant : "+restaurantId+" has been deleted")
                                .build()
                );
    }

//    @GetMapping("/top-meals")
//    public ResponseEntity<List<TopMealsResponse>> filterTopMeal(){
//        return ResponseEntity.ok().body(foodService.filterTopMeal());
//    }
}
