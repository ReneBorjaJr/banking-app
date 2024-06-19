package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Deposit;
import com.rene.bankingapp.domain.enums.Medium;
import com.rene.bankingapp.domain.enums.TransactionType;
import com.rene.bankingapp.service.DepositService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class DepositController {

    @Autowired
    private DepositService depositService;


    @GetMapping("/accounts/{accountId}/deposits")
    public ResponseEntity<?> getDepositsByAccount(@PathVariable Long accountId){

        return depositService.getAllDepositsByAccount(accountId);

    }



    @GetMapping("/deposits/{depositId}")
    public ResponseEntity<?> getDepositById(@PathVariable Long depositId){

        return depositService.getADepositById(depositId);

    }


    @PostMapping("/accounts/{accountId}/deposits")
    public ResponseEntity<?> createADeposit(@PathVariable Long accountId, @NotNull @RequestPart TransactionType depositType, @RequestPart(required = false) Long payee_id, @NotNull @RequestPart Medium depositMedium, @Positive @NotNull @RequestPart Double depositAmount, @RequestPart(required = false) String depositDescription){

        return depositService.createTheDeposit(accountId, depositType, payee_id, depositMedium, depositAmount, depositDescription);

    }



    @PutMapping("/deposits/{depositId}")
    public ResponseEntity<?> updateDeposit(@PathVariable Long depositId, @Valid @RequestBody Deposit depositToUpdateWith){

        return depositService.updateADeposit(depositId, depositToUpdateWith);

    }



    @DeleteMapping("/deposits/{depositId}")
    public ResponseEntity<?> deleteDeposit(@PathVariable Long depositId){

        return depositService.deleteADeposit(depositId);

    }

    @PutMapping("/deposits/process/{depositId}")
    public ResponseEntity<?> processDeposit(@PathVariable Long depositId){

        return depositService.processDepositById(depositId);

    }

}
