package com.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.demo.entity.Employee;

@Transactional
@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Long>{
    Employee findByEmail(String email);
}
