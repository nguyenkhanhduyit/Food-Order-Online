package com.foodorder.repository;

import com.foodorder.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

   @Query(value = "SELECT o.*" +
           " FROM orders o " +
           "JOIN order_items oi ON o.id = oi.order_id " +
           " WHERE oi.restaurant_id = :restaurantId AND o.create_at BETWEEN :startDate AND :endDate",nativeQuery = true)
   List<Order> findAllByOrderItemsRestaurantIdAndCreateAtBetween(
           Long restaurantId,
           Date startDate,
            Date endDate);

   @Query(nativeQuery = true,
   value = " SELECT *" +
           " FROM orders" +
           " WHERE orders.user_id = :userId"
   )
   List<Order> findAllOrderOfUser(Long userId);
}
