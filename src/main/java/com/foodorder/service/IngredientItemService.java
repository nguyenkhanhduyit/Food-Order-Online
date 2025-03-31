package com.foodorder.service;

import com.foodorder.dto.request.IngredientItemRequest;
import com.foodorder.dto.response.IngredientItemResponse;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.IngredientItemMapper;
import com.foodorder.model.IngredientCategory;
import com.foodorder.model.IngredientItem;
import com.foodorder.model.Restaurant;
import com.foodorder.repository.IngredientCategoryRepository;
import com.foodorder.repository.IngredientItemRepository;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.service.iservice.IIngredientItemService;
import jakarta.persistence.TableGenerator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class IngredientItemService implements IIngredientItemService {

    IngredientCategoryRepository ingredientCategoryRepository;
    IngredientItemMapper ingredientItemMapper;
    IngredientItemRepository ingredientItemRepository;
    RestaurantRepository restaurantRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3
    ,rollbackFor = Exception.class)
    public IngredientItemResponse createIngredientItem(
                                                       Long restaurantId,
                                                       Long ingredientCategoryId,
                                                       IngredientItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"));
        IngredientCategory ingredientCategory =
                restaurant.getIngredientCategory()
                        .stream()
                        .filter(iC ->
                                iC.getId().equals(ingredientCategoryId)
                        )
                                .findFirst()
                        .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Category not found"));

        IngredientItem ingredientItem = new IngredientItem();
        ingredientItem.setIngredientCategory(ingredientCategory);
        ingredientItem.setName(ingredientItemMapper.toIngredientItem(request).getName());
        ingredientCategory.getIngredientItems().add(ingredientItem);
        restaurantRepository.save(restaurant);
        return IngredientItemResponse.builder()
                .id(restaurant.getIngredientCategory()
                        .stream()
                        .filter(iC ->
                                iC.getId().equals(ingredientCategoryId)
                        )
                        .findFirst()
                        .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Category not found"))
                        .getIngredientItems()
                        .stream()
                        .filter(iI -> iI.getName().equals(ingredientItem.getName()))
                        .findFirst().orElseThrow(()-> new ResourceNotAvailableException("Error Happen")).getId()
                )
                .name(ingredientItem.getName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public IngredientItemResponse getIngredientItemById(Long ingredientItemId) {
        return ingredientItemMapper.toResponse(ingredientItemRepository.findById(ingredientItemId)
                .orElseThrow(()-> new ResourceNotAvailableException("Ingredient item not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientItemResponse> getAllsIngredientItemByIngredientCategoryId(Long ingredientCategoryId) {
        return ingredientCategoryRepository.findById(ingredientCategoryId)
                .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Category not found"))
                .getIngredientItems()
                .stream()
                .map(ingredientItemMapper::toResponse).toList();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3)
    public IngredientItemResponse updateIngredientItemById(Long ingredientItemId,IngredientItemRequest request) {
        IngredientItem ingredientItem = ingredientItemRepository.findById(ingredientItemId)
                .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Item not found"));
        ingredientItem = ingredientItemMapper.updateIngredientItem(ingredientItem,request);
        ingredientItemRepository.save(ingredientItem);
        return ingredientItemMapper.toResponse(ingredientItem);
    }

    @Override
    @Transactional(timeout = 3)
    public boolean deleteIngredientItemById(Long ingredientItemId) {
        IngredientItem ingredientItem = ingredientItemRepository.findById(ingredientItemId)
                        .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Item not found"));
         ingredientItemRepository.delete(ingredientItem);
         return true;
    }

    @Override
    @Transactional(timeout = 3)
    public boolean updateStoke(Long ingredientItemId) {
       IngredientItem ingredientItem =  ingredientItemRepository.findById(ingredientItemId)
                .orElseThrow(() -> new ResourceNotAvailableException("Ingredient Item not found"));
       ingredientItem.setInStoke(!ingredientItem.isInStoke());
       ingredientItemRepository.save(ingredientItem);
        return true;
    }
}
