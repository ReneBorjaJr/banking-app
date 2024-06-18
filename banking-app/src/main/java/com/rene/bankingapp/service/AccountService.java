package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import com.rene.bankingapp.repository.AccountRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

public class AccountService {
    @Autowired
    private static AccountRepository accountRepository;

    public static Iterable<Account> findAll() {
        return accountRepository.findAll();
    }

    public static Account save(Account account) {
        return account;
    }

    private void verifyaccount(Long accountId) throws ResourceNotFoundException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Account with ID " + accountId + " Does not exits ");
        }

    }

    public ResponseEntity<?> getAccountById(Long accountId) {
        verifyaccount(accountId);
        Optional<Account> account = accountRepository.findById(accountId);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account) {
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newAccountUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.getId())
                .toUri();
        responseHeaders.setLocation(newAccountUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateAccount(@RequestBody Account account) {
        verifyaccount(account.getId());
        // Save the entity
        Account a = accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        verifyaccount(accountId);
        accountRepository.deleteById(accountId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    protected void verifyAccount(Long accountId) throws ResourceNotFoundException {
        Optional<Account> account = accountRepository.findById(accountId);
        if(account.isEmpty()) {
            throw new ResourceNotFoundException("Poll with id " + accountId + " not found");
        }

    }



}
