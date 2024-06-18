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
        account = AccountService.save(account);

        // Set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newAccountUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.getId())
                .toUri();
        responseHeaders.setLocation(newAccountUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value="/accounts/{accountId}", method=RequestMethod.GET)
    public ResponseEntity<?> getAccount(@PathVariable Long accountId) throws ResourceNotFoundException {
        verifyaccount(accountId);
        Optional<Account> a = AccountService.findById(accountId);
        if(a.isEmpty()) {
            throw new ResourceNotFoundException("Account with id " + accountId + " not found");
        }
        return new ResponseEntity<> (a, HttpStatus.OK);
    }




}
