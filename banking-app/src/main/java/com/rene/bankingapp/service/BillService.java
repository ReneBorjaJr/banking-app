package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Bill;
import com.rene.bankingapp.exceptions.ResourceNotFoundException;
import com.rene.bankingapp.repository.BillRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(Long billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isPresent()) {
            return optionalBill.get();
        } else {
            throw new ResourceNotFoundException("Error fetching bill with id: " + billId);
        }
    }

    public List<Bill> getBillsByAccountId(Long accountId) {
        return billRepository.findByAccountId(accountId);
    }

    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Bill updateBill(Long billId, Bill updatedBill) {
        Optional<Bill> optionalExistingBill = billRepository.findById(billId);
        if (optionalExistingBill.isPresent()) {
            Bill existingBill = optionalExistingBill.get();
            existingBill.setStatus(updatedBill.getStatus());
            existingBill.setPayee(updatedBill.getPayee());
            existingBill.setNickname(updatedBill.getNickname());
            existingBill.setCreationDate(updatedBill.getCreationDate());
            existingBill.setPaymentDate(updatedBill.getPaymentDate());
            existingBill.setRecurringDate(updatedBill.getRecurringDate());
            existingBill.setUpcomingPaymentDate(updatedBill.getUpcomingPaymentDate());
            existingBill.setPaymentAmount(updatedBill.getPaymentAmount());
//            existingBill.setAccountId(updatedBill.getAccountId());

            return billRepository.save(existingBill);
        } else {
            throw new ResourceNotFoundException("Error creating bill: Account not found " + billId);
        }
    }

    public boolean deleteBill(Long billId) {
        Optional<Bill> optionalExistingBill = billRepository.findById(billId);
        if (optionalExistingBill.isPresent()) {
            Bill existingBill = optionalExistingBill.get();
            billRepository.delete(existingBill);
            return true;
        } else {
            throw new ResourceNotFoundException("Bill ID does not exist " + billId);
        }
    }
}



