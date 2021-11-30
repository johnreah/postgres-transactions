package com.johnreah.postgres.cdi;

import javax.persistence.*;

@Entity
public class SampleEntity {
    @Id
    @GeneratedValue
    private Long id;

    private int intValue;

    @Column(unique = true)
    private String stringValue;

    public SampleEntity() {}

    public SampleEntity(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Long getId() {
        return id;
    }
}
