package com.johnreah.postgres.jdbc_old;

import java.sql.*;

/**
 * Add some methods to a JDBC Connection object specifically for Postgres.
 * Default transaction behaviour is autocommit, managed on client side.
 * BEGIN TRANSACTION suspends autocommit (apparently) for the duration of
 * the transaction. Current transaction can be queried with
 *   select * from txid_current()
 *   select txid_status(id)
 * https://www.cybertec-postgresql.com/en/disabling-autocommit-in-postgresql-can-damage-your-health/
 */
public class JdbcConnection {

    Connection connection = null;

    public Connection getConnection() {
        return this.connection;
    }

    public void connect(String url, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int executeSql(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int result = statement.executeUpdate(sql);
            return result;
        }
    }

    public int countRowsInTable(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery("select count(*) from junk")) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    /**
     * A hacky way of testing whether a transaction is in progress.
     */
    public boolean isTransactionInProgress() throws SQLException {
        int first, second;
        try (Statement statement = this.connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery("select txid_current from txid_current()")) {
                rs.next();
                first = rs.getInt("txid_current");
            }
            try (ResultSet rs = statement.executeQuery("select txid_current from txid_current()")) {
                rs.next();
                second = rs.getInt("txid_current");
            }
            //System.out.println(String.format("first=%d, second=%d", first, second));
        }
        return first == second;
    }

}
