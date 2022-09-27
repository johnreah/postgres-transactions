package com.johnreah.postgrestransactions6springdatajdbc;

import com.johnreah.postgrestransactions6springdatajdbc.support.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class App6Tests extends AbstractIntegrationTest {

	Logger log = LoggerFactory.getLogger("com.johnreah.postgrestransactions6springdatajdbc.App6Tests");

	@Test
	void contextLoads() {
	}

	@Test
	void testContainerRuns() throws InterruptedException {
		assertTrue(postgresContainer.isRunning());
	}

}
