package com.rene.bankingapp.repository;

import com.rene.bankingapp.domain.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {

    // Find account by customerId
    @Query("SELECT a FROM Account a WHERE a.customer.id = :customerId")
    List<Account> findAllByCustomerId(@Param("customerId") Long customerId);

}

