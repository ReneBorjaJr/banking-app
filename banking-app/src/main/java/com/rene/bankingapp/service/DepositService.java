package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Deposit;
import com.rene.bankingapp.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    public ResponseEntity<Iterable<Deposit>> getAllDepositsByAccount(Long accountId){

        // get all deposits logic

        return ResponseEntity<>;
    }

    public ResponseEntity<?> getADepositById(Long depositId) {

        // logic for getting a deposit by id

        return ResponseEntity<>;
    }

    public ResponseEntity<?> createTheDeposit(Long accountId, DepositType depositType, Long payeeId, DepositMedium depositMedium, Double depositAmount, String depositDescription) {


        // deposit logic

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
}
