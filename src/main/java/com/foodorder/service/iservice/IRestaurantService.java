package com.foodorder.service.iservice;

import com.foodorder.dto.request.RestaurantRequest;
import com.foodorder.dto.response.RestaurantDTOResponse;
import com.foodorder.dto.response.RestaurantResponse;

import java.util.List;

public interface IRestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest request, Long userId);
    RestaurantResponse updateRestaurant(Long restaurantId,Long userId, RestaurantRequest request);
    void deleteRestaurant(Long userId,Long restaurantId);
    List<RestaurantResponse> getAllRestaurants();
    List<RestaurantResponse> searchRestaurant(String request);
    RestaurantResponse findRestaurantByRestaurantId(Long id);
    RestaurantDTOResponse addToFavorites(Long userId,Long restaurantId,Long foodId);
    void updateRestaurantStatus(Long userId,Long restaurantId);
    List<RestaurantResponse> getAllRestaurantsByUserId(Long userId);

}
