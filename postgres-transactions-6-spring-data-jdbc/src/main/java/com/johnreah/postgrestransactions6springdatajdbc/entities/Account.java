package com.johnreah.postgrestransactions6springdatajdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class Account {

    @Id
    private long id;

    private String description;
    private double balance;
    private OffsetDateTime balanceTimestamp;
    private String reference;
    private long accountTypeId;

}
