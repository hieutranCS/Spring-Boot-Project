package com.spring.security.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.security.demo.dto.EmployeeDto;
import com.spring.security.demo.dto.EmployeeViewDto;
import com.spring.security.demo.dto.impl.EmployeeViewInterface;
import com.spring.security.demo.entity.Employee;
import com.spring.security.demo.service.AccountService;
import com.spring.security.demo.service.EmployeeService;
import com.spring.security.demo.service.UserService;

@Controller
public class EmployeeController {

	private EmployeeService employeeService;
	private UserService userService;
	private AccountService accountService;

	public EmployeeController(EmployeeService employeeService, UserService userService, AccountService accountService) {
		this.employeeService = employeeService;
		this.userService = userService;
		this.accountService = accountService;
	}

	@GetMapping("/employee/view={email}")
	public String accountDeposit(@PathVariable(value = "email") String email, Model model) {
		List<EmployeeViewInterface> employeeViewInterfaces = userService.findUserAccountByEmail(email);

		List<EmployeeViewDto> employeeViewDtos = userService.convertInterfaceToDto(employeeViewInterfaces);

		model.addAttribute("user", employeeViewDtos);

		return "userview";
	}

	@GetMapping("/employee/account/delete/{number}")
	public String accountDelete(@PathVariable(value = "number") Long number) {
		accountService.deleteAccountByAccountNumber(number);
		return "redirect:/employee";
	}

	@GetMapping("/employee/delete={email}")
	public String userDelete(@PathVariable(value = "email") String email, Model model) {
		userService.deleteUserByEmail(email);
		return "redirect:/employee";
	}

	@GetMapping("/admin/edit={email}")
	public String adminEdit(@PathVariable(value = "email") String email, Model model) {
		Employee employee = employeeService.findByEmail(email);
		EmployeeDto employeeDto = employeeService.convertEntityToDto(employee);
		model.addAttribute("employee", employeeDto);
		return "editemployee";
	}

	@PostMapping("/admin/edit/save")
	public String adminEditSave(@Valid @ModelAttribute("employee") EmployeeDto employeeDto, BindingResult result,
			Model model) {

		if (employeeDto.getRole().toUpperCase().equals("ADMIN") == false
				&& employeeDto.getRole().toUpperCase().equals("EMPLOYEE") == false) {
			result.rejectValue("role", null, "Invalid Role");
		}
		if (result.hasErrors()) {
			model.addAttribute("employee", employeeDto);
			return "editemployee";
		}

		employeeService.editEmployee(employeeDto);
		return "redirect:/admin";
	}

	@GetMapping("/admin/delete={email}")
	public String adminDelete(@PathVariable(value = "email") String email) {
		employeeService.deleteByEmail(email);
		return "redirect:/admin";
	}
	

}
