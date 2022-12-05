package com.spring.security.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.demo.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByNumber(Long number);
    
    @Override
	Optional<Account> findById(Long id);
    

}
