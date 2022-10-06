package com.spring.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.demo.entity.Department;
import com.spring.demo.entity.Employee;
import com.spring.demo.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
	private DepartmentRepository departmentRepository;

	@Override
	public List<Department> getAllDepartment() {
		return departmentRepository.findAll();
	}

	@Override
	public void saveDepartment(Department department,Employee employee) {
		department.setEmployee(employee);
		this.departmentRepository.save(department);
	}

	@Override
	public Department getDepartmentByEmployeeId(long id) {
		Optional<Department> optional = departmentRepository.getDepartmentByEmployeeEmployeeId(id);
		Department department = new Department();
		if (optional.isPresent()) {
			return department = optional.get();
		} else {
			return department;
		}
	}

	@Override
	public void deleteDepartmentByEmployeeId(long id) {
		this.departmentRepository.deleteByEmployeeEmployeeId(id);		
	}
    
    

}
