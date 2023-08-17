package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.EmployeeDTO;
import com.example.pizzeria.DTOs.Mappers.EmployeeDTOMapper;
import com.example.pizzeria.Entities.Employee;
import com.example.pizzeria.Enum.Role;
import com.example.pizzeria.Repositories.EmployeeRepository;
import com.example.pizzeria.Services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDTOMapper employeeDTOMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeDTO addEmployee(Employee employee) {
        employee.setRole(Role.EMPLOYEE);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeDTOMapper.apply(employeeRepository.save(employee));
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Long emplpoyeeId) {
        return employeeDTOMapper.apply(
                employeeRepository.findById(emplpoyeeId)
                        .orElseThrow(() -> new NoSuchElementException("No employee found with ID: " + emplpoyeeId))
        );
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(employeeDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No employee found with ID: " + employeeDTO.id()));
        employee.setEmail(employeeDTO.email());
        employee.setImage(employeeDTO.image());
        employee.setFirstName(employeeDTO.firstName());
        employee.setLastName(employeeDTO.lastName());
        employeeRepository.save(employee);
        return employeeDTO;
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}
