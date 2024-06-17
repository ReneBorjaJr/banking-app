package com.rene.bankingapp.repository;

import com.rene.bankingapp.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    }

