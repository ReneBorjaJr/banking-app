package com.rene.bankingapp.repository;

import com.rene.bankingapp.domain.Account;
import org.springframework.data.repository.CrudRepository;


public interface AccountRepository extends CrudRepository<Account, Long> {

}
