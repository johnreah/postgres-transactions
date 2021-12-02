package com.johnreah.postgres.jdbc;

public class Line extends Entity {

    private Long orderId;
    private String productCode;
    private String description;
    private int numUnits;
    private double unitPrice;

    public Line(Long orderId, String productCode, String description, int numUnits, double unitPrice) {
        super(null);
        this.setOrderId(orderId);
        this.setProductCode(productCode);
        this.setDescription(description);
        this.setNumUnits(numUnits);
        this.setUnitPrice(unitPrice);
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
