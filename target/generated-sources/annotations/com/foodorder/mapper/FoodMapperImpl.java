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
    date = "2025-04-01T00:00:40+0700",
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
        List<String> list = request.getImages();
        if ( list != null ) {
            food.setImages( new ArrayList<String>( list ) );
        }
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
        List<String> list = request.getImages();
        if ( list != null ) {
            foodResponse.setImages( new ArrayList<String>( list ) );
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
        if ( food.getImages() != null ) {
            List<String> list = request.getImages();
            if ( list != null ) {
                food.getImages().clear();
                food.getImages().addAll( list );
            }
            else {
                food.setImages( null );
            }
        }
        else {
            List<String> list = request.getImages();
            if ( list != null ) {
                food.setImages( new ArrayList<String>( list ) );
            }
        }
        food.setAvailable( request.isAvailable() );
        food.setVegetarian( request.isVegetarian() );
        food.setSeasonal( request.isSeasonal() );

        return food;
    }
}
