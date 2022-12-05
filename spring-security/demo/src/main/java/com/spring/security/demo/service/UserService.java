package com.spring.security.demo.service;

import java.util.List;

import com.spring.security.demo.dto.AccountDto;
import com.spring.security.demo.dto.EmployeeViewDto;
import com.spring.security.demo.dto.RegisterDto;
import com.spring.security.demo.dto.UserDto;
import com.spring.security.demo.dto.impl.EmployeeViewInterface;
import com.spring.security.demo.entity.User;

public interface UserService {
    void saveUser(RegisterDto registerDto);
    void saveAccount(AccountDto accountDto, User user);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
    List<EmployeeViewInterface>findUserAccountByEmail(String email);
	List<EmployeeViewDto> convertInterfaceToDto(List<EmployeeViewInterface> employeeViewInterfaces);
	
	void deleteUserByEmail(String email);
	void editUser( UserDto userDto);
}
