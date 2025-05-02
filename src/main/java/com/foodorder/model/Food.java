package com.foodorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "foods",uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id","name"}))
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",columnDefinition = "VARCHAR(255) COLLATE utf8mb4_general_ci")
    private String name;

    private String description;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FoodCategory foodCategory;

    @Column(length = 1000)
    String imageUrl; // URL ảnh món ăn từ Cloudinary

    @ElementCollection
            @CollectionTable(name = "food_gallery",joinColumns = @JoinColumn(name = "food_id"))
            @Column(name = "gallery",length = 1000)
    List<String> galleryUrls = new ArrayList<>();

    private boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id",nullable = false)
    private Restaurant restaurant;

    private boolean isVegetarian = false;

    private boolean isSeasonal = false;

    @ElementCollection
    @CollectionTable(name = "food_ingredient_categories",joinColumns = @JoinColumn(name = "food_id"))
    @Column(name = "ingredient_categories_id")
    private List<IngredientCategory> ingredientCategories = new ArrayList<>();

    @ManyToMany
    private List<IngredientItem> ingredientItems = new ArrayList<>();

    private Date creationDate;

    @OneToMany
    @JsonIgnore
    private List<CartItem> cartItem;

    @OneToMany
    @JsonIgnore
    private List<OrderItem> orderItem;
}
