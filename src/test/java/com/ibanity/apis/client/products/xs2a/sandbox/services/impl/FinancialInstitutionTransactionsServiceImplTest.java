package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionTransactionDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static com.ibanity.apis.client.models.IbanityProduct.Xs2a;
import static java.time.Instant.parse;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialInstitutionTransactionsServiceImplTest {

    private static final UUID USER_ID = fromString("21178928-3cbb-4e81-91b6-f8f23238cfe3");
    private static final UUID ACCOUNT_ID = fromString("b865968d-c08a-49e8-a8f9-5883416d73f5");
    private static final UUID FINANCIAL_INSTITUTION_ID = fromString("8465220e-55c9-446d-a717-2196fd934555");
    private static final UUID TRANSACTION_ID = fromString("a7a1bdd6-5148-4165-a1ce-35a99b6ce423");

    private static final String TRANSACTION_ENDPOINT = "https://api.ibanity.com/sandbox/financial-institutions/8465220e-55c9-446d-a717-2196fd934555/financial-institution-users/21178928-3cbb-4e81-91b6-f8f23238cfe3/financial-institution-accounts/b865968d-c08a-49e8-a8f9-5883416d73f5/financial-institution-transactions";
    private static final String API_SCHEMA_ENDPOINT = "https://api.ibanity.com/sandbox/financial-institutions/{financialInstitutionId}/financial-institution-users/{financialInstitutionUserId}/financial-institution-accounts/{financialInstitutionAccountId}/financial-institution-transactions/{financialInstitutionTransactionId}";
    public static final String FIRST_LINK = "/sandbox/financial-institutions/c96751a0-d43f-4f15-861e-d79ee49f152f/financial-institution-users/51e863e8-5213-4c3f-8303-430e83ca49fc/financial-institution-accounts";

    @InjectMocks
    private FinancialInstitutionTransactionsServiceImpl financialInstitutionTransactionsService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(Xs2a, "sandbox", "financialInstitution", "financialInstitutionAccount", "financialInstitutionTransactions"))
                .thenReturn(API_SCHEMA_ENDPOINT);
    }

    @Test
    void find() throws IOException, URISyntaxException {
        FinancialInstitutionTransactionReadQuery query = FinancialInstitutionTransactionReadQuery.builder()
                .financialInstitutionAccountId(ACCOUNT_ID)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionUserId(USER_ID)
                .financialInstitutionTransactionId(TRANSACTION_ID)
                .build();

        when(ibanityHttpClient.get(new URI(TRANSACTION_ENDPOINT + "/" + TRANSACTION_ID)))
                .thenReturn(loadHttpResponse("json/sandbox/find_transaction.json"));

        FinancialInstitutionTransaction actual = financialInstitutionTransactionsService.find(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpectedForFind());
    }

    @Test
    void list() throws URISyntaxException, IOException {
        FinancialInstitutionTransactionsReadQuery query = FinancialInstitutionTransactionsReadQuery.builder()
                .financialInstitutionAccountId(ACCOUNT_ID)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionUserId(USER_ID)
                .build();

        when(ibanityHttpClient.get(new URI(TRANSACTION_ENDPOINT + "?limit=10")))
                .thenReturn(loadHttpResponse("json/sandbox/list_transactions.json"));

        IbanityCollection<FinancialInstitutionTransaction> actual = financialInstitutionTransactionsService.list(query);
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
        when(ibanityHttpClient.delete(new URI(TRANSACTION_ENDPOINT + "/" + TRANSACTION_ID)))
                .thenReturn(loadHttpResponse("json/sandbox/delete_transaction.json"));

        FinancialInstitutionTransactionDeleteQuery query = FinancialInstitutionTransactionDeleteQuery.builder()
                .financialInstitutionAccountId(ACCOUNT_ID)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionUserId(USER_ID)
                .financialInstitutionTransactionId(TRANSACTION_ID)
                .build();

        FinancialInstitutionTransaction actual = financialInstitutionTransactionsService.delete(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(FinancialInstitutionTransaction.builder()
                .id(TRANSACTION_ID)
                .build());
    }

    @Test
    void create() throws URISyntaxException, IOException {
        FinancialInstitutionTransactionCreationQuery query = FinancialInstitutionTransactionCreationQuery.builder()
                .financialInstitutionAccountId(ACCOUNT_ID)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .financialInstitutionUserId(USER_ID)
                .amount(new BigDecimal("6.99"))
                .counterpartName("ALLIED BANK PHILIPPINES (UK) PLC")
                .counterpartReference("BE4325885699707387")
                .currency("EUR")
                .description("Small Cotton Shoes")
                .executionDate(parse("2018-10-25T00:00:00Z"))
                .remittanceInformation("NEW SHOES")
                .remittanceInformationType("unstructured")
                .valueDate(parse("2018-10-22T00:00:00Z"))
                .build();

        when(ibanityHttpClient.post(new URI(TRANSACTION_ENDPOINT), createRequest(query)))
                .thenReturn(loadHttpResponse("json/sandbox/find_transaction.json"));

        FinancialInstitutionTransaction actual = financialInstitutionTransactionsService.create(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpectedForFind());
    }

    private RequestApiModel createRequest(FinancialInstitutionTransactionCreationQuery query) {
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
                                .type(FinancialInstitutionTransaction.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private FinancialInstitutionTransaction createExpectedForFind() {
        return FinancialInstitutionTransaction.builder()
                .id(fromString("6411162a-fe6f-428e-ae52-850291497f6b"))
                .amount(new BigDecimal("6.99"))
                .counterpartName("ALLIED BANK PHILIPPINES (UK) PLC")
                .counterpartReference("BE4325885699707387")
                .createdAt(parse("2019-05-09T09:18:13.024220Z"))
                .currency("EUR")
                .description("Small Cotton Shoes")
                .executionDate(parse("2018-10-25T00:00:00Z"))
                .remittanceInformation("NEW SHOES")
                .remittanceInformationType("unstructured")
                .updatedAt(parse("2019-05-09T09:18:13.024220Z"))
                .valueDate(parse("2018-10-22T00:00:00Z"))
                .financialInstitutionAccountId(fromString("63b35ad7-5af7-47b7-b27c-da4791320d21"))
                .selfLink("https://api.ibanity.com/sandbox/financial-institutions/eb4aca94-136d-4077-bce0-220daeb50a4e/financial-institution-users/d2c3aaf0-e617-44e6-9da8-e28bed46d9d4/financial-institution-accounts/63b35ad7-5af7-47b7-b27c-da4791320d21/financial-institution-transactions/6411162a-fe6f-428e-ae52-850291497f6b")
                .build();
    }
}
