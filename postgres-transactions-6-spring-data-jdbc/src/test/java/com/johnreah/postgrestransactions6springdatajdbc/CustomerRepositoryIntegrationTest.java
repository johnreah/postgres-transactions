package com.johnreah.postgrestransactions6springdatajdbc;

import com.johnreah.postgrestransactions6springdatajdbc.entities.Customer;
import com.johnreah.postgrestransactions6springdatajdbc.repositories.CustomerRepository;
import com.johnreah.postgrestransactions6springdatajdbc.support.AbstractIntegrationTest;
import com.johnreah.postgrestransactions6springdatajdbc.support.DatabaseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private DatabaseUtils databaseUtils;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void beforeEach() {
        databaseUtils.deleteEverything();
    }

    @Test
    public void testPersistence() {
        Customer customer = Customer.builder()
                .firstName("Test")
                .lastName("Customer")
                .reference("TEST_CUSTOMER")
                .build();
        customer = customerRepository.save(customer);
        assertTrue(customer.getId() != 0, "Customer ID after save should be non-zero");
    }

    @Test
    public void testCustomerAccountLinking() {

    }

}
