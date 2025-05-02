package com.foodorder.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IngredientItemInIngredientCategory {
    IngredientCategoryResponse ingredientCategoryResponse;
    List<IngredientItemResponse> ingredientItemResponses;
}
