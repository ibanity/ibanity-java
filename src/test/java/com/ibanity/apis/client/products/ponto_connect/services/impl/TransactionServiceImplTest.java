package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Transaction;
import com.ibanity.apis.client.products.ponto_connect.models.read.TransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.TransactionsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.time.Instant.parse;
import static java.util.Collections.emptyMap;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceImplTest {

    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final UUID ACCOUNT_ID = fromString("8804e34f-12b0-4b70-86bf-265f013ca232");
    private static final UUID TRANSACTION_ID = fromString("a4a47c21-b606-464f-ae17-f8e3af6772c9");
    private static final UUID SYNCHRONIZATION_ID = UUID.fromString("9d36e759-b606-41dd-8d18-c882bd8db03d");

    private static final String TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/{accountId}/transactions/{transactionId}";
    private static final String GET_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/transactions/a4a47c21-b606-464f-ae17-f8e3af6772c9";
    private static final String LIST_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/transactions?page%5Blimit%5D=10";
    private static final String UPDATED_TRANSACTIONS_ENDPOINT = "https://api.ibanity.com/ponto-connect/synchronizations/{synchronizationId}/updated-transactions";
    private static final String UPDATED_TRANSACTIONS_URI = "https://api.ibanity.com/ponto-connect/synchronizations/9d36e759-b606-41dd-8d18-c882bd8db03d/updated-transactions?page%5Blimit%5D=10";

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "transactions")).thenReturn(TRANSACTION_ENDPOINT);
    }

    @Test
    public void find() throws Exception {
        when(ibanityHttpClient.get(new URI(GET_TRANSACTION_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/transaction.json"));

        Transaction actual = transactionService.find(TransactionReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountId(ACCOUNT_ID)
                .transactionId(TRANSACTION_ID)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_TRANSACTION_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/transactions.json"));

        IbanityCollection<Transaction> actual = transactionService.list(TransactionsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountId(ACCOUNT_ID)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    @Test
    public void listUpdatedForSynchronization() throws Exception {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "synchronization", "updatedTransactions")).thenReturn(UPDATED_TRANSACTIONS_ENDPOINT);
        when(ibanityHttpClient.get(new URI(UPDATED_TRANSACTIONS_URI), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/updated-transactions.json"));

        IbanityCollection<Transaction> actual = transactionService.listUpdatedForSynchronization(TransactionsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .synchronizationId(SYNCHRONIZATION_ID)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    private Transaction createExpected() {
        return Transaction.builder()
                .id(TRANSACTION_ID)
                .valueDate(parse("2020-06-19T00:00:00.000Z"))
                .executionDate(parse("2020-06-19T00:00:00.000Z"))
                .amount(new BigDecimal("2.76"))
                .counterpartName("Rice Ltd")
                .counterpartReference("BE62467153327786")
                .description("Wire transfer")
                .currency("EUR")
                .remittanceInformationType("unstructured")
                .remittanceInformation("Tempora animi qui!")
                .bankTransactionCode("PMNT-IRCT-ESCT")
                .proprietaryBankTransactionCode("63058")
                .endToEndId("a965fa0788c241f3b13df3fbda744a07")
                .purposeCode("CASH")
                .mandateId("486")
                .creditorId("315")
                .additionalInformation("Aut.")
                .digest("2acc87e558b508f7b4bd21829adee7aaace14eedde2b65ec8459201ea7a76050")
                .internalReference("ae7976b0-78b7-492f-99d2-8e85ab5ea006")
                .cardReference("6666")
                .cardReferenceType("MASKEDPAN")
                .fee(new BigDecimal("3.14"))
                .build();
    }
}
