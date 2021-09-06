package com.johnreah.postgres.jdbc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JdbcConnectionTest {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/transactiontest";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";

    @AfterAll
    public static void tearDown() throws SQLException {
        JdbcConnection jdbcConnection = new JdbcConnection();
        jdbcConnection.connect(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        jdbcConnection.executeSql("drop table if exists junk");
    }

    @Test
    public void testConnection() throws SQLException {
        JdbcConnection jdbcConnection = new JdbcConnection();
        jdbcConnection.connect(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        assertEquals("public", jdbcConnection.getConnection().getSchema(), "Should connect to public schema");
    }

    @Test
    public void testCreateTable() throws SQLException {
        JdbcConnection jdbcConnection = new JdbcConnection();
        jdbcConnection.connect(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        assertEquals(0, jdbcConnection.executeSql("drop table if exists junk"), "Delete should return 0");
        assertEquals(0, jdbcConnection.executeSql("create table junk(col1 int)"), "Create should return 0");
        assertEquals(3, jdbcConnection.executeSql("insert into junk(col1) values (111), (222), (333)"), "Insert should return 3");
        assertEquals(3, jdbcConnection.executeSql("insert into junk(col1) values (444), (555), (666)"), "Insert should return 3");

        try (Statement statement = jdbcConnection.getConnection().createStatement()) {
            try (ResultSet rs = statement.executeQuery("select * from junk")) {
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                }
                assertEquals(6, rowCount, "Row count should equal number of rows inserted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstraintViolationException() throws SQLException {
        JdbcConnection jdbcConnection = new JdbcConnection();
        jdbcConnection.connect(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        assertEquals(0, jdbcConnection.executeSql("drop table if exists junk"), "Delete should return 0");
        assertEquals(0, jdbcConnection.executeSql("create table junk(col1 int primary key)"), "Create should return 0");
        assertEquals(1, jdbcConnection.executeSql("insert into junk(col1) values (111)"), "Insert should return 1");
        assertThrows(PSQLException.class, () -> jdbcConnection.executeSql("insert into junk(col1) values (111)"), "Insert should fail");
    }

    @Test
    public void testConstraintViolationWithoutTransaction() throws SQLException {
        JdbcConnection jdbcConnection = new JdbcConnection();
        jdbcConnection.connect(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        assertEquals(0, jdbcConnection.executeSql("drop table if exists junk"), "Delete should return 0");
        assertEquals(0, jdbcConnection.executeSql("create table junk(col1 int primary key)"), "Create should return 0");
        assertEquals(1, jdbcConnection.executeSql("insert into junk(col1) values (111)"), "Insert should return 1");
        assertEquals(1, jdbcConnection.executeSql("insert into junk(col1) values (222)"), "Insert should return 1");
        assertEquals(1, jdbcConnection.executeSql("insert into junk(col1) values (333)"), "Insert should return 1");
        assertThrows(PSQLException.class, () -> jdbcConnection.executeSql("insert into junk(col1) values (333)"), "Insert should fail");
        assertEquals(3, jdbcConnection.countRowsInTable("junk"), "We inserted 3 distinct rows before failure");
    }

    @Test
    public void testConstraintViolationInsideTransaction() throws SQLException {
        JdbcConnection jdbcConnection = new JdbcConnection();
        jdbcConnection.connect(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        assertEquals(0, jdbcConnection.executeSql("drop table if exists junk"), "Delete should return 0");
        assertEquals(0, jdbcConnection.executeSql("create table junk(col1 int primary key)"), "Create should return 0");
        assertEquals(0, jdbcConnection.executeSql("begin transaction"));
        assertEquals(1, jdbcConnection.executeSql("insert into junk(col1) values (111)"), "Insert should return 1");
        assertEquals(1, jdbcConnection.executeSql("insert into junk(col1) values (222)"), "Insert should return 1");
        assertEquals(1, jdbcConnection.executeSql("insert into junk(col1) values (333)"), "Insert should return 1");
        assertThrows(PSQLException.class, () -> jdbcConnection.executeSql("insert into junk(col1) values (333)"), "Insert should fail");
        assertEquals(0, jdbcConnection.executeSql("commit transaction"), "commit");
        assertEquals(0, jdbcConnection.countRowsInTable("junk"), "Transaction should remove rows");
    }

    @Test
    public void testIsTransactionInProgress() throws SQLException {
        JdbcConnection jdbcConnection = new JdbcConnection();
        jdbcConnection.connect(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        assertEquals(false, jdbcConnection.isTransactionInProgress(), "Expecting no transaction");
//        jdbcConnection.getConnection().setAutoCommit(false);
        jdbcConnection.executeSql("begin transaction");
        assertEquals(true, jdbcConnection.isTransactionInProgress(), "Expecting a transaction in progress");
        jdbcConnection.executeSql("commit transaction");
//        jdbcConnection.getConnection().setAutoCommit(true);
        assertEquals(false, jdbcConnection.isTransactionInProgress(), "Expecting no transaction");
    }

}

