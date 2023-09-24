package com.example.pizzeria.Controllers;

import com.example.pizzeria.Configuration.AuthenticationRequest;
import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.DTOs.EmployeeDTO;
import com.example.pizzeria.Entities.Employee;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Services.EmployeeService;
import com.example.pizzeria.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ImageService imageService;

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<EmployeeDTO> getAllEmployees(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize){
        return employeeService.getAllEmployees(pageNumber, pageSize);
    }
    @PostMapping("/add")
    public EmployeeDTO addEmployee(@RequestBody Employee employee){
        return employeeService.addEmployee(employee);
    }
    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable("id") Long employeeId){
        return employeeService.getEmployeeById(employeeId);
    }
    @PutMapping("/update")
    public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateEmployee(employeeDTO);
    }

    @PutMapping(value = "/update/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public EmployeeDTO updateEmployee(@RequestPart("employeeId") Long employeeId,
                                  @RequestPart("image") MultipartFile image) {
        try {
            Image images = imageService.uploadFile(image);
            return employeeService.updateEmployeeImage(employeeId, images);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @DeleteMapping("/delete/{id}")
    public void deleteEmployee(@PathVariable("id") Long employeeId){
        employeeService.deleteEmployee(employeeId);
    }

    @PostMapping("/password/verification/{employeeId}")
    public boolean passwordVerification(@RequestBody AuthenticationRequest authenticationRequest, @PathVariable("employeeId") Long employeeId) {
        return employeeService.passwordVerification(authenticationRequest.getPassword(), employeeId);
    }

    @PutMapping("/update/password/{employeeId}")
    public EmployeeDTO updatePassword(@RequestBody AuthenticationRequest authenticationRequest, @PathVariable("employeeId") Long employeeId) {
        return employeeService.passwordUpdate(authenticationRequest.getPassword(), employeeId);
    }
}
