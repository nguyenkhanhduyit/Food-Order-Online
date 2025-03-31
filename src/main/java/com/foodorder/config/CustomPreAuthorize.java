package com.foodorder.config;

import com.foodorder.dto.response.RestaurantResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.service.CartService;
import com.foodorder.service.RestaurantService;
import com.foodorder.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("CustomPreAuthorize")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CustomPreAuthorize {

    UserService userService;
    RestaurantService restaurantService;
    CartService cartService;

    public boolean isUserRequestingTheirOwnData(Authentication authentication,Long userId){
       UserResponse userResponse = userService.findUserById(userId);
       return userResponse != null && userResponse.getEmail().equals(authentication.getName());
    }

    public boolean isUserRequestingTheirRestaurantOwnData(Authentication authentication,Long restaurantId){
        RestaurantResponse restaurantResponse = restaurantService.findRestaurantByRestaurantId(restaurantId);
        UserResponse userResponse = userService.findUserByEmail(authentication.getName());
        return userResponse != null && restaurantResponse != null && userResponse.getId().equals(restaurantResponse.getUser().getId());
    }

    public boolean isUserRequestingTheirCartOwnData(Authentication authentication,Long cartId){
        return cartService.checkUserOwnerCart(authentication.getName(),cartId);
    }
}
