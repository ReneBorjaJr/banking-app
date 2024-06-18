package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.exception.ResourceNotFoundException;
import com.rene.bankingapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class AccountService {
    @Autowired
    private static AccountRepository accountRepository;
    public static Iterable<Account> findAll(){
        return accountRepository.findAll();
    }

    public static Account save(Account account) {
        return account;
    }
    protected void verifyaccount(Long accountId) throws ResourceNotFoundException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()){
            throw new ResourceNotFoundException("Account with ID " + accountId + " Does not exits ");
        }

    }
}
