package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.domain.Withdrawal;
import com.rene.bankingapp.domain.enums.TransactionType;
import com.rene.bankingapp.exceptions.InsufficientFundsException;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import com.rene.bankingapp.exceptions.TransactionMismatchException;
import com.rene.bankingapp.repository.AccountRepository;
import com.rene.bankingapp.repository.WithdrawalRepository;
import com.rene.bankingapp.successfulresponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private AccountRepository accountRepository;


    public ResponseEntity<?> createWithdrawal(Withdrawal withdrawal, Long accountId) {
        verifyAccountExists(accountId);
        verifyNotDeposit(withdrawal.getType());
        withdrawal = withdrawalRepository.save(withdrawal);
        if (withdrawal.getMedium().equalsIgnoreCase("balance")) {
            Double newAccountBalance = accountRepository.findById(accountId).get().getBalance() - withdrawal.getAmount();
            accountRepository.findById(accountId).get().setBalance(newAccountBalance);
        }
        if (withdrawal.getMedium().equalsIgnoreCase("rewards")) {
            Integer newRewardsBalance = accountRepository.findById(accountId).get().getRewards() - withdrawal.getAmount().intValue();
            accountRepository.findById(accountId).get().setRewards(newRewardsBalance);
        }
        ApiResponse<Withdrawal> successfulResponse = new ApiResponse<>();
        List<Withdrawal> listOfWithdrawals = new ArrayList<>();
        listOfWithdrawals.add(withdrawal);
        successfulResponse.setCode(HttpStatus.CREATED.value());
        successfulResponse.setMessage("Withdrawal successful");
        successfulResponse.setData(listOfWithdrawals);
        return new ResponseEntity<>(successfulResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllWithdrawalsByAccountId(Long accountId) {
        verifyAccountExists(accountId);
        Iterable<Withdrawal> withdrawals = withdrawalRepository.findAll();
        List<Withdrawal> listOfWithdrawals = new ArrayList<>();
        withdrawals.forEach(listOfWithdrawals::add);
        ApiResponse<Withdrawal> successfulResponse = new ApiResponse<>();
        successfulResponse.setCode(HttpStatus.OK.value());
        successfulResponse.setMessage("All withdrawals retrieved successfully");
        successfulResponse.setData(listOfWithdrawals);
        return new ResponseEntity<>(listOfWithdrawals, HttpStatus.OK);
    }


    public ResponseEntity<?> getWithdrawalById(Long withdrawalId) {
        verifyWithdrawalExists(withdrawalId);
        Optional<Withdrawal> withdrawal = withdrawalRepository.findById(withdrawalId);
        List<Withdrawal> listOfWithdrawal = new ArrayList<>();
        withdrawal.ifPresent(listOfWithdrawal::add);
        ApiResponse<Withdrawal> successfulResponse = new ApiResponse<>();
        successfulResponse.setCode(HttpStatus.OK.value());
        successfulResponse.setMessage("Withdrawal with id " + withdrawalId + " retrieved");
        successfulResponse.setData(listOfWithdrawal);
        return new ResponseEntity<>(successfulResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> updateWithdrawal(Withdrawal withdrawal, Long withdrawalId) {
        verifyWithdrawalExists(withdrawalId);
        verifySufficientFunds(withdrawalId, withdrawal.getAmount());
        Double dbAmount = withdrawalRepository.findById(withdrawalId).get().getAmount();
        Double bodyAmount = withdrawal.getAmount();
        Long accountId = withdrawalRepository.findById(withdrawalId).get().getPayer_id();
        Double balance = accountRepository.findById(accountId).get().getBalance();
        Double newBalance;
        String dbMedium = withdrawalRepository.findById(withdrawalId).get().getMedium();
        String bodyMedium = withdrawal.getMedium();
        if (!dbMedium.equalsIgnoreCase(bodyMedium)) {
            deleteWithdrawal(withdrawalId);
            createWithdrawal(withdrawal, accountId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (dbAmount > bodyAmount) {
            newBalance = balance + (dbAmount - bodyAmount);
            accountRepository.findById(accountId).get().setBalance(newBalance);
        }
        if (dbAmount < bodyAmount) {
            newBalance = balance - (bodyAmount - dbAmount);
            accountRepository.findById(accountId).get().setBalance(newBalance);
        }
        ResponseEntity<?> newWithdrawal = createWithdrawal(withdrawal, withdrawalId);
        List<Withdrawal> listOfWithdrawal = new ArrayList<>();
        listOfWithdrawal.add((Withdrawal) newWithdrawal.getBody());
        ApiResponse<Withdrawal> successfulResponse = new ApiResponse<>();
        successfulResponse.setCode(HttpStatus.OK.value());
        successfulResponse.setMessage("Updated withdrawal successfully");
        successfulResponse.setData(listOfWithdrawal);
        return new ResponseEntity<>(successfulResponse, HttpStatus.OK);

    }

    public ResponseEntity<?> deleteWithdrawal(Long withdrawalId) {
        verifyWithdrawalExists(withdrawalId);
        withdrawalRepository.deleteById(withdrawalId);
        Long accountId = withdrawalRepository.findById(withdrawalId).get().getPayer_id();
        Double withdrawal = withdrawalRepository.findById(withdrawalId).get().getAmount();
        if (withdrawalRepository.findById(withdrawalId).get().getMedium().equalsIgnoreCase("balance")) {
            Double newAccountBalance = accountRepository.findById(accountId).get().getBalance() + withdrawal;
            accountRepository.findById(accountId).get().setBalance(newAccountBalance);
        }
        if (withdrawalRepository.findById(withdrawalId).get().getMedium().equalsIgnoreCase("rewards")) {
            Integer newRewardsBalance = accountRepository.findById(accountId).get().getRewards() + withdrawal.intValue();
            accountRepository.findById(accountId).get().setRewards(newRewardsBalance);
        }
        ApiResponse<Withdrawal> successfulResponse = new ApiResponse<>();
        successfulResponse.setCode(HttpStatus.OK.value());
        successfulResponse.setMessage("Withdrawal with id: " + withdrawalId + " successfully deleted");
        return new ResponseEntity<>(successfulResponse, HttpStatus.OK);
    }


    private void verifyAccountExists(Long accountId) throws ResourceNotFoundException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Account with Id: " + accountId + " not found.");
        }
    }

    public void verifyNotDeposit(TransactionType transactionType) {
        if (transactionType.equals(TransactionType.DEPOSIT)) {
            throw new TransactionMismatchException("Transaction type: " + transactionType.name() + " is not valid for this operation.");
        }
    }

    public void verifyWithdrawalExists(Long withdrawalId) throws ResourceNotFoundException {
        Optional<Withdrawal> withdrawal = withdrawalRepository.findById(withdrawalId);
        if (withdrawal.isEmpty()) {
            throw new ResourceNotFoundException("Withdrawal with Id: " + withdrawalId + " not found.");
        }
    }

    public void verifySufficientFunds(Long accountId, Double withdrawalAmount) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        Account account = accountOptional.get();
        Double accountBalance = account.getBalance();
        if (accountBalance < withdrawalAmount) {
            throw new InsufficientFundsException("Insufficient funds in account for deposit transaction of: " + withdrawalAmount + ". Current account balance: " + accountBalance + ".");
        }
    }
}
