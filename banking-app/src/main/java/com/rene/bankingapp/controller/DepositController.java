package com.rene.bankingapp.controller;

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
    public ResponseEntity<?> createADeposit(@PathVariable Long accountId, @NotNull TransactionType depositType, @RequestParam(required = false) Long payee_id, @NotNull Medium depositMedium, @Positive @NotNull Double depositAmount, @RequestParam(required = false, defaultValue = "No description given.") String depositDescription){

        return depositService.createTheDeposit(accountId, depositType, payee_id, depositMedium, depositAmount, depositDescription);

    }



    @PutMapping("/deposits/{depositId}")
    public ResponseEntity<?> updateDeposit(@PathVariable Long depositId, @Valid Deposit depositToUpdateWith){

        return depositService.updateADeposit(depositId, depositToUpdateWith);

    }



    @DeleteMapping("/deposits/{depositId}")
    public ResponseEntity<?> deleteDeposit(@PathVariable Long depositId){

        return depositService.deleteADeposit(depositId);

    }
}
