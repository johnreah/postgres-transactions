package com.johnreah.postgrestransactions6springdatajdbc.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public class AbstractIntegrationTest {

	//private static DockerImageName postgresImageName = DockerImageName.parse("utel/postgres-fastlight2").asCompatibleSubstituteFor("postgres");

	@Container
	static protected PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer("postgres")
			.withDatabaseName("postgres_transactions_spring_data_jdbc")
			.withUsername("postgres")
			.withPassword("postgres");

	@DynamicPropertySource
	public static void configure(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresContainer::getUsername);
		registry.add("spring.datasource.password", postgresContainer::getPassword);
	}

}
