package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialInstitution implements IbanityModel {

    public static final String RESOURCE_TYPE = "financialInstitution";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;

    private String bic;
    private String name;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private Long maxRequestedAccountReferences;
    private Long minRequestedAccountReferences;
    private boolean futureDatedPaymentsAllowed;
    private boolean requiresCredentialStorage;
    private boolean requiresCustomerIpAddress;
    private boolean sandbox;


}
