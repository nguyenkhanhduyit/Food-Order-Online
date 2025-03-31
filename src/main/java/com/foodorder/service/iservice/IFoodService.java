package com.foodorder.service.iservice;

import com.foodorder.dto.request.FoodRequest;
import com.foodorder.dto.response.FoodResponse;

import java.util.List;

public interface IFoodService {

    public FoodResponse createFood(FoodRequest request);

    boolean deleteFood(Long foodId);

    public List<FoodResponse> getAllFoodsInRestaurant(
            Long restaurantId,Long foodCategory,boolean isAvailable,
            boolean isVegetarian, boolean isSeasonal);

    public List<FoodResponse> searchFood(String query);

    public FoodResponse findFoodById(Long foodId);

    public FoodResponse updateFood(Long foodId,FoodRequest request);

    public boolean updateAvailableFoodState(Long foodId);
}
