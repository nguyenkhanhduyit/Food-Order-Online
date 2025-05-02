package com.foodorder.service.iservice;

import com.foodorder.dto.request.IngredientItemRequest;
import com.foodorder.dto.response.IngredientItemResponse;
import java.util.List;

public interface IIngredientItemService {
    public IngredientItemResponse createIngredientItem(String token,
                                       Long restaurantId,
                                       Long ingredientCategoryId,
                                       IngredientItemRequest request);
    public IngredientItemResponse getIngredientItemById(
            Long restaurantId,Long ingredientCategoryId
            ,Long ingredientItemId);
    public List<IngredientItemResponse> getAllsIngredientItemByIngredientCategoryId(Long restaurantId,Long ingredientCategoryId);
    public IngredientItemResponse updateIngredientItem(
            String token
            ,Long restaurantId,Long ingredientCategoryId
            ,Long ingredientItemId,IngredientItemRequest request);
    public String deleteIngredientItem(String token
            ,Long restaurantId,Long ingredientCategoryId
            ,Long ingredientItemId);
    public String updateStoke( String token
            ,Long restaurantId,Long ingredientCategoryId
            ,Long ingredientItemId);
}
