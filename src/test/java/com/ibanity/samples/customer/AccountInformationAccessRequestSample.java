package com.ibanity.samples.customer;

import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.services.IbanityService;

public class AccountInformationAccessRequestSample {

    private final AccountInformationAccessRequestsService accountInformationAccessRequestsService;

    public AccountInformationAccessRequestSample(IbanityService ibanityService) {
        accountInformationAccessRequestsService = ibanityService.xs2aService().accountInformationAccessRequestsService();
    }

    public AccountInformationAccessRequest create(FinancialInstitution financialInstitution, CustomerAccessToken customerAccessToken,
                                                  String consentReference, String redirectUrl) {
        AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery =
                AccountInformationAccessRequestCreationQuery.builder()
                        .customerAccessToken(customerAccessToken.getToken())
                        .financialInstitutionId(financialInstitution.getId())
                        .redirectURI(redirectUrl)
                        .consentReference(consentReference)
                        .build();

        return accountInformationAccessRequestsService.create(accountInformationAccessRequestCreationQuery);
    }
}
