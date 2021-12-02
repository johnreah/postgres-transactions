package com.johnreah.postgres.jdbc_old;

import com.johnreah.postgres.jdbc.ConnectionPool;

import java.sql.*;

public class BookDao {

    private ConnectionPool connectionPool;

    public BookDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Note the amount of boilerplate code required to catch all possible exceptions compared
     * with the amount of code actually doing useful work. I've chosen to rethrow the exception
     * so we can catch it in the tests. This version inserts a single book with no transaction.
     * The amount of boilerplate can be reduced using try-with-resources (Java 7+).
     */
    public void insert(Book book) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("insert into books(title) values(?)");
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.err.println(e.toString());
            throw e;
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This inserts multiple books with multiple database calls - no transaction.
     * No attempt is made to handle exceptions. If an insert fails an exception will be thrown
     * but earlier inserts will have been committed.
     */
    public void insertMultipleBooksWithoutTransaction(Book... books) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into books(title) values(?)");
        for (Book book: books) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.execute();
        }
    }

    /**
     * This inserts multiple books with multiple database calls within a transaction.
     * If an insert fails, the transaction is rolled back and the exception rethrown,
     * and earlier inserts are rolled back.
     */
    public void insertMultipleBooksInTransaction(Book... books) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into books(title) values(?)");
        boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            for (Book book: books) {
                preparedStatement.setString(1, book.getTitle());
                preparedStatement.execute();
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            System.err.println(e.toString());
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

    public long count() throws SQLException {
        try (Statement statement = connectionPool.getConnection().createStatement()) {
            try (ResultSet rs = statement.executeQuery("select count(*) from books")) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public void deleteAll() throws SQLException {
        connectionPool.getConnection().createStatement().execute("delete from books");
    }

}
