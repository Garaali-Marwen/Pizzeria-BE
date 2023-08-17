package com.example.pizzeria.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Item item;
    @ManyToMany
    @JoinTable(
            name = "orderItem_ingredient",
            joinColumns =  @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns =  @JoinColumn(name = "ingredient_id", referencedColumnName = "id"))
    private List<Ingredient> ingredients = new ArrayList<>();
    private String size;
    private int quantity;
    @ManyToOne
    private Client client;
    private double price;
}
