package com.foodorder.service;

import com.foodorder.model.*;
import com.foodorder.dto.request.RestaurantRequest;
import com.foodorder.dto.response.FavoriteResponse;
import com.foodorder.dto.response.RestaurantResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.enums.ROLE;
import com.foodorder.exception.declare.ArgumentRequestInValid;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.*;
import com.foodorder.repository.AddressRepository;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    CloudinaryService cloudinaryService;
    UserService userService;

    static String CREATE_SUCCESS = "Create Restaurant Successfully";

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,
            timeout = 20,
            rollbackFor = Exception.class
    )
    public String createRestaurant(RestaurantRequest request, String token) {
        User owner = userService.findUserByToken(token);
        //count quantity restaurant created of user
        int countLimitingRestaurantWithEveryCustomer
                = restaurantRepository.CountLimitingRestaurantWithEveryCustomer(owner.getId());
        // just allow every user create with quantity restaurant = 2
        if(countLimitingRestaurantWithEveryCustomer == 2)
            throw new ResourceNotAvailableException("User Rate Limiting Restaurant With Every Customer");
        // create new object address to assign for restaurant
        Address address = new Address();
        address.setAddress(request.getAddress().getStreetAddress()+request.getAddress().getDistrict()+request.getAddress().getCity());
        // add a new role is restaurant owner
        owner.getRoles().add(ROLE.ROLE_RESTAURANT_OWNER.name());
        // convert contact request to contact
        Contact contact = contactMapper.toContact(request.getContact());
        //create new object restaurant to assign
        Restaurant restaurant = restaurantMapper.toRestaurant(request);
        log.info("Request Restaurant.logo :{}",request.getLogo());
        log.info("Request Restaurant.gallery :{}",request.getGallery());
        try {
            if(request.getLogo() != null && !request.getLogo().isEmpty())
                restaurant.setLogoUrl(cloudinaryService.uploadImage(request.getLogo()));
            if(request.getGallery() != null && !request.getGallery().isEmpty())
            {
                List<String> galleryUrls = request.getGallery()
                        .stream()
                        .filter(file -> !file.isEmpty())
                        .map(file -> {
                            try{
                                return cloudinaryService.uploadImage(file);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList();
                restaurant.setGalleryUrls(galleryUrls);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        restaurant.setUser(owner);
        restaurant.setAddress(address);
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setContact(contact);
        address.setRestaurant(restaurant);
        owner.getRestaurants().add(restaurant);
        userRepository.save(owner);// save user

        RestaurantResponse response = restaurantMapper.toResponse(restaurant);
        response.setLogoUrl(restaurant.getLogoUrl());
        response.setGalleryUrls(restaurant.getGalleryUrls());
        response.setId(owner.getRestaurants().getLast().getId());
        response.setAddress(addressMapper.toResponse(owner.getRestaurants().getLast().getAddress()));
        //convert contact to contact response
        response.setContact(contactMapper.toResponse(owner.getRestaurants().getLast().getContact()));
        UserResponse userResponse = userMapper.toUserResponse(owner);
        userResponse.setRoles(
                new HashSet<>(owner.getRoles())
        );
        response.setUser(userResponse);
        return CREATE_SUCCESS;
         /*
        Updated and test completed create restaurant, upload image, ...
        */
    }

    @Override
    @Transactional(timeout = 20,isolation = Isolation.SERIALIZABLE,
            rollbackFor = Exception.class
    )
    public RestaurantResponse updateRestaurant(Long restaurantId, String token, RestaurantRequest request) {
        Address address = addressRepository.findAddressByRestaurantId(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Address Not Found With Restaurant Id And Customer Id"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotAvailableException("Restaurant not found by this id"));

        User user = userService.findUserByToken(token);
        if(!restaurant.getUser().getId().toString().equals(user.getId().toString()))
            throw new ArgumentRequestInValid("User not is owner restaurant");

        restaurantMapper.updateRestaurant(restaurant,request);
        if(!request.getLogo().isEmpty()){
            cloudinaryService.deleteImage(restaurant.getLogoUrl());
            try {
                restaurant.setLogoUrl(cloudinaryService.uploadImage(request.getLogo()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(!request.getGallery().isEmpty())
        {
            if(restaurant.getGalleryUrls()!=null){
                //xoá ảnh trên Cloudinary
                restaurant.getGalleryUrls().forEach(cloudinaryService::deleteImage);
                restaurant.getGalleryUrls().clear();
            }else
                restaurant.setGalleryUrls(new ArrayList<>());
            //thêm ảnh mới
            List<String> galleryUrls = request.getGallery()
                    .stream()
                    .filter(file -> !file.isEmpty())
                    .map(file -> {
                        try{
                            return cloudinaryService.uploadImage(file);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            restaurant.getGalleryUrls().addAll(galleryUrls);
        }

        Contact contact = contactMapper.toContact(request.getContact());
        address.setAddress(request.getAddress().getStreetAddress()+request.getAddress().getDistrict()+request.getAddress().getCity());
        restaurant.setAddress(address);
        restaurant.setContact(contact);
        userRepository.save(user);
        restaurantRepository.save(restaurant);
        RestaurantResponse restaurantResponse = restaurantMapper.toResponse(restaurant);
        restaurantResponse.setLogoUrl(restaurant.getLogoUrl());
        restaurantResponse.setGalleryUrls(restaurant.getGalleryUrls());
        return restaurantResponse;
        /*
         Updated and test complete all
        */
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 20,
            rollbackFor = Exception.class
    )
    public void deleteRestaurant(String token,Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow( () -> new ResourceNotAvailableException("Restaurant not found by this id"));

        if(restaurant.getLogoUrl()!=null)
            cloudinaryService.deleteImage(restaurant.getLogoUrl());

        if(restaurant.getGalleryUrls()!=null)
            restaurant.getGalleryUrls().forEach(cloudinaryService::deleteImage);

        User owner = userService.findUserByToken(token);
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
        /*updated and test completed all*/
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 20,
            rollbackFor = Exception.class)
    public void updateRestaurantStatus(String token,Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found by this id"));
        User owner = userService.findUserByToken(token);
        if(restaurant.getUser() != owner)
            throw new ResourceNotAvailableException("User not is owner this restaurant");
        restaurant.setOpen(!restaurant.isOpen());
        restaurantRepository.save(restaurant);
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurantsByUserToken(String token) {

        User owner = userService.findUserByToken(token);

        List<Restaurant> restaurants = restaurantRepository.findAllByUserId(owner.getId());// list restaurant

        if(restaurants == null || restaurants.isEmpty())
            throw new ResourceNotAvailableException("Not Any Restaurant With User Id");

        return  restaurants
                .stream()
                .map(request -> {
                    RestaurantResponse response = restaurantMapper.toResponse(request);
                    response.setAddress(addressMapper.toResponse(request.getAddress()));
                    response.setContact(contactMapper.toResponse(request.getContact()));
                    response.setLogoUrl(request.getLogoUrl());
                    response.setGalleryUrls(request.getGalleryUrls());
                    return response;
                })
                .toList();
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();// list restaurant
        if(restaurants.isEmpty())
            throw new ResourceNotAvailableException("Not Any Restaurant Available");
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
                .orElseThrow(() -> new ResourceNotAvailableException("Restaurant Not Found by this id"));
        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 20)
    public FavoriteResponse addToFavorites(String token, Long restaurantId) {
        User user = userService.findUserByToken(token);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotAvailableException("Restaurant not found by this id"));
        Favorite favorite = Favorite.builder()
                .restaurantId(restaurant.getId())
                .foodId(null)
                .build();
        FavoriteResponse favoriteResponse = new FavoriteResponse();
        if(user.getFavorites().contains(favorite)) {
            user.getFavorites().remove(favorite);
        }
        else {
            user.getFavorites().add(favorite);
            favoriteResponse.setRestaurantName(restaurant.getName());
            if(!restaurant.getGalleryUrls().isEmpty())
                favoriteResponse.setImageUrl(restaurant.getGalleryUrls().getFirst());
            else favoriteResponse.setImageUrl(null);
        }
        userRepository.save(user);
        return favoriteResponse;
        /*Updated and test completed all*/
    }


}
