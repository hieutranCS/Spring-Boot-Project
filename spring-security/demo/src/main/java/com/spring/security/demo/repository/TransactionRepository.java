package com.spring.security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.demo.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
   
	
}
