package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Deposit;
import com.rene.bankingapp.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
public class DepositController {

    @Autowired
    private DepositService depositService;


    @GetMapping("/accounts/{accountId}/deposits")
    public ResponseEntity<Iterable<Deposit>> getDepositsByAccount(@PathVariable Long accountId){
        return depositService.getAllDepositsByAccount(accountId);
    }



    @GetMapping("/deposits/{depositId}")
    public ResponseEntity<?> getDepositById(@PathVariable Long depositId){
        return depositService.getADepositById(depositId);
    }


    @PostMapping("/accounts/{accountId}/deposits")
    public ResponseEntity<?> createADeposit(@PathVariable Long accountId, DepositType depositType, @RequestParam(required = false) Long payee_id, DepositMedium depositMedium, Double depositAmount, @RequestParam(required = false, defaultValue = "No description given.") String depositDescription){

        return depositService.createTheDeposit(accountId, depositType, payee_id, depositMedium, depositAmount, depositDescription);
    }



    @PutMapping("/deposits/{depositId}")
    public ResponseEntity<?> updateDeposit(@PathVariable Long depositId, Deposit updateDeposit){
        return depositService.updateADeposit(depositId, updateDeposit);
    }



    @DeleteMapping("/deposits/{depositId}")
    public ResponseEntity<?> deleteDeposit(@PathVariable Long depositId){
        return depositService.deleteADeposit(depositId);
    }

}
