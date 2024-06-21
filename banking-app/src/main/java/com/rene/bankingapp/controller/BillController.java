package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Bill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BillController {

    private List<Bill> bills = new ArrayList<>();

    @GetMapping("/accounts/{accountId}/bills")
    public List<Bill> getBillsForAccount(@PathVariable Long accountId) {
        List<Bill> accountBills = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getAccount().getId().equals(accountId)) {
                accountBills.add(bill);
            }
        }
        return accountBills;
    }

    @GetMapping("/bills/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long billId) {
        for (Bill bill : bills) {
            if (bill.getId().equals(billId)) {
                return ResponseEntity.ok(bill);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/customers/{customerId}/bills")
    public List<Bill> getBillsForCustomer(@PathVariable Long customerId) {
        List<Bill> customerBills = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getAccount().getCustomerId().equals(customerId)) {
                customerBills.add(bill);
            }
        }
        return customerBills;
    }

    @PostMapping("/accounts/{accountId}/bills")
    public ResponseEntity<String> createBill(@PathVariable Long accountId, @RequestBody Bill bill) {
        bill.getAccount().setId(accountId);
        bills.add(bill);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created bill and added it to the account");
    }

    @PutMapping("/bills/{billId}")
    public ResponseEntity<String> updateBill(@PathVariable Long billId, @RequestBody Bill updatedBill) {
        for (Bill bill : bills) {
            if (bill.getId().equals(billId)) {
                bill.setStatus(updatedBill.getStatus());
                bill.setPayee(updatedBill.getPayee());
                // Add other fields as needed
                return ResponseEntity.ok("Bill updated");
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/bills/{billId}")
    public ResponseEntity<String> deleteBill(@PathVariable Long billId) {
        for (Bill bill : bills) {
            if (bill.getId().equals(billId)) {
                bills.remove(bill);
                return ResponseEntity.ok("Bill deleted successfully");
            }
        }
        return ResponseEntity.notFound().build();
    }
}

