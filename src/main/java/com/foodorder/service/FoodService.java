package com.foodorder.service;

import com.foodorder.dto.request.FoodRequest;
import com.foodorder.dto.request.QueryRequest;
import com.foodorder.dto.response.FoodResponse;
import com.foodorder.dto.response.IngredientCategoryResponse;
import com.foodorder.dto.response.IngredientItemInIngredientCategory;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.FoodCategoryMapper;
import com.foodorder.mapper.IngredientItemMapper;
import com.foodorder.model.*;
import com.foodorder.repository.*;
import com.foodorder.mapper.FoodMapper;
import com.foodorder.service.iservice.IFoodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    CloudinaryService cloudinaryService;
    UserService userService;
    IngredientCategoryRepository ingredientCategoryRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 20, rollbackFor = {Exception.class})
    public FoodResponse createFood(String token, FoodRequest request) {
        // Get restaurant from user with restaurant ID
        Restaurant restaurant = userService.findUserByToken(token)
                .getRestaurants()
                .stream()
                .filter(r -> r.getId().equals(request.getRestaurantId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotAvailableException("Restaurant not found"));

        // Map request to Food entity
        Food newFood = foodMapper.toFood(request);

        // Set restaurant
        newFood.setRestaurant(restaurant);

        // Set food category
        FoodCategory foodCategory = foodCategoryRepository.findById(request.getFoodCategoryId())
                .orElseThrow(() -> new ResourceNotAvailableException("Food Category not found"));
        // Optional: Verify foodCategory belongs to the restaurant
        if (!foodCategory.getRestaurant().getId().equals(restaurant.getId())) {
            throw new ResourceNotAvailableException("Food Category does not belong to the specified restaurant");
        }
        newFood.setFoodCategory(foodCategory);

        // Set ingredient categories
        List<IngredientCategory> ingredientCategories = new ArrayList<>();
        if (request.getIngredientCategoryId() != null && !request.getIngredientCategoryId().isEmpty()) {
            ingredientCategories = request.getIngredientCategoryId()
                    .stream()
                    .map(categoryId -> {
                        IngredientCategory category = ingredientCategoryRepository.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotAvailableException("Ingredient Category not found with ID: " + categoryId));
                        // Verify category belongs to the restaurant
                        if (!category.getRestaurant().getId().equals(restaurant.getId())) {
                            throw new ResourceNotAvailableException("Ingredient Category with ID: " + categoryId + " does not belong to the restaurant");
                        }
                        return category;
                    })
                    .toList();
            newFood.setIngredientCategories(ingredientCategories);
        } else {
            newFood.setIngredientCategories(new ArrayList<>()); // Initialize empty list
        }

        // Set ingredient items
        List<IngredientItem> ingredientItems = new ArrayList<>();
        if (request.getIngredientItemsId() != null && !request.getIngredientItemsId().isEmpty()) {
            // Get the set of valid category IDs for validation
            Set<Long> validCategoryIds = ingredientCategories.stream()
                    .map(IngredientCategory::getId)
                    .collect(Collectors.toSet());

            ingredientItems = request.getIngredientItemsId()
                    .stream()
                    .map(itemId -> {
                        IngredientItem item = ingredientItemRepository.findById(itemId)
                                .orElseThrow(() -> new ResourceNotAvailableException("Ingredient Item not found with ID: " + itemId));
                        // Verify item belongs to the restaurant
                        if (!item.getIngredientCategory().getRestaurant().getId().equals(restaurant.getId())) {
                            throw new ResourceNotAvailableException("Ingredient Item with ID: " + itemId + " does not belong to the restaurant");
                        }
                        // Verify item belongs to one of the provided categories (if categories are provided)
                        if (!validCategoryIds.isEmpty() && !validCategoryIds.contains(item.getIngredientCategory().getId())) {
                            throw new ResourceNotAvailableException("Ingredient Item with ID: " + itemId + " does not belong to any of the provided Ingredient Categories");
                        }
                        return item;
                    })
                    .toList();
            newFood.setIngredientItems(ingredientItems);
        } else {
            newFood.setIngredientItems(new ArrayList<>()); // Initialize empty list
        }

        // Set images
        try {
            if (request.getImageLogo() != null && !request.getImageLogo().isEmpty()) {
                newFood.setImageUrl(cloudinaryService.uploadImage(request.getImageLogo()));
            }
            if (request.getImagesGallery() != null && !request.getImagesGallery().isEmpty()) {
                List<String> galleryUrls = request.getImagesGallery()
                        .stream()
                        .filter(file -> !file.isEmpty())
                        .map(file -> {
                            try {
                                return cloudinaryService.uploadImage(file);
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to upload gallery image", e);
                            }
                        })
                        .toList();
                newFood.setGalleryUrls(galleryUrls);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }

        // Set creation date
        newFood.setCreationDate(new Date());

        // Save food
        Food savedFood = foodRepository.save(newFood);

        // Add food to restaurant's foods list (optional, depending on whether restaurant.foods is managed)
        restaurant.getFoods().add(savedFood);

        // Prepare response
        FoodResponse foodResponse = foodMapper.toResponse(savedFood);
        foodResponse.setId(savedFood.getId());
        foodResponse.setFoodCategoryResponse(foodCategoryMapper.toResponse(savedFood.getFoodCategory()));
        List<IngredientItemInIngredientCategory> ingredientItemInIngredientCategories = new ArrayList<>();
        ingredientItemInIngredientCategories = savedFood.getIngredientCategories().stream()
                        .map(
            ingredientCategory -> {
IngredientItemInIngredientCategory ingredientItemInIngredientCategory = new IngredientItemInIngredientCategory();
                ingredientItemInIngredientCategory.setIngredientCategoryResponse(
                        IngredientCategoryResponse.builder()
                                .id(ingredientCategory.getId())
                                .name(ingredientCategory.getName())
                                .build()
                );
                ingredientItemInIngredientCategory.setIngredientItemResponses(
                        ingredientCategory.getIngredientItems()
                                .stream()
                                .filter(
                                        i -> savedFood.getIngredientItems().contains(i)
                                )
                                .map(
                                        ingredientItemMapper::toResponse
                                )
                                .toList()
                );
                return ingredientItemInIngredientCategory;
            }
                        )
                                .toList();
        foodResponse.setIngredientItemInIngredientCategories(ingredientItemInIngredientCategories);
        foodResponse.setImageUrl(savedFood.getImageUrl());
        foodResponse.setGalleryUrls(savedFood.getGalleryUrls());
        return foodResponse;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 20, rollbackFor = {Exception.class})
    public FoodResponse updateFood(String token, Long foodId, FoodRequest request) {
        // Lấy restaurant từ token và restaurantId
        Restaurant restaurant = userService.findUserByToken(token)
                .getRestaurants()
                .stream()
                .filter(r -> r.getId().equals(request.getRestaurantId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotAvailableException("Không tìm thấy nhà hàng"));

        // Lấy food từ repository (hiệu quả hơn so với lọc qua restaurant.getFoods())
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotAvailableException("Không tìm thấy món ăn để cập nhật"));

        // Xác thực món ăn thuộc về nhà hàng
        if (!food.getRestaurant().getId().equals(restaurant.getId())) {
            throw new ResourceNotAvailableException("Món ăn không thuộc nhà hàng được chỉ định");
        }

        // Cập nhật các trường cơ bản bằng mapper
        food = foodMapper.updateFood(food, request);

        // Cập nhật danh mục món ăn (food category) nếu thay đổi
        if (!food.getFoodCategory().getId().equals(request.getFoodCategoryId())) {
            FoodCategory foodCategory = foodCategoryRepository.findById(request.getFoodCategoryId())
                    .orElseThrow(() -> new ResourceNotAvailableException("Không tìm thấy danh mục món ăn"));
            // Xác thực danh mục món ăn thuộc nhà hàng
            if (!foodCategory.getRestaurant().getId().equals(restaurant.getId())) {
                throw new ResourceNotAvailableException("Danh mục món ăn không thuộc nhà hàng được chỉ định");
            }
            food.setFoodCategory(foodCategory);
        }

        // Cập nhật danh mục nguyên liệu (ingredient categories)
        List<IngredientCategory> ingredientCategories = new ArrayList<>();
        if (request.getIngredientCategoryId() != null && !request.getIngredientCategoryId().isEmpty()) {
            ingredientCategories = request.getIngredientCategoryId()
                    .stream()
                    .map(categoryId -> {
                        IngredientCategory category = ingredientCategoryRepository.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotAvailableException("Không tìm thấy danh mục nguyên liệu với ID: " + categoryId));
                        // Xác thực danh mục nguyên liệu thuộc nhà hàng
                        if (!category.getRestaurant().getId().equals(restaurant.getId())) {
                            throw new ResourceNotAvailableException("Danh mục nguyên liệu với ID: " + categoryId + " không thuộc nhà hàng");
                        }
                        return category;
                    })
                    .toList();
            // Thay bằng danh sách mutable mới để tránh UnsupportedOperationException
            food.setIngredientCategories(new ArrayList<>(ingredientCategories));
        }

        // Cập nhật nguyên liệu (ingredient items)
        List<IngredientItem> ingredientItems = new ArrayList<>();
        if (request.getIngredientItemsId() != null && !request.getIngredientItemsId().isEmpty()) {
            // Lấy tập hợp ID danh mục hợp lệ để xác thực
            Set<Long> validCategoryIds = ingredientCategories.stream()
                    .map(IngredientCategory::getId)
                    .collect(Collectors.toSet());

            ingredientItems = request.getIngredientItemsId()
                    .stream()
                    .map(itemId -> {
                        IngredientItem item = ingredientItemRepository.findById(itemId)
                                .orElseThrow(() -> new ResourceNotAvailableException("Không tìm thấy nguyên liệu với ID: " + itemId));
                        // Xác thực nguyên liệu thuộc nhà hàng
                        if (!item.getIngredientCategory().getRestaurant().getId().equals(restaurant.getId())) {
                            throw new ResourceNotAvailableException("Nguyên liệu với ID: " + itemId + " không thuộc nhà hàng");
                        }
                        // Xác thực nguyên liệu thuộc một trong các danh mục được cung cấp (nếu có)
                        if (!validCategoryIds.isEmpty() && !validCategoryIds.contains(item.getIngredientCategory().getId())) {
                            throw new ResourceNotAvailableException("Nguyên liệu với ID: " + itemId + " không thuộc danh mục nguyên liệu được cung cấp");
                        }
                        return item;
                    })
                    .toList();
            // Thay bằng danh sách mutable mới để tránh UnsupportedOperationException
            food.setIngredientItems(new ArrayList<>(ingredientItems));
        }

        // Cập nhật hình ảnh
        try {
            if (request.getImageLogo() != null && !request.getImageLogo().isEmpty()) {
                food.setImageUrl(cloudinaryService.uploadImage(request.getImageLogo()));
            }
            if (request.getImagesGallery() != null && !request.getImagesGallery().isEmpty()) {
                List<String> galleryUrls = request.getImagesGallery()
                        .stream()
                        .filter(file -> !file.isEmpty())
                        .map(file -> {
                            try {
                                return cloudinaryService.uploadImage(file);
                            } catch (Exception e) {
                                throw new RuntimeException("Không thể tải lên hình ảnh thư viện", e);
                            }
                        })
                        .toList();
                food.setGalleryUrls(new ArrayList<>(galleryUrls)); // Đảm bảo danh sách mutable
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải lên hình ảnh", e);
        }

        Food updatedFood = foodRepository.save(food);

        FoodResponse foodResponse = foodMapper.toResponse(updatedFood);
        foodResponse.setFoodCategoryResponse(foodCategoryMapper.toResponse(updatedFood.getFoodCategory()));
        List<IngredientItemInIngredientCategory> ingredientItemInIngredientCategories = updatedFood.getIngredientCategories()
                .stream()
                .map(ingredientCategory -> {
                    IngredientItemInIngredientCategory ingredientItemInIngredientCategory = new IngredientItemInIngredientCategory();
                    ingredientItemInIngredientCategory.setIngredientCategoryResponse(
                            IngredientCategoryResponse.builder()
                                    .id(ingredientCategory.getId())
                                    .name(ingredientCategory.getName())
                                    .build()
                    );
                    ingredientItemInIngredientCategory.setIngredientItemResponses(
                            ingredientCategory.getIngredientItems()
                                    .stream()
                                    .filter(i -> updatedFood.getIngredientItems().contains(i))
                                    .map(ingredientItemMapper::toResponse)
                                    .toList()
                    );
                    return ingredientItemInIngredientCategory;
                })
                .toList();
        foodResponse.setIngredientItemInIngredientCategories(ingredientItemInIngredientCategories);
        foodResponse.setImageUrl(updatedFood.getImageUrl());
        foodResponse.setGalleryUrls(updatedFood.getGalleryUrls());
        return foodResponse;
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20,
            rollbackFor = {
                    Exception.class,
            })
    public boolean deleteFood(String token,Long restaurantId,Long foodId) {
        Restaurant restaurant = userService.findUserByToken(token).getRestaurants()
                        .stream().filter(r -> r.getId().equals(restaurantId))
                        .findFirst()
                                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found"));
       Food food = restaurant.getFoods().stream().filter(f -> f.getId().equals(foodId))
                        .findFirst()
                .orElseThrow(()-> new ResourceNotAvailableException("Food not belong to Restaurant"));
        cloudinaryService.deleteImage(food.getImageUrl());
        food.getGalleryUrls().forEach(cloudinaryService::deleteImage);
        foodRepository.deleteById(foodId);
        return true;
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodResponse> getAllFoods() {
        return foodRepository.findAll().stream()
                .map(food -> {
                    FoodResponse foodResponse = foodMapper.toResponse(food);
                    foodResponse.setFoodCategoryResponse(foodCategoryMapper.toResponse(food.getFoodCategory()));
                    List<IngredientItemInIngredientCategory> ingredientItemInIngredientCategories = food.getIngredientCategories()
                            .stream()
                            .map(ingredientCategory -> {
                                IngredientItemInIngredientCategory itemInCategory = new IngredientItemInIngredientCategory();
                                itemInCategory.setIngredientCategoryResponse(
                                        IngredientCategoryResponse.builder()
                                                .id(ingredientCategory.getId())
                                                .name(ingredientCategory.getName())
                                                .build()
                                );
                                itemInCategory.setIngredientItemResponses(
                                        ingredientCategory.getIngredientItems()
                                                .stream()
                                                .filter(i -> food.getIngredientItems().contains(i))
                                                .map(ingredientItemMapper::toResponse)
                                                .toList()
                                );
                                return itemInCategory;
                            })
                            .toList();
                    foodResponse.setIngredientItemInIngredientCategories(ingredientItemInIngredientCategories);
                    foodResponse.setImageUrl(food.getImageUrl());
                    foodResponse.setGalleryUrls(food.getGalleryUrls());
                    return foodResponse;
                })
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<FoodResponse> getAllFoodsInRestaurant(Long restaurantId,
                                                      Long foodCategory,
                                                      Boolean isVegetarian,
                                                      Boolean isSeasonal) {
        List<Food> foods = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotAvailableException("Restaurant not found with this id: " + restaurantId))
            .getFoods();

        List<Food> filteredFoods = filterFood(foods, foodCategory, isVegetarian, isSeasonal);

        return filteredFoods.stream()
        .map(food -> {
            FoodResponse foodResponse = foodMapper.toResponse(food);
            foodResponse.setFoodCategoryResponse(foodCategoryMapper.toResponse(food.getFoodCategory()));
List<IngredientItemInIngredientCategory> ingredientItemInIngredientCategories = food.getIngredientCategories()
                    .stream()
                    .map(ingredientCategory -> {
                        IngredientItemInIngredientCategory itemInCategory = new IngredientItemInIngredientCategory();
                        itemInCategory.setIngredientCategoryResponse(
                                IngredientCategoryResponse.builder()
                                        .id(ingredientCategory.getId())
                                        .name(ingredientCategory.getName())
                                        .build()
                        );
                        itemInCategory.setIngredientItemResponses(
                                ingredientCategory.getIngredientItems()
                                        .stream()
                                        .filter(i -> food.getIngredientItems().contains(i))
                                        .map(ingredientItemMapper::toResponse)
                                        .toList()
                        );
                        return itemInCategory;
                    })
                    .toList();
            foodResponse.setIngredientItemInIngredientCategories(ingredientItemInIngredientCategories);
            foodResponse.setImageUrl(food.getImageUrl());
            foodResponse.setGalleryUrls(food.getGalleryUrls());
            return foodResponse;
        })
        .toList();
    }

    private List<Food> filterFood(List<Food> foods,
                                  Long foodCategory,
                                  Boolean isVegetarian,
                                  Boolean isSeasonal) {
        return foods.stream()
                .filter(food -> {
                    boolean matchesCategory = foodCategory == null ||
                            (food.getFoodCategory() != null && food.getFoodCategory().getId().equals(foodCategory));

                    boolean matchesVegetarian = isVegetarian == null || food.isVegetarian() == isVegetarian;

                    boolean matchesSeasonal = isSeasonal == null || food.isSeasonal() == isSeasonal;

                    return matchesCategory && matchesVegetarian && matchesSeasonal;
                })
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<FoodResponse> searchFood(QueryRequest query) {
        return foodRepository.searchFoodByQuery(query.getQuery()).stream()
                .map(food -> {
                    FoodResponse foodResponse = foodMapper.toResponse(food);
                    foodResponse.setFoodCategoryResponse(foodCategoryMapper.toResponse(food.getFoodCategory()));
                    List<IngredientItemInIngredientCategory> ingredientItemInIngredientCategories = food.getIngredientCategories()
                            .stream()
                            .map(ingredientCategory -> {
                                IngredientItemInIngredientCategory itemInCategory = new IngredientItemInIngredientCategory();
                                itemInCategory.setIngredientCategoryResponse(
                                        IngredientCategoryResponse.builder()
                                                .id(ingredientCategory.getId())
                                                .name(ingredientCategory.getName())
                                                .build()
                                );
                                itemInCategory.setIngredientItemResponses(
                                        ingredientCategory.getIngredientItems()
                                                .stream()
                                                .filter(i -> food.getIngredientItems().contains(i))
                                                .map(ingredientItemMapper::toResponse)
                                                .toList()
                                );
                                return itemInCategory;
                            })
                            .toList();
                    foodResponse.setIngredientItemInIngredientCategories(ingredientItemInIngredientCategories);
                    foodResponse.setImageUrl(food.getImageUrl());
                    foodResponse.setGalleryUrls(food.getGalleryUrls());
                    return foodResponse;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FoodResponse findFoodById(Long foodId) {
       Food food = foodRepository.findById(foodId)
                .orElseThrow(()-> new ResourceNotAvailableException("Food not found"));
        // Prepare response
        FoodResponse foodResponse = foodMapper.toResponse(food);
        foodResponse.setId(food.getId());
        foodResponse.setFoodCategoryResponse(foodCategoryMapper.toResponse(food.getFoodCategory()));
        List<IngredientItemInIngredientCategory> ingredientItemInIngredientCategories = new ArrayList<>();
        ingredientItemInIngredientCategories = food.getIngredientCategories().stream()
                .map(
                ingredientCategory -> {
                    IngredientItemInIngredientCategory ingredientItemInIngredientCategory = new IngredientItemInIngredientCategory();
                    ingredientItemInIngredientCategory.setIngredientCategoryResponse(
                            IngredientCategoryResponse.builder()
                                    .id(ingredientCategory.getId())
                                    .name(ingredientCategory.getName())
                                    .build()
                    );
                    ingredientItemInIngredientCategory.setIngredientItemResponses(
                            ingredientCategory.getIngredientItems()
                                    .stream()
                                    .filter(
                                            i -> food.getIngredientItems().contains(i)
                                    )
                                    .map(
                                            ingredientItemMapper::toResponse
                                    )
                                    .toList()
                    );
                    return ingredientItemInIngredientCategory;
                        }
                )
                .toList();
        foodResponse.setIngredientItemInIngredientCategories(ingredientItemInIngredientCategories);
        foodResponse.setImageUrl(food.getImageUrl());
        foodResponse.setGalleryUrls(food.getGalleryUrls());
        return foodResponse;
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20,
            rollbackFor = {
                    Exception.class,
            })
    public FoodResponse updateAvailableFoodState(String token,Long restaurantId,Long foodId) {
        Restaurant restaurant = userService.findUserByToken(token)
                .getRestaurants()
                .stream()
                .filter(r -> r.getId().equals(restaurantId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotAvailableException("User not is owner this restaurant"));
        Food food = restaurant.getFoods()
                .stream()
                .filter(f -> f.getId().equals(foodId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotAvailableException("Food not belong to restaurant"));
        food.setAvailable(!food.isAvailable());
        foodRepository.save(food); // Prepare response
        FoodResponse foodResponse = foodMapper.toResponse(food);
        foodResponse.setId(food.getId());
        foodResponse.setFoodCategoryResponse(foodCategoryMapper.toResponse(food.getFoodCategory()));
        List<IngredientItemInIngredientCategory> ingredientItemInIngredientCategories = new ArrayList<>();
        ingredientItemInIngredientCategories = food.getIngredientCategories().stream()
                .map(
                        ingredientCategory -> {
                            IngredientItemInIngredientCategory ingredientItemInIngredientCategory = new IngredientItemInIngredientCategory();
                            ingredientItemInIngredientCategory.setIngredientCategoryResponse(
                                    IngredientCategoryResponse.builder()
                                            .id(ingredientCategory.getId())
                                            .name(ingredientCategory.getName())
                                            .build()
                            );
                            ingredientItemInIngredientCategory.setIngredientItemResponses(
                                    ingredientCategory.getIngredientItems()
                                            .stream()
                                            .filter(
                                                    i -> food.getIngredientItems().contains(i)
                                            )
                                            .map(
                                                    ingredientItemMapper::toResponse
                                            )
                                            .toList()
                            );
                            return ingredientItemInIngredientCategory;
                        }
                )
                .toList();
        foodResponse.setIngredientItemInIngredientCategories(ingredientItemInIngredientCategories);
        foodResponse.setImageUrl(food.getImageUrl());
        foodResponse.setGalleryUrls(food.getGalleryUrls());
        return foodResponse;
    }

}
