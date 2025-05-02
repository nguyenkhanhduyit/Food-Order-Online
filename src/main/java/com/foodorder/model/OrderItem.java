package com.foodorder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Food food;

    @ManyToOne
    private Restaurant restaurant;

    private int quantity;

    private BigDecimal totalPrice;

    @ManyToMany
    @JoinTable(
            name = "order_item_ingredient_item",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_item_id")
    )
    private List<IngredientItem> ingredients;

    @ManyToOne
    private Order order;
}
