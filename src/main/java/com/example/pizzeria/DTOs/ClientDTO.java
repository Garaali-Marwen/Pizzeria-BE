package com.example.pizzeria.DTOs;

import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Enum.Role;

public record ClientDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        int phoneNumber,
        Image image
) {
}
