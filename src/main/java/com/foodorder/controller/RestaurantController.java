package com.foodorder.controller;

import com.foodorder.dto.request.RestaurantRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.FavoriteResponse;
import com.foodorder.dto.response.RestaurantResponse;
import com.foodorder.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    RestaurantService restaurantService;

    @PostMapping("/new")
    @PreAuthorize(value = "(@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication)" +
            " and hasRole('ROLE_USER'))" +
            " or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> createRestaurant(
            @CookieValue(value = "authToken", required = true) String token,
            @ModelAttribute @Valid RestaurantRequest request) {
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
                        .message(restaurantService.createRestaurant(request,token))
                        .build());
        /*updated and test completed all*/
    }


    @PutMapping("/update/{restaurantId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication, #restaurantId) " +
            "and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication) ) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<RestaurantResponse>> updateRestaurant(
            @CookieValue(value = "authToken", required = true) String token,
            @PathVariable Long restaurantId,
            @ModelAttribute @Valid RestaurantRequest request) {
        RestaurantResponse response = restaurantService.updateRestaurant(restaurantId, token, request);
        return ResponseEntity.ok().body(
                ApiResponse.<RestaurantResponse>builder()
                        .code(200)
                        .message(response)
                        .build());
        /*updated and test completed all*/
    }


    @DeleteMapping("/delete/{restaurantId}")
    @PreAuthorize(value = "( hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId)) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteRestaurant(
            @CookieValue(value = "authToken", required = true) String token,
            @PathVariable("restaurantId") Long restaurantId
    ){
        restaurantService.deleteRestaurant(token,restaurantId);
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
                        .message("Restaurant have been deleted")
                        .build());
        /*updated and test completed all*/
    }


    @PutMapping("/update-status/{restaurantId}")
    @PreAuthorize(value = "( hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId) )" +
            " or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateRestaurantStatus(
            @CookieValue(value = "authToken", required = true) String token,
            @PathVariable("restaurantId") Long restaurantId
    ){
        restaurantService.updateRestaurantStatus(token,restaurantId);
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
                        .message("Restaurant Status have been updated")
                        .build());
        /*Updated and test completed all*/
    }


    @GetMapping("/get")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication) )" +
            " or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> findRestaurantByUser(
            @CookieValue(value = "authToken", required = true) String token
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<RestaurantResponse>>builder()
                        .code(200)
                        .message(restaurantService.getAllRestaurantsByUserToken(token))
                        .build());
        /*Updated and test completed all*/
    }


    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getAllRestaurants(){
        return ResponseEntity.ok().body(
                ApiResponse.<List<RestaurantResponse>>builder()
                        .code(200)
                        .message(restaurantService.getAllRestaurants())
                        .build());
        /*Updated and testing completed*/
    }


    @GetMapping("/{query}")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> searchRestaurant(
          @PathVariable String query){
        return ResponseEntity.ok().body(
                ApiResponse.<List<RestaurantResponse>>builder()
                        .code(200)
                .message(restaurantService.searchRestaurant(query))
                        .build());
        /*Updated and test completed all*/
    }


    @GetMapping("/get/{restaurantId}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId))")
    public ResponseEntity<ApiResponse<RestaurantResponse>> getRestaurantByRestaurantId(
            @PathVariable Long restaurantId){
        return ResponseEntity.ok().body(
                ApiResponse.<RestaurantResponse>builder()
                        .code(200)
                        .message(restaurantService.findRestaurantByRestaurantId(restaurantId))
                        .build());
         /*
        Updated and test completed
        */
    }


    @PostMapping("/favorite/{restaurantId}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or " +
            "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication) ")
    public ResponseEntity<ApiResponse<FavoriteResponse>> addToFavorites(
            @CookieValue(value = "authToken", required = true) String token,
            @PathVariable Long restaurantId){
        return ResponseEntity.ok().body(
                ApiResponse.<FavoriteResponse>builder()
                        .code(200)
                        .message(restaurantService.addToFavorites(token,restaurantId))
                        .build());
        /*Updated and test completed all*/
    }

}
