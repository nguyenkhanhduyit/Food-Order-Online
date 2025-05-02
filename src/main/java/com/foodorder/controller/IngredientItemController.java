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
    @PreAuthorize(value = "( hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication) ) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<IngredientItemResponse>> createIngredientCategory(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId,
            @RequestBody @Valid IngredientItemRequest request
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientItemResponse>builder()
                        .code(200)
.message(ingredientItemService.createIngredientItem(token,restaurantId,ingredientCategoryId,request))
                        .build()
        );
        /*Updated and test completed all*/
    }


    @PutMapping(value = "/update/{restaurantId}/{ingredientCategoryId}/{ingredientItemId}")
    @PreAuthorize(value = "( hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication) ) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<IngredientItemResponse>> updateIngredientItem(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId,
            @PathVariable Long ingredientItemId,
            @RequestBody @Valid IngredientItemRequest request
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientItemResponse>builder()
                        .code(200)
.message(ingredientItemService.updateIngredientItem(token,restaurantId,ingredientCategoryId,ingredientItemId,request))
                        .build()
        );
        /*Updated and test completed all*/
    }


    @PutMapping(value = "/update-stoke/{restaurantId}/{ingredientCategoryId}/{ingredientItemId}")
    @PreAuthorize(value = "( hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication) ) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateStoke(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId,
            @PathVariable Long ingredientItemId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
                        .message(ingredientItemService.updateStoke(token, restaurantId, ingredientCategoryId, ingredientItemId))
                        .build()
        );
        /*Updated and test completed all*/
    }



    @DeleteMapping(value = "/remove/{restaurantId}/{ingredientCategoryId}/{ingredientItemId}")
    @PreAuthorize(value = "( hasRole('ROLE_RESTAURANT_OWNER') " +
            " and @CustomPreAuthorize.isUserRequestingTheirOwnData(authentication) ) " +
            "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteIngredientItem(
            @CookieValue(name = "authToken",required = true) String token,
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId,
            @PathVariable Long ingredientItemId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .code(200)
.message(ingredientItemService.deleteIngredientItem(token, restaurantId, ingredientCategoryId, ingredientItemId))
                        .build()
        );
        /*Updated and test completed all*/
    }


    @GetMapping(value = "/get/{restaurantId}/{ingredientCategoryId}/{ingredientItemId}")
    public ResponseEntity<ApiResponse<IngredientItemResponse>> getIngredientItemById(
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId,
            @PathVariable Long ingredientItemId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<IngredientItemResponse>builder()
                        .code(200)
.message(ingredientItemService.getIngredientItemById(restaurantId, ingredientCategoryId, ingredientItemId))
                        .build()
        );
        /*Updated and test completed all*/
    }

    @GetMapping(value = "/get-all/{restaurantId}/{ingredientCategoryId}")
    public ResponseEntity<ApiResponse<List<IngredientItemResponse>>> getAllsIngredientItemByIngredientCategoryId(
            @PathVariable Long restaurantId,
            @PathVariable Long ingredientCategoryId
    ){
        return ResponseEntity.ok().body(
                ApiResponse.<List<IngredientItemResponse>>builder()
                        .code(200)
.message(ingredientItemService.getAllsIngredientItemByIngredientCategoryId(restaurantId,ingredientCategoryId))
                        .build()
        );
        /*Updated and test completed all*/
    }


}
