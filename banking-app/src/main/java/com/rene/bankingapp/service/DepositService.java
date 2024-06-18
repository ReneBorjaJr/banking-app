package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Deposit;
import com.rene.bankingapp.domain.enums.Medium;
import com.rene.bankingapp.domain.enums.TransactionType;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import com.rene.bankingapp.repository.DepositRepository;
import com.rene.bankingapp.successfulresponse.ApiResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class DepositService {

    private static final Logger log = LoggerFactory.getLogger(DepositService.class);

    @Autowired
    private DepositRepository depositRepository;

    public ResponseEntity<Iterable<Deposit>> getAllDepositsByAccount(Long accountId){

        // validate that account exists

        // run method that gets all deposits by accountId and store it in an object

        // generate a Successful response object and store relevant data in it


        List<Deposit> listForResponse = new ArrayList<>();

        allDeposits.forEach(listForResponse::add);

        ApiResponse<Deposit> apiResponse = new ApiResponse<>(200, listForResponse);




        // populate the response entity

        return ResponseEntity<>;
    }

    public ResponseEntity<?> getADepositById(Long depositId) {

        // validate that deposit exists

        // write code that returns the deposit

        // generate a successful response object and store relevant data in it


        List<Deposit> listForResponse = new ArrayList<>();
        Deposit deposit = depositOptional.get();
        listForResponse.add(deposit);
        ApiResponse<Deposit> apiResponse = new ApiResponse<>(200, listForResponse);



        // populate the response entity

        return ResponseEntity<>;
    }

    public ResponseEntity<?> createTheDeposit(Long accountId, TransactionType depositType, Long payeeId, Medium depositMedium, Double depositAmount, String depositDescription) {

        // validate that medium is not withdraw

        // validate that account exists

        // check deposit type
          // if deposit type = deposit
            // run a method that processes deposit transaction type

          // if deposit type = p2p
            // run a method that processes p2p transaction type

        return ResponseEntity<>;
    }

    public ResponseEntity<?> updateADeposit(Long depositId, Deposit depositToUpdateWith) {

        //update deposit logic

        return new ResponseEntity<>;
    }

    public ResponseEntity<?> deleteADeposit(Long depositId) {

        // delete deposit logic

        return ResponseEntity<>;
    }

    // Verify Methods

    public void verifyDepositExists(Long depositId) throws ResourceNotFoundException {

        // validation logic

    }

    public void verifyAccountExists(Long accountId) throws ResourceNotFoundException {

        // validation logic

    }

    public void verifyNotWithdraw(TransactionType transactionType){

        // throw TransactionMismatchException or something
    }



    // Process Transaction Methods
    public ResponseEntity<?> processDepositTransaction(Long accountId, @Null Long payeeId, Medium depositMedium, Double depositAmount, String depositDescription){

        // check deposit medium
            // if Balance medium
                // make Balance deposit

            // if Points medium
                // make Points deposit

        return ResponseEntity<>;
    }

    public ResponseEntity<?> processP2pTransaction(Long accountId, @NotNull Long payeeId, Medium depositMedium, Double depositAmount, String depositDescription){

        // validate payeeId

        // validate that deposit medium is not rewards, if so, throw new MediumMismatch error

        // make p2p deposit

        return ResponseEntity<>;
    }


    // Create Deposit Methods
    public ResponseEntity<?> createBalanceDeposit(Long accountId, Double depositAmount, String depositDescription){

        // create new Deposit object

        // set values of object

        // save object to repo and store method call as a new Deposit object

        // process deposit

        return ResponseEntity<>;
    }

    public ResponseEntity<?> createRewardsDeposit(Long accountId, Double depositAmount, String depositDescription){

        // create new Deposit object

        // set values of object

        // save object to repo and store method call as a new Deposit object

        // process deposit

        return ResponseEntity<>;
    }

    public ResponseEntity<?> createP2pDeposit(Long accountId, Long payeeId, Double depositAmount, String depositDescription){

        // create new Deposit object

        // set values of object

        // save object to repo and store method call as a new Deposit object

        // process deposit

        return ResponseEntity<>;
    }


    // Process Deposit Methods

    public ResponseEntity<?> processDepositByDeposit(Deposit deposit){

            // get account id from deposit
            // get transactionType

                // if p2p
                    // validate that payee and payer account still exist
                    // check that payer balance >= deposit amount, if not throw InsufficientFundsException
                    // add depositAmount to payee account balance
                    // update deposit status


                // if Deposit
                    // validate account still exists
                    // add deposit amount based on medium type to account balance
                    // update deposit status

            Double payeeBalance = payeeAccount.getBalance();
            Double payeeNewBalance = payeeBalance + depositAmount;

            payeeAccount.setBalance(payeeNewBalance);

            accountRepository.save(payeeAccount);

            // subtract depositAmount from payer account

            Optional<Account> payerAccountOptional = accountRepository.findById(accountId);
            Account payerAccount = payerAccountOptional.get();

            Double payerBalance = payerAccount.getBalance();
            Double payerNewBalance = payerBalance - depositAmount;

            payerAccount.setBalance(payerNewBalance);

            accountRepository.save(payerAccount);

            // update deposit status
            deposit.setStatus("Completed");
            depositRepository.save(deposit);

        } else {
            // if Deposit
            // add deposit amount based on medium type to account balance
            Optional<Account> accountOptional = accountRepository.findById(accountId);
            Account account = accountOptional.get();

            Double accountBalance = account.getBalance();
            Double accountNewBalance = accountBalance + depositAmount;

            account.setBalance(accountNewBalance);

            accountRepository.save(account);

            // update deposit status
            deposit.setStatus("Completed");
            depositRepository.save(deposit);
        }

        return ResponseEntity<>;
    }
}
