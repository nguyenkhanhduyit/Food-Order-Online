package com.foodorder.mapper;

import com.foodorder.dto.request.FoodRequest;
import com.foodorder.dto.response.FoodResponse;
import com.foodorder.model.Food;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:01:21+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class FoodMapperImpl implements FoodMapper {

    @Override
    public Food toFood(FoodRequest request) {
        if ( request == null ) {
            return null;
        }

        Food food = new Food();

        food.setName( request.getName() );
        food.setDescription( request.getDescription() );
        food.setPrice( request.getPrice() );
        food.setAvailable( request.isAvailable() );
        food.setVegetarian( request.isVegetarian() );
        food.setSeasonal( request.isSeasonal() );

        return food;
    }

    @Override
    public FoodResponse toResponse(Food request) {
        if ( request == null ) {
            return null;
        }

        FoodResponse foodResponse = new FoodResponse();

        foodResponse.setId( request.getId() );
        foodResponse.setName( request.getName() );
        foodResponse.setDescription( request.getDescription() );
        foodResponse.setPrice( request.getPrice() );
        foodResponse.setImageUrl( request.getImageUrl() );
        List<String> list = request.getGalleryUrls();
        if ( list != null ) {
            foodResponse.setGalleryUrls( new ArrayList<String>( list ) );
        }
        foodResponse.setAvailable( request.isAvailable() );
        foodResponse.setVegetarian( request.isVegetarian() );
        foodResponse.setSeasonal( request.isSeasonal() );
        foodResponse.setCreationDate( request.getCreationDate() );

        return foodResponse;
    }

    @Override
    public Food updateFood(Food food, FoodRequest request) {
        if ( request == null ) {
            return food;
        }

        food.setName( request.getName() );
        food.setDescription( request.getDescription() );
        food.setPrice( request.getPrice() );
        food.setAvailable( request.isAvailable() );
        food.setVegetarian( request.isVegetarian() );
        food.setSeasonal( request.isSeasonal() );

        return food;
    }
}
