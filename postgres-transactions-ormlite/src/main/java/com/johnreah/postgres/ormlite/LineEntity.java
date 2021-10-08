package com.johnreah.postgres.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "lines")
public class LineEntity {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, columnDefinition = "integer not null references orders(id) on delete cascade")
    private OrderEntity order;

    @DatabaseField(canBeNull = false)
    private String productCode;

    @DatabaseField
    private String description;

    @DatabaseField
    private int numUnits;

    @DatabaseField
    private double unitPrice;

    public LineEntity() {
    }

    public LineEntity(String productCode, String description, int numUnits, double unitPrice) {
        this.setProductCode(productCode);
        this.setDescription(description);
        this.setNumUnits(numUnits);
        this.setUnitPrice(unitPrice);
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
