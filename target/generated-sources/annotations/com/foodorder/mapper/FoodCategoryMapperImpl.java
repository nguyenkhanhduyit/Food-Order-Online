package com.foodorder.mapper;

import com.foodorder.dto.request.FoodCategoryRequest;
import com.foodorder.dto.response.FoodCategoryResponse;
import com.foodorder.model.FoodCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:01:21+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class FoodCategoryMapperImpl implements FoodCategoryMapper {

    @Override
    public FoodCategory toFoodCategory(FoodCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        FoodCategory foodCategory = new FoodCategory();

        foodCategory.setName( request.getName() );

        return foodCategory;
    }

    @Override
    public FoodCategoryResponse toResponse(FoodCategory request) {
        if ( request == null ) {
            return null;
        }

        FoodCategoryResponse foodCategoryResponse = new FoodCategoryResponse();

        foodCategoryResponse.setId( request.getId() );
        foodCategoryResponse.setName( request.getName() );

        return foodCategoryResponse;
    }

    @Override
    public FoodCategory updateFoodCategory(FoodCategory target, FoodCategoryRequest request) {
        if ( request == null ) {
            return target;
        }

        target.setName( request.getName() );

        return target;
    }
}
