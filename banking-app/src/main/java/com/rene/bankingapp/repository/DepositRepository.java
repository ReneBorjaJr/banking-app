package com.rene.bankingapp.repository;

import com.rene.bankingapp.domain.Deposit;
import org.springframework.data.repository.CrudRepository;

public interface DepositRepository extends CrudRepository<Deposit, Long> {

}
