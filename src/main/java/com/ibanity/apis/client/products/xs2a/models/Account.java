package com.ibanity.apis.client.products.xs2a.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibanity.apis.client.models.IbanityModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Account implements IbanityModel {

    public static final String RESOURCE_TYPE = "account";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private UUID financialInstitutionId;

    @JsonProperty("subtype")
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
