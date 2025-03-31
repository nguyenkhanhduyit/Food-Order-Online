package com.foodorder.model;

import com.foodorder.enums.ROLE;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "cart")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String fullName;

    @Column(unique = true,nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    @ElementCollection
    Set<String> roles = new HashSet<>(Set.of(ROLE.ROLE_USER.name()));

    @OneToMany
    List<Order> orders = new ArrayList<>();

    @ElementCollection
    List<RestaurantDTO> favorites = new ArrayList<>();

    @OneToMany
    List<Address> address = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    Cart cart;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    List<Restaurant> restaurants = new ArrayList<>();
}