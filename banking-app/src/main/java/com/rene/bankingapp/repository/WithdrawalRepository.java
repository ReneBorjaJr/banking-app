package com.rene.bankingapp.repository;

import com.rene.bankingapp.domain.Withdrawal;
import org.springframework.data.repository.CrudRepository;

public interface WithdrawalRepository extends CrudRepository<Withdrawal, Long> {
}
