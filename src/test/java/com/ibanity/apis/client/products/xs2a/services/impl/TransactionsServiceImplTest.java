package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.Transaction;
import com.ibanity.apis.client.products.xs2a.models.read.TransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.TransactionsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionsServiceImplTest {

    private static final UUID ACCOUNT_ID = UUID.fromString("1c020714-759c-4ee6-ae87-5ce667937e77");
    private static final UUID TRANSACTION_ID = UUID.fromString("eb535c31-f619-4092-9db2-4db84149ddcb");
    private static final UUID FINANCIAL_INSTITUTION_ID = UUID.fromString("99477654-a061-414c-afb4-19e37d13c5a3");

    private static final String CUSTOMER_ACCESS_TOKEN = UUID.randomUUID().toString();
    private static final String TRANSACTION_ENDPOINT = "https://api.ibanity.com/xs2a/customer/financial-institutions/{financialInstitutionId}/accounts/{accountId}/transactions/{transactionId}";
    private static final String TRANSACTION_URI = "https://api.ibanity.com/xs2a/customer/financial-institutions/99477654-a061-414c-afb4-19e37d13c5a3/accounts/1c020714-759c-4ee6-ae87-5ce667937e77/transactions/eb535c31-f619-4092-9db2-4db84149ddcb";
    private static final String TRANSACTIONS_URI = "https://api.ibanity.com/xs2a/customer/financial-institutions/99477654-a061-414c-afb4-19e37d13c5a3/accounts/1c020714-759c-4ee6-ae87-5ce667937e77/transactions?limit=10";
    private static final String FIRST_LINK = "https://api.ibanity.com/xs2a/customer/financial-institutions/99477654-a061-414c-afb4-19e37d13c5a3/accounts/1c020714-759c-4ee6-ae87-5ce667937e77/transactions";

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private TransactionsServiceImpl transactionsService;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "transactions")).thenReturn(TRANSACTION_ENDPOINT);
    }

    @Test
    public void find() throws Exception {
        TransactionReadQuery transactionReadQuery =
                TransactionReadQuery.builder()
                        .transactionId(TRANSACTION_ID)
                        .accountId(ACCOUNT_ID)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();


        when(ibanityHttpClient.get(new URI(TRANSACTION_URI), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadFile("json/transaction.json"));

        Transaction actual = transactionsService.find(transactionReadQuery);

        assertThat(actual).isEqualTo(createExpected());
    }

    @Test
    public void list() throws Exception {
        IbanityCollection<Transaction> expected =
                IbanityCollection.<Transaction>builder()
                        .pageLimit(10)
                        .firstLink(FIRST_LINK)
                        .items(newArrayList(createExpected()))
                        .build();

        TransactionsReadQuery transactionReadQuery =
                TransactionsReadQuery.builder()
                        .accountId(ACCOUNT_ID)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();

        when(ibanityHttpClient.get(new URI(TRANSACTIONS_URI), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadFile("json/transactions.json"));

        IbanityCollection<Transaction> actual = transactionsService.list(transactionReadQuery);

        assertThat(actual).isEqualTo(expected);
    }

    private Transaction createExpected() {
        return Transaction.builder()
                .amount(new Double("6.99"))
                .currency("EUR")
                .remittanceInformationType("unstructured")
                .remittanceInformation("NEW SHOES")
                .description("Small Cotton Shoes")
                .valueDate(Instant.parse("2018-10-22T00:00:00Z"))
                .counterpartName("ABBOTSTONE AGRICULTURAL PROPERTY UNIT TRUST")
                .counterpartReference("BE4779002273920627")
                .executionDate(Instant.parse("2018-10-25T00:00:00.000Z"))
                .selfLink(TRANSACTION_URI)
                .id(TRANSACTION_ID)
                .build();
    }

}
