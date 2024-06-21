package com.rene.bankingapp.repository;

import com.rene.bankingapp.domain.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    // Find account by customerId
    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId")
    List<Account> findAllByCustomerId(@Param("customerId") Long customerId);


}
