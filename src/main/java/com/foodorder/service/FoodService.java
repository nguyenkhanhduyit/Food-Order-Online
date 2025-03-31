package com.foodorder.service;

import com.foodorder.dto.request.FoodRequest;
import com.foodorder.dto.response.FoodResponse;
import com.foodorder.dto.response.TopMealsResponse;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.FoodCategoryMapper;
import com.foodorder.mapper.IngredientItemMapper;
import com.foodorder.model.Food;
import com.foodorder.model.FoodCategory;
import com.foodorder.model.IngredientItem;
import com.foodorder.model.Restaurant;
import com.foodorder.repository.FoodCategoryRepository;
import com.foodorder.mapper.FoodMapper;
import com.foodorder.repository.FoodRepository;
import com.foodorder.repository.IngredientItemRepository;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.service.iservice.IFoodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class FoodService implements IFoodService {

    FoodMapper foodMapper;
    RestaurantRepository restaurantRepository;
    FoodCategoryRepository foodCategoryRepository;
    FoodRepository foodRepository;
    IngredientItemRepository ingredientItemRepository;
    FoodCategoryMapper foodCategoryMapper;
    IngredientItemMapper ingredientItemMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,
    rollbackFor = {
            Exception.class,
            ResourceNotAvailableException.class
    })
    public FoodResponse createFood(FoodRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"));
        Food newFood = foodMapper.toFood(request);
        newFood.setIngredientItems(
        request.getIngredientItemsId().stream().map(
        ingredientItemId ->
        ingredientItemRepository.findById(ingredientItemId)
            .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Item Not Found"))
            ).toList());
        newFood.setFoodCategory(foodCategoryRepository.findById(request.getFoodCategoryId())
                .orElseThrow(()-> new ResourceNotAvailableException("Food Category not found")));
        newFood.setRestaurant(restaurant);
        restaurant.getFoods().add(newFood);
        newFood.setCreationDate(new Date());
        foodRepository.save(newFood);

        FoodResponse foodResponse = foodMapper.toResponse(newFood);
        foodResponse.setId(newFood.getId());
        foodResponse.setFoodCategoryResponse(foodCategoryMapper.toResponse(newFood.getFoodCategory()));
        foodResponse.setIngredientItemsResponse(
                newFood.getIngredientItems().stream()
                .map(ingredientItemMapper::toResponse)
                .toList());
        return foodResponse;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,
            rollbackFor = {
                    Exception.class,
                    ResourceNotAvailableException.class
            })
    public boolean deleteFood(Long foodId) {
        foodRepository.deleteById(foodId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodResponse> getAllFoodsInRestaurant(Long restaurantId,
                                                      Long foodCategory,
                                                      boolean isAvailable,
                                                      boolean isVegetarian,
                                                      boolean isSeasonal) {
       List<Food> foods = restaurantRepository.findById(restaurantId)
               .orElseThrow(()-> new ResourceNotAvailableException("Food not found"))
               .getFoods();

       return filterFood(foods,foodCategory,isAvailable,isVegetarian,isSeasonal)
               .stream()
               .map(foodMapper::toResponse)
               .toList();
    }
    private List<Food> filterFood(List<Food> foods,
                                  Long foodCategory,
                                  boolean isAvailable,
                                  boolean isVegetarian,
                                  boolean isSeasonal) {
        return foods.stream()
                .filter(food ->
food.getFoodCategory().getId().equals(foodCategory)
        && food.isAvailable()== isAvailable
                        && food.isVegetarian() == isVegetarian
                                    && food.isSeasonal() == isSeasonal
        ).toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<FoodResponse> searchFood(String query) {
        return foodRepository.searchFoodByQuery(query).stream()
                .map(foodMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FoodResponse findFoodById(Long foodId) {
        return foodMapper.toResponse(foodRepository.findById(foodId)
                .orElseThrow(()-> new ResourceNotAvailableException("Food not found")));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,
            rollbackFor = {
                    Exception.class,
                    ResourceNotAvailableException.class
            })
    public FoodResponse updateFood(Long foodId,FoodRequest request) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(()-> new ResourceNotAvailableException("Food not found"));
        food = foodMapper.updateFood(food,request);
        List<IngredientItem> ingredientItems = new ArrayList<>();
        request.getIngredientItemsId().forEach(
                ingredientItemId ->
                        ingredientItems.add(ingredientItemRepository.findById(ingredientItemId)
                        .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Item Not Found with ingredient item id")))
        );
        food.setIngredientItems(ingredientItems);
        FoodCategory foodCategory = foodCategoryRepository.findById(request.getFoodCategoryId())
                .orElseThrow(()-> new ResourceNotAvailableException("FoodCategory not found with this id "));
        food.setFoodCategory(foodCategory);
        return foodMapper.toResponse(foodRepository.save(food));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,
            rollbackFor = {
                    Exception.class,
                    ResourceNotAvailableException.class
            })
    public boolean updateAvailableFoodState(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(()-> new ResourceNotAvailableException("Food not found "));
        food.setAvailable(!food.isAvailable());
        foodRepository.save(food);
        return true;
    }

//    public List<TopMealsResponse> filterTopMeal(){
//        List<Food> foods = foodRepository.findAll();
//        return List.of(null);
//    }

}
