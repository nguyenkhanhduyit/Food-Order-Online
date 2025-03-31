package com.foodorder.mapper;

import com.foodorder.dto.request.IngredientItemRequest;
import com.foodorder.dto.response.IngredientItemResponse;
import com.foodorder.model.IngredientItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IngredientItemMapper {
    public IngredientItem toIngredientItem(IngredientItemRequest request);
    public IngredientItemResponse toResponse(IngredientItem ingredientItem);
    public IngredientItem updateIngredientItem(@MappingTarget IngredientItem target,IngredientItemRequest request);
}
