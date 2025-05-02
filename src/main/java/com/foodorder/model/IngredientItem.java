package com.foodorder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingredient_items",uniqueConstraints = @UniqueConstraint(columnNames = {"ingredient_category_id","name"}))
public class IngredientItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",columnDefinition = "VARCHAR(255) COLLATE utf8mb4_general_ci")
    private String name;

    @ManyToOne
    @JoinColumn(name = "ingredient_category_id",nullable = false)
    private IngredientCategory ingredientCategory;

    private boolean isInStoke = false;

    @OneToMany
    private List<CartItem> cartItems;

    @ManyToMany(mappedBy = "ingredients")
    private List<OrderItem> orderItems;
}
