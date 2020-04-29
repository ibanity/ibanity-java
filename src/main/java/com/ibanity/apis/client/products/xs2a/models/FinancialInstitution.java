package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FinancialInstitution implements IbanityModel {

    public static final String RESOURCE_TYPE = "financialInstitution";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private String requestId;

    private String bic;
    private String name;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private String country;
    private String status;

    private List<String> authorizationModels;
    private List<String> bulkPaymentsProductTypes;
    private List<String> paymentsProductTypes;
    private List<String> periodicPaymentsProductTypes;

    private Long maxRequestedAccountReferences;
    private Long minRequestedAccountReferences;

    private boolean futureDatedPaymentsAllowed;
    private boolean requiresCredentialStorage;
    private boolean requiresCustomerIpAddress;
    private boolean sandbox;
    private boolean bulkPaymentsEnabled;
    private boolean paymentsEnabled;
    private boolean periodicPaymentsEnabled;


}
