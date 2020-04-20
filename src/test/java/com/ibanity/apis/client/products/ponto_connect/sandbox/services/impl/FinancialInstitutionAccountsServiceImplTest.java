package com.ibanity.apis.client.products.ponto_connect.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FinancialInstitutionAccountsServiceImplTest {

    private static final UUID ACCOUNT_ID = UUID.fromString("8a532347-3e21-4783-8aec-685c7ba0ac55");
    private static final UUID FINANCIAL_INSTITUTION_ID = UUID.fromString("953934eb-229a-4fd2-8675-07794078cc7d");
    private static final String ACCESS_TOKEN = "o8drk02QucaUes24017lj8VfHSvUukpwdOAZ7N_31e4.id1xyaKQjyzxg0ceqxW7FVZSci5C5RbpJVJJSSXpFdI";
    private static final String ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/sandbox/financial-institutions/{financialInstitutionId}/financial-institution-accounts/{financialInstitutionAccountId}";
    private static final String GET_ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/sandbox/financial-institutions/" + FINANCIAL_INSTITUTION_ID + "/financial-institution-accounts/" + ACCOUNT_ID;
    private static final String LIST_ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/sandbox/financial-institutions/" + FINANCIAL_INSTITUTION_ID +"/financial-institution-accounts?limit=10";

    @InjectMocks
    private FinancialInstitutionAccountsServiceImpl financialInstitutionAccountsService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "sandbox", "financialInstitution", "financialInstitutionAccounts")).thenReturn(ACCOUNT_ENDPOINT);
    }

    @Test
    void find() throws IOException {
        when(ibanityHttpClient.get(URI.create(GET_ACCOUNT_ENDPOINT), ACCESS_TOKEN)).thenReturn(loadHttpResponse("json/ponto-connect/financialInstitutionAccount.json"));
        FinancialInstitutionAccountReadQuery accountReadQuery = FinancialInstitutionAccountReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .financialInstitutionAccountId(ACCOUNT_ID)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();

        FinancialInstitutionAccount actual = financialInstitutionAccountsService.find(accountReadQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    @Test
    void list() throws IOException {
        when(ibanityHttpClient.get(URI.create(LIST_ACCOUNT_ENDPOINT), ACCESS_TOKEN)).thenReturn(loadHttpResponse("json/ponto-connect/financialInstitutionAccounts.json"));
        FinancialInstitutionAccountsReadQuery accountsReadQuery = FinancialInstitutionAccountsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();

        IbanityCollection<FinancialInstitutionAccount> actual = financialInstitutionAccountsService.list(accountsReadQuery);

        assertThat(actual.getItems()).containsExactly(createExpected());
    }

    private FinancialInstitutionAccount createExpected() {
        return FinancialInstitutionAccount.builder()
                .id(ACCOUNT_ID)
                .subtype("checking")
                .reference("BE17618075636065")
                .referenceType("IBAN")
                .description("Velit expedita voluptas.")
                .currency("EUR")
                .availableBalance(new BigDecimal("567.32"))
                .currentBalance(new BigDecimal("567.32"))
                .build();
    }
}