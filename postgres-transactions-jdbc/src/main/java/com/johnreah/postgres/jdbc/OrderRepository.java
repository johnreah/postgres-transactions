package com.johnreah.postgres.jdbc;

import java.sql.Connection;

public class OrderRepository extends Repository<Order> {

    public OrderRepository(Connection connection) {
        super(connection);
    }

    @Override
    public String getSaveSql(Order order) {
        return String.format("insert into orders(dt, customer) values('%s', '%s')", order.getLocalDateTime().toString(), order.getCustomer());
    }

    @Override
    protected String getCountSql() {
        return "select count(*) from orders";
    }

    @Override
    protected String getDeleteAllSql() {
        return "delete from orders";
    }

}
