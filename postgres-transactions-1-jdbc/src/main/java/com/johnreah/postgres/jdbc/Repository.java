package com.johnreah.postgres.jdbc;

import java.sql.*;

abstract class Repository<T extends Entity> {

    private Connection connection;

    protected abstract String getSaveSql(T t);
    protected abstract String getCountSql();
    protected abstract String getDeleteAllSql();

    public Repository(Connection connection) {
        this.connection = connection;
    }

    public T save(T t) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql = this.getSaveSql(t);
            long lastInsertedId = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                t.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println(e.toString());
            throw e;
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public long count() throws SQLException {
        Statement statement = null;
        long numRecords = 0;
        try {
            statement = connection.createStatement();
            String sql = this.getCountSql();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                numRecords = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println(e.toString());
            throw e;
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return numRecords;
    }

    public void deleteAll() throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql = this.getDeleteAllSql();
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println(e.toString());
            throw e;
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
