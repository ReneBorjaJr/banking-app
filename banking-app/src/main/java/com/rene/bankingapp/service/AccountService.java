package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.domain.Customer;
import com.rene.bankingapp.exceptions.InternalServerErrorException;
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


    private void verifyAccount(Long accountId) throws ResourceNotFoundException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Account with ID " + accountId + " Does not exits ");
        } else {
            System.out.println("Account with ID " + accountId + " exits ");
        }
    }


    public ResponseEntity<?> getAccountById(Long accountId) {
        verifyAccount(accountId);
        ApiResponse<Account> response = new ApiResponse<>();

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Successfully fetched account");
            List<Account> listOfAccounts = new ArrayList<>();
            listOfAccounts.add(account);
            response.setData(listOfAccounts);
        } else {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Unable to fetch account");
            response.setData(null);
        }

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    public ResponseEntity<?> createAccount(Account account, Long customerId) {
        verifyCustomer(customerId);
        verifyAccountIdMatchesCustomerId(customerId, account);
        try {
            account = accountRepository.save(account);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create account", e);
        }
        ApiResponse<Account> successfulResponse = new ApiResponse<>();
        successfulResponse.setCode(HttpStatus.CREATED.value());
        successfulResponse.setMessage("Account created successfully");
        List<Account> newAccount = new ArrayList<>();
        newAccount.add(account);
        successfulResponse.setData(newAccount);
        return new ResponseEntity<>(successfulResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateAccount(Account account, Long accountId) {
        verifyAccount(accountId);
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
        try {
            verifyAccount(accountId);
            ApiResponse<?> apiResponse = new ApiResponse<>();
            apiResponse.setCode(HttpStatus.OK.value());
            apiResponse.setMessage("Account successfully deleted");
            accountRepository.deleteById(accountId);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<?> apiResponse = new ApiResponse<>();
            apiResponse.setCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage("Account not found");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
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

    private void verifyAccountIdMatchesCustomerId(Long customerId, Account account) {
        if (!customerId.equals(account.getCustomerId())) {
            throw new ResourceNotFoundException("Customer id: " + customerId + " does not match the entered customer id: " + account.getCustomerId());
        }
    }
}
