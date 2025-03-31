package com.foodorder.service;

import com.foodorder.dto.request.FoodCategoryRequest;
import com.foodorder.dto.response.FoodCategoryResponse;
import com.foodorder.exception.declare.ResourceAlreadyExistException;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.FoodCategoryMapper;
import com.foodorder.model.FoodCategory;
import com.foodorder.model.Restaurant;
import com.foodorder.repository.FoodCategoryRepository;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.service.iservice.IFoodCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class FoodCategoryService implements IFoodCategoryService {

    FoodCategoryMapper foodCategoryMapper;
    FoodCategoryRepository foodCategoryRepository;
    RestaurantRepository restaurantRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,rollbackFor = Exception.class)
    public FoodCategoryResponse createFoodCategory(Long restaurantId,FoodCategoryRequest request) {
        // assign request to FoodCategory
        FoodCategory newFooCategory = foodCategoryMapper.toFoodCategory(request);
        // find restaurant from restaurant id if not found throw exception
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant Not Found With This Id"));
        newFooCategory.setRestaurant(restaurant);
        //check whether FoodCategory already exist in Restaurant yet?
        restaurant.getFoodCategories().forEach(
                foodCategory -> {
                    if(foodCategory.getName().equals(newFooCategory.getName().toLowerCase()))
                        throw new ResourceAlreadyExistException("FoodCategory Already");
                }
        );
        restaurant.getFoodCategories().add(newFooCategory);
        restaurantRepository.save(restaurant);
        return foodCategoryMapper.toResponse(newFooCategory);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,rollbackFor = Exception.class)
    public FoodCategoryResponse updateFoodCategory(Long restaurantId,Long foodCategoryId,FoodCategoryRequest request) {
        //find Restaurant from Db
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant Not Found By This Id"));
        // get FoodCategory from Restaurant
        FoodCategory foodCategory = foodCategoryRepository.findById(foodCategoryId)
                .orElseThrow(()-> new ResourceNotAvailableException("FoodCategory Not Found By This Id"));
        if(!restaurant.getFoodCategories().contains(foodCategory))
            throw new ResourceAlreadyExistException("FoodCategory Not Exist In Restaurant To Update");
        int index = restaurant.getFoodCategories().indexOf(foodCategory);
        restaurant.getFoodCategories().get(index).setName(request.getName());
        restaurantRepository.save(restaurant);
        foodCategory.setName(request.getName());
        return foodCategoryMapper.toResponse(foodCategory);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 3,rollbackFor = Exception.class)
    public boolean deleteFoodCategory(Long restaurantId,Long foodCategoryId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant Not Found By This Id"));
        FoodCategory foodCategory = foodCategoryRepository.findById(foodCategoryId)
                .orElseThrow(()-> new ResourceNotAvailableException("FoodCategory Not Found By This Id"));
        restaurant.getFoodCategories().remove(foodCategory);
        restaurantRepository.save(restaurant);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FoodCategoryResponse> getAllFoodCategoriesInRestaurant(Long restaurantId){
        return restaurantRepository.findById(restaurantId)
            .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"))
                .getFoodCategories().stream().map(foodCategoryMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FoodCategoryResponse getFoodCategoryById(Long restaurantId,Long foodCategoryId) {
        return foodCategoryMapper.toResponse(
                restaurantRepository.findById(restaurantId)
                        .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"))
                        .getFoodCategories()
                        .stream()
                        .filter(foodCategory ->
                                    foodCategory.getId().equals(foodCategoryId))
                        .findFirst()
                        .orElseThrow(()-> new ResourceNotAvailableException("FoodCategory not found"))
        );
    }
}
