package com.example.pizzeria.Entities;

import com.example.pizzeria.Enum.Unit;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
    @OneToMany(mappedBy = "ingredient")
    private List<ItemIngredient> itemIngredients = new ArrayList<>();
    @OneToOne(mappedBy = "ingredient")
    private StockItem stockItem;
    @Enumerated(EnumType.STRING)
    private Unit unit;
}
