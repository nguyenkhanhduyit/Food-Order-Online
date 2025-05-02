package com.foodorder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food_categories",uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id","name"}))
public class FoodCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name = "name",columnDefinition = "VARCHAR(255) COLLATE utf8mb4_general_ci")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id",nullable = false)
    private Restaurant restaurant;

    @OneToMany
    private List<Food> foods;
}
