package com.foodorder.repository;

import com.foodorder.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    @Query(value = "SELECT * FROM restaurants r "
            +" WHERE lower(r.name) LIKE lower(concat('%',:query,'%')) "
            +" or lower(r.cuisine_type) LIKE lower(concat('%',:query,'%'))"
            ,nativeQuery = true)
    List<Restaurant> findBySearchString(String query);

    Optional<Restaurant> findByUserId(Long id);

    @Query(value = "SELECT COUNT(r.id) AS total_restaurants "
            + " FROM restaurants r " +
            " WHERE r.user_id = :userId ",nativeQuery = true)
    int CountLimitingRestaurantWithEveryCustomer(Long userId);

    @Query(value = "SELECT * FROM restaurants r "
            +" WHERE r.user_id = :userId"
            ,nativeQuery = true)
    List<Restaurant> findAllByUserId(Long userId);
}
