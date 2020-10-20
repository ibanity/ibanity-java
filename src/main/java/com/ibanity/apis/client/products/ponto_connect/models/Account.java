package com.ibanity.apis.client.products.ponto_connect.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account implements IbanityModel {

    public static final String RESOURCE_TYPE = "account";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private UUID financialInstitutionId;
    private String requestId;

    private String selfLink;
    private String subtype;
    private String currency;
    private String description;
    private String reference;
    private String referenceType;
    private BigDecimal currentBalance;
    private BigDecimal availableBalance;
    private boolean deprecated;

    private Instant synchronizedAt;
    private Synchronization latestSynchronization;
    
    private Instant authorizedAt;
    private Instant authorizationExpirationExpectedAt;
}
