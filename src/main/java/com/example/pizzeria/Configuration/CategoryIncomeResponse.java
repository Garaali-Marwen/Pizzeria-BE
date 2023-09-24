package com.example.pizzeria.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryIncomeResponse {
    private String name;
    private Integer month;
    private Double income;
    private List<Double> data;

    public CategoryIncomeResponse(String name, List<Double> data) {
        this.name = name;
        this.data = data;
    }

    public CategoryIncomeResponse(String name, Integer month, Double income) {
        this.name = name;
        this.month = month;
        this.income = income;
    }
}
