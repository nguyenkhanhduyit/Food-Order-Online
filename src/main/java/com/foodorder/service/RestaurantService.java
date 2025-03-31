package com.foodorder.service;

import com.foodorder.model.*;
import com.foodorder.dto.request.RestaurantRequest;
import com.foodorder.dto.response.RestaurantDTOResponse;
import com.foodorder.dto.response.RestaurantResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.enums.ROLE;
import com.foodorder.exception.declare.ArgumentRequestInValid;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.*;
import com.foodorder.repository.AddressRepository;
import com.foodorder.repository.FoodRepository;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.repository.UserRepository;
import com.foodorder.service.iservice.IRestaurantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional(rollbackFor = {
        ResourceNotAvailableException.class,
        Exception.class,
        ArgumentRequestInValid.class
})
@Slf4j
public class RestaurantService implements IRestaurantService {

    RestaurantRepository restaurantRepository;
    AddressRepository addressRepository;
    UserRepository userRepository;
    RestaurantMapper restaurantMapper;
    AddressMapper addressMapper;
    ContactMapper contactMapper;
    UserMapper userMapper;
    FoodRepository foodRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,
            timeout = 2,
            rollbackFor = Exception.class
    )
    public RestaurantResponse createRestaurant(RestaurantRequest request, Long userId) {
        //count quantity restaurant created of user
        int countLimitingRestaurantWithEveryCustomer
                = restaurantRepository.CountLimitingRestaurantWithEveryCustomer(userId);
        // just allow every user create with quantity restaurant = 2
        if(countLimitingRestaurantWithEveryCustomer == 2)
            throw new ResourceNotAvailableException("User Rate Limiting Restaurant With Every Customer");
        // create new object address to assign for restaurant
        Address address = new Address();
        address.setAddress(request.getAddress().addressComplete());
        //find user with id pass
        User owner = userRepository.findById(userId)
        .orElseThrow( () -> new ResourceNotAvailableException("User to assign Restaurant Not Found"));
        // add a new role is restaurant owner
        owner.getRoles().add(ROLE.ROLE_RESTAURANT_OWNER.name());
        // convert contact request to contact
        Contact contact = contactMapper.toContact(request.getContact());
        //create new object restaurant to assign
        Restaurant restaurant = restaurantMapper.toRestaurant(request);
        restaurant.setUser(owner);
        restaurant.setAddress(address);
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setContact(contact);
        address.setRestaurant(restaurant);
        owner.getRestaurants().add(restaurant);
        userRepository.save(owner);// save user

        RestaurantResponse response = restaurantMapper.toResponse(restaurant);
        response.setId(owner.getRestaurants().getLast().getId());
        response.setAddress(addressMapper.toResponse(owner.getRestaurants().getLast().getAddress()));
        //convert contact to contact response
        response.setContact(contactMapper.toResponse(owner.getRestaurants().getLast().getContact()));
        UserResponse userResponse = userMapper.toUserResponse(owner);
        userResponse.setRoles(
                new HashSet<>(owner.getRoles())
        );
        response.setUser(userResponse);
        return response;
    }

    @Override
    @Transactional(timeout = 2,isolation = Isolation.SERIALIZABLE,
            rollbackFor = Exception.class
    )
    public RestaurantResponse updateRestaurant(Long restaurantId, Long userId, RestaurantRequest request) {
        Address address = addressRepository.findAddressByRestaurantId(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Address Not Found With Restaurant Id And Customer Id"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotAvailableException("Restaurant not found by this id"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotAvailableException("User not found by this username"));
        if(!restaurant.getUser().getId().toString().equals(user.getId().toString()))
            throw new ArgumentRequestInValid("User with this id not owner restaurant");

        restaurantMapper.updateRestaurant(restaurant,request);
        Contact contact = contactMapper.toContact(request.getContact());
        address.setAddress(request.getAddress().addressComplete());
        restaurant.setAddress(address);
        restaurant.setContact(contact);
        userRepository.save(user);
        restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(restaurant);
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 2,
            rollbackFor = Exception.class
    )
    public void deleteRestaurant(Long userId,Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow( () -> new ResourceNotAvailableException("Restaurant not found by this id"));
        User owner = userRepository.findById(userId)
                .orElseThrow( () -> new ResourceNotAvailableException("User to delete Restaurant Not Found"));
        if(!owner.getRestaurants().contains(restaurant))
            throw new ResourceNotAvailableException("User And Restaurant Owner Mismatch");
        owner.getRestaurants().remove(restaurant);
        if(owner.getRestaurants().isEmpty()){
           Set<String> roles = owner.getRoles()
                   .stream()
                   .filter(role -> !role.equals(ROLE.ROLE_RESTAURANT_OWNER.name()))
                   .collect(Collectors.toSet());
           owner.setRoles(roles);
        }
        userRepository.save(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();// list restaurant
        if(restaurants == null || restaurants.isEmpty())
            throw new ResourceNotAvailableException("Not Any Restaurant Available");
        return restaurants.stream().map(restaurantMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurantsByUserId(Long userId) {
        List<Restaurant> restaurants = restaurantRepository.findAllByUserId(userId);// list restaurant
        if(restaurants == null || restaurants.isEmpty())
            throw new ResourceNotAvailableException("Not Any Restaurant With User Id");
        return restaurants.stream().map(restaurantMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> searchRestaurant(String request) {
        List<Restaurant> restaurants = restaurantRepository.findBySearchString(request);
        if(restaurants == null || restaurants.isEmpty())
            throw new ResourceNotAvailableException("Not Any Restaurant Available");
        return restaurants.stream().map(restaurantMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse findRestaurantByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow( () -> new ResourceNotAvailableException("Restaurant Not Found by this id"));
        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 2)
    public RestaurantDTOResponse addToFavorites(Long userId,Long restaurantId,Long foodId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotAvailableException("User Not Found By User Id"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found by this id"));
        Food food = foodRepository.findById(foodId)
                .orElseThrow(()-> new ResourceNotAvailableException("Food not found"));

        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setRestaurantId(restaurant.getId());
        restaurantDTO.setFoodId(food.getId());
        restaurantDTO.setImagesJson(food.getImages());

        if(user.getFavorites().contains(restaurantDTO)) user.getFavorites().remove(restaurantDTO);

        else user.getFavorites().add(restaurantDTO);
        userRepository.save(user);
        RestaurantDTOResponse restaurantDTOResponse =
                RestaurantDTOResponse.builder()
                        .foodName(food.getName())
                        .build();
        restaurantDTOResponse.setImagesJson(restaurantDTO.getImagesJson());
        return restaurantDTOResponse;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 2,
    rollbackFor = Exception.class)
    public void updateRestaurantStatus(Long userId,Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found by this id"));
        User owner = userRepository.findById(userId)
                .orElseThrow( () -> new ResourceNotAvailableException("User Owner Restaurant Not Found By This Username"));
        if(restaurant.getUser() != owner)
            throw new ResourceNotAvailableException("User And Restaurant Owner Mismatch");
        restaurant.setOpen(!restaurant.isOpen());
        restaurantRepository.save(restaurant);
    }
}
