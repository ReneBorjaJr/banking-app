package com.rene.bankingapp.service;

import com.rene.bankingapp.domain.Customer;
import com.rene.bankingapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CustomerService {
    @Autowired
    private CustomerRepository repository;
    public CustomerRepository getRepository() {
        return repository;
    }

    public void setRepository(CustomerRepository repository) {
        this.repository = repository;
    }


    public Iterable<Customer> findAllCustomers() {
        return repository.findAll();
    }

    public Customer findCustomerById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

public Customer saveCustomer(Customer customer) {
    return repository.save(customer);
}

public Customer updateCustomer(Long id, Customer customerDetails) {
    Customer customer = repository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    customer.setFirstName(customerDetails.getFirstName());
    customer.setLastName(customerDetails.getLastName());
    customer.setAddress(customerDetails.getAddress());
    return repository.save(customer);
}

public void deleteCustomer(Long id) {
    repository.deleteById(id);
}
    }
