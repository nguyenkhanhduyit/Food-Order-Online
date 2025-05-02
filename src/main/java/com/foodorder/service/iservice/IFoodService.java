package com.foodorder.service.iservice;

import com.foodorder.dto.request.FoodRequest;
import com.foodorder.dto.request.QueryRequest;
import com.foodorder.dto.response.FoodResponse;

import java.util.List;

public interface IFoodService {

    public FoodResponse createFood(String token,FoodRequest request);

    public FoodResponse updateFood(String token,Long foodId,FoodRequest request);

    boolean deleteFood(String token,Long restaurantId,Long foodId);
    public List<FoodResponse> getAllFoods();
    public List<FoodResponse> getAllFoodsInRestaurant(
            Long restaurantId,Long foodCategory,
            Boolean isVegetarian, Boolean isSeasonal);

    public List<FoodResponse> searchFood(QueryRequest query);

    public FoodResponse findFoodById(Long foodId);


    public FoodResponse updateAvailableFoodState(String token,Long restaurantId,Long foodId);
}
