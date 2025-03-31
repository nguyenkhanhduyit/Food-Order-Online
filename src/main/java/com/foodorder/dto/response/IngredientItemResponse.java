package com.foodorder.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class IngredientItemResponse {
    private Long id;
    private String name;
}
