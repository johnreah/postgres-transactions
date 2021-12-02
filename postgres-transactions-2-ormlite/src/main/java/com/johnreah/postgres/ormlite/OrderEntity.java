package com.johnreah.postgres.ormlite;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "orders")
public class OrderEntity {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, dataType = DataType.DATE_LONG)
    private Date dt;

    @DatabaseField
    private String customer;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<LineEntity> lines;
    public ForeignCollection<LineEntity> getLines() {
        return this.lines;
    }

    public OrderEntity() {
    }

    public OrderEntity(Date date, String customer) {
        this.setDt(date);
        this.setCustomer(customer);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

}