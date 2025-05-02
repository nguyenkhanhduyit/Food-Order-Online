package com.foodorder.mapper;

import com.foodorder.dto.request.IngredientCategoryRequest;
import com.foodorder.dto.response.IngredientCategoryResponse;
import com.foodorder.model.IngredientCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:01:21+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class IngredientCategoryMapperImpl implements IngredientCategoryMapper {

    @Override
    public IngredientCategory toIngredientCategory(IngredientCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        IngredientCategory ingredientCategory = new IngredientCategory();

        ingredientCategory.setName( request.getName() );

        return ingredientCategory;
    }

    @Override
    public IngredientCategoryResponse toResponse(IngredientCategory request) {
        if ( request == null ) {
            return null;
        }

        IngredientCategoryResponse.IngredientCategoryResponseBuilder ingredientCategoryResponse = IngredientCategoryResponse.builder();

        ingredientCategoryResponse.id( request.getId() );
        ingredientCategoryResponse.name( request.getName() );

        return ingredientCategoryResponse.build();
    }
}
