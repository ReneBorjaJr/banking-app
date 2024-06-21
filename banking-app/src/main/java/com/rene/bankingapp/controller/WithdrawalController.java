package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Withdrawal;
import com.rene.bankingapp.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @GetMapping(value = "/accounts/{accountId}/withdrawals")
    public ResponseEntity<?> getAllWithdrawalsByAccountId(@PathVariable Long accountId) {
        return withdrawalService.getAllWithdrawalsByAccountId(accountId);
    }


    @GetMapping(value = "/withdrawals/{withdrawalId}")
    public ResponseEntity<?> getWithdrawalById(@PathVariable Long withdrawalId) {
        return withdrawalService.getWithdrawalById(withdrawalId);
    }


    @PostMapping(value = "/accounts/{accountId}/withdrawals")
    public ResponseEntity<?> createWithdrawal(@RequestBody Withdrawal withdrawal, @PathVariable Long accountId) {
        return withdrawalService.createWithdrawal(withdrawal, accountId);
    }


    @PutMapping(value = "/withdrawals/{withdrawalId}")
    public ResponseEntity<?> updateWithdrawal(@RequestBody Withdrawal withdrawal, @PathVariable Long withdrawalId) {
        return withdrawalService.updateWithdrawal(withdrawal, withdrawalId);
    }


    @DeleteMapping(value = "/withdrawals/{withdrawalId}")
    public ResponseEntity<?> deleteWithdrawal(@PathVariable Long withdrawalId) {
        return withdrawalService.deleteWithdrawal(withdrawalId);
    }
}
