package com.foodorder.service;

import com.foodorder.dto.request.IngredientCategoryRequest;
import com.foodorder.dto.response.IngredientCategoryResponse;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.IngredientCategoryMapper;
import com.foodorder.model.IngredientCategory;
import com.foodorder.model.Restaurant;
import com.foodorder.repository.IngredientCategoryRepository;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.service.iservice.IIngredientCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class IngredientCategoryService implements IIngredientCategoryService {

    RestaurantRepository restaurantRepository;
    IngredientCategoryMapper ingredientCategoryMapper;
    IngredientCategoryRepository ingredientCategoryRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,
    rollbackFor = Exception.class)
    public IngredientCategoryResponse createIngredientCategory(Long restaurantId, IngredientCategoryRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found by this is "));
        IngredientCategory ingredientCategory = ingredientCategoryMapper.toIngredientCategory(request);
        ingredientCategory.setRestaurant(restaurant);
        restaurant.getIngredientCategory().add(ingredientCategory);
        restaurantRepository.save(restaurant);
        return IngredientCategoryResponse.builder()
                .id(ingredientCategory.getId())
                .name(ingredientCategory.getName())
                .build();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,
            rollbackFor = Exception.class)
    public IngredientCategoryResponse updateIngredientCategory(Long restaurantId,Long ingredientCategoryId, IngredientCategoryRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found by this is "));
        IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(ingredientCategoryId)
                        .orElseThrow(()-> new ResourceNotAvailableException("IngredientCategory not found"));
        int index = restaurant.getIngredientCategory().indexOf(ingredientCategory);
        restaurant.getIngredientCategory().get(index).setName(request.getName());
        restaurantRepository.save(restaurant);
        return IngredientCategoryResponse.builder()
                .id(ingredientCategory.getId())
                .name(restaurant.getIngredientCategory().get(index).getName())
                .build();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,
            rollbackFor = Exception.class)
    public boolean deleteIngredientCategory(Long restaurantId, Long ingredientCategoryId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found by this is "));
        IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(ingredientCategoryId)
                .orElseThrow(()-> new ResourceNotAvailableException("IngredientCategory not found"));
       restaurant.getIngredientCategory().remove(ingredientCategory);
        restaurantRepository.save(restaurant);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientCategoryResponse> getAllIngredientCategories(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"))
                .getIngredientCategory().stream()
                .map(ingredientCategoryMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public IngredientCategoryResponse getIngredientCategoryById(Long restaurantId,Long ingredientCategoryId) {
        return ingredientCategoryMapper.toResponse(
                restaurantRepository.findById(restaurantId)
                        .orElseThrow(
                                ()-> new ResourceNotAvailableException("Restaurant not found"))
                        .getIngredientCategory()
                        .stream()
                        .filter(ingredientCategory ->
                                ingredientCategory.getId().equals(ingredientCategoryId))
                        .findFirst().orElseThrow(()-> new ResourceNotAvailableException("Ingredient Category not found"))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientCategoryResponse> getIngredientCategoriesByQuery(Long restaurantId,String query) {
        return ingredientCategoryRepository.searchByQuery(restaurantId,query)
                .stream()
                .map(ingredientCategoryMapper::toResponse)
                .toList();
    }
}
