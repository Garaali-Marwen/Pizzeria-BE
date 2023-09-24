package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.DTOs.EmployeeDTO;
import com.example.pizzeria.DTOs.Mappers.EmployeeDTOMapper;
import com.example.pizzeria.Entities.Client;
import com.example.pizzeria.Entities.Employee;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Enum.Role;
import com.example.pizzeria.Repositories.EmployeeRepository;
import com.example.pizzeria.Services.EmployeeService;
import com.example.pizzeria.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ImageService imageService;

    @Override
    public EmployeeDTO addEmployee(Employee employee) {
        employee.setRole(Role.EMPLOYEE);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeDTOMapper.apply(employeeRepository.save(employee));
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return employeeRepository.findAll(pageable)
                .map(employeeDTOMapper);
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
        employee.setFirstName(employeeDTO.firstName());
        employee.setLastName(employeeDTO.lastName());
        employeeRepository.save(employee);
        return employeeDTO;
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public boolean passwordVerification(String password, Long emplpoyeeId) {
        Employee employee = employeeRepository.findById(emplpoyeeId).orElseThrow(() -> new NoSuchElementException("not found"));
        return passwordEncoder.matches(password, employee.getPassword());
    }

    @Override
    public EmployeeDTO passwordUpdate(String password, Long emplpoyeeId) {
        Employee employee = employeeRepository.findById(emplpoyeeId).orElseThrow(() -> new NoSuchElementException("Not found"));
        employee.setPassword(passwordEncoder.encode(password));
        return employeeDTOMapper.apply(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDTO updateEmployeeImage(Long employeeId, Image image) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("No client found with id: " + employeeId));
        Image imageDelete = employee.getImage();
        employee.setImage(image);
        Employee updatedEmployee = employeeRepository.save(employee);
        imageService.deleteImage(imageDelete.getId());
        return employeeDTOMapper.apply(updatedEmployee);
    }
}
