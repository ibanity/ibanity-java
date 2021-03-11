package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Balance implements IsabelModel {
    public static final String RESOURCE_TYPE = "balance";

    private String id;
    private String selfLink;
    private String requestId;

    private Instant datetime;
    private Double amount;
    private String subtype;
}
