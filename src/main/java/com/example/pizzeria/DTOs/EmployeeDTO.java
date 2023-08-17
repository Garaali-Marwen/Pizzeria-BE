package com.example.pizzeria.DTOs;

import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Enum.Role;

public record EmployeeDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        Image image

) {
}
