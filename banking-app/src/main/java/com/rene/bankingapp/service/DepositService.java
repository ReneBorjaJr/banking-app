package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Deposit;
import com.rene.bankingapp.exception.ResourceNotFoundException;
import com.rene.bankingapp.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    public ResponseEntity<Iterable<Deposit>> getAllDepositsByAccount(Long accountId){

        // validate that account exists

        // run method that gets all deposits by accountId and store it in an object

        // generate a Successful response object and store relevant data in it

        // populate the response entity

        return ResponseEntity<>;
    }

    public ResponseEntity<?> getADepositById(Long depositId) {

        // validate that deposit exists

        // write code that returns the deposit

        // generate a successful response object and store relevant data in it

        // populate the response entity

        return ResponseEntity<>;
    }

    public ResponseEntity<?> createTheDeposit(Long accountId, DepositType depositType, Long payeeId, DepositMedium depositMedium, Double depositAmount, String depositDescription) {


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

        return ResponseEntity<>;
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

    // Process Transaction Methods
    public ResponseEntity<?> processDepositTransaction(Long accountId, DepositMedium depositMedium, Double depositAmount, String depositDescription){

        // check deposit medium
            // if Balance medium
                // make Balance deposit

            // if Points medium
                // make Points deposit

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


    // Process Deposit Methods

    public ResponseEntity<?> processDeposit(Deposit deposit){


        // get deposit id from deposit object and make sure it exists

        // check deposit status

        // if status is pending

            // get account id from deposit
            // get transactionType

                // if p2p
                    // validate that payee account still exists
                    // add depositAmount to payee account balance
                    // update deposit status

                // if Deposit
                    // validate account still exists
                    // add deposit amount based on medium type to account balance
                    // update deposit status

        return ResponseEntity<>;
    }
}
