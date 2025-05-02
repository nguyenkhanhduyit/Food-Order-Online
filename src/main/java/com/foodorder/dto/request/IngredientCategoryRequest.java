package com.foodorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientCategoryRequest {
    @NotBlank(message = "Hãy nhập tên Ingredient Category")
    private String name;

    public void setName(String name) {
        this.name = name.trim().replace("  "," ");;
    }
}
