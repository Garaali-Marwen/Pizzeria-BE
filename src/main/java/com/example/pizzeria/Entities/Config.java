package com.example.pizzeria.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String contactPhone;
    private String deliveryPhone;
    private String contactEmail;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Image> carouselImages;
}
