package com.johnreah.postgres.jdbc;

import java.sql.Connection;

public class LineRepository extends Repository<Line> {

    public LineRepository(Connection connection) {
        super(connection);
    }

    @Override
    public String getSaveSql(Line line) {
        return String.format("insert into lines(orderId, productCode, description, numUnits, unitPrice) values(%d, '%s', '%s', %d, %.02f)",
                line.getOrderId(), line.getProductCode(), line.getDescription(), line.getNumUnits(), line.getUnitPrice());
    }

    @Override
    protected String getCountSql() {
        return "select count(*) from lines";
    }

    @Override
    protected String getDeleteAllSql() {
        return "delete from lines";
    }
}
