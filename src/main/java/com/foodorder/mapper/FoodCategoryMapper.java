package com.foodorder.mapper;

import com.foodorder.dto.request.FoodCategoryRequest;
import com.foodorder.dto.response.FoodCategoryResponse;
import com.foodorder.model.FoodCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FoodCategoryMapper {
    public FoodCategory toFoodCategory(FoodCategoryRequest request);
    public FoodCategoryResponse toResponse(FoodCategory request);
    public FoodCategory updateFoodCategory(@MappingTarget FoodCategory target, FoodCategoryRequest request);
}
