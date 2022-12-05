package com.spring.security.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.security.demo.dto.AccountDto;
import com.spring.security.demo.dto.EmployeeDto;
import com.spring.security.demo.dto.RegisterDto;
import com.spring.security.demo.dto.TransactionDto;
import com.spring.security.demo.dto.UserDto;
import com.spring.security.demo.entity.Account;
import com.spring.security.demo.entity.Employee;
import com.spring.security.demo.entity.User;
import com.spring.security.demo.service.AccountService;
import com.spring.security.demo.service.EmployeeService;
import com.spring.security.demo.service.UserService;
import com.spring.security.demo.service.impl.AccountServiceImpl;
import com.spring.security.demo.service.impl.UserServiceImpl;

@Controller
public class AuthController {

	private EmployeeService employeeService;
	private UserService userService;
	private AccountService accountService;

	public AuthController(EmployeeService employeeService, UserService userService, AccountService accountService) {
		this.employeeService = employeeService;
		this.userService = userService;
		this.accountService = accountService;
	}

	@GetMapping("/index")
	public String home() {
		return "index";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
		Cookie cookieWithSlash = new Cookie("JSESSIONID", null);
		// Tomcat adds extra slash at the end of context path (e.g. "/foo/")
		cookieWithSlash.setPath(request.getContextPath() + "/");
		cookieWithSlash.setMaxAge(0);

		// Remove cookies on logout so that invalidSessionURL (session timeout) is not
		// displayed on proper logout event
		response.addCookie(cookieWithSlash);
		return "logout";
	}

	@GetMapping("/user/login")
	public String userLogin() {
		return "userlogin";
	}

	@GetMapping("/admin/login")
	public String adminLogin() {
		return "adminlogin";
	}

	@GetMapping("/user/register")
	public String showRegistrationForm(Model model) {
		RegisterDto user = new RegisterDto();
		model.addAttribute("user", user);
		return "userregister";
	}

	@PostMapping("/user/register/save")
	public String registration(@Valid @ModelAttribute("user") RegisterDto dto, BindingResult result, Model model) {
		User userEmail = userService.findByEmail(dto.getEmail());
		Employee employee = employeeService.findByEmail(dto.getEmail());

		if (userEmail != null || employee != null) {
			result.rejectValue("email", null, "Email is already registered");
		}

		if (result.hasErrors()) {
			model.addAttribute("user", dto);
			return "userregister";
		}

		userService.saveUser(dto);
		return "redirect:/user/register?success";
	}

	@GetMapping("/admin/register")
	public String employeeRegistrationForm(Model model) {
		RegisterDto emp = new RegisterDto();
		model.addAttribute("employee", emp);
		return "adminregister";
	}

	@PostMapping("/admin/register/save")
	public String employeeRegistration(@Valid @ModelAttribute("employee") RegisterDto dto, BindingResult result,
			Model model) {
		Employee employee = employeeService.findByEmail(dto.getEmail());
		User user = userService.findByEmail(dto.getEmail());

		if (employee != null || user != null) {
			result.rejectValue("email", null, "Email is already registered");
		}

		if (dto.getRole().toUpperCase().equals("ADMIN") == false
				&& dto.getRole().toUpperCase().equals("EMPLOYEE") == false) {
			result.rejectValue("role", null, "Invalid Role");
		}

		if (result.hasErrors()) {
			model.addAttribute("employee", dto);
			return "adminregister";
		}

		employeeService.saveEmployee(dto);
		return "redirect:/admin/register?success";
	}

	@GetMapping("/account/register")
	public String accountRegistrationForm(Model model, HttpServletRequest httpServletRequest) {
		AccountDto acc = new AccountDto();

		Object sc = httpServletRequest.getSession()
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

		Authentication authentication = ((SecurityContext) sc).getAuthentication();

		String userEmail = authentication.getName();

		boolean generateNum = false;

		while (generateNum != true) {
			long randomNum = ThreadLocalRandom.current().nextInt(1, 99999 + 1);
			Account checkAccNumber = accountService.findByNumber(randomNum);
			if (checkAccNumber == null) {
				acc.setNumber(randomNum);
				generateNum = true;
			}
		}
		acc.setUserEmail(userEmail);
		acc.setTransactions(acc.getTransactions());
		model.addAttribute("account", acc);

		return "accountregister";

	}

	@PostMapping("/account/register/save")
	public String accountRegistration(@Valid @ModelAttribute("account") AccountDto dto, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			model.addAttribute("account", dto);
			return "accountregister";
		}

		User user = userService.findByEmail(dto.getUserEmail());
		userService.saveAccount(dto, user);
		return "redirect:/user";
	}

	@GetMapping("/admin")
	public String listRegisteredUsers(Model model) {
		List<EmployeeDto> employees = employeeService.findAllEmployees();
		model.addAttribute("employees", employees);
		return "admin";
	}

	@GetMapping("/user")
	public String userPage(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String userEmail = auth.getName();

		User user = userService.findByEmail(userEmail);
		UserDto userDto = UserServiceImpl.convertEntityToDto(user);

		model.addAttribute("user", userDto);

		return "user";
	}

	@GetMapping("/account/deposit={number}")
	public String accountDeposit(@PathVariable(value = "number") Long id, Model model) {
		Account account = accountService.findByNumber(id);
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setAccountNumber(account.getNumber());
		transactionDto.setBalance(account.getBalance());

		model.addAttribute("transaction", transactionDto);

		return "deposit";
	}

	@PostMapping("/account/deposit/save")
	public String depositSave(@Valid @ModelAttribute("transaction") TransactionDto transactionDto, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			model.addAttribute("transaction", transactionDto);
			return "deposit";
		}

		String dateTimeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a"));
		transactionDto.setComment("deposit");
		transactionDto.setDate(dateTimeString);
		BigDecimal total = transactionDto.getAmount().add(transactionDto.getBalance());
		transactionDto.setBalance(total);
		accountService.saveTransaction(transactionDto);

		return "redirect:/user";
	}

	@GetMapping("/account/withdraw={number}")
	public String accountWithdraw(@PathVariable(value = "number") Long id, Model model) {
		Account account = accountService.findByNumber(id);
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setAccountNumber(account.getNumber());
		transactionDto.setBalance(account.getBalance());

		model.addAttribute("transaction", transactionDto);

		return "withdraw";
	}

	@PostMapping("/account/withdraw/save")
	public String withdrawSave(@Valid @ModelAttribute("transaction") TransactionDto transactionDto,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("transaction", transactionDto);
			return "withdraw";
		}
		BigDecimal total = transactionDto.getBalance().subtract(transactionDto.getAmount());

		if (total.compareTo(BigDecimal.ZERO) < 0) {
			result.rejectValue("amount", null, "Invalid Amount");
		}

		if (result.hasErrors()) {
			model.addAttribute("transaction", transactionDto);
			return "withdraw";
		}

		String dateTimeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a"));
		transactionDto.setComment("withdraw");
		transactionDto.setDate(dateTimeString);
		transactionDto.setBalance(total);
		accountService.saveTransaction(transactionDto);

		return "redirect:/user";
	}

	@GetMapping("/account/statement={number}")
	public String accountStatement(@PathVariable(value = "number") Long id, Model model) {
		Account account = accountService.findByNumber(id);

		AccountDto accountDto = AccountServiceImpl.convertEntityToDto(account);

		model.addAttribute("transaction", accountDto);

		return "statement";
	}

	@GetMapping("/user/edit={email}")
	public String userEdit(@PathVariable(value = "email") String email, Model model,
			HttpServletRequest httpServletRequest) {
		User user = userService.findByEmail(email);
		UserDto userDto = UserServiceImpl.convertEntityToDto(user);
		System.out.println(userDto.toString() + " edit");
		Object sc = httpServletRequest.getSession()
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		if (!(sc instanceof SecurityContext)) {
			System.out.println("\n" + "wrong");
		}

		Authentication authentication = ((SecurityContext) sc).getAuthentication();
		for (GrantedAuthority i : authentication.getAuthorities()) {
			model.addAttribute("authen", i);
		}
		
		
		String roleString = null;
		for (GrantedAuthority i : authentication.getAuthorities()) {
			roleString = i.toString();
		}
		
		model.addAttribute("user", userDto);

		

		return "userpage";
	}

	@PostMapping("/user/edit/save")
	public String userEditSave(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model,
			HttpServletRequest httpServletRequest) {
		Object sc = httpServletRequest.getSession()
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		if (!(sc instanceof SecurityContext)) {
			System.out.println("\n" + "wrong");
		}
		Authentication authentication = ((SecurityContext) sc).getAuthentication();

		if (result.hasErrors()) {
			for (GrantedAuthority i : authentication.getAuthorities()) {
				model.addAttribute("authen", i);
			}
			
			model.addAttribute("user", userDto);
			return "userpage";
		}

		userService.editUser(userDto);
		String roleString = null;
		for (GrantedAuthority i : authentication.getAuthorities()) {
			roleString = i.toString();
		}
		
			
		if(roleString.equals("ADMIN") || roleString.equals("EMPLOYEE")) {
			return "redirect:/employee";
		}
		else {
			return "redirect:/user";
		}
		
	}

	@GetMapping("/employee")
	public String emplopyee(Model model, HttpServletRequest httpServletRequest) {

		List<UserDto> userDto = userService.findAllUsers();

		model.addAttribute("user", userDto);

		Object sc = httpServletRequest.getSession()
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		if (!(sc instanceof SecurityContext)) {
			System.out.println("\n" + "wrong");
		}
		Authentication authentication = ((SecurityContext) sc).getAuthentication();

		for (GrantedAuthority i : authentication.getAuthorities()) {
			model.addAttribute("authen", i);
		}

		return "employeepage";
	}

}
