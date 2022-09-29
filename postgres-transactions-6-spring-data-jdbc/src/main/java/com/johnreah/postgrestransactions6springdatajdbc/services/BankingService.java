package com.johnreah.postgrestransactions6springdatajdbc.services;

import com.johnreah.postgrestransactions6springdatajdbc.entities.Account;
import com.johnreah.postgrestransactions6springdatajdbc.entities.Customer;
import com.johnreah.postgrestransactions6springdatajdbc.repositories.AccountRepository;
import com.johnreah.postgrestransactions6springdatajdbc.repositories.CustomerRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankingService {

    @Autowired
    @Getter
    private CustomerRepository customerRepository;

    @Autowired
    @Getter
    private AccountRepository accountRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findByIdNotNull();
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findByIdNotNull();
    }

    public Customer findCustomerByReference(String reference) {
        return customerRepository.findByReference(reference);
    }

}
