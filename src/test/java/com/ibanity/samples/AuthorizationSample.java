package com.ibanity.samples;

import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.Authorization;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.create.AuthorizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.create.AuthorizationPortalCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.create.MetaRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.products.xs2a.services.AuthorizationsService;
import com.ibanity.apis.client.services.IbanityService;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class AuthorizationSample {

    private final AuthorizationsService authorizationsService;
    private final AccountInformationAccessRequestsService accountInformationAccessRequestsService;

    public AuthorizationSample(IbanityService ibanityService) {
        accountInformationAccessRequestsService = ibanityService.xs2aService().accountInformationAccessRequestsService();
        authorizationsService = ibanityService.xs2aService().authorizationsService();
    }


    public AccountInformationAccessRequest createAiar(
            FinancialInstitution financialInstitution,
            CustomerAccessToken customerAccessToken,
            String consentReference, String redirectUrl) {
        AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery =
                AccountInformationAccessRequestCreationQuery.builder()
                        .customerAccessToken(customerAccessToken.getToken())
                        .financialInstitutionId(financialInstitution.getId())
                        .redirectUri(redirectUrl)
                        .consentReference(consentReference)
                        .allowFinancialInstitutionRedirectUri(true)
                        .customerIpAddress("1.1.1.1")
                        .allowedAccountSubtypes(newArrayList("checking", "savings", "securities"))
                        .skipIbanityCompletionCallback(true)
                        .state("myCustomState")
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

    public Authorization create(AccountInformationAccessRequest accountInformationAccessRequest, FinancialInstitution financialInstitution, CustomerAccessToken customerAccessToken, String code) {
        Map<String, String> authorizationCode = newHashMap();
        authorizationCode.put("code", code);
        AuthorizationCreationQuery authorizationCreationQuery = AuthorizationCreationQuery.builder()
                .accountInformationAccessRequestId(accountInformationAccessRequest.getId())
                .customerAccessToken(customerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .queryParameters(authorizationCode)
                .build();
        return authorizationsService.create(authorizationCreationQuery);
    }
}
