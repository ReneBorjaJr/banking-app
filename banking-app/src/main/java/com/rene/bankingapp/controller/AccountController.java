package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.repository.AccountRepository;
import com.rene.bankingapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @RequestMapping(value = "/accounts/{customerId}", method = RequestMethod.POST)
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account, @PathVariable Long customerId) {
        return accountService.createAccount(account, customerId);
    }

    @RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAccountById(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId);
    }

    @RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAccount(@RequestBody @Valid Account account, @PathVariable Long accountId) {
        return accountService.updateAccount(account, accountId);
    }

    @RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        return accountService.deleteAccount(accountId);
    }

    @RequestMapping(value = "/customers/{customerId}/accounts", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAccountsForCustomer(@PathVariable Long customerId) {
        return accountService.getAllAccountsForCustomer(customerId);
    }


}





