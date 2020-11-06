package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account implements IbanityModel {

    public static final String RESOURCE_TYPE = "account";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private UUID financialInstitutionId;
    private String requestId;

    private String subtype;
    private String currency;
    private String description;
    private String reference;
    private String referenceType;
    private BigDecimal currentBalance;
    private BigDecimal availableBalance;
    private Instant synchronizedAt;
    private Synchronization latestSynchronization;
    private String internalReference;
    private String product;
    private String holderName;
    private Instant currentBalanceChangedAt;
    private Instant currentBalanceReferenceDate;
    private Instant currentBalanceVariationObservedAt;
    private Instant availableBalanceChangedAt;
    private Instant availableBalanceReferenceDate;
    private Instant availableBalanceVariationObservedAt;
    private Instant authorizedAt;
    private Instant authorizationExpirationExpectedAt;
    private String availability;
}
