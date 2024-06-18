package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.domain.Deposit;
import com.rene.bankingapp.domain.enums.Medium;
import com.rene.bankingapp.domain.enums.TransactionType;
import com.rene.bankingapp.exceptions.InsufficientFundsException;
import com.rene.bankingapp.exceptions.MediumMismatchException;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import com.rene.bankingapp.exceptions.TransactionMismatchException;
import com.rene.bankingapp.repository.AccountRepository;
import com.rene.bankingapp.repository.DepositRepository;
import com.rene.bankingapp.successfulresponse.ApiResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class DepositService {

    private static final Logger log = LoggerFactory.getLogger(DepositService.class);

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<?> getAllDepositsByAccount(Long accountId){

        // verify account exists
        verifyAccountExists(accountId);

        // run method that gets all deposits by accountId and store it in an object
        Iterable<Deposit> allDeposits = depositRepository.findAllByAccountId(accountId);

        // generate a Successful response object and store relevant data in it
        List<Deposit> listForResponse = new ArrayList<>();

        allDeposits.forEach(listForResponse::add);

        ApiResponse<Deposit> apiResponse = new ApiResponse<>(200, listForResponse);



        // populate the response entity
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    public ResponseEntity<?> getADepositById(Long depositId) {

        // validate that deposit exists
        verifyDepositExists(depositId);

        // write code that returns the deposit
        Optional<Deposit> depositOptional = depositRepository.findById(depositId);


        // generate a successful response object and store relevant data in it
        List<Deposit> listForResponse = new ArrayList<>();
        Deposit deposit = depositOptional.get();
        listForResponse.add(deposit);
        ApiResponse<Deposit> apiResponse = new ApiResponse<>(200, listForResponse);


        // populate the response entity

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> createTheDeposit(Long accountId, TransactionType depositType, Long payeeId, Medium depositMedium, Double depositAmount, String depositDescription) {

        // validate that medium is not withdraw
        verifyNotWithdraw(depositType);

        // validate that account exists
        verifyAccountExists(accountId);

        // check deposit type
        // if deposit type = deposit
        if (depositType.equals(TransactionType.DEPOSIT)) {
            // run a method that processes deposit transaction type
            return processDepositTransaction(accountId, payeeId, depositMedium, depositAmount, depositDescription);

        } else {
            // if deposit type = p2p
            // run a method that processes p2p transaction type
            return processP2pTransaction(accountId, payeeId, depositMedium, depositAmount, depositDescription);

        }
    }

//    public ResponseEntity<?> updateADeposit(Long depositId, Deposit depositToUpdateWith) {
//
//        //update deposit logic
//
//        return new ResponseEntity<>;
//    }
//
//    public ResponseEntity<?> deleteADeposit(Long depositId) {
//
//        // delete deposit logic
//        verifyDepositExists(depositId);
//        accountRepository.deleteById(depositId);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    // Verify Methods

    public void verifyDepositExists(Long depositId) throws ResourceNotFoundException {

        // validation logic
        Optional<Deposit> deposit = depositRepository.findById(depositId);
        if(deposit.isEmpty()){
            throw new ResourceNotFoundException("Deposit with Id: " + depositId + " not found.");
        }
    }

    public void verifyAccountExists(Long accountId) throws ResourceNotFoundException {

        // validation logic

        Optional<Account> account = accountRepository.findById(accountId);
        if(account.isEmpty()){
            throw new ResourceNotFoundException("Account with Id: " + accountId + " not found.");
        }

    }

    public void verifyNotWithdraw(TransactionType transactionType){

        // throw TransactionMismatchException or something

        if (transactionType.equals(TransactionType.WITHDRAWAL)){
            throw new TransactionMismatchException("Transaction type: " + transactionType.name() + " is not valid for this operation.");
        }

    }

    public void verifySufficientFunds(Long accountId, Double depositAmount){

        // verify account exists
        // verifyAccountExists(accountId);

        // get account balance
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        Account account = accountOptional.get();
        Double accountBalance = account.getBalance();

        // check for sufficient funds

        if (accountBalance < depositAmount){
            throw new InsufficientFundsException("Insufficient funds in account for deposit transaction of: " + depositAmount + ". Current account balance: " + accountBalance + ".");
        }
    }



    // Process Transaction Methods
    public ResponseEntity<?> processDepositTransaction(Long accountId, @Null Long payeeId, Medium depositMedium, Double depositAmount, String depositDescription){

        // check deposit medium
            // if Balance medium
            if (depositMedium.equals(Medium.BALANCE)){
                // make Balance deposit
                return createBalanceDeposit(accountId, depositAmount, depositDescription);

            } else {
                // if rewards medium
                // make rewards deposit
                return createRewardsDeposit(accountId, depositAmount, depositDescription);
            }
    }

    public ResponseEntity<?> processP2pTransaction(Long accountId, @NotNull Long payeeId, Medium depositMedium, Double depositAmount, String depositDescription){

        // validate payeeId
        verifyAccountExists(payeeId);

        // validate that deposit medium is not rewards, if so, throw new MediumMismatch error
        if (depositMedium.equals(Medium.REWARDS)){
            throw new MediumMismatchException("Medium type: " + depositMedium.name() + " is not valid for this operation.");
        }

        // make p2p deposit
        return createP2pDeposit(accountId, payeeId, depositAmount, depositDescription);

    }


    // Create Deposit Methods
    public ResponseEntity<?> createBalanceDeposit(Long accountId, Double depositAmount, String depositDescription){

        // create new Deposit object
        Deposit deposit = new Deposit();

        // set values of object
        deposit.setType("Deposit");
        deposit.setTransactionDate(LocalTime.now().toString());
        deposit.setStatus("Pending");
        deposit.setMedium("Balance");
        deposit.setAmount(depositAmount);
        deposit.setDescription(depositDescription);

        Optional<Account> account = accountRepository.findById(accountId);
        deposit.setAccount(account.get());

        // save object to repo and store method call as a new Deposit object
        deposit = depositRepository.save(deposit);

        // process deposit
        processDepositByDeposit(deposit);

        // return created response entity
        List<Deposit> listForResponse = new ArrayList<>();
        listForResponse.add(deposit);

        ApiResponse<?> apiResponse = new ApiResponse<>(201, "Created deposit and added it to the account.", listForResponse);

        //return response entity
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    public ResponseEntity<?> createRewardsDeposit(Long accountId, Double depositAmount, String depositDescription){

        // create new Deposit object
        Deposit deposit = new Deposit();

        // set values of object
        deposit.setType("Deposit");
        deposit.setTransactionDate(LocalTime.now().toString());
        deposit.setStatus("Pending");
        deposit.setMedium("Rewards");
        deposit.setAmount(depositAmount);
        deposit.setDescription(depositDescription);

        Optional<Account> account = accountRepository.findById(accountId);
        deposit.setAccount(account.get());

        // save object to repo and store method call as a new Deposit object
        deposit = depositRepository.save(deposit);

        // process deposit
        processDepositByDeposit(deposit);

        // return created response entity
        List<Deposit> listForResponse = new ArrayList<>();
        listForResponse.add(deposit);

        ApiResponse<?> apiResponse = new ApiResponse<>(201, "Created deposit and added it to the account.", listForResponse);

        //return response entity
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    public ResponseEntity<?> createP2pDeposit(Long accountId, Long payeeId, Double depositAmount, String depositDescription){

        // create new Deposit object
        Deposit deposit = new Deposit();

        // set values of object
        deposit.setType("P2P");
        deposit.setTransactionDate(LocalTime.now().toString());
        deposit.setStatus("Pending");
        deposit.setPayee_id(payeeId);
        deposit.setMedium("Balance");
        deposit.setAmount(depositAmount);
        deposit.setDescription(depositDescription);

        Optional<Account> account = accountRepository.findById(accountId);
        deposit.setAccount(account.get());

        // save object to repo and store method call as a new Deposit object
        deposit = depositRepository.save(deposit);

        // process deposit
        processDepositByDeposit(deposit);

        // return created response entity
        List<Deposit> listForResponse = new ArrayList<>();
        listForResponse.add(deposit);

        ApiResponse<?> apiResponse = new ApiResponse<>(201, "Created deposit and added it to the account.", listForResponse);

        //return response entity
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }


    // Process Deposit Methods

    public void processDepositByDeposit(Deposit deposit){

            // get account id from deposit
            Long accountId = deposit.getAccount().getId();

            // validate account still exists
            verifyAccountExists(accountId);

            // get transactionType
            String transactionType = deposit.getType();

            // get deposit amount
            Double depositAmount = deposit.getAmount();


        // if p2p
        if (transactionType.equals("P2P")) {

            // check payee account still exists
            Long payeeId = deposit.getPayee_id();
            verifyAccountExists(payeeId);

            // check that payer balance >= deposit amount, if not throw InsufficientFundsException
            verifySufficientFunds(accountId, depositAmount);

            // add depositAmount to payee account balance
            Optional<Account> payeeOptional = accountRepository.findById(payeeId);
            Account payeeAccount = payeeOptional.get();

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


    }


}
