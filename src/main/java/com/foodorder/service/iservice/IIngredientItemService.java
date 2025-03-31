package com.foodorder.service.iservice;

import com.foodorder.dto.request.IngredientItemRequest;
import com.foodorder.dto.response.IngredientCategoryResponse;
import com.foodorder.dto.response.IngredientItemResponse;

import java.util.List;

public interface IIngredientItemService {
    public IngredientItemResponse createIngredientItem(Long restaurantId,Long ingredientCategoryId, IngredientItemRequest request);
    public IngredientItemResponse getIngredientItemById(Long ingredientItemId);
    public List<IngredientItemResponse> getAllsIngredientItemByIngredientCategoryId(Long ingredientCategoryId);
    public IngredientItemResponse updateIngredientItemById(Long ingredientItemId,IngredientItemRequest request);
    public boolean deleteIngredientItemById(Long ingredientItemId);
    public boolean updateStoke(Long ingredientItemId );
}
