package com.foodorder.controller;

import com.foodorder.dto.request.IngredientItemRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.dto.response.IngredientItemResponse;
import com.foodorder.service.IngredientItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/ingredient-item")
@RestController
@RequiredArgsConstructor
public class IngredientItemController {

    private final IngredientItemService ingredientItemService;

    @PostMapping(value = "/new/{restaurantId}/{ingredientCategoryId}")
    @PreAuthorize(value = "(hasRole('ROLE_RESTAURANT_OWNER') " +
            "and @CustomPreAuthorize.isUserRequestingTheirRestaurantOwnData(authentication,#restaurantId)) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<IngredientItemResponse>> createIngredientCategory(
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId,
            @RequestBody @Valid IngredientItemRequest request
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientItemResponse>builder()
                        .code(200)
                        .message(ingredientItemService.createIngredientItem(restaurantId,ingredientCategoryId,request))
                        .build()
        );
    }

    @GetMapping(value = "/get-one/{ingredientItemId}")
    public ResponseEntity<ApiResponse<IngredientItemResponse>> getIngredientItemById(
            @PathVariable Long ingredientItemId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientItemResponse>builder()
                        .code(200)
                        .message(ingredientItemService.getIngredientItemById(ingredientItemId))
                        .build()
        );
    }

    @GetMapping(value = "/get-all-by-ingredient-category/{ingredientCategoryId}")
    public ResponseEntity<ApiResponse<List<IngredientItemResponse>>> getAllsIngredientItemByIngredientCategoryId(
            @PathVariable Long ingredientCategoryId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<IngredientItemResponse>>builder()
                        .code(200)
                        .message(ingredientItemService.getAllsIngredientItemByIngredientCategoryId(ingredientCategoryId))
                        .build()
        );
    }

    @PutMapping(value = "/update/{ingredientItemId}")
    public ResponseEntity<ApiResponse<IngredientItemResponse>> updateIngredientItemById(
            @PathVariable Long ingredientItemId,
            @RequestBody @Valid IngredientItemRequest request
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientItemResponse>builder()
                        .code(200)
                        .message(ingredientItemService.updateIngredientItemById(ingredientItemId,request))
                        .build()
        );
    }

    @PutMapping(value = "/update-stoke/{ingredientItemId}")
    public ResponseEntity<ApiResponse<Boolean>> updateStoke(
            @PathVariable Long ingredientItemId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<Boolean>builder()
                        .code(200)
                        .message(ingredientItemService.updateStoke(ingredientItemId))
                        .build()
        );
    }

    @DeleteMapping(value = "/remove/{ingredientItemId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteIngredientItem(
            @PathVariable Long ingredientItemId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<Boolean>builder()
                        .code(200)
                        .message(ingredientItemService.deleteIngredientItemById(ingredientItemId))
                        .build()
        );
    }

}
