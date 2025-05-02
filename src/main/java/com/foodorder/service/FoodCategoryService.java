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
    UserService userService;

    static final String CREATE_SUCCESS = " Created Food Category Successfully !!!";


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20,rollbackFor = Exception.class)
    public String createFoodCategory(String token,Long restaurantId,FoodCategoryRequest request) {
        // assign request to FoodCategory
        FoodCategory newFooCategory = foodCategoryMapper.toFoodCategory(request);
        // find restaurant from restaurant id if not found throw exception
        Restaurant restaurant = userService.findUserByToken(token)
                        .getRestaurants()
                .stream()
                .filter(i->i.getId().toString().equals(restaurantId.toString()))
                .findFirst()
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant Not Found With This Id"));

        newFooCategory.setRestaurant(restaurant);
        //check whether FoodCategory already exist in Restaurant yet?
        restaurant.getFoodCategories().forEach(
                foodCategory -> {
                    if(foodCategory.getName().equalsIgnoreCase(newFooCategory.getName()))
                        throw new ResourceAlreadyExistException("FoodCategory name already");
                }
        );
        restaurant.getFoodCategories().add(newFooCategory);
        restaurantRepository.save(restaurant);
        return CREATE_SUCCESS;
        /*Updated and test completed all*/
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20,rollbackFor = Exception.class)
    public FoodCategoryResponse updateFoodCategory(String token,
                                                   Long restaurantId,
                                                   Long foodCategoryId,
                                                   FoodCategoryRequest request) {
        //find Restaurant from Db
        Restaurant restaurant = userService.findUserByToken(token)
                .getRestaurants()
                .stream()
                .filter(i -> i.getId().toString().equals(restaurantId.toString()))
                .findFirst()
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant Not Found With This Id"));
        // get FoodCategory from Restaurant
        FoodCategory foodCategory = restaurant.getFoodCategories()
                .stream()
                .filter(i -> i.getId().toString().equals(foodCategoryId.toString()))
                .findFirst()
                .orElseThrow(()-> new ResourceNotAvailableException("FoodCategory Not Found By This Id"));
        foodCategory.setName(request.getName());
        restaurantRepository.save(restaurant);
        return foodCategoryMapper.toResponse(foodCategory);
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20,rollbackFor = Exception.class)
    public boolean deleteFoodCategory(String token,Long restaurantId,Long foodCategoryId) {
        Restaurant restaurant = userService.findUserByToken(token)
                .getRestaurants()
                .stream()
                .filter(i -> i.getId().toString().equals(restaurantId.toString()))
                .findFirst()
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant Not Found With This Id"));
        FoodCategory foodCategory = restaurant.getFoodCategories()
                .stream()
                .filter(i -> i.getId().toString().equals(foodCategoryId.toString()))
                .findFirst()
                .orElseThrow(()-> new ResourceNotAvailableException("FoodCategory Not Found By This Id"));
        restaurant.getFoodCategories().remove(foodCategory);
        restaurantRepository.save(restaurant);
        return true;
        /*Updated and test completed all*/
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
                                    foodCategory.getId().toString().equals(foodCategoryId.toString()))
                        .findFirst()
                        .orElseThrow(()-> new ResourceNotAvailableException("FoodCategory not found with this id"))
        );
    }
}
