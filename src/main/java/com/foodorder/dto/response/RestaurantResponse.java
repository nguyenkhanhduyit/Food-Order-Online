package com.foodorder.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantResponse {
    Long id;

    UserResponse user;

    String name;

    String description;

    String cuisineType;

    AddressResponse address;

    ContactResponse contact;

    String openTime;

    String logoUrl;

    List<String>galleryUrls;

    LocalDateTime registrationDate;

}
