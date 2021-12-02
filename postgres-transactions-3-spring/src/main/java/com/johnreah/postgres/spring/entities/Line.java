package com.johnreah.postgres.spring.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lines")
public class Line {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int numUnits;

    @Column(nullable = false)
    private double unitPrice;

    public Line() {
    }

    public Line(Order order, String productCode, String description, int numUnits, double unitPrice) {
        this.setOrder(order);
        this.setProductCode(productCode);
        this.setDescription(description);
        this.setNumUnits(numUnits);
        this.setUnitPrice(unitPrice);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumUnits() {
        return numUnits;
    }

    public void setNumUnits(int numUnits) {
        this.numUnits = numUnits;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}

