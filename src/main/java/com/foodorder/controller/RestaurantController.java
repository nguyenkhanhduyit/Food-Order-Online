package com.foodorder.controller;


import com.foodorder.dto.request.RestaurantRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.RestaurantDTOResponse;
import com.foodorder.dto.response.RestaurantResponse;
import com.foodorder.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/restaurant")
@RequiredArgsConstructor
@Validated
public class RestaurantController {

    RestaurantService restaurantService;
    @PostMapping("/new-restaurant/{userId}")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication,#userId)")
    public ResponseEntity<ApiResponse<RestaurantResponse>> createRestaurant(
            @PathVariable Long userId,
            @RequestBody @Valid RestaurantRequest request){
        return ResponseEntity.ok().body(
                ApiResponse.<RestaurantResponse>builder()
                        .code(200)
                        .message(restaurantService.createRestaurant(request, userId)).build());
    }


    @PutMapping("/update-restaurant/{userId}/{restaurantId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId) )" +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<RestaurantResponse>> updateRestaurant(
            @PathVariable("restaurantId")Long restaurantId,
            @PathVariable("userId") Long userId,
            @RequestBody @Valid RestaurantRequest request
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<RestaurantResponse>builder()
                        .code(200)
                        .message(restaurantService.updateRestaurant(restaurantId,userId,request))
                        .build());
    }

    @DeleteMapping("/delete-restaurant/{userId}/{restaurantId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication,#userId)) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteRestaurant(
            @PathVariable("userId")Long userId,
            @PathVariable("restaurantId") Long restaurantId
    ){
        restaurantService.deleteRestaurant(userId,restaurantId);
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
                        .message("Restaurant have been deleted")
                        .build());
    }

    @PutMapping("/update-restaurant-status/{userId}/{restaurantId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication,#userId))" +
            " or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateRestaurantStatus(
            @PathVariable("userId") Long userId,
            @PathVariable("restaurantId") Long restaurantId
    ){
        restaurantService.updateRestaurantStatus(userId,restaurantId);
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
                        .message("Restaurant Status have been updated")
                        .build());
    }

    @GetMapping("/get-restaurant/{userId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication,#userId))" +
            " or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> findRestaurantByUserId(@PathVariable Long userId){
        return ResponseEntity.ok().body(
                ApiResponse.<List<RestaurantResponse>>builder()
                        .code(200)
                        .message(restaurantService.getAllRestaurantsByUserId(userId))
                        .build());
    }

    @GetMapping("/")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getAllRestaurants(){
        return ResponseEntity.ok().body(
                ApiResponse.<List<RestaurantResponse>>builder()
                        .code(200)
                        .message(restaurantService.getAllRestaurants())
                        .build());
    }

    @GetMapping("/{query}")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> searchRestaurant(
          @PathVariable String query){
        return ResponseEntity.ok().body(
                ApiResponse.<List<RestaurantResponse>>builder()
                        .code(200)
                .message(restaurantService.searchRestaurant(query))
                        .build());
    }

    @GetMapping("/{restaurantId}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<RestaurantResponse>> getRestaurantById(
            @PathVariable Long restaurantId){
        return ResponseEntity.ok().body(
                ApiResponse.<RestaurantResponse>builder()
                        .code(200)
                        .message(restaurantService.findRestaurantByRestaurantId(restaurantId))
                        .build());
    }

    @PostMapping("/add-to-favorite/{userId}/{restaurantId}/{foodId}")
    @PreAuthorize(value = "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication,#userId)")
    public ResponseEntity<ApiResponse<RestaurantDTOResponse>> addToFavorites(
            @PathVariable Long userId,
            @PathVariable Long restaurantId,
            @PathVariable Long foodId){
        return ResponseEntity.ok().body(
                ApiResponse.<RestaurantDTOResponse>builder()
                        .code(200)
                        .message(restaurantService.addToFavorites(userId,restaurantId,foodId))
                        .build());
    }

}
