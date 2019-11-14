package com.ibanity.samples.customer;

import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.create.AuthorizationPortalCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.create.MetaRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.AccountInformationAccessRequestReadQuery;
import com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.services.IbanityService;

import static com.google.common.collect.Lists.newArrayList;

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
                        .redirectUri(redirectUrl)
                        .consentReference(consentReference)
                        .allowFinancialInstitutionRedirectUri(true)
                        .customerIpAddress("1.1.1.1")
                        .allowedAccountSubtypes(newArrayList("checking", "savings"))
                        .skipIbanityCompletionCallback(false)
                        .metaRequestCreationQuery(MetaRequestCreationQuery.builder()
                                .authorizationPortalCreationQuery(AuthorizationPortalCreationQuery.builder()
                                        .disclaimerContent("thisIsACusomOneContent")
                                        .disclaimerTitle("thisIsACusomOneTitle")
                                        .financialInstitutionPrimaryColor("#000000")
                                        .financialInstitutionSecondaryColor("#000000")
                                        .build())
                                .build())
                        .build();

        return accountInformationAccessRequestsService.create(accountInformationAccessRequestCreationQuery);
    }

    public AccountInformationAccessRequest find(AccountInformationAccessRequest accountInformationAccessRequest, FinancialInstitution financialInstitution, CustomerAccessToken customerAccessToken) {
        return accountInformationAccessRequestsService.find(AccountInformationAccessRequestReadQuery.builder()
                .accountInformationAccessRequestId(accountInformationAccessRequest.getId())
                .financialInstitutionId(financialInstitution.getId())
                .customerAccessToken(customerAccessToken.getToken())
                .build());
    }
}
