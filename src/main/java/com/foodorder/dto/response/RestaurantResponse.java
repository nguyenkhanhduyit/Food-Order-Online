package com.foodorder.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantResponse {
    Long id;

    UserResponse user;

    String name;

    String description;

    String cuisineType;

    AddressResponse address;

    ContactResponse contact;

    String openTime;

    List<String>images;

    LocalDateTime registrationDate;

}
