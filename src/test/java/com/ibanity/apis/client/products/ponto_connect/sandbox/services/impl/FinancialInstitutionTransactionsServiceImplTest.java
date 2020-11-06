package com.ibanity.apis.client.products.ponto_connect.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;
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
import java.time.Instant;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FinancialInstitutionTransactionsServiceImplTest {

    private static final UUID ACCOUNT_ID = fromString("8a532347-3e21-4783-8aec-685c7ba0ac55");
    private static final UUID TRANSACTION_ID = fromString("f87f120d-1dd7-4226-8b0e-fd83c9ed631a");
    private static final UUID FINANCIAL_INSTITUTION_ID = fromString("953934eb-229a-4fd2-8675-07794078cc7d");
    private static final String ACCESS_TOKEN = "o8drk02QucaUes24017lj8VfHSvUukpwdOAZ7N_31e4.id1xyaKQjyzxg0ceqxW7FVZSci5C5RbpJVJJSSXpFdI";
    private static final String TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/sandbox/financial-institutions/{financialInstitutionId}/financial-institution-accounts/{financialInstitutionAccountId}/financial-institution-transactions/{financialInstitutionTransactionId}";
    private static final String GET_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/sandbox/financial-institutions/953934eb-229a-4fd2-8675-07794078cc7d/financial-institution-accounts/8a532347-3e21-4783-8aec-685c7ba0ac55/financial-institution-transactions/f87f120d-1dd7-4226-8b0e-fd83c9ed631a";
    private static final String LIST_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/sandbox/financial-institutions/953934eb-229a-4fd2-8675-07794078cc7d/financial-institution-accounts/8a532347-3e21-4783-8aec-685c7ba0ac55/financial-institution-transactions?page%5Blimit%5D=10";
    private static final String CREATE_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/sandbox/financial-institutions/953934eb-229a-4fd2-8675-07794078cc7d/financial-institution-accounts/8a532347-3e21-4783-8aec-685c7ba0ac55/financial-institution-transactions";

    @InjectMocks
    private FinancialInstitutionTransactionsServiceImpl financialInstitutionTransactionsService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "sandbox", "financialInstitution", "financialInstitutionAccount", "financialInstitutionTransactions")).thenReturn(TRANSACTION_ENDPOINT);
    }

    @Test
    void find() throws IOException {
        when(ibanityHttpClient.get(URI.create(GET_TRANSACTION_ENDPOINT), ACCESS_TOKEN)).thenReturn(loadHttpResponse("json/ponto-connect/financialInstitutionTransaction.json"));
        FinancialInstitutionTransactionReadQuery request = FinancialInstitutionTransactionReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionAccountId(ACCOUNT_ID)
                .financialInstitutionTransactionId(TRANSACTION_ID)
                .build();

        FinancialInstitutionTransaction actual = financialInstitutionTransactionsService.find(request);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    @Test
    void list() throws IOException {
        when(ibanityHttpClient.get(URI.create(LIST_TRANSACTION_ENDPOINT), ACCESS_TOKEN)).thenReturn(loadHttpResponse("json/ponto-connect/financialInstitutionTransactions.json"));
        FinancialInstitutionTransactionsReadQuery request = FinancialInstitutionTransactionsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionAccountId(ACCOUNT_ID)
                .build();

        IbanityCollection<FinancialInstitutionTransaction> actual = financialInstitutionTransactionsService.list(request);

        assertThat(actual.getItems()).containsExactly(createExpected());
    }

    @Test
    void create() throws IOException {
        FinancialInstitutionTransactionCreationQuery request = FinancialInstitutionTransactionCreationQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionAccountId(ACCOUNT_ID)
                .remittanceInformationType("unstructured")
                .remittanceInformation("NEW SHOES")
                .description("Small Cotton Shoes")
                .currency("EUR")
                .counterpartName("Otro Bank")
                .counterpartReference("BE9786154282554")
                .amount(new BigDecimal("84.42"))
                .valueDate(Instant.parse("2020-05-22T00:00:00Z"))
                .executionDate(Instant.parse("2020-05-25T00:00:00Z"))
                .build();
        when(ibanityHttpClient.post(URI.create(CREATE_TRANSACTION_ENDPOINT), createIbanityModel(request), ACCESS_TOKEN)).thenReturn(loadHttpResponse("json/ponto-connect/financialInstitutionTransaction.json"));

        FinancialInstitutionTransaction actual = financialInstitutionTransactionsService.create(request);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    private Object createIbanityModel(FinancialInstitutionTransactionCreationQuery query) {
        FinancialInstitutionTransaction transaction = FinancialInstitutionTransaction.builder()
                .amount(query.getAmount())
                .currency(query.getCurrency())
                .remittanceInformation(query.getRemittanceInformation())
                .remittanceInformationType(query.getRemittanceInformationType())
                .counterpartName(query.getCounterpartName())
                .counterpartReference(query.getCounterpartReference())
                .valueDate(query.getValueDate())
                .executionDate(query.getExecutionDate())
                .description(query.getDescription())
                .build();
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .attributes(transaction)
                                .type(com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionTransaction.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private FinancialInstitutionTransaction createExpected() {
        return FinancialInstitutionTransaction.builder()
                .id(fromString("f87f120d-1dd7-4226-8b0e-fd83c9ed631a"))
                .remittanceInformationType("unstructured")
                .remittanceInformation("NEW SHOES")
                .description("Small Cotton Shoes")
                .currency("EUR")
                .counterpartName("Otro Bank")
                .counterpartReference("BE9786154282554")
                .amount(new BigDecimal("84.42"))
                .valueDate(Instant.parse("2020-05-22T00:00:00Z"))
                .executionDate(Instant.parse("2020-05-25T00:00:00Z"))
                .build();
    }
}
