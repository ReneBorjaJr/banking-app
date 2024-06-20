package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.domain.Customer;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import com.rene.bankingapp.repository.AccountRepository;
import com.rene.bankingapp.repository.CustomerRepository;
import com.rene.bankingapp.successfulresponse.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;


    public ResponseEntity<?> getAllAccounts() {
        //New instance of successful response
        ApiResponse<Account> successfulResponse = new ApiResponse<>();

        //storing a code in successfulResponse (HttpStatus.ok.value = 200)
        successfulResponse.setCode(HttpStatus.OK.value());

        //Storing message into successfulResponse
        successfulResponse.setMessage("Success");

        //Iterable of type Account. Its job is to find all accounts
        Iterable<Account> allAccounts = accountRepository.findAll();

        //New list to store accounts
        List<Account> listOfAccounts = new ArrayList<>();

        //Method referencing. adding what is in allAccounts into listOfAccounts
        allAccounts.forEach(listOfAccounts::add);

        //storing the 'Data' into successfulResponse
        successfulResponse.setData(listOfAccounts);

        return new ResponseEntity<>(successfulResponse, HttpStatus.OK);
    }

//    public static Account save(Account account) {
//        return account;
//    }

    private void verifyAccount(Long accountId) throws ResourceNotFoundException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Account with ID " + accountId + " Does not exits ");
        }
    }

    public ResponseEntity<?> getAccountById(Long accountId) {
        verifyAccount(accountId);
        ApiResponse<Account> successfulResponse = new ApiResponse<>();
        successfulResponse.setCode(HttpStatus.OK.value());
        successfulResponse.setMessage("Successfully fetched account");
        Optional<Account> account = accountRepository.findById(accountId);
        List<Account> listOfAccounts = new ArrayList<>();
        Account myAccount = account.get();
        listOfAccounts.add(myAccount);
        successfulResponse.setData(listOfAccounts);
        return new ResponseEntity<>(successfulResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> createAccount(Account account, Long customerId) {
        verifyCustomer(customerId);
        account = accountRepository.save(account);
        ApiResponse<Account> successfulResponse = new ApiResponse<>();
        successfulResponse.setCode(HttpStatus.CREATED.value());
        successfulResponse.setMessage("Account created");
        List<Account> newAccount = new ArrayList<>();
        newAccount.add(account);
        successfulResponse.setData(newAccount);
        return new ResponseEntity<>(successfulResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateAccount(Account account, Long pollId) {
        verifyAccount(pollId);
        account = accountRepository.save(account);
        ApiResponse<Account> successfulResponse = new ApiResponse<>();
        successfulResponse.setCode(HttpStatus.OK.value());
        successfulResponse.setMessage("Customer account updated");
        List<Account> updatedAccount = new ArrayList<>();
        updatedAccount.add(account);
        successfulResponse.setData(updatedAccount);
        return new ResponseEntity<>(successfulResponse, HttpStatus.OK);

    }

    public ResponseEntity<?> deleteAccount(Long accountId) {
        verifyAccount(accountId);
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setMessage("Account successfully deleted");
        accountRepository.deleteById(accountId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllAccountsForCustomer(Long customerId) {
        // check if the customer exists via customerId
        verifyCustomer(customerId);
        // get all accounts for the customer
        List<Account> allAccounts = accountRepository.findAllByCustomerId(customerId);
        // Create an instance of ApiResponse
        ApiResponse<Account> successfulResponse = new ApiResponse<>();
        //Store code in ApiResponse
        successfulResponse.setCode(HttpStatus.OK.value());
        //Store message in ApiResponse
        successfulResponse.setMessage("Success");
        //Store data in ApiResponse
        successfulResponse.setData(allAccounts);
        return new ResponseEntity<>(successfulResponse, HttpStatus.OK);
    }

    private void verifyCustomer(Long customerId) throws ResourceNotFoundException {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isEmpty()) {
            throw new ResourceNotFoundException("Customer with id " + customerId + " not found");
        }
    }
}