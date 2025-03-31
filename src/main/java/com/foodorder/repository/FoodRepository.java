package com.foodorder.repository;

import com.foodorder.dto.response.FoodResponse;
import com.foodorder.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food,Long> {

    List<Food> findByRestaurantId(Long restaurantId);

    @Query(nativeQuery = true,
    value = "SELECT * "+
            " FROM foods "+
    " WHERE LOWER(foods.name) LIKE LOWER(CONCAT('%',:query,'%')) " +
            " or POSITION(LOWER(:query) in LOWER(foods.name)) > 0 ")
    List<Food> searchFoodByQuery(String query);
}
