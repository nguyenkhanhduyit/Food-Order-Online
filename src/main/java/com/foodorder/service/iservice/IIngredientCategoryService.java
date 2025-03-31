package com.foodorder.service.iservice;

import com.foodorder.dto.request.IngredientCategoryRequest;
import com.foodorder.dto.response.IngredientCategoryResponse;

import java.util.List;

public interface IIngredientCategoryService {
    public IngredientCategoryResponse createIngredientCategory(Long restaurantId, IngredientCategoryRequest request);
    public IngredientCategoryResponse updateIngredientCategory(Long restaurantId,Long ingredientCategoryId, IngredientCategoryRequest request);
    public boolean deleteIngredientCategory(Long restaurantId, Long ingredientCategoryId);
    public List<IngredientCategoryResponse> getAllIngredientCategories(Long restaurantId);
    public IngredientCategoryResponse getIngredientCategoryById(Long restaurantId,Long ingredientCategoryId);
    public List<IngredientCategoryResponse> getIngredientCategoriesByQuery(Long restaurantId,String query);
}
