package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.PendingTransaction;
import com.ibanity.apis.client.products.ponto_connect.models.read.PendingTransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.PendingTransactionsReadQuery;
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
public class PendingTransactionServiceImplTest {

    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final UUID ACCOUNT_ID = fromString("8804e34f-12b0-4b70-86bf-265f013ca232");
    private static final UUID PENDING_TRANSACTION_ID = fromString("a4a47c21-b606-464f-ae17-f8e3af6772c9");
    private static final UUID SYNCHRONIZATION_ID = UUID.fromString("9d36e759-b606-41dd-8d18-c882bd8db03d");

    private static final String PENDING_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/{accountId}/pending-transactions/{pendingTransactionId}";
    private static final String GET_PENDING_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/pending-transactions/a4a47c21-b606-464f-ae17-f8e3af6772c9";
    private static final String LIST_PENDING_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/pending-transactions?page%5Blimit%5D=10";
    private static final String UPDATED_PENDING_TRANSACTIONS_ENDPOINT = "https://api.ibanity.com/ponto-connect/synchronizations/{synchronizationId}/updated-pending-transactions";
    private static final String UPDATED_PENDING_TRANSACTIONS_URI = "https://api.ibanity.com/ponto-connect/synchronizations/9d36e759-b606-41dd-8d18-c882bd8db03d/updated-pending-transactions?page%5Blimit%5D=10";

    @InjectMocks
    private PendingTransactionServiceImpl pendingTransactionService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "pendingTransactions")).thenReturn(PENDING_TRANSACTION_ENDPOINT);
    }

    @Test
    public void find() throws Exception {
        when(ibanityHttpClient.get(new URI(GET_PENDING_TRANSACTION_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/pendingTransaction.json"));

        PendingTransaction actual = pendingTransactionService.find(PendingTransactionReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountId(ACCOUNT_ID)
                .pendingTransactionId(PENDING_TRANSACTION_ID)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_PENDING_TRANSACTION_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/pendingTransactions.json"));

        IbanityCollection<PendingTransaction> actual = pendingTransactionService.list(PendingTransactionsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountId(ACCOUNT_ID)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    @Test
    public void listUpdatedForSynchronization() throws Exception {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "synchronization", "updatedPendingTransactions")).thenReturn(UPDATED_PENDING_TRANSACTIONS_ENDPOINT);
        when(ibanityHttpClient.get(new URI(UPDATED_PENDING_TRANSACTIONS_URI), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/updatedPendingTransactions.json"));

        IbanityCollection<PendingTransaction> actual = pendingTransactionService.listUpdatedForSynchronization(PendingTransactionsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .synchronizationId(SYNCHRONIZATION_ID)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    private PendingTransaction createExpected() {
        return PendingTransaction.builder()
                .id(PENDING_TRANSACTION_ID)
                .valueDate(parse("2020-06-19T00:00:00.000Z"))
                .executionDate(parse("2020-06-20T00:00:00.000Z"))
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
