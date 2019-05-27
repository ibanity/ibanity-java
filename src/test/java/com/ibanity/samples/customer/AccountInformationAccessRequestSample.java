package com.ibanity.samples.customer;

import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.services.impl.AccountInformationAccessRequestsServiceImpl;

public class AccountInformationAccessRequestSample {

    private final AccountInformationAccessRequestsService accountInformationAccessRequestsService;

    public AccountInformationAccessRequestSample(IbanityService ibanityService) {
        accountInformationAccessRequestsService = new AccountInformationAccessRequestsServiceImpl(ibanityService.apiUrlProvider(), ibanityService.ibanityHttpClient());
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
