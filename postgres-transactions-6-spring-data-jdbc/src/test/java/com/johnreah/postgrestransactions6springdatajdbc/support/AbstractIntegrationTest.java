package com.johnreah.postgrestransactions6springdatajdbc.support;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "spring.datasource.url=jdbc:tc:postgresql:14-alpine://testcontainers/postgres_transactions_spring_data_jdbc"
})
public class AbstractIntegrationTest {

}
