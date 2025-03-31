package com.foodorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodCategoryRequest {
    @NotBlank(message = "name food category is blank")
    @Size(min = 7,max = 50,message = "Please enter valid length of food category ")
    private String name;
}
