package com.spring.security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.demo.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
}
