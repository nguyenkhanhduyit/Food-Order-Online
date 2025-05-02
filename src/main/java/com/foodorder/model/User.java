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

    //url to Cloudinary
    String avatarUrl;

    @Column(nullable = false)
    @ElementCollection
    @CollectionTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"))
    Set<String> roles = new HashSet<>(Set.of(ROLE.ROLE_USER.name()));

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    List<Order> orders = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_favorites",joinColumns = @JoinColumn(name = "user_id"))
    List<Favorite> favorites = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    List<Address> address = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    Cart cart;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    List<Restaurant> restaurants = new ArrayList<>();
}