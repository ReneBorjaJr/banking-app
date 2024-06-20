package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.domain.Withdrawal;
import com.rene.bankingapp.domain.enums.Medium;
import com.rene.bankingapp.domain.enums.TransactionType;
import com.rene.bankingapp.exceptions.InsufficientFundsException;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import com.rene.bankingapp.exceptions.TransactionMismatchException;
import com.rene.bankingapp.repository.AccountRepository;
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
    private AccountRepository accountRepository;

    public void verifyWithdrawalExists(Long withdrawalId) throws ResourceNotFoundException {

        // validation logic
        Optional<Withdrawal> withdrawal = withdrawalService.getWithdrawal(withdrawalId);
        if(withdrawal.isEmpty()){
            throw new ResourceNotFoundException("Withdrawal with Id: " + withdrawalId + " not found.");
        }
    }

    public void verifyAccountExists(Long accountId) throws ResourceNotFoundException {


        Optional<Account> account = accountRepository.findById(accountId);
        if(account.isEmpty()){
            throw new ResourceNotFoundException("Account with Id: " + accountId + " not found.");
        }

    }

    public void verifyNotDeposit(TransactionType transactionType){


        if (transactionType.equals(TransactionType.DEPOSIT)){
            throw new TransactionMismatchException("Transaction type: " + transactionType.name() + " is not valid for this operation.");
        }

    }

    public void verifySufficientFunds(Long accountId, Double withdrawalAmount){

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        Account account = accountOptional.get();
        Double accountBalance = account.getBalance();


        if (accountBalance < withdrawalAmount){
            throw new InsufficientFundsException("Insufficient funds in account for deposit transaction of: " + withdrawalAmount + ". Current account balance: " + accountBalance + ".");
        }
    }


    @GetMapping(value="/accounts/{accountId}/withrdawals")
    public ResponseEntity<Iterable<Withdrawal>> getAllWithdrawals(@PathVariable Long accountId) {
        verifyAccountExists(accountId);
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
    public ResponseEntity<?> createWithdrawal(@RequestBody Withdrawal withdrawal, @PathVariable Long accountId) {
        verifyAccountExists(accountId);
        verifyNotDeposit(withdrawal.getType());
        withdrawal = withdrawalService.createWithdrawal(withdrawal);
//        withdrawal.getAmount()

        if (withdrawal.getMedium().equalsIgnoreCase("balance")) {
            Double newAccountBalance = accountRepository.findById(accountId).get().getBalance() - withdrawal.getAmount();
            accountRepository.findById(accountId).get().setBalance(newAccountBalance);
        }
        if (withdrawal.getMedium().equalsIgnoreCase("rewards")){
            Integer newRewardsBalance = accountRepository.findById(accountId).get().getRewards() - withdrawal.getAmount().intValue();
            accountRepository.findById(accountId).get().setRewards(newRewardsBalance);
        }
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
    @PutMapping(value="/withdrawals/{withdrawalId}")
    public ResponseEntity<?> updateWithdrawal(@RequestBody Withdrawal withdrawal, @PathVariable Long withdrawalId) {
        verifyWithdrawalExists(withdrawalId);
        verifySufficientFunds(withdrawalId, withdrawal.getAmount());
        Double dbAmount = withdrawalService.getWithdrawal(withdrawalId).get().getAmount();
        Double bodyAmount = withdrawal.getAmount();
        Long accountId = withdrawalService.getWithdrawal(withdrawalId).get().getPayer_id();
        Double balance = accountRepository.findById(accountId).get().getBalance();
        Double newBalance;
        String dbMedium = withdrawalService.getWithdrawal(withdrawalId).get().getMedium();
        String bodyMedium = withdrawal.getMedium();
        if(!dbMedium.equalsIgnoreCase(bodyMedium)){
            deleteWithdrawal(withdrawalId);
            createWithdrawal(withdrawal, accountId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if(dbAmount > bodyAmount){
            newBalance = balance + (dbAmount - bodyAmount);
            accountRepository.findById(accountId).get().setBalance(newBalance);
        }
        if(dbAmount < bodyAmount){
            newBalance = balance - (bodyAmount - dbAmount);
            accountRepository.findById(accountId).get().setBalance(newBalance);
        }
        Withdrawal w = withdrawalService.createWithdrawal(withdrawal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping(value="/withdrawals/{withdrawalId}")
    public ResponseEntity<?> deleteWithdrawal(@PathVariable Long withdrawalId) {
        verifyWithdrawalExists(withdrawalId);
        withdrawalService.delete(withdrawalId);
        Long accountId = withdrawalService.getWithdrawal(withdrawalId).get().getPayer_id();
        Double withdrawal = withdrawalService.getWithdrawal(withdrawalId).get().getAmount();
        if (withdrawalService.getWithdrawal(withdrawalId).get().getMedium().equalsIgnoreCase("balance")) {
            Double newAccountBalance = accountRepository.findById(accountId).get().getBalance() + withdrawal;
            accountRepository.findById(accountId).get().setBalance(newAccountBalance);
        }
        if (withdrawalService.getWithdrawal(withdrawalId).get().getMedium().equalsIgnoreCase("rewards")){
            Integer newRewardsBalance = accountRepository.findById(accountId).get().getRewards() + withdrawal.intValue();
            accountRepository.findById(accountId).get().setRewards(newRewardsBalance);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
