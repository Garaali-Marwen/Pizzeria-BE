package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.EmployeeDTO;
import com.example.pizzeria.Entities.Employee;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeDTOMapper implements Function<Employee, EmployeeDTO> {
    @Override
    public EmployeeDTO apply(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getRole(),
                employee.getImage()
        );
    }
}
