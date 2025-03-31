package com.foodorder.repository;

import com.foodorder.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

   public List<Order> findByUserId(Long userId);

   public List<Order> findByRestaurantId(Long restaurantId);
}
