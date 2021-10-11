package com.johnreah.postgres.jdbc_old;

import com.johnreah.postgres.jdbc.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class BookDaoTest {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres_transactions_jdbc";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";

    private ConnectionPool connectionPool = new ConnectionPool(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    private BookDao bookDao = new BookDao(connectionPool);

    @BeforeAll
    public static void beforeAll() throws SQLException {
        Connection connection = new ConnectionPool(JDBC_URL, JDBC_USER, JDBC_PASSWORD).getConnection();
        Statement statement = connection.createStatement();
        statement.execute("drop table if exists books");
        statement.execute("create table books(id serial primary key, title varchar(256) unique)");
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        bookDao.deleteAll();
    }

    @Test
    public void givenUniqueConstraint_whenUniqueEntitiesInserted_thenOk() throws SQLException {
        assertEquals(0, bookDao.count(), "Table should be empty before first insert");
        Book book1 = new Book("Book One");
        bookDao.insert(book1);
        Book book2 = new Book("Book Two");
        bookDao.insert(book2);
        assertEquals(2, bookDao.count(), "Should be 2 rows after 2 successful inserts");
    }

    @Test
    public void givenUniqueConstraint_whenDuplicateEntitiesInserted_thenException() throws SQLException {
        assertEquals(0, bookDao.count(), "Table should be empty before first insert");

        Book book1 = new Book("Book One");
        bookDao.insert(book1);
        assertEquals(1, bookDao.count(), "Should be 1 row after first insert");

        Book book2 = new Book("Book One");
        assertThrows(SQLException.class, () -> bookDao.insert(book2), "Insert of duplicate should fail");
        assertEquals(1, bookDao.count(), "Should still be 1 row after failed insert");
    }

    @Test
    public void givenUniqueConstraintWithoutTransaction_whenDistinctEntitiesInserted_thenOk() throws SQLException {
        assertEquals(0, bookDao.count(), "Table should be empty before first insert");

        Book book1 = new Book("Book One");
        Book book2 = new Book("Book Two");
        bookDao.insertMultipleBooksWithoutTransaction(book1, book2);
        assertEquals(2, bookDao.count(), "Should be 2 rows after successful insert");
    }

    @Test
    public void givenUniqueConstraintWithoutTransaction_whenDupesInserted_thenException() throws SQLException {
        assertEquals(0, bookDao.count(), "Table should be empty before first insert");

        Book book1 = new Book("Book One");
        Book book2 = new Book("Book One");
        assertThrows(SQLException.class, () -> bookDao.insertMultipleBooksWithoutTransaction(book1, book2), "Should fail");
        assertEquals(1, bookDao.count(), "Should be 1 row after partial insert");
    }

    @Test
    public void givenUniqueConstraintAndTransaction_whenDistinctEntitiesInserted_thenOk() throws SQLException {
        assertEquals(0, bookDao.count(), "Table should be empty before first insert");

        Book book1 = new Book("Book One");
        Book book2 = new Book("Book Two");
        bookDao.insertMultipleBooksInTransaction(book1, book2);
        assertEquals(2, bookDao.count(), "Should be 2 rows after successful insert");
    }

    @Test
    public void givenUniqueConstraintAndTransaction_whenDuplicateEntitiesInserted_thenRollback() throws SQLException {
        assertEquals(0, bookDao.count(), "Table should be empty before first insert");

        Book book1 = new Book("Book One");
        Book book2 = new Book("Book One");
        assertThrows(SQLException.class, () -> bookDao.insertMultipleBooksInTransaction(book1, book2), "Should fail");
        assertEquals(0, bookDao.count(), "Should be 0 rows after rollback");
    }

}