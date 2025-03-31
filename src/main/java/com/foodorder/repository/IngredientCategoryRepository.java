package com.foodorder.repository;

import com.foodorder.model.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory,Long> {

    @Query(nativeQuery = true, value = "SELECT * " +
            " FROM ingredient_category " +
            " WHERE ingredient_category.name = :name")
    boolean isExistIngredientCategory(String name);

    @Query(nativeQuery = true,
            value = " SELECT * "+
                    " FROM ingredient_category ic "+
                    " WHERE ic.restaurant_id = :restaurantId and ic.name LIKE CONCAT('%',:query,'%')")
    List<IngredientCategory> searchByQuery(Long restaurantId,String query);
}
