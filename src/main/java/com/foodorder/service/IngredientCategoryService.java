package com.foodorder.service;

import com.foodorder.dto.request.IngredientCategoryRequest;
import com.foodorder.dto.request.QueryRequest;
import com.foodorder.dto.response.IngredientCategoryResponse;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.IngredientCategoryMapper;
import com.foodorder.model.IngredientCategory;
import com.foodorder.model.Restaurant;
import com.foodorder.model.User;
import com.foodorder.repository.IngredientCategoryRepository;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.service.iservice.IIngredientCategoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class IngredientCategoryService implements IIngredientCategoryService {

    @PersistenceContext
    EntityManager entityManager;

    RestaurantRepository restaurantRepository;
    IngredientCategoryMapper ingredientCategoryMapper;
    IngredientCategoryRepository ingredientCategoryRepository;
    UserService userService;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,
    rollbackFor = Exception.class)
    public IngredientCategoryResponse createIngredientCategory(String token,Long restaurantId, IngredientCategoryRequest request) {
        User owner = userService.findUserByToken(token);
        Restaurant restaurant = this.findRestaurantFromUser(owner,restaurantId);
        IngredientCategory ingredientCategory = ingredientCategoryMapper.toIngredientCategory(request);
        ingredientCategory.setRestaurant(restaurant);
        restaurant.getIngredientCategory().add(ingredientCategory);
        restaurantRepository.save(restaurant);
        return IngredientCategoryResponse.builder()
                .id(restaurant.getIngredientCategory().stream()
                    .filter(i -> i.getName().equals(request.getName())) // nếu name là duy nhất
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("IngredientCategory not found after save"))
                    .getId()
                )
                .name(ingredientCategory.getName())
                .build();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,
            rollbackFor = Exception.class)
    public IngredientCategoryResponse updateIngredientCategory(
            String token,Long restaurantId,
            Long ingredientCategoryId, IngredientCategoryRequest request) {

        User owner = userService.findUserByToken(token);
        Restaurant restaurant = findRestaurantFromUser(owner,restaurantId);
        IngredientCategory ingredientCategory =
                restaurant.getIngredientCategory()
                        .stream()
                        .filter(i -> i.getId().toString().equals(ingredientCategoryId.toString()))
                        .findFirst()
                        .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Category not found"));
        ingredientCategory.setName(request.getName());
        restaurantRepository.save(restaurant);
        return IngredientCategoryResponse.builder()
                .id(ingredientCategory.getId())
                .name(ingredientCategory.getName())
                .build();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20,
            rollbackFor = Exception.class)
    public String deleteIngredientCategory(String token,Long restaurantId, Long ingredientCategoryId) {
        Restaurant restaurant = findRestaurantFromUser(userService.findUserByToken(token),restaurantId);
       boolean resultOfRemove = restaurant.getIngredientCategory()
               .removeIf(i ->i.getId().toString().equals(ingredientCategoryId.toString()));
       if(!resultOfRemove) throw new ResourceNotAvailableException("Ingredient Category not found to delete");
       restaurantRepository.save(restaurant);
       return "Ingredient Category deleted successfully";
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
        return
        ingredientCategoryMapper.toResponse(
                restaurantRepository.findById(restaurantId)
                        .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"))
                        .getIngredientCategory()
                .stream()
                .filter(ingredientCategory ->
                        ingredientCategory.getId().toString().equals(ingredientCategoryId.toString()))
                .findFirst().orElseThrow(()-> new ResourceNotAvailableException("Ingredient Category not found"))
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<IngredientCategoryResponse> getIngredientCategoriesByQuery(Long restaurantId, QueryRequest query) {
        return ingredientCategoryRepository.searchByQuery(restaurantId,query.getQuery())
                .stream()
                .map(ingredientCategoryMapper::toResponse)
                .toList();
    }

    public Restaurant findRestaurantFromUser(User owner,Long restaurantId){
       return owner.getRestaurants()
                .stream()
                .filter(i ->
                        i.getId().toString().equals(restaurantId.toString())
                ).findFirst()
                .orElseThrow(()-> new ResourceNotAvailableException("User not is owner any restaurant"));
    }

}
