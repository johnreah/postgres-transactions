package com.johnreah.postgres.jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OrderRepositoryTest {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres_transactions_jdbc";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";
    private ConnectionPool connectionPool = new ConnectionPool(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

    private Connection connection;
    private OrderRepository orderRepository;
    private LineRepository lineRepository;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        Connection connection = new ConnectionPool(JDBC_URL, JDBC_USER, JDBC_PASSWORD).getConnection();
        Statement statement = connection.createStatement();
        statement.execute("drop table if exists lines");
        statement.execute("drop table if exists orders");
        statement.execute("create table orders(id serial primary key, dt timestamp without time zone, customer varchar(100))");
        statement.execute("create table lines(id serial primary key, orderId int, productCode varchar(100), description varchar(100), numUnits int, unitPrice dec(10,2), constraint fkOrder foreign key(orderId) references orders(id))");
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = connectionPool.getConnection();
        orderRepository = new OrderRepository(connection);
        lineRepository = new LineRepository(connection);
        lineRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @AfterEach
    void afterEach() throws SQLException {
        connection.close();
    }

    @Test
    void givenEmptyTable_whenCreateValidOrderWithoutTransaction_thenOrderCreated() throws SQLException {
        Order order = new Order(LocalDateTime.now(), "Joe Bloggs");
        Order savedOrder = orderRepository.save(order);
        assertTrue(savedOrder.getId() != null && savedOrder.getId() > 0, "ID of created order must be positive integer");

        Line line1 = new Line(order.getId(), "prod001", "Product One", 1, 11.11);
        Line line2 = new Line(order.getId(), "prod002", "Product Two", 2, 22.22);
        lineRepository.save(line1);
        lineRepository.save(line2);

        assertTrue(orderRepository.count() == 1, "Number of orders should be 1");
        assertTrue(lineRepository.count() == 2, "Number of lines should be 2");
    }

    @Test
    void givenEmptyTable_whenBrokenOrderCreatedWithoutTransaction_thenIncompleteOrderCreated() throws SQLException {
        Order order = new Order(LocalDateTime.now(), "Joe Bloggs");
        Order savedOrder = orderRepository.save(order);
        assertTrue(savedOrder.getId() != null && savedOrder.getId() > 0, "ID of created order must be positive integer");
        long orderId = savedOrder.getId();

        Line line1 = new Line(orderId, "prod001", "Product One", 1, 11.11);
        Line line2 = new Line(-999L, "prod002", "Product Two", 2, 22.22);
        lineRepository.save(line1);
        assertThrows(SQLException.class, () -> {
            lineRepository.save(line2);
        });

        assertTrue(orderRepository.count() == 1, "Number of orders should be 1");
        assertTrue(lineRepository.count() == 1, "Number of lines should be 1");
    }

    @Test
    void givenEmptyTable_whenBrokenOrderCreatedWithTransaction_thenOrderIsCleanedUp() throws SQLException {
        try {
            connection.setAutoCommit(false);

            Order order = new Order(LocalDateTime.now(), "Joe Bloggs");
            Order savedOrder = orderRepository.save(order);
            assertTrue(savedOrder.getId() != null && savedOrder.getId() > 0, "ID of created order must be positive integer");
            long orderId = savedOrder.getId();

            Line line1 = new Line(orderId, "prod001", "Product One", 1, 11.11);
            Line line2 = new Line(-999L, "prod002", "Product Two", 2, 22.22);
            lineRepository.save(line1);
            lineRepository.save(line2);

            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
        }

        assertTrue(orderRepository.count() == 0, "Number of orders should be 0");
        assertTrue(lineRepository.count() == 0, "Number of lines should be 0");
    }

}