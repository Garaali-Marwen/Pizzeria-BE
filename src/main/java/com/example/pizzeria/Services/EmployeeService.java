package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.EmployeeDTO;
import com.example.pizzeria.Entities.Employee;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO addEmployee(Employee employee);
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(Long emplpoyeeId);
    EmployeeDTO updateEmployee(EmployeeDTO employeeDTO);
    void deleteEmployee(Long employeeId);
}
