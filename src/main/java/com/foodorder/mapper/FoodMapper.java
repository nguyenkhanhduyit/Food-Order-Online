package com.foodorder.mapper;

import com.foodorder.dto.request.FoodRequest;
import com.foodorder.dto.response.FoodResponse;
import com.foodorder.model.Food;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    public Food toFood(FoodRequest request);

    public FoodResponse toResponse(Food request);

    public Food updateFood(@MappingTarget Food food, FoodRequest request);
}
