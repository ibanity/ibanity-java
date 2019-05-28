package com.ibanity.apis.client.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements IbanityModel{

    public static final String RESOURCE_TYPE    = "transaction";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private Double amount;
    private String currency;

    private Instant valueDate;
    private Instant executionDate;

    private String description;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String counterpartName;
    private String counterpartReference;
}

