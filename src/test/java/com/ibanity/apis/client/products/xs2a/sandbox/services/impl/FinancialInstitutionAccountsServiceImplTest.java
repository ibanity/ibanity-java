package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionAccountCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionAccountDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static com.ibanity.apis.client.models.IbanityProduct.Xs2a;
import static java.time.Instant.parse;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialInstitutionAccountsServiceImplTest {

    private static final UUID USER_ID = fromString("51e863e8-5213-4c3f-8303-430e83ca49fc");
    private static final UUID ACCOUNT_ID = fromString("1bcb2b9e-176d-42fb-9bbb-cc9c7744d573");
    private static final UUID FINANCIAL_INSTITUTION_ID = fromString("c96751a0-d43f-4f15-861e-d79ee49f152f");
    private static final String API_SCHEMA_ENDPOINT = "https://api.ibanity.com/sandbox/financial-institutions/{financialInstitutionId}/financial-institution-users/{financialInstitutionUserId}/financial-institution-accounts/{financialInstitutionAccountId}";
    private static final String ACCOUNT_ENPOINT = "https://api.ibanity.com/sandbox/financial-institutions/c96751a0-d43f-4f15-861e-d79ee49f152f/financial-institution-users/51e863e8-5213-4c3f-8303-430e83ca49fc/financial-institution-accounts";
    private static final String FIRST_LINK = "/sandbox/financial-institutions/c96751a0-d43f-4f15-861e-d79ee49f152f/financial-institution-users/51e863e8-5213-4c3f-8303-430e83ca49fc/financial-institution-accounts";

    @InjectMocks
    private FinancialInstitutionAccountsServiceImpl financialInstitutionAccountsService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(Xs2a, "sandbox", "financialInstitution", "financialInstitutionAccounts"))
                .thenReturn(API_SCHEMA_ENDPOINT);
    }

    @Test
    void find() throws IOException, URISyntaxException {
        FinancialInstitutionAccountReadQuery query = FinancialInstitutionAccountReadQuery.builder()
                .financialInstitutionAccountId(ACCOUNT_ID)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionUserId(USER_ID)
                .build();

        when(ibanityHttpClient.get(new URI(ACCOUNT_ENPOINT + "/" + ACCOUNT_ID)))
                .thenReturn(loadFile("json/sandbox/find_account.json"));

        FinancialInstitutionAccount actual = financialInstitutionAccountsService.find(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpectedForFind());
    }

    @Test
    void list() throws URISyntaxException, IOException {
        FinancialInstitutionAccountsReadQuery query = FinancialInstitutionAccountsReadQuery.builder()
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionUserId(USER_ID)
                .build();

        when(ibanityHttpClient.get(new URI(ACCOUNT_ENPOINT + "?limit=10")))
                .thenReturn(loadFile("json/sandbox/list_accounts.json"));

        IbanityCollection<FinancialInstitutionAccount> actual = financialInstitutionAccountsService.list(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(
                IbanityCollection.builder()
                        .pageLimit(10)
                        .firstLink(FIRST_LINK)
                        .items(newArrayList(createExpectedForFind()))
                        .build()
        );
    }

    @Test
    void delete() throws Exception {
        when(ibanityHttpClient.delete(new URI(ACCOUNT_ENPOINT + "/" + ACCOUNT_ID)))
                .thenReturn(loadFile("json/sandbox/delete_account.json"));

        FinancialInstitutionAccountDeleteQuery query = FinancialInstitutionAccountDeleteQuery.builder()
                .financialInstitutionAccountId(ACCOUNT_ID)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionUserId(USER_ID)
                .build();

        FinancialInstitutionAccount actual = financialInstitutionAccountsService.delete(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(FinancialInstitutionAccount.builder()
                .id(ACCOUNT_ID)
                .build());
    }

    @Test
    void create() throws URISyntaxException, IOException {
        FinancialInstitutionAccountCreationQuery query = FinancialInstitutionAccountCreationQuery.builder()
                .availableBalance(0.0)
                .currency("EUR")
                .currentBalance(0.0)
                .description("Checking account")
                .reference("BE6621814485468913")
                .referenceType("IBAN")
                .subType("checking")
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionUserId(USER_ID)
                .build();

        when(ibanityHttpClient.post(new URI(ACCOUNT_ENPOINT), createRequest(query)))
                .thenReturn(loadFile("json/sandbox/find_account.json"));

        FinancialInstitutionAccount actual = financialInstitutionAccountsService.create(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpectedForFind());
    }

    private RequestApiModel createRequest(FinancialInstitutionAccountCreationQuery query) {
        FinancialInstitutionAccount account = FinancialInstitutionAccount.builder()
                .availableBalance(query.getAvailableBalance())
                .currency(query.getCurrency())
                .currentBalance(query.getCurrentBalance())
                .description(query.getDescription())
                .reference(query.getReference())
                .referenceType(query.getReferenceType())
                .subType(query.getSubType())
                .build();
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .attributes(account)
                                .type(FinancialInstitutionAccount.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private FinancialInstitutionAccount createExpectedForFind() {
        return FinancialInstitutionAccount.builder()
                .id(ACCOUNT_ID)
                .availableBalance(0.0)
                .createdAt(parse("2019-05-09T09:18:06.271096Z"))
                .currency("EUR")
                .currentBalance(0.0)
                .description("Checking account")
                .reference("BE6621814485468913")
                .referenceType("IBAN")
                .subType("checking")
                .updatedAt(parse("2019-05-09T09:18:06.271096Z"))
                .selfLink("https://api.ibanity.com/sandbox/financial-institutions/c96751a0-d43f-4f15-861e-d79ee49f152f/financial-institution-users/51e863e8-5213-4c3f-8303-430e83ca49fc/financial-institution-accounts/1bcb2b9e-176d-42fb-9bbb-cc9c7744d573")
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionUserId(USER_ID)
                .build();
    }
}