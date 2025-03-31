package com.foodorder.mapper;

import com.foodorder.dto.request.RestaurantRequest;
import com.foodorder.dto.response.RestaurantResponse;
import com.foodorder.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface RestaurantMapper {

    public RestaurantResponse toResponse(Restaurant request);

    public Restaurant toRestaurant(RestaurantRequest request);

    @Mapping(target = "address", ignore = true)
    @Mapping(target = "contact", ignore = true)
    public Restaurant updateRestaurant (@MappingTarget Restaurant target,RestaurantRequest request);
}
