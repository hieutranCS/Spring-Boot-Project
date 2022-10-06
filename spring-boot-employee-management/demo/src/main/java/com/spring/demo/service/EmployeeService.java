package com.spring.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.demo.entity.Employee;


public interface EmployeeService {
    List<Employee> getAllEmployees();
	void saveEmployee(Employee employee);
	Employee getEmployeeById(long id);
	void deleteEmployeeById(long id);
	Boolean validatePhone(String number);
	Boolean validateEmail(String email);
	String validateEmployee(Employee employee);
	Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection );
}
