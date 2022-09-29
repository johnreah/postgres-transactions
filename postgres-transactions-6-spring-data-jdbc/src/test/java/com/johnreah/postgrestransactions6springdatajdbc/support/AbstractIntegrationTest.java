package com.johnreah.postgrestransactions6springdatajdbc.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public class AbstractIntegrationTest {

	private static DockerImageName postgresImageName = DockerImageName.parse("fastlight/postgres-transaction-test").asCompatibleSubstituteFor("postgres");

	@Container
	static protected PostgreSQLContainer postgresContainer = new PostgreSQLContainer<>(postgresImageName)
			//.withDatabaseName("postgres_transactions_spring_data_jdbc")
			.withUsername("postgres")
			.withPassword("postgres")
			.waitingFor(new LogMessageWaitStrategy()
					.withRegEx(".*database system is ready to accept connections.*\\s")
					.withTimes(1)
					.withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS)));

	@DynamicPropertySource
	public static void configure(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresContainer::getUsername);
		registry.add("spring.datasource.password", postgresContainer::getPassword);
	}

}
