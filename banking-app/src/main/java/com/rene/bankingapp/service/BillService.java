package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Account;
import com.rene.bankingapp.domain.Bill;
import com.rene.bankingapp.domain.Customer;
import com.rene.bankingapp.domain.enums.BillStatus;
import com.rene.bankingapp.exceptions.ConflictException;
import com.rene.bankingapp.exceptions.InvalidInputException;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import com.rene.bankingapp.repository.AccountRepository;
import com.rene.bankingapp.repository.BillRepository;
import com.rene.bankingapp.repository.CustomerRepository;
import com.rene.bankingapp.successfulresponse.ApiResponse;
import com.rene.bankingapp.util.BillCreationRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

//    public List<Bill> getAllBills() {
//        logger.info("Fetching all bills");
//        return billRepository.findAll();
//    }

    public ResponseEntity<?> getBillsForAnAccount(Long accountId){

        verifyAccountExists(accountId);

        List<Bill> allBills = billRepository.findByAccountId(accountId);

        ApiResponse<Bill> apiResponse = new ApiResponse<>(200, "All deposits with accountId (" + accountId + ") retrieved successfully.", allBills);

        logger.info("All deposits with accountId (" + accountId + ") retrieved successfully.");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


//    public Bill getBillById(Long billId) {
//        logger.info("Fetching bill with id: {}", billId);
//        Optional<Bill> optionalBill = billRepository.findById(billId);
//        if (optionalBill.isPresent()) {
//            return optionalBill.get();
//        } else {
//            logger.warn("Bill with id {} not found", billId);
//            throw new ResourceNotFoundException("Error fetching bill with id: " + billId);
//        }
//    }

    public ResponseEntity<?> getABillById(Long billId){

        verifyBillExists(billId);
        Bill bill = billRepository.findById(billId).get();

        List<Bill> listForResponse = new ArrayList<>();
        listForResponse.add(bill);
        ApiResponse<Bill> apiResponse = new ApiResponse<>(200, listForResponse);

        logger.info("Bill with Id (" + billId + ") retrieved successfully.");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);


        }


//    public List<Bill> getBillsByAccountId(Long accountId) {
//        logger.info("Fetching bills for account id: {}", accountId);
//        return billRepository.findByAccountId(accountId);
//    }

    public ResponseEntity<?> getAllBillsByCustomerId(Long customerId){

        verifyCustomerExists(customerId);

        List<Account> listOfCustomersAccounts = accountRepository.findAllByCustomerId(customerId);

        List<Bill> allOfTheBills = new ArrayList<>();

        for (Account account : listOfCustomersAccounts) {

            Long accountId = account.getId();
            List<Bill> billsByAccount = billRepository.findByAccountId(accountId);
            allOfTheBills.addAll(billsByAccount);

        }

        ApiResponse<Bill> apiResponse = new ApiResponse<>(200, "All Bills belonging to Customer with Id (" + customerId + ") retrieved successfully.");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


//    public Bill createBill(Bill bill) {
//        logger.info("Creating a new bill");
//        return billRepository.save(bill);
//    }


    public ResponseEntity<?> createTheBill(Long accountId, BillCreationRequest billCreationRequest){

        verifyProperBillCreationParams(accountId, billCreationRequest);

        BillStatus billStatus = billCreationRequest.getBillStatus();
        String payee = billCreationRequest.getPayee();
        String nickname = billCreationRequest.getNickname();
        Integer recurringDate = billCreationRequest.getRecurringDate();
        Double paymentAmount = billCreationRequest.getPaymentAmount();

        Bill bill = new Bill();

        bill.setStatus(billStatus.name());

        bill.setPayee(payee);
        bill.setNickname(nickname);

        LocalDate creationDate = LocalDate.now();
        bill.setCreationDate(LocalDate.now().toString());

        bill.setPaymentDate("Awaiting payment.");

        if (billStatus.equals(BillStatus.RECURRING)) {

            bill.setRecurringDate(recurringDate);

            LocalDate upcomingPaymentDate = calculateUpcomingPaymentDate(recurringDate, creationDate);

            bill.setUpcomingPaymentDate(upcomingPaymentDate.toString());
        }

        bill.setPaymentAmount(paymentAmount);
        bill.setAccountId(accountId);

        bill = billRepository.save(bill);

        List<Bill> listForResponse = new ArrayList<>();
        listForResponse.add(bill);

        ApiResponse<Bill> apiResponse = new ApiResponse<>(201, "Created the bill and added it to the account", listForResponse);

        logger.info("Created the bill and added it to the account");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

//    public Bill updateBill(Long billId, Bill updatedBill) {
//        logger.info("Updating bill with id: {}", billId);
//        Optional<Bill> optionalExistingBill = billRepository.findById(billId);
//        if (optionalExistingBill.isPresent()) {
//            Bill existingBill = optionalExistingBill.get();
//            existingBill.setStatus(updatedBill.getStatus());
//            existingBill.setPayee(updatedBill.getPayee());
//            existingBill.setNickname(updatedBill.getNickname());
//            existingBill.setCreationDate(updatedBill.getCreationDate());
//            existingBill.setPaymentDate(updatedBill.getPaymentDate());
//            existingBill.setRecurringDate(updatedBill.getRecurringDate());
//            existingBill.setUpcomingPaymentDate(updatedBill.getUpcomingPaymentDate());
//            existingBill.setPaymentAmount(updatedBill.getPaymentAmount());
//            existingBill.getAccount().setId(updatedBill.getAccount().getId());
//
//            return billRepository.save(existingBill);
//        } else {
//            logger.warn("Bill with id {} not found for update", billId);
//            throw new ResourceNotFoundException("Error creating bill: Account not found " + billId);
//        }
//    }


    public ResponseEntity<?> updateABill(Long billId, Bill billToUpdateWith){

        verifyBillExists(billId);

        verifyProperBillToUpdate(billId, billToUpdateWith);

        String billStatus = billToUpdateWith.getStatus();
        if (billStatus.equals("RECURRING")){

            String unparsedCreationDate = billToUpdateWith.getCreationDate();

            LocalDate creationDate = LocalDate.parse(unparsedCreationDate);

            Integer recurringDate = billToUpdateWith.getRecurringDate();

            LocalDate upcomingPaymentDate = calculateUpcomingPaymentDate(recurringDate, creationDate);

            billToUpdateWith.setUpcomingPaymentDate(upcomingPaymentDate.toString());
        }

        billRepository.save(billToUpdateWith);

        List<Bill> listForResponse = new ArrayList<>();
        listForResponse.add(billToUpdateWith);

        ApiResponse<Bill> apiResponse = new ApiResponse<>(200, "Accepted Bill modification for bill with Id (" + billId + ").");

        logger.info("Accepted Bill modification for bill with Id (" + billId + ").");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }




//    public boolean deleteBill(Long billId) {
//        logger.info("Deleting bill with id: {}", billId);
//        Optional<Bill> optionalExistingBill = billRepository.findById(billId);
//        if (optionalExistingBill.isPresent()) {
//            Bill existingBill = optionalExistingBill.get();
//            billRepository.delete(existingBill);
//            return true;
//        } else {
//            logger.warn("Bill with id {} not found for deletion", billId);
//            throw new ResourceNotFoundException("Bill ID does not exist " + billId);
//        }
//    }

    public ResponseEntity<?> deleteABill(Long billId){

        verifyBillExists(billId);

        billRepository.deleteById(billId);

        logger.info("Bill with Id (" + billId + ") deleted successfully.");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    private void verifyAccountExists(Long accountId){

        Optional<Account> account = accountRepository.findById(accountId);
        if(account.isEmpty()){
            throw new ResourceNotFoundException("Account with Id (" + accountId + ") not found.");
        }
    }

    private void verifyBillExists(Long billId) {
        Optional<Bill> deposit = billRepository.findById(billId);
        if(deposit.isEmpty()){
            throw new ResourceNotFoundException("Bill with Id (" + billId + ") not found.");
        }

    }

    private void verifyCustomerExists(Long customerId) {

        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isEmpty()){
            throw new ResourceNotFoundException("Customer with Id (" + customerId + ") not found.");
        }
    }

    private void verifyProperBillCreationParams(Long accountId, BillCreationRequest billCreationRequest) {

        verifyAccountExists(accountId);

        BillStatus billStatus = billCreationRequest.getBillStatus();
        verifyProperBillStatus(billStatus);

        Long billRequestAccountId = billCreationRequest.getAccountId();
        if (!billRequestAccountId.equals(accountId)){
            throw new ConflictException("AccountId must match BillCreation Request's accountId.");
        }

        if (billCreationRequest.getBillStatus().equals(BillStatus.RECURRING) && billCreationRequest.getRecurringDate() == null){
            throw new ConflictException("Reccuring date can not be null for Bill status (" + billStatus.name() + ").");
        }
    }

    private void verifyProperBillStatus(BillStatus billStatus) {

        if (!billStatus.equals(BillStatus.PENDING) && !billStatus.equals(BillStatus.RECURRING)){
            throw new ConflictException("Bill status type (" + billStatus.name() + ") is not valid for this operation.");
        }
    }

    private void verifyProperBillToUpdate(Long billId, Bill billToUpdateWith){

        Bill originalBill = billRepository.findById(billId).get();

        if (!billId.equals(billToUpdateWith.getId())){
            throw new ConflictException("Updated billId must match previous billId.");
        }

        if (!originalBill.getStatus().equals("PENDING") && !originalBill.getStatus().equals("RECURRING")){
            throw new InvalidInputException("Can not update deposit with status (" + billToUpdateWith.getStatus() + ").");
        }

        if (!billToUpdateWith.getNickname().equals(originalBill.getNickname())){
            throw new ConflictException("Updated nickname must match previous bill nickname.");
        }

        if (!billToUpdateWith.getCreationDate().equals(originalBill.getCreationDate())){
            throw new ConflictException("Updated creation date must match previous bill creation date.");
        }

        if (!billToUpdateWith.getPaymentDate().equals(originalBill.getPaymentDate())){
            throw new ConflictException("Updated payment date must match previous bill payment date.");
        }

        if (billToUpdateWith.getStatus().equals("RECURRING") && billToUpdateWith.getRecurringDate() == null){
            throw new InvalidInputException("Can not update bill to recurring without specified recurring date.");
        }

        if (billToUpdateWith.getStatus().equals("CANCELED") || billToUpdateWith.getStatus().equals("COMPLETED")){
            logger.warn("Status for updated bill is either cancelled or completed, and upcoming payment date is not null.\n This field should be left null for this action, and will be overwritten by the system.");
        }

        if (!billToUpdateWith.getRecurringDate().equals(originalBill.getRecurringDate()) && billToUpdateWith.getUpcomingPaymentDate() != null){
            logger.warn("Reccurring date is different in updated bill compared to original, and upcoming payment date is not null.\n This field should be left null for this action, and will be overwritten by the system.");
        }
    }

    private LocalDate calculateUpcomingPaymentDate(Integer recurringDate, LocalDate creationDate){

        LocalDate upcomingPaymentDate = LocalDate.parse("0000-00-00");
        try {
            upcomingPaymentDate = YearMonth.from(creationDate).plusMonths(1).atDay(recurringDate);
        } catch (DateTimeException e){
            calculateUpcomingPaymentDate(recurringDate - 1, creationDate);
        }
        return upcomingPaymentDate;
    }
}


