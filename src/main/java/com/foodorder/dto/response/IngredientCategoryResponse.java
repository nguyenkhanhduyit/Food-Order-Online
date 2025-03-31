package com.foodorder.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class IngredientCategoryResponse {
    private Long id;
    private String name;
}
