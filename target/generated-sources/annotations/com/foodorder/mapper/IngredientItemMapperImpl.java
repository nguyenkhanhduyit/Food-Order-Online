package com.foodorder.mapper;

import com.foodorder.dto.request.IngredientItemRequest;
import com.foodorder.dto.response.IngredientItemResponse;
import com.foodorder.model.IngredientItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-01T00:00:41+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class IngredientItemMapperImpl implements IngredientItemMapper {

    @Override
    public IngredientItem toIngredientItem(IngredientItemRequest request) {
        if ( request == null ) {
            return null;
        }

        IngredientItem ingredientItem = new IngredientItem();

        ingredientItem.setName( request.getName() );

        return ingredientItem;
    }

    @Override
    public IngredientItemResponse toResponse(IngredientItem ingredientItem) {
        if ( ingredientItem == null ) {
            return null;
        }

        IngredientItemResponse.IngredientItemResponseBuilder ingredientItemResponse = IngredientItemResponse.builder();

        ingredientItemResponse.id( ingredientItem.getId() );
        ingredientItemResponse.name( ingredientItem.getName() );

        return ingredientItemResponse.build();
    }

    @Override
    public IngredientItem updateIngredientItem(IngredientItem target, IngredientItemRequest request) {
        if ( request == null ) {
            return target;
        }

        target.setName( request.getName() );

        return target;
    }
}
