package com.foodorder.service;

import com.foodorder.dto.request.IngredientItemRequest;
import com.foodorder.dto.response.IngredientItemResponse;
import com.foodorder.exception.declare.ResourceAlreadyExistException;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.IngredientItemMapper;
import com.foodorder.model.IngredientCategory;
import com.foodorder.model.IngredientItem;
import com.foodorder.model.Restaurant;
import com.foodorder.repository.IngredientCategoryRepository;
import com.foodorder.repository.IngredientItemRepository;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.service.iservice.IIngredientItemService;
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

    IngredientItemMapper ingredientItemMapper;
    IngredientItemRepository ingredientItemRepository;
    RestaurantRepository restaurantRepository;
    UserService userService;
    IngredientCategoryService ingredientCategoryService;

    static final String UPDATE_STOKE_SUCCESS = "Update Stoke Successfully !!!";
    static final String DELETE_SUCCESS = "Deleted Successfully !!!";

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20
    ,rollbackFor = Exception.class)
    public IngredientItemResponse createIngredientItem(
                                       String token,
                                       Long restaurantId,
                                       Long ingredientCategoryId,
                                       IngredientItemRequest request) {
        Restaurant restaurant = ingredientCategoryService.findRestaurantFromUser(
                userService.findUserByToken(token),restaurantId
        );
        IngredientCategory ingredientCategory = restaurant
                .getIngredientCategory()
                        .stream()
                        .filter(i ->
                                i.getId().equals(ingredientCategoryId)
                        )
                                .findFirst()
                        .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Category not found"));
        IngredientItem ingredientItem = new IngredientItem();
        ingredientItem.setIngredientCategory(ingredientCategory);
        ingredientItem.setName(ingredientItemMapper.toIngredientItem(request).getName());
        ingredientCategory.getIngredientItems().add(ingredientItem);
        restaurant = restaurantRepository.save(restaurant);
        return ingredientItemMapper.toResponse(restaurant.getIngredientCategory().stream()
            .filter(i -> i.getId().toString().equals(ingredientCategory.getId().toString()))
            .findFirst()
            .orElseThrow(() -> new ResourceNotAvailableException("Ingredient Category not found"))
            .getIngredientItems().stream()
            .filter(i -> i.getName().equalsIgnoreCase(ingredientItem.getName()))
            .findFirst()
            .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Item not found")));
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(readOnly = true)
    public IngredientItemResponse getIngredientItemById(Long restaurantId,Long ingredientCategoryId
            ,Long ingredientItemId) {
        return ingredientItemMapper.toResponse(
        restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"))
        .getIngredientCategory()
        .stream().filter(i -> i.getId().toString().equals(ingredientCategoryId.toString()))
        .findFirst()
        .orElseThrow(()->new ResourceNotAvailableException("Ingredient Category not found "))
        .getIngredientItems()
        .stream().filter(i->i.getId().toString().equals(ingredientItemId.toString()))
        .findFirst().orElseThrow(()-> new ResourceNotAvailableException("Ingredient Item not found"))
                );
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientItemResponse> getAllsIngredientItemByIngredientCategoryId(
            Long restaurantId,
            Long ingredientCategoryId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"))
                .getIngredientCategory()
                .stream().filter(i -> i.getId().toString().equals(ingredientCategoryId.toString()))
                .findFirst()
                .orElseThrow(()->new ResourceNotAvailableException("Ingredient Category not found "))
                .getIngredientItems()
                .stream()
                .map(ingredientItemMapper::toResponse).toList();
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public IngredientItemResponse updateIngredientItem(
            String token,Long restaurantId,Long ingredientCategoryId,
            Long ingredientItemId,IngredientItemRequest request) {
IngredientItem ingredientItem = findIngredientItemFromArgs(token, restaurantId, ingredientCategoryId, ingredientItemId);
        ingredientItem = ingredientItemMapper.updateIngredientItem(ingredientItem,request);
        ingredientItemRepository.save(ingredientItem);
        return ingredientItemMapper.toResponse(ingredientItem);
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(timeout = 20,isolation = Isolation.SERIALIZABLE)
    public String deleteIngredientItem(String token
            ,Long restaurantId,Long ingredientCategoryId
            ,Long ingredientItemId) {
        Restaurant restaurant = userService.findUserByToken(token)
            .getRestaurants()
            .stream()
            .filter(i ->i.getId().toString().equals(restaurantId.toString()))
            .findFirst()
            .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"));
        boolean isRemove = restaurant.getIngredientCategory()
            .stream().filter(i -> i.getId().toString().equals(ingredientCategoryId.toString()))
            .findFirst()
            .orElseThrow(()->new ResourceNotAvailableException("Ingredient Category not found "))
            .getIngredientItems().removeIf(i -> i.getId().toString().equals(ingredientItemId.toString()));
        if(!isRemove) throw new ResourceNotAvailableException("Ingredient Item not found to delete");
        restaurantRepository.save(restaurant);
         return DELETE_SUCCESS;
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(timeout = 20,isolation = Isolation.SERIALIZABLE)
    public String updateStoke( String token
            ,Long restaurantId,Long ingredientCategoryId
            ,Long ingredientItemId) {
        IngredientItem ingredientItem = findIngredientItemFromArgs(
        token, restaurantId, ingredientCategoryId, ingredientItemId);
       ingredientItem.setInStoke(!ingredientItem.isInStoke());
       ingredientItemRepository.save(ingredientItem);
        return UPDATE_STOKE_SUCCESS;
        /*Updated and test completed all*/
    }

    private IngredientItem findIngredientItemFromArgs(
            String token,Long restaurantId,Long ingredientCategoryId,Long ingredientItemId
    ){
    return userService.findUserByToken(token)
            .getRestaurants()
            .stream()
            .filter(i ->i.getId().toString().equals(restaurantId.toString()))
            .findFirst()
            .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"))
            .getIngredientCategory()
            .stream().filter(i -> i.getId().toString().equals(ingredientCategoryId.toString()))
            .findFirst()
            .orElseThrow(()->new ResourceNotAvailableException("Ingredient Category not found "))
            .getIngredientItems()
            .stream().filter(i->i.getId().toString().equals(ingredientItemId.toString()))
            .findFirst().orElseThrow(()-> new ResourceNotAvailableException("Ingredient Item not found"));
    }
}
