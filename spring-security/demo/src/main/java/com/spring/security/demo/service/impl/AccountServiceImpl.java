package com.spring.security.demo.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.spring.security.demo.dto.AccountDto;
import com.spring.security.demo.dto.TransactionDto;
import com.spring.security.demo.entity.Account;
import com.spring.security.demo.entity.Transaction;
import com.spring.security.demo.repository.AccountRepository;
import com.spring.security.demo.repository.TransactionRepository;
import com.spring.security.demo.repository.UserRepository;
import com.spring.security.demo.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Account findByNumber(Long num) {
        return accountRepository.findByNumber(num);
    }

    @Override
    public Optional<Account> findById(Long num) {
        return accountRepository.findById(num);
    }

    public static AccountDto convertEntityToDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setBalance(account.getBalance());
        accountDto.setNumber(account.getNumber());
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction a : account.getTransactions()) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setAmount(a.getAmount());
            transactionDto.setBalance(a.getBalance());
            transactionDto.setComment(a.getComment());
            transactionDto.setDate(a.getDate());
            transactionDto.setAccountNumber(account.getNumber());
            transactionDtos.add(transactionDto);
        }

        Collections.sort(transactionDtos, new Comparator<TransactionDto>() {
            @Override
			public int compare(TransactionDto o1, TransactionDto o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                  return 0;
                return o2.getDate().compareTo(o1.getDate());
            }
          });
          

        accountDto.setTransactions(transactionDtos);

        return accountDto;
    }

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Account account = accountRepository.findByNumber(transactionDto.getAccountNumber());
        Set<Transaction> setTransactions = new HashSet<>();
        for (Transaction t : account.getTransactions()) {
            setTransactions.add(t);
        }

        setTransactions.add(addTransaction(transactionDto));

        account.setTransactions(setTransactions);
        account.setBalance(transactionDto.getBalance());
        accountRepository.save(account);
    }

    private Transaction addTransaction(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setComment(transactionDto.getComment());
        transaction.setDate(transactionDto.getDate());
        transaction.setBalance(transactionDto.getBalance());
        return transactionRepository.save(transaction);
    }

	@Override
	public void deleteAccountByAccountNumber(Long number) {
		Account account = accountRepository.findByNumber(number);
		Long id = account.getId();
		
		accountRepository.deleteById(id);
	}

}
