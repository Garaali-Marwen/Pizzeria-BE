package com.example.pizzeria.Entities;

import com.example.pizzeria.Enum.IngredientType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ItemIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Item item;
    @ManyToOne
    private Ingredient ingredient;
    private float quantity;
    @Enumerated(EnumType.STRING)
    private IngredientType type;
    private double price;
    @OneToOne
    private StockItem stockItem;

}
