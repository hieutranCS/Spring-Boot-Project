package com.spring.security.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.security.demo.dto.EmployeeDto;
import com.spring.security.demo.dto.RegisterDto;
import com.spring.security.demo.entity.Employee;
import com.spring.security.demo.entity.Role;
import com.spring.security.demo.repository.EmployeeRepository;
import com.spring.security.demo.repository.RoleRepository;
import com.spring.security.demo.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveEmployee(RegisterDto employeeDto) {
        Employee employee = new Employee();
        employee.setName(employeeDto.getFirstName() + " " + employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());

        // encrypt the password once we integrate spring security
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));

        Role role = roleRepository.findByName(employeeDto.getRole().toUpperCase());
        if (role == null) {
            role = checkRoleExist(employeeDto.getRole().toUpperCase());
        }
        employee.setRoles(Stream.of(role).collect(Collectors.toSet()));
        employeeRepository.save(employee);
    }

    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public List<EmployeeDto> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map((employee) -> convertEntityToDto(employee))
                .collect(Collectors.toList());
    }

    @Override
	public EmployeeDto convertEntityToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(employee.getId());
        String[] name = employee.getName().split(" ");
        employeeDto.setFirstName(name[0]);
        employeeDto.setLastName(name[1]);
        employeeDto.setEmail(employee.getEmail());
        for (Role r : employee.getRoles()) {
            employeeDto.setRole(r.getName());
        }

        return employeeDto;
    }

    private Role checkRoleExist(String name) {
        Role role = new Role();
        if(name.equals("EMPLOYEE")) {
          role.setId(1L);
        }
        else {
          role.setId(2L);
        }
        
        role.setName(name);
        return roleRepository.save(role);
    }

	@Override
	public void editEmployee(EmployeeDto employeeDto) {
		  Employee employee = new Employee();
	        employee.setName(employeeDto.getFirstName() + " " + employeeDto.getLastName());
	        employee.setEmail(employeeDto.getEmail());
	        employee.setId(employeeDto.getId());

	        // encrypt the password once we integrate spring security
	        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));

	        Role role = roleRepository.findByName(employeeDto.getRole().toUpperCase());
	        if (role == null) {
	            role = checkRoleExist(employeeDto.getRole().toUpperCase());
	        }
	 
	        employee.setRoles(Stream.of(role).collect(Collectors.toSet()));
	        employeeRepository.save(employee);
		
	}

	@Override
	public void deleteByEmail(String email) {
		Employee employee = employeeRepository.findByEmail(email);
		EmployeeDto employeeDto = convertEntityToDto(employee);
		Long id = employee.getId();
		String employeRole = employeeDto.getRole().toUpperCase();
		employeeRepository.deleteById(id);
		
		   Role role = roleRepository.findByName(employeRole);
	        if (role == null) {
	            role = checkRoleExist(employeeDto.getRole().toUpperCase());
	        }
	}
}
