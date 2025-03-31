package com.foodorder.repository;

import com.foodorder.dto.response.CartResponse;
import com.foodorder.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    " FROM users u inner join carts c on u.id = c.user_id " +
                    " WHERE u.email = :userName AND c.id = :cartId ")
    Optional<Cart> checkUserOwnCart(String userName, Long cartId);
}
