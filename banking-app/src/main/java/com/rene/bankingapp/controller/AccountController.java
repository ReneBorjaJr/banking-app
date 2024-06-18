package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.exception.ResourceNotFoundException;
import com.rene.bankingapp.repository.AccountRepository;
import com.rene.bankingapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(value="/accounts", method= RequestMethod.GET)
    public ResponseEntity<Iterable<Account>> getAllAccounts() {
        Iterable<Account> allAccounts = AccountService.findAll();
        return new ResponseEntity<>(AccountService.findAll(), HttpStatus.OK);

    }

    @RequestMapping(value="/accounts", method=RequestMethod.POST)
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account) {
      return accountService.createAccount(account);
    }

    @RequestMapping(value="/accounts/{accountId}", method=RequestMethod.GET)
    public ResponseEntity<?> getAccount(@PathVariable Long accountId) {

        return accountService.getAccountById(accountId);
    }




}
