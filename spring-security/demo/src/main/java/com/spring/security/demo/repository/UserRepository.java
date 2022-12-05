package com.spring.security.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.security.demo.dto.impl.EmployeeViewInterface;
import com.spring.security.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	@Query("SELECT u.email as email , u.name as name, a.number as accountNumber , a.balance as accountBalance FROM User u join u.accounts a where u.email = ?1")
	List<EmployeeViewInterface> findUserAccountByEmail(String email);


	
}
