package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.domain.Withdrawal;
import com.rene.bankingapp.domain.enums.TransactionType;
import com.rene.bankingapp.exceptions.InsufficientFundsException;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import com.rene.bankingapp.exceptions.TransactionMismatchException;
import com.rene.bankingapp.service.AccountService;
import com.rene.bankingapp.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WithdrawalController {
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private AccountService accountService;

    public void verifyWithdrawalExists(Long withdrawalId) throws ResourceNotFoundException {

        // validation logic
        Optional<Withdrawal> withdrawal = withdrawalService.getWithdrawal(withdrawalId);
        if(withdrawal.isEmpty()){
            throw new ResourceNotFoundException("Withdrawal with Id: " + withdrawalId + " not found.");
        }
    }

    public void verifyAccountExists(Long accountId) throws ResourceNotFoundException {


        Optional<Account> account = accountService.getAccountById2(accountId);
        if(account.isEmpty()){
            throw new ResourceNotFoundException("Account with Id: " + accountId + " not found.");
        }

    }

    public void verifyNotWithdrawal(TransactionType transactionType){


        if (transactionType.equals(TransactionType.DEPOSIT)){
            throw new TransactionMismatchException("Transaction type: " + transactionType.name() + " is not valid for this operation.");
        }

    }

    public void verifySufficientFunds(Long accountId, Double withdrawalAmount){

        Optional<Account> accountOptional = accountService.getAccountById2(accountId);
        Account account = accountOptional.get();
        Double accountBalance = account.getBalance();


        if (accountBalance < withdrawalAmount){
            throw new InsufficientFundsException("Insufficient funds in account for deposit transaction of: " + withdrawalAmount + ". Current account balance: " + accountBalance + ".");
        }
    }


    @GetMapping(value="/accounts/{accountId}/withrdawals")
    public ResponseEntity<Iterable<Withdrawal>> getAllWithdrawals() {
        Iterable<Withdrawal> withdrawals = withdrawalService.getAllWithdrawals();
        return new ResponseEntity<>(withdrawalService.getAllWithdrawals(), HttpStatus.OK);
    }
    @GetMapping(value="/withdrawals/{withdrawalId}")
    public ResponseEntity<?> getWithdrawal(@PathVariable Long withdrawalId) {
        verifyWithdrawalExists(withdrawalId);
        Optional<Withdrawal> withdrawal = withdrawalService.getWithdrawal(withdrawalId);
        return new ResponseEntity<> (withdrawal, HttpStatus.OK);
    }
    @PostMapping(value="/accounts/{accountId}/withdrawals")
    public ResponseEntity<?> createWithdrawal(@RequestBody Withdrawal withdrawal) {
        verifyNotWithdrawal(withdrawal.getType());
        withdrawal = withdrawalService.createWithdrawal(withdrawal);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
    @PutMapping(value="/polls/{pollId}")
    public ResponseEntity<?> updateWithdrawal(@RequestBody Withdrawal withdrawal, @PathVariable Long withdrawalId) {
        verifyWithdrawalExists(withdrawalId);
        verifySufficientFunds(withdrawalId, withdrawal.getAmount());
        Withdrawal w = withdrawalService.createWithdrawal(withdrawal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping(value="/withdrawals/{withdrawalId}")
    public ResponseEntity<?> deleteWithdrawal(@PathVariable Long withdrawalId) {
        verifyWithdrawalExists(withdrawalId);
        withdrawalService.delete(withdrawalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
