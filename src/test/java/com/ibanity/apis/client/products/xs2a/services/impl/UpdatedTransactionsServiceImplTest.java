package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.Transaction;
import com.ibanity.apis.client.products.xs2a.models.read.UpdatedTransactionsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdatedTransactionsServiceImplTest {

    private static final UUID SYNCHRONIZATION_ID = UUID.fromString("04e98b21-8213-4aec-b373-13eb51f948e9");
    private static final UUID TRANSACTION_ID = UUID.fromString("eb535c31-f619-4092-9db2-4db84149ddcb");

    private static final String CUSTOMER_ACCESS_TOKEN = UUID.randomUUID().toString();
    private static final String UPDATED_TRANSACTIONS_ENDPOINT = "https://api.ibanity.com/xs2a/customer/synchronizations/{synchronizationId}/updated-transactions";
    private static final String UPDATED_TRANSACTIONS_URI = "https://api.ibanity.com/xs2a/customer/synchronizations/04e98b21-8213-4aec-b373-13eb51f948e9/updated-transactions?page%5Blimit%5D=10";
    private static final String FIRST_LINK = "https://api.ibanity.com/xs2a/customer/synchronizations/04e98b21-8213-4aec-b373-13eb51f948e9/updated-transactions";

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private UpdatedTransactionsServiceImpl updatedTransactionsService;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "synchronization", "updatedTransactions")).thenReturn(UPDATED_TRANSACTIONS_ENDPOINT);
    }

    @Test
    public void list() throws Exception {
        IbanityCollection<Transaction> expected =
                IbanityCollection.<Transaction>builder()
                        .pageLimit(10)
                        .firstLink(FIRST_LINK)
                        .items(newArrayList(createExpected()))
                        .build();

        UpdatedTransactionsReadQuery updatedTransactionsReadQuery =
                UpdatedTransactionsReadQuery.builder()
                        .synchronizationId(SYNCHRONIZATION_ID)
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();

            when(ibanityHttpClient.get(new URI(UPDATED_TRANSACTIONS_URI), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/updated-transactions.json"));

        IbanityCollection<Transaction> actual = updatedTransactionsService.list(updatedTransactionsReadQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    private Transaction createExpected() {
        return Transaction.builder()
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
                .id(TRANSACTION_ID)
                .build();
    }

}
