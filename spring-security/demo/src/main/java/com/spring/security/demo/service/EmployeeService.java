package com.spring.security.demo.service;

import java.util.List;

import com.spring.security.demo.dto.EmployeeDto;
import com.spring.security.demo.dto.RegisterDto;
import com.spring.security.demo.entity.Employee;

public interface EmployeeService {
    void saveEmployee(RegisterDto registerDto);

    Employee findByEmail(String email);

    List<EmployeeDto> findAllEmployees();
    EmployeeDto convertEntityToDto(Employee employee);

	void editEmployee(EmployeeDto employeeDto);

	void deleteByEmail(String email);
}
