package com.rene.bankingapp.controller;

import com.rene.bankingapp.domain.Bill;
import com.rene.bankingapp.service.BillService;
import com.rene.bankingapp.util.BillCreationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
public class BillController {

    @Autowired
    private BillService billService;

//    private List<Bill> bills = new ArrayList<>();

//    @GetMapping("/accounts/{accountId}/bills")
//    public List<Bill> getBillsForAccount(@PathVariable Long accountId) {
//        List<Bill> accountBills = new ArrayList<>();
//        for (Bill bill : bills) {
//            if (bill.getAccount().getId().equals(accountId)) {
//                accountBills.add(bill);
//            }
//        }
//        return accountBills;
//    }

    @GetMapping("/accounts/{accountId}/bills")
    public ResponseEntity<?> getBillsForAccount(@PathVariable Long accountId) {

        return billService.getBillsForAnAccount(accountId);
    }


//    @GetMapping("/bills/{billId}")
//    public ResponseEntity<Bill> getBillById(@PathVariable Long billId) {
//        for (Bill bill : bills) {
//            if (bill.getId().equals(billId)) {
//                return ResponseEntity.ok(bill);
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }

    @GetMapping("/bills/{billId}")
    public ResponseEntity<?> getBillById(@PathVariable Long billId) {

        return billService.getABillById(billId);
    }

//    @GetMapping("/customers/{customerId}/bills")
//    public List<Bill> getBillsForCustomer(@PathVariable Long customerId) {
//        List<Bill> customerBills = new ArrayList<>();
//        for (Bill bill : bills) {
//            if (bill.getAccount().getCustomer().getId().equals(customerId)) {
//                customerBills.add(bill);
//            }
//        }
//        return customerBills;
//    }

    @GetMapping("/customers/{customerId}/bills")
    public ResponseEntity<?> getBillsForCustomer(@PathVariable Long customerId) {

        return billService.getAllBillsByCustomerId(customerId);
    }

//    @PostMapping("/accounts/{accountId}/bills")
//    public ResponseEntity<String> createBill(@PathVariable Long accountId, @RequestBody Bill bill) {
//        bill.getAccount().setId(accountId);
//        bills.add(bill);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Created bill and added it to the account");
//    }

    @PostMapping("/accounts/{accountId}/bills")
    public ResponseEntity<?> createABill(@PathVariable Long accountId, @RequestBody @Valid BillCreationRequest billRequest) {

        return billService.createTheBill(accountId, billRequest);

    }


//    @PutMapping("/bills/{billId}")
//    public ResponseEntity<String> updateBill(@PathVariable Long billId, @RequestBody Bill updatedBill) {
//        for (Bill bill : bills) {
//            if (bill.getId().equals(billId)) {
//                bill.setStatus(updatedBill.getStatus());
//                bill.setPayee(updatedBill.getPayee());
//                // Add other fields as needed
//                return ResponseEntity.ok("Bill updated");
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }

    @PutMapping("/bills/{billId}")
    public ResponseEntity<?> updateBill(@PathVariable Long billId, @RequestBody @Valid Bill billToUpdateWith) {

        return billService.updateABill(billId, billToUpdateWith);

    }


//    @DeleteMapping("/bills/{billId}")
//    public ResponseEntity<String> deleteBill(@PathVariable Long billId) {
//        for (Bill bill : bills) {
//            if (bill.getId().equals(billId)) {
//                bills.remove(bill);
//                return ResponseEntity.ok("Bill deleted successfully");
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }
//}

    @DeleteMapping("/bills/{billId")
    public ResponseEntity<?> deleteBill(@PathVariable Long billId){

        return billService.deleteABill(billId);

    }



}

