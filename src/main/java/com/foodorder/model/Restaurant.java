package com.foodorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurants",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "instagram"),
        @UniqueConstraint(columnNames = "x"),
        @UniqueConstraint(columnNames = "mobile")
})
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User user;

    String name;

    String description;

    String cuisineType;

    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    List<FoodCategory> foodCategories = new ArrayList<>();

    @OneToOne(mappedBy = "restaurant",cascade = CascadeType.ALL,orphanRemoval = true)
    Address address;

    @Embedded
    Contact contact;

    String openTime;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "restaurant")
    List<Order> orders = new ArrayList<>();

    @ElementCollection
    @Column(length = 1000)
    List<String>images;

    LocalDateTime registrationDate;

    boolean isOpen = true;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL,orphanRemoval = true)
    List<Food> foods = new ArrayList<>();


    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL,orphanRemoval = true)
    List<IngredientCategory> ingredientCategory;
}
