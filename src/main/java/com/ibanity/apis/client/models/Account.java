package com.ibanity.apis.client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
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
public class Account implements IbanityModel {

    public static final String RESOURCE_TYPE    = "account";
    public static final String RESOURCE_PATH    = "accounts";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private UUID financialInstitutionId;

    @JsonProperty("subType")
    private String subType;
    private String currency;
    private String description;
    private String reference;
    private String referenceType;
    private Double currentBalance;
    private Double availableBalance;
    private Instant synchronizedAt;
    private Synchronization lastSynchronization;
}
