package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Account;

import com.rene.bankingapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

      @RequestMapping(value="/accounts/{accountId}", method=RequestMethod.PUT)
       public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable Long accountId) {
        return accountService.updateAccount(account);

    }

    @RequestMapping(value="/accounts/{accountId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }


}





