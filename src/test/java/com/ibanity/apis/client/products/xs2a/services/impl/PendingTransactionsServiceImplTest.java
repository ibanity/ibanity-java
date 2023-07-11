package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.ErrorMeta;
import com.ibanity.apis.client.models.FinancialInstitutionResponse;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityError;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.PendingTransaction;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.read.PendingTransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.PendingTransactionsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PendingTransactionsServiceImplTest {

    private static final UUID ACCOUNT_ID = UUID.fromString("1c020714-759c-4ee6-ae87-5ce667937e77");
    private static final UUID PENDING_TRANSACTION_ID = UUID.fromString("eb535c31-f619-4092-9db2-4db84149ddcb");
    private static final UUID FINANCIAL_INSTITUTION_ID = UUID.fromString("99477654-a061-414c-afb4-19e37d13c5a3");
    private static final UUID SYNCHRONIZATION_ID = UUID.fromString("9d36e759-b606-41dd-8d18-c882bd8db03d");

    private static final String CUSTOMER_ACCESS_TOKEN = UUID.randomUUID().toString();
    private static final String PENDING_TRANSACTION_ENDPOINT = "https://api.ibanity.com/xs2a/customer/financial-institutions/{financialInstitutionId}/accounts/{accountId}/pending-transactions/{pendingTransactionId}";
    private static final String PENDING_TRANSACTION_URI = "https://api.ibanity.com/xs2a/customer/financial-institutions/99477654-a061-414c-afb4-19e37d13c5a3/accounts/1c020714-759c-4ee6-ae87-5ce667937e77/pending-transactions/eb535c31-f619-4092-9db2-4db84149ddcb";
    private static final String PENDING_TRANSACTIONS_URI = "https://api.ibanity.com/xs2a/customer/financial-institutions/99477654-a061-414c-afb4-19e37d13c5a3/accounts/1c020714-759c-4ee6-ae87-5ce667937e77/pending-transactions?page%5Blimit%5D=10";
    private static final String FIRST_LINK = "https://api.ibanity.com/xs2a/customer/financial-institutions/99477654-a061-414c-afb4-19e37d13c5a3/accounts/1c020714-759c-4ee6-ae87-5ce667937e77/pending-transactions";
    private static final String UPDATED_PENDING_TRANSACTIONS_ENDPOINT = "https://api.ibanity.com/xs2a/customer/synchronizations/{synchronizationId}/updated-pending-transactions";
    private static final String UPDATED_PENDING_TRANSACTIONS_URI = "https://api.ibanity.com/xs2a/customer/synchronizations/9d36e759-b606-41dd-8d18-c882bd8db03d/updated-pending-transactions?page%5Blimit%5D=10";

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private PendingTransactionsServiceImpl pendingTransactionsService;

    @Test
    public void find() throws Exception {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "account", "pendingTransactions")).thenReturn(PENDING_TRANSACTION_ENDPOINT);

        PendingTransactionReadQuery pendingTransactionReadQuery =
                PendingTransactionReadQuery.builder()
                        .pendingTransactionId(PENDING_TRANSACTION_ID)
                        .accountId(ACCOUNT_ID)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();


        when(ibanityHttpClient.get(new URI(PENDING_TRANSACTION_URI), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/pendingTransaction.json"));

        PendingTransaction actual = pendingTransactionsService.find(pendingTransactionReadQuery);

        assertThat(actual).isEqualTo(createExpected());
    }

    @Test
    public void listForAccount() throws Exception {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "account", "pendingTransactions")).thenReturn(PENDING_TRANSACTION_ENDPOINT);

        IbanityCollection<PendingTransaction> expected =
                IbanityCollection.<PendingTransaction>builder()
                        .pageLimit(10)
                        .firstLink(FIRST_LINK)
                        .items(Collections.singletonList(createExpected()))
                        .latestSynchronization(Synchronization.builder()
                                .resourceId("1c020714-759c-4ee6-ae87-5ce667937e77")
                                .resourceType("account")
                                .subtype("accountPendingTransactions")
                                .status("success")
                                .errors(Collections.singletonList(IbanityError.builder()
                                        .code("authorizationInvalid")
                                        .detail("The authorization is invalid, you should ask the customer to reauthorize the account")
                                        .meta(ErrorMeta.builder()
                                                .financialInstitutionResponse(FinancialInstitutionResponse.builder()
                                                        .body("{\"tppMessages\":[{\"category\":\"ERROR\",\"code\":\"NOT_FOUND\",\"text\":\"3.2 - Not Found\"}]}")
                                                        .requestId("354fwfwef4w684")
                                                        .statusCode(500)
                                                        .timestamp(Instant.parse("2019-05-09T09:18:00.000Z"))
                                                        .requestUri("http://google.com")
                                                        .build())
                                                .build())
                                        .build()))
                                .id(UUID.fromString("b49dff7e-ad7b-4992-9753-8f2a004cf343"))
                                .createdAt(Instant.parse("2019-04-25T09:06:07.171Z"))
                                .updatedAt(Instant.parse("2019-04-25T09:06:08.044Z"))
                                .build())
                        .build();

        PendingTransactionsReadQuery pendingTransactionReadQuery =
                PendingTransactionsReadQuery.builder()
                        .accountId(ACCOUNT_ID)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();

        when(ibanityHttpClient.get(new URI(PENDING_TRANSACTIONS_URI), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/pendingTransactions.json"));

        IbanityCollection<PendingTransaction> actual = pendingTransactionsService.list(pendingTransactionReadQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void listUpdatedForSynchronization() throws Exception {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "synchronization", "updatedPendingTransactions")).thenReturn(UPDATED_PENDING_TRANSACTIONS_ENDPOINT);

        IbanityCollection<PendingTransaction> expected =
                IbanityCollection.<PendingTransaction>builder()
                        .pageLimit(10)
                        .firstLink("https://api.ibanity.com/xs2a/customer/synchronizations/9d36e759-b606-41dd-8d18-c882bd8db03d/updated-pending-transactions")
                        .items(Collections.singletonList(createExpected()))
                        .build();

        PendingTransactionsReadQuery pendingTransactionReadQuery =
                PendingTransactionsReadQuery.builder()
                        .synchronizationId(SYNCHRONIZATION_ID)
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();

        when(ibanityHttpClient.get(new URI(UPDATED_PENDING_TRANSACTIONS_URI), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/updatedPendingTransactions.json"));

        IbanityCollection<PendingTransaction> actual = pendingTransactionsService.listUpdatedForSynchronization(pendingTransactionReadQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    private PendingTransaction createExpected() {
        return PendingTransaction.builder()
                .amount(new BigDecimal("6.99"))
                .currency("EUR")
                .remittanceInformationType("unstructured")
                .remittanceInformation("NEW SHOES")
                .description("Small Cotton Shoes")
                .valueDate(Instant.parse("2018-10-22T00:00:00Z"))
                .counterpartName("ABBOTSTONE AGRICULTURAL PROPERTY UNIT TRUST")
                .counterpartReference("BE4779002273920627")
                .executionDate(Instant.parse("2018-10-25T00:00:00.000Z"))
                .internalReference("transaction_12345")
                .bankTransactionCode("PMNT-IRCT-ESCT")
                .proprietaryBankTransactionCode("prop123")
                .endToEndId("61dd468606594217af9965ad3928280d")
                .purposeCode("CASH")
                .mandateId("12345678")
                .creditorId("98765")
                .additionalInformation("addional")
                .selfLink(PENDING_TRANSACTION_URI)
                .id(PENDING_TRANSACTION_ID)
                .cardReference("6666")
                .cardReferenceType("MASKEDPAN")
                .fee(new BigDecimal("3.14"))
                .build();
    }

}
