package com.foodorder.service.iservice;

import com.foodorder.dto.request.FoodCategoryRequest;
import com.foodorder.dto.response.FoodCategoryResponse;
import java.util.List;

public interface IFoodCategoryService {
    String createFoodCategory (String token,Long restaurantId,FoodCategoryRequest request);
    FoodCategoryResponse updateFoodCategory (String token,
                                             Long restaurantId,
                                             Long foodCategoryId,
                                             FoodCategoryRequest request);
    boolean deleteFoodCategory(String token,Long restaurantId,Long foodCategoryId);
    List<FoodCategoryResponse> getAllFoodCategoriesInRestaurant(Long restaurantId);
    FoodCategoryResponse getFoodCategoryById(Long restaurantId,Long foodCategoryId);
}
