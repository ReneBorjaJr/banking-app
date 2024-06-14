package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Withdrawal;
import com.rene.bankingapp.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;
    public Iterable<Withdrawal> getAllWithdrawals(){
        return withdrawalRepository.findAll();
    }
    public Optional<Withdrawal> getWithdrawal(Long ID){
        return withdrawalRepository.findById(ID);
    }
    public Withdrawal save(Withdrawal withdrawal){
        return withdrawalRepository.save(withdrawal);
    }
    public void delete(Long ID){
        withdrawalRepository.deleteById(ID);
    }
}
