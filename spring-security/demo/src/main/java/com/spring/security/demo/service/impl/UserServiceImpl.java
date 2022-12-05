package com.spring.security.demo.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.security.demo.dto.AccountDto;
import com.spring.security.demo.dto.EmployeeViewDto;
import com.spring.security.demo.dto.RegisterDto;
import com.spring.security.demo.dto.TransactionDto;
import com.spring.security.demo.dto.UserDto;
import com.spring.security.demo.dto.impl.EmployeeViewInterface;
import com.spring.security.demo.entity.Account;
import com.spring.security.demo.entity.Role;
import com.spring.security.demo.entity.Transaction;
import com.spring.security.demo.entity.User;
import com.spring.security.demo.repository.AccountRepository;
import com.spring.security.demo.repository.RoleRepository;
import com.spring.security.demo.repository.UserRepository;
import com.spring.security.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private AccountRepository accountRepository;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void saveUser(RegisterDto userDto) {
		User user = new User();
		user.setName(userDto.getFirstName() + " " + userDto.getLastName());
		user.setEmail(userDto.getEmail());

		// encrypt the password once we integrate spring security
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		Role role = roleRepository.findByName("USER");
		if (role == null) {
			role = addRole("USER");
		}
		user.setRoles(Stream.of(role).collect(Collectors.toSet()));
		userRepository.save(user);
	}

	@Override
	public void saveAccount(AccountDto accountDto, User user) {
		Account account = addAccount(accountDto);
		Set<Account> setAccounts = new HashSet<>();
		for (Account a : user.getAccounts()) {
			setAccounts.add(a);
		}
		setAccounts.add(account);
		user.setAccounts(setAccounts);
		userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map((employee) -> convertEntityToDto(employee)).collect(Collectors.toList());
	}

	public static UserDto convertEntityToDto(User user) {
		UserDto userDto = new UserDto();

		String[] name = user.getName().split(" ");
		userDto.setId(user.getId());
		userDto.setFirstName(name[0]);
		userDto.setLastName(name[1]);
		userDto.setEmail(user.getEmail());
		userDto.setPassword(user.getPassword());
		for (Role r : user.getRoles()) {
			userDto.setRole(r.getName());
		}

		List<AccountDto> accountDtos = new ArrayList<>();
		for (Account a : user.getAccounts()) {
			accountDtos.add(AccountServiceImpl.convertEntityToDto(a));
		}

		userDto.setAccountDtos(accountDtos);

		return userDto;
	}

	private Role addRole(String name) {
		Role role = new Role();
		role.setId(3L);
		role.setName(name);
		return roleRepository.save(role);
	}

	private Account addAccount(AccountDto accountDto) {
		Account account = new Account();
		account.setNumber(accountDto.getNumber());
		account.setBalance(accountDto.getBalance());
		return accountRepository.save(account);
	}

	@Override
	public List<EmployeeViewInterface> findUserAccountByEmail(String email) {
		return userRepository.findUserAccountByEmail(email);
	}

	@Override
	public List<EmployeeViewDto> convertInterfaceToDto(List<EmployeeViewInterface> employeeViewInterfaces) {
		List<EmployeeViewDto> employeeViewDtos = new ArrayList<>();

		for (EmployeeViewInterface e : employeeViewInterfaces) {
			EmployeeViewDto employeeViewDto = new EmployeeViewDto();
			employeeViewDto.setName(e.getName());
			employeeViewDto.setEmail(e.getEmail());
			employeeViewDto.setAccountBalance(e.getAccountBalance());
			employeeViewDto.setAccountNumber(e.getAccountNumber());
			employeeViewDtos.add(employeeViewDto);
		}

		return employeeViewDtos;
	}

	@Override
	public void deleteUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		Long id = user.getId();
		userRepository.deleteById(id);
		Role role = roleRepository.findByName("USER");
		if (role == null) {
			role = addRole("USER");
		}
	}

	@Override
	public void editUser(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getFirstName() + " " + userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setId(userDto.getId());
		

		// encrypt the password once we integrate spring security
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		Role role = roleRepository.findByName("USER");
		if (role == null) {
			role = addRole("USER");
		}
		user.setRoles(Stream.of(role).collect(Collectors.toSet()));
		
		User userAccounts = findByEmail(userDto.getEmail());
		UserDto userDtoAccountsDto = convertEntityToDto(userAccounts);

		Set<Account> setAccounts = new HashSet<>();
		for (AccountDto a : userDtoAccountsDto.getAccountDtos()) {
			Account account = new Account();
			account.setBalance(a.getBalance());
			account.setId(a.getId());
			account.setNumber(a.getNumber());
			account.setTransactions(converTransactionDto(a.getTransactions()));
			setAccounts.add(account);
		}
		
		user.setAccounts(setAccounts);

		
		userRepository.save(user);
		
	}
	
	public Set<Transaction> converTransactionDto(List<TransactionDto> transactionDtos) {
		  Set<Transaction> transactions = new HashSet<>();
	        for (TransactionDto transactionDto : transactionDtos) {
	            Transaction transaction = new Transaction();
	            transaction.setId(transactionDto.getId());
	            transaction.setAmount(transactionDto.getAmount());
	            transaction.setComment(transactionDto.getComment());
	            transaction.setDate(transactionDto.getDate());
	            transaction.setBalance(transactionDto.getBalance());
	            transactions.add(transaction);
	        }
		return transactions;
	}

}
