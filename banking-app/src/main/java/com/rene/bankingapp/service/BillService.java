package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Bill;
import com.rene.bankingapp.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId).orElse(null);
    }

    public List<Bill> getBillsByAccountId(Long accountId) {
        return billRepository.findByAccountId(accountId);
    }

    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Bill updateBill(Long billId, Bill updatedBill) {
        Bill existingBill = billRepository.findById(billId).orElse(null);
        if (existingBill != null) {
            // Update fields
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
        }
        return null;
    }

    public boolean deleteBill(Long billId) {
        Bill existingBill = billRepository.findById(billId).orElse(null);
        if (existingBill != null) {
            billRepository.delete(existingBill);
            return true;
        }
        return false;
    }
}


