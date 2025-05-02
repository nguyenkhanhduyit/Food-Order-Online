package com.foodorder.controller;

import com.foodorder.dto.request.IngredientCategoryRequest;
import com.foodorder.dto.request.QueryRequest;
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
            "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication))")
    public ResponseEntity<ApiResponse<IngredientCategoryResponse>> createIngredientCategory(
            @CookieValue(value = "authToken", required = true) String token,
             @PathVariable Long restaurantId,
             @RequestBody @Valid IngredientCategoryRequest request
            ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientCategoryResponse>builder()
                        .code(200)
                        .message(ingredientCategoryService.createIngredientCategory(token,restaurantId,request))
                        .build()
        );
        /*Updated and test completed all*/
    }

    @PutMapping(value = "/update/{restaurantId}/{ingredientCategoryId}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or" +
            "(hasRole('ROLE_RESTAURANT_OWNER') and " +
            "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication))")
    public ResponseEntity<ApiResponse<IngredientCategoryResponse>> updateIngredientCategory(
            @CookieValue(value = "authToken", required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId,
            @RequestBody @Valid IngredientCategoryRequest request
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientCategoryResponse>builder()
                        .code(200)
.message(ingredientCategoryService.updateIngredientCategory(token,restaurantId,ingredientCategoryId,request))
                        .build()
        );
        /*Updated and test completed all*/
    }


    @GetMapping(value = "/get-all/{restaurantId}")
    public ResponseEntity<ApiResponse<List<IngredientCategoryResponse>>> getAllIngredientCategoriesInRestaurant(
            @PathVariable Long restaurantId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<IngredientCategoryResponse>>builder()
                        .code(200)
                        .message(ingredientCategoryService.getAllIngredientCategories(restaurantId))
                        .build()
        );
        /*Updated and test completed all*/
    }


    @GetMapping(value = "/get/{restaurantId}/{ingredientCategoryId}")
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
        /*Updated and test completed all*/
    }


    @GetMapping(value = "/query/{restaurantId}")
    public ResponseEntity<ApiResponse<List<IngredientCategoryResponse>>> getIngredientCategoriesByQuery(
            @PathVariable Long restaurantId,
            @RequestBody @Valid QueryRequest query
            ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<IngredientCategoryResponse>>builder()
                        .code(200)
                        .message(ingredientCategoryService.getIngredientCategoriesByQuery(restaurantId,query))
                        .build()
        );
        /*Updated and test completed all*/
    }



    @DeleteMapping(value = "/remove/{restaurantId}/{ingredientCategoryId}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or" +
            "(hasRole('ROLE_RESTAURANT_OWNER') and " +
            "@CustomPreAuthorize.isUserRequestingTheirOwnData(authentication))")
    public ResponseEntity<ApiResponse<String>> deleteIngredientCategory(
            @CookieValue(name = "authToken", required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
.message(ingredientCategoryService.deleteIngredientCategory(token,restaurantId,ingredientCategoryId))
                        .build()
        );
        /*Updated and test completed all*/
    }
}
