package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionCreationQuery {

    private String name;
    private String bic;
    private String logoUrl;
    private String country;
    private boolean financialInstitutionCustomerReferenceRequired;
    private String sharedBrandReference;
    private String sharedBrandName;
    private boolean pendingTransactionsAvailable;
    private String timeZone;

    @Builder.Default
    private List<String> authorizationModels = Collections.emptyList();
}
