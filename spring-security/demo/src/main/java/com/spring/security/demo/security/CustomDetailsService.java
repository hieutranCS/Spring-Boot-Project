package com.spring.security.demo.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.security.demo.entity.Employee;
import com.spring.security.demo.entity.Role;
import com.spring.security.demo.entity.User;
import com.spring.security.demo.repository.EmployeeRepository;
import com.spring.security.demo.repository.UserRepository;

@Service
public class CustomDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		Employee employee = employeeRepository.findByEmail(email);
		if (user != null && employee == null) {
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					mapRolesToAuthorities(user.getRoles()));
		} 
		else if (employee != null && user == null) {
			return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPassword(),
					mapRolesToAuthorities(employee.getRoles()));
		}
		else {
			throw new UsernameNotFoundException("Invalid username or password." + email);
		}

	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
