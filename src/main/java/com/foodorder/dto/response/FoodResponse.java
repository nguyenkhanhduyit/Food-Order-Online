package com.foodorder.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
public class FoodResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String imageUrl;

    private List<String> galleryUrls;

    private FoodCategoryResponse foodCategoryResponse;

    private boolean available;

    private boolean isVegetarian;

    private boolean isSeasonal;

   private List<IngredientItemInIngredientCategory> ingredientItemInIngredientCategories = new ArrayList<>();

    private Date creationDate;

}
