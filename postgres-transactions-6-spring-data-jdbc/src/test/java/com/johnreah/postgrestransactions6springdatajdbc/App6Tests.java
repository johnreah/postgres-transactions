package com.johnreah.postgrestransactions6springdatajdbc;

import com.johnreah.postgrestransactions6springdatajdbc.support.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.assertTrue;

class App6Tests extends AbstractIntegrationTest {

//	@Container
//	static PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER = new PostgreSQLContainer("postgres")
//			.withDatabaseName("postgres_transactions_spring_data_jdbc")
//			.withUsername("postgres")
//			.withPassword("postgres");
//
//	@DynamicPropertySource
//	static void registerProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.datasource.url", () -> POSTGRES_SQL_CONTAINER.getJdbcUrl());
//		registry.add("spring.datasource.username", () -> POSTGRES_SQL_CONTAINER.getUsername());
//		registry.add("spring.datasource.password", () -> POSTGRES_SQL_CONTAINER.getPassword());
//	}

	@Test
	void contextLoads() {
	}

	@Test
	void testContainerLoads() {
		assertTrue(true);
	}

}
