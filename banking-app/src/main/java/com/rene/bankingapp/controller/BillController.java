package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Bill;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class BillController {

    private List<Bill> bills = new ArrayList<>();

    @GetMapping("/accounts/{accountId}/bills")
    public List<Bill> getBillsForAccount(@PathVariable Long accountId) {
        List<Bill> accountBills = findBillsByAccountId(accountId);
        if (accountBills.isEmpty()) {
            throw new ResourceNotFoundException("No bills found for account with ID: " + accountId);
        }
        return accountBills;
    }

    @GetMapping("/bills/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long billId) {
        Bill bill = findBillById(billId);
        return ResponseEntity.ok(bill);
    }

    @GetMapping("/customers/{customerId}/bills")
    public List<Bill> getBillsForCustomer(@PathVariable Long customerId) {
        List<Bill> customerBills = findBillsByCustomerId(customerId);
        if (customerBills.isEmpty()) {
            throw new ResourceNotFoundException("No bills found for customer with ID: " + customerId);
        }
        return customerBills;
    }

    @PostMapping("/accounts/{accountId}/bills")
    public ResponseEntity<String> createBill(@PathVariable Long accountId, @RequestBody @Valid Bill bill) {
        try {
            bill.setId(generateBillId());
            bill.getAccount().setId(accountId);
            bills.add(bill);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created bill and added it to the account");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating bill: " + ex.getMessage());
        }
    }

    @PutMapping("/bills/{billId}")
    public ResponseEntity<String> updateBill(@PathVariable Long billId, @RequestBody @Valid Bill updatedBill) {
        try {
            Bill existingBill = findBillById(billId);
            existingBill.setStatus(updatedBill.getStatus());
            existingBill.setPayee(updatedBill.getPayee());
            existingBill.setNickname(updatedBill.getNickname());
            existingBill.setCreationDate(updatedBill.getCreationDate());
            existingBill.setPaymentDate(updatedBill.getPaymentDate());
            existingBill.setRecurringDate(updatedBill.getRecurringDate());
            existingBill.setUpcomingPaymentDate(updatedBill.getUpcomingPaymentDate());
            existingBill.setPaymentAmount(updatedBill.getPaymentAmount());
            return ResponseEntity.ok("Bill updated");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/bills/{billId}")
    public ResponseEntity<String> deleteBill(@PathVariable Long billId) {
        Iterator<Bill> iterator = bills.iterator();
        while (iterator.hasNext()) {
            Bill bill = iterator.next();
            if (bill.getId().equals(billId)) {
                iterator.remove();
                return ResponseEntity.ok("Bill deleted successfully");
            }
        }throw new ResourceNotFoundException("Bill with ID " + billId + " not found");
    }

    private List<Bill> findBillsByAccountId(Long accountId) {
        List<Bill> accountBills = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getAccount().getId().equals(accountId)) {
                accountBills.add(bill);
            }
        }return accountBills;
    }

    private List<Bill> findBillsByCustomerId(Long customerId) {
        List<Bill> customerBills = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getCustomer().getId().equals(customerId)) {
                customerBills.add(bill);
            }
        }return customerBills;
    }

    private Bill findBillById(Long billId) {
        return bills.stream()
                .filter(bill -> bill.getId().equals(billId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Bill with ID " + billId + " not found"));
    }
    private Long generateBillId() {
        return Long.valueOf(bills.size() + 1);
    }
}


