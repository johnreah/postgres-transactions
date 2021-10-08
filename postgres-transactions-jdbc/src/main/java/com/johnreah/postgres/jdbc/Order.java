package com.johnreah.postgres.jdbc;

import java.time.LocalDateTime;

public class Order extends Entity {

    private LocalDateTime localDateTime;
    private String customer;

    public Order(LocalDateTime localDateTime, String customer) {
        super(null);
        this.localDateTime = localDateTime;
        this.customer = customer;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
