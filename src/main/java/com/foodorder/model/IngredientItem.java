package com.foodorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingredient_items")
public class IngredientItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private IngredientCategory ingredientCategory;

    private boolean isInStoke = true;

    @OneToMany
    private List<CartItem> cartItems;

    @ManyToMany(mappedBy = "ingredients")
    private List<OrderItem> orderItems;
}
