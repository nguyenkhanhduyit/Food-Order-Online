package com.foodorder.service.iservice;

import com.foodorder.dto.request.IngredientCategoryRequest;
import com.foodorder.dto.request.QueryRequest;
import com.foodorder.dto.response.IngredientCategoryResponse;

import java.util.List;

public interface IIngredientCategoryService {
public IngredientCategoryResponse createIngredientCategory(String token,Long restaurantId,IngredientCategoryRequest request);
public IngredientCategoryResponse updateIngredientCategory(String token,Long restaurantId,Long ingredientCategoryId, IngredientCategoryRequest request);
public String deleteIngredientCategory(String token,Long restaurantId, Long ingredientCategoryId);
public List<IngredientCategoryResponse> getAllIngredientCategories(Long restaurantId);
public IngredientCategoryResponse getIngredientCategoryById(Long restaurantId,Long ingredientCategoryId);
public List<IngredientCategoryResponse> getIngredientCategoriesByQuery(Long restaurantId, QueryRequest query);
}
