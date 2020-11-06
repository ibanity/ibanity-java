package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Synchronization;
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
    private static final String TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/{accountId}/transactions/{transactionId}";
    private static final String GET_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/transactions/a4a47c21-b606-464f-ae17-f8e3af6772c9";
    private static final String LIST_TRANSACTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/transactions?page%5Blimit%5D=10";

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

    private Transaction createExpected() {
        return Transaction.builder()
                .id(TRANSACTION_ID)
                .valueDate(parse("2019-05-05T00:00:00.000Z"))
                .executionDate(parse("2019-05-08T00:00:00.000Z"))
                .amount(new BigDecimal("6"))
                .counterpartName("Lind, Predovic and Hessel")
                .counterpartReference("BE82230387495521")
                .description("Wire transfer")
                .currency("EUR")
                .remittanceInformationType("unstructured")
                .remittanceInformation("Nisi omnis sint.")
                .build();
    }

    private Synchronization createSynchronization() {
        return Synchronization.builder()
                .id(fromString("1b3e3011-d018-4785-bbba-9aa75ba14d45"))
                .createdAt(parse("2019-09-02T11:28:35.971Z"))
                .updatedAt(parse("2019-09-02T11:28:36.551Z"))
                .resourceId("8804e34f-12b0-4b70-86bf-265f013ca232")
                .resourceType("account")
                .status("success")
                .subtype("accountDetails")
                .build();
    }
}
