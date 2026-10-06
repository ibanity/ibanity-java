package com.ibanity.apis.client.products.ponto_connect.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

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
    private boolean deprecated;
    private String timeZone;
    private boolean bulkPaymentsEnabled;
    private String bulkPaymentsProductTypes;
    private boolean futureDatedPaymentsAllowed;
    private String maintenanceFrom;
    private String maintenanceTo;
    private String maintenanceType;
    private boolean paymentsEnabled;
    private String paymentsProductTypes;
    private boolean periodicPaymentsEnabled;
    private String periodicPaymentsProductTypes;
    private String sharedBrandName;
    private String sharedBrandReference;
    private boolean pendingTransactionsAvailable;
    private Integer bulkPaymentInstructionsLimit;
    private Integer expectedAuthorizationLifetime;
    private boolean paymentDebtorAccountReferenceRequired;
    private boolean bulkPaymentDebtorAccountReferenceRequired;
}

