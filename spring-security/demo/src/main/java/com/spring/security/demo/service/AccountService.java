package com.spring.security.demo.service;
import java.util.Optional;

import com.spring.security.demo.dto.TransactionDto;
import com.spring.security.demo.entity.Account;

public interface AccountService {
    Account findByNumber(Long num);
    Optional<Account> findById(Long id);

    void saveTransaction(TransactionDto transactionDto);

    void deleteAccountByAccountNumber(Long number);

}
