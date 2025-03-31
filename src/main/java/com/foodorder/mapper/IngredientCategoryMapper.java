package com.foodorder.mapper;

import com.foodorder.dto.request.IngredientCategoryRequest;
import com.foodorder.dto.response.IngredientCategoryResponse;
import com.foodorder.model.IngredientCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientCategoryMapper {
    public IngredientCategory toIngredientCategory(IngredientCategoryRequest request);
    public IngredientCategoryResponse toResponse (IngredientCategory request);
}
