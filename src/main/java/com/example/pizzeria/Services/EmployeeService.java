package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.DTOs.EmployeeDTO;
import com.example.pizzeria.Entities.Employee;
import com.example.pizzeria.Entities.Image;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO addEmployee(Employee employee);
    Page<EmployeeDTO> getAllEmployees(Integer pageNumber, Integer pageSize);
    EmployeeDTO getEmployeeById(Long employeeId);
    EmployeeDTO updateEmployee(EmployeeDTO employeeDTO);
    void deleteEmployee(Long employeeId);
    boolean passwordVerification(String password, Long employeeId);
    EmployeeDTO passwordUpdate(String password, Long employeeId);
    EmployeeDTO updateEmployeeImage(Long employeeId, Image image);

}
