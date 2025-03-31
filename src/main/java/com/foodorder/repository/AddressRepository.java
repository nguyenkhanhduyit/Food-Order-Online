package com.foodorder.repository;

import com.foodorder.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

        @Query(value = "SELECT * FROM addresses ad "
                + " WHERE ad.restaurant_id = :restaurantId",nativeQuery = true)
        Optional<Address> findAddressByRestaurantId(Long restaurantId);
}
