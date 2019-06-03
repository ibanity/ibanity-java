package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.factory.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.AccountInformationAccessLinks;
import com.ibanity.apis.client.products.xs2a.models.links.AccountLinks;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountInformationAccessRequestsServiceImplTest {

    private static final UUID FINANCIAL_INSTITUTION_ID = fromString("4876fdd6-7333-4f9f-b142-ba520ca497b1");
    private static final UUID ACCOUNT_INFORMATION_ACCESS_REQUEST_ID = fromString("cd273ba1-cb2a-464d-b85d-62c9fc4dc8d9");

    private static final String CONSENT_REFERENCE = "ae63691f-64e4-4563-a055-a387d38f07e2";
    private static final String CUSTOMER_ACCESS_TOKEN = "itsme";
    private static final String AIAR_ENDPOINT = "https://api.ibanity.com/xs2a/customer/financial-institutions/{financialInstitutionId}/account-information-access-requests/{accountInformationAccessRequestId}";
    private static final String AIAR_ENDPOINT_FOR_FIND = "https://api.ibanity.com/xs2a/customer/financial-institutions/4876fdd6-7333-4f9f-b142-ba520ca497b1/account-information-access-requests/cd273ba1-cb2a-464d-b85d-62c9fc4dc8d9";
    private static final String AIAR_ENDPOINT_FOR_CREATE = "https://api.ibanity.com/xs2a/customer/financial-institutions/4876fdd6-7333-4f9f-b142-ba520ca497b1/account-information-access-requests";
    private static final String CALLBACK = "https://callback.ibanity.com/sandbox/fi/aiar/i?state=dmF1bHQ6djE6Q3drVzA5VUdCVkhPOUxLc1NjdStpeUZmMmh2L3EremUrdWNUemxwUmlaemtsclgwS095cW15WFJTbGRsN2FFKzRmYjZmMFFkSWxZWUJNQnVCWFJiY2RISUZlNUJQVG1qVjFFTDIvM1BpWXhEVXY4WHA5cEZLdHFrK1FBS2xFYXRYSlhRaXMwNEVndHZNcTJHS1VGK0d2T3h3SkZQalRjUDlJVWRaRzZiMFdHck5nS2tDdTZhck03NEtoalRGR2lCNnVDVjBkdHNIMmNVWEZud1haYUMvN3YyWFFjTzkyZTlyYTRBOVdjd2dSSmtoYnNLS1E0ODgyNWI0WUJMSW8wRW5qNDBRdjd0Z3F2WUY5TElFb1ZpL0VScFNtemx0eER0a0pvSEREUEJlV1phTUVjZUU5RytpLzNieUhuaFdzci84cU02NXJBVjZ3eG1LbEVZR3UzUmRrRTVlL2kyQ0ZxdjRYNEJWNXFZU0JvY2xZZDFxeFZRVUtjVGRPWWk1RG02UjFZZVBvdW4rRDNDbW53eFhRWDNiVlplSFY0UFpPelJHSDJHa1V6UGN2Sjhnei9kN2VFeXBvTk9MaEVQOWNkT0FleUxSUTQ3WXVsN2k1d2NLdHpSYUtFR0JoTTJtdkJoREM2SWY5K1BHK0pKcU9QeGkrRVNCdWovMm5lNWMzWXYxMHMwZGpVV0NPUDRXR3JCakcvbDAwQVhCU2EyaEJmZGFIZ1dlUjZYMVlQOVM1V2pLQzVUOXFxQlRWMklUZWtDUkxzem5PRFZ2ZUtJMUtWald5ajVJUXpTWEkvVE5RTGU4NDhSODFtZTRMSjFaY1pBNkdSaXhSN1p0Y0hrSTNidVlRSWdydmJCNWhMOXVMb2Q5UDFvc0w3dVB6Y205WW50UlVnUDE3SmFVN0RQL2lVYklNeGVjTUxPOEV3YTdvSVdkTG5LUFRyRTk2VkxBaFZMM2lVUXBBY0lJbVk2ekJhMDBRUjg0TUk3Vjh0MXE0aHgzNnBaaWVaRElMVmQ2WXR3cXk5MTZ5YWp0YzNaSWZXYVpMNVByNE1CUjVOTXd0RE1XQTVWNlMySGJPOE5Sd0NZQnB2MytDeFBzVTFuQW54MXN6Uy9sbGpVQTBlU29IbFJDSUJQZUg4Q08wZittMlVYd2hCQXYvKzRudGJWNm12STdCQkJhOGJUcDg0SkE1QjhmNUdWcUx0QWwxdC9hOXNPTEw5ZlNwU3FpbTM2MjhPS3E1UlhoVGRzMytPYWRudkJFSEszSmhpYWxndEVmOTQ1SE1TTHlkK2x2eVFEUHdkTTM3N3ZkQjVZUFBLR2ljaVI0YmFRWjNuZVdZOFBGOHpSQXh1dmplN1l6L0ROZUllc2lQY0xkU1FUMFA5TXg0LzRZRVpKdVE9PQ==";
    public static final String RELATED_ACCOUNTS = "https://api.ibanity.com/xs2a/customer/financial-institutions/4876fdd6-7333-4f9f-b142-ba520ca497b1/account-information-access-requests/df9069eb-0577-484c-b9f0-c4b0ebbba11e/accounts";

    @InjectMocks
    private AccountInformationAccessRequestsServiceImpl accountInformationAccessRequestsService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accountInformationAccessRequests"))
                .thenReturn(AIAR_ENDPOINT);
    }

    @Test
    void create() throws IOException {
        AccountInformationAccessRequestCreationQuery creationQuery =
                AccountInformationAccessRequestCreationQuery.builder()
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .redirectURI("https://fake-tpp.com/access-granted")
                        .consentReference(CONSENT_REFERENCE)
                        .requestedAccountReferences(newArrayList("BE9766801628897565"))
                        .locale("fr")
                        .customerIpAddress("0.0.0.0")
                        .build();

        when(ibanityHttpClient.post(buildUri(AIAR_ENDPOINT_FOR_CREATE), toIbanityModel(creationQuery), creationQuery.getCustomerAccessToken()))
                .thenReturn(loadFile("json/createAccountInformationAccessRequest.json"));

        AccountInformationAccessRequest actual = accountInformationAccessRequestsService.create(creationQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpectedForCreate());
    }

    @Test
    void find() throws Exception {
        AccountInformationAccessRequestCreationQuery creationQuery =
                AccountInformationAccessRequestCreationQuery.builder()
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .accountInformationAccessRequestId(ACCOUNT_INFORMATION_ACCESS_REQUEST_ID)
                        .build();

        when(ibanityHttpClient.get(buildUri(AIAR_ENDPOINT_FOR_FIND), creationQuery.getCustomerAccessToken()))
                .thenReturn(loadFile("json/accountInformationAccessRequest.json"));

        AccountInformationAccessRequest actual = accountInformationAccessRequestsService.find(creationQuery);

        assertThat(actual).isEqualToComparingFieldByField(expectedForFind());
    }

    private RequestApiModel toIbanityModel(AccountInformationAccessRequestCreationQuery creationQuery) {
        AccountInformationAccessRequest accessRequest = AccountInformationAccessRequest.builder()
                .redirectUri(creationQuery.getRedirectURI())
                .consentReference(creationQuery.getConsentReference())
                .requestedAccountReferences(creationQuery.getRequestedAccountReferences())
                .locale(creationQuery.getLocale())
                .customerIpAddress(creationQuery.getCustomerIpAddress())
                .build();
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .attributes(accessRequest)
                                .type(AccountInformationAccessRequest.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private AccountInformationAccessRequest expectedForFind() {
        return AccountInformationAccessRequest.builder()
                .id(ACCOUNT_INFORMATION_ACCESS_REQUEST_ID)
                .requestedAccountReferences(newArrayList("BE9386908098818901"))
                .status("received")
                .build();
    }

    private AccountInformationAccessRequest createExpectedForCreate() {
        return AccountInformationAccessRequest.builder()
                .id(ACCOUNT_INFORMATION_ACCESS_REQUEST_ID)
                .status("received")
                .requestedAccountReferences(newArrayList("BE9386908098818901"))
                .accountInformationAccessLinks(AccountInformationAccessLinks.builder()
                        .redirect(CALLBACK)
                        .build())
                .accountLinks(AccountLinks.builder()
                        .related(RELATED_ACCOUNTS)
                        .build())
                .build();
    }
}