package com.foodorder.repository;

import com.foodorder.model.IngredientItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientItemRepository extends JpaRepository<IngredientItem,Long> {
}
