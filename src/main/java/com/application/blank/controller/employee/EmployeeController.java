package com.application.blank.controller.employee;

import com.application.blank.dto.employee.EmployeeDTO;
import com.application.blank.entity.employee.Employee;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.service.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<EmployeeDTO>> getAllTheEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) throws ResourceNotFoundException {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok().body(employee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody EmployeeDTO employeeDTO) throws ResourceNotFoundException {
        EmployeeDTO newEmployee = employeeService.saveEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO)
            throws ResourceNotFoundException {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable Long id) throws ResourceNotFoundException {
        return employeeService.deleteEmployee(id);
    }
}

