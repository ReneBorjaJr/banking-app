package com.rene.bankingapp.repository;

import com.rene.bankingapp.domain.Deposit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DepositRepository extends CrudRepository<Deposit, Long> {


    @Query(value="SELECT * FROM DEPOSIT WHERE ACCOUNT_ID = ?1")
    public Iterable<Deposit> findAllByAccountId(Long accountId);
}
