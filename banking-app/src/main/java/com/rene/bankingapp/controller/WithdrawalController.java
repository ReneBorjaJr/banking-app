package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Withdrawal;
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


    @GetMapping(value="/accounts/{accountId}/withrdawals")
    public ResponseEntity<Iterable<Withdrawal>> getAllWithdrawals() {
        Iterable<Withdrawal> withdrawals = withdrawalService.getAllWithdrawals();
        return new ResponseEntity<>(withdrawalService.getAllWithdrawals(), HttpStatus.OK);
    }
    @GetMapping(value="/withdrawals/{withdrawalId}")
    public ResponseEntity<?> getWithdrawal(@PathVariable Long withdrawalId) {
        Optional<Withdrawal> withdrawal = withdrawalService.getWithdrawal(withdrawalId);
        return new ResponseEntity<> (withdrawal, HttpStatus.OK);
    }
    @PostMapping(value="/accounts/{accountId}/withdrawals")
    public ResponseEntity<?> createWithdrawal(@RequestBody Withdrawal withdrawal) {
        withdrawal = withdrawalService.createWithdrawal(withdrawal);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
    @PutMapping(value="/polls/{pollId}")
    public ResponseEntity<?> updateWithdrawal(@RequestBody Withdrawal withdrawal, @PathVariable Long withdrawalId) {
        Withdrawal w = withdrawalService.createWithdrawal(withdrawal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping(value="/withdrawals/{withdrawalId}")
    public ResponseEntity<?> deleteWithdrawal(@PathVariable Long withdrawalId) {
        withdrawalService.delete(withdrawalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
