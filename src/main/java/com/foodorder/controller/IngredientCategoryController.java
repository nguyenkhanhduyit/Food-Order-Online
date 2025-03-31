package com.foodorder.controller;

import com.foodorder.dto.request.IngredientCategoryRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.IngredientCategoryResponse;
import com.foodorder.service.IngredientCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient-category")
@RequiredArgsConstructor
public class IngredientCategoryController {

    private final IngredientCategoryService ingredientCategoryService;

    @PostMapping(value = "/new/{restaurantId}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or" +
            "(hasRole('ROLE_RESTAURANT_OWNER') and " +
            "@CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId))")
    public ResponseEntity<ApiResponse<IngredientCategoryResponse>> createIngredientCategory(
            @PathVariable Long restaurantId,
            @RequestBody @Valid IngredientCategoryRequest request
            ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientCategoryResponse>builder()
                        .code(200)
                        .message(ingredientCategoryService.createIngredientCategory(restaurantId,request))
                        .build()
        );
    }


    @GetMapping(value = "/get-all/{restaurantId}")
    public ResponseEntity<ApiResponse<List<IngredientCategoryResponse>>> getAllsIngredientCategoryInRestaurant(
            @PathVariable Long restaurantId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<IngredientCategoryResponse>>builder()
                        .code(200)
                        .message(ingredientCategoryService.getAllIngredientCategories(restaurantId))
                        .build()
        );
    }

    @GetMapping(value = "/get-one/{restaurantId}/{ingredientCategoryId}")
    public ResponseEntity<ApiResponse<IngredientCategoryResponse>> getIngredientCategoryById(
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientCategoryResponse>builder()
                        .code(200)
                        .message(ingredientCategoryService.getIngredientCategoryById(restaurantId,ingredientCategoryId))
                        .build()
        );
    }

    @GetMapping(value = "/get-query/{restaurantId}")
    public ResponseEntity<ApiResponse<List<IngredientCategoryResponse>>> getIngredientCategoriesByQuery(
            @PathVariable Long restaurantId,
            @RequestParam String query
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<IngredientCategoryResponse>>builder()
                        .code(200)
                        .message(ingredientCategoryService.getIngredientCategoriesByQuery(restaurantId,query))
                        .build()
        );
    }

    @PutMapping(value = "/update/{restaurantId}/{ingredientCategoryId}")
    public ResponseEntity<ApiResponse<IngredientCategoryResponse>> updateIngredientCategory(
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId,
            @RequestBody @Valid IngredientCategoryRequest request
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientCategoryResponse>builder()
                        .code(200)
.message(ingredientCategoryService.updateIngredientCategory(restaurantId,ingredientCategoryId,request))
                        .build()
        );
    }

    @DeleteMapping(value = "/remove/{restaurantId}/{ingredientCategoryId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteIngredientCategory(
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<Boolean>builder()
                        .code(200)
                        .message(ingredientCategoryService.deleteIngredientCategory(restaurantId,ingredientCategoryId))
                        .build()
        );
    }
}
