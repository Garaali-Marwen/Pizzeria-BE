package com.example.pizzeria.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT", length = 5000)
    private String description;
    private double price;
    @OneToMany(mappedBy = "item")
    private List<ItemIngredient> itemIngredients = new ArrayList<>();
    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();
    @OneToOne(mappedBy = "item")
    private StockItem stockItem;
    @OneToMany(mappedBy = "item")
    private List<ItemDiscount> itemDiscounts = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
    @ManyToOne
    private Category category;
    @OneToMany
    private List<Size> sizes = new ArrayList<>();
}
