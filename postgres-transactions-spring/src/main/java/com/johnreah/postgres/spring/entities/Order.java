package com.johnreah.postgres.spring.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime dt;

    @Column(nullable = false)
    private String customer;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<Line> lines = new HashSet<>();

    public Order() {
    }

    public Order(LocalDateTime dt, String customer) {
        this.setDt(dt);
        this.setCustomer(customer);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDt() {
        return dt;
    }

    public void setDt(LocalDateTime dt) {
        this.dt = dt;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public void setLines(Set<Line> lines) {
        this.lines = lines;
    }
}

