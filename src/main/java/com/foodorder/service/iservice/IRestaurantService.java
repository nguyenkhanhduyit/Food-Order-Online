package com.foodorder.service.iservice;

import com.foodorder.dto.request.RestaurantRequest;
import com.foodorder.dto.response.FavoriteResponse;
import com.foodorder.dto.response.RestaurantResponse;

import java.util.List;

public interface IRestaurantService {

    String createRestaurant(RestaurantRequest request, String token);
    RestaurantResponse updateRestaurant(Long restaurantId,String token, RestaurantRequest request);
    void deleteRestaurant(String token,Long restaurantId);
    List<RestaurantResponse> getAllRestaurants();
    List<RestaurantResponse> searchRestaurant(String request);
    RestaurantResponse findRestaurantByRestaurantId(Long id);
    FavoriteResponse addToFavorites(String token, Long restaurantId);
    void updateRestaurantStatus(String token,Long restaurantId);
    List<RestaurantResponse> getAllRestaurantsByUserToken(String token);

}
