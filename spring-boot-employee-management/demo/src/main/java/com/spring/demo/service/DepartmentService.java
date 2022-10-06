package com.spring.demo.service;

import java.util.List;

import com.spring.demo.entity.Department;
import com.spring.demo.entity.Employee;

public interface DepartmentService {
    List<Department> getAllDepartment();
    Department getDepartmentByEmployeeId(long id);
    void saveDepartment(Department department, Employee employee);
    void deleteDepartmentByEmployeeId(long id);

}
