package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.IntradayTransaction;
import com.ibanity.apis.client.products.isabel_connect.models.read.IntradayTransactionsReadQuery;
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
import java.time.LocalDate;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class IntradayTransactionServiceImplTest {
    private static final String ACCOUNT_ID = "93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1";
    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final String INTRADAY_TRANSACTIONS_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/accounts/{accountId}/intraday-transactions";
    private static final String LIST_INTRADAY_TRANSACTIONS_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/accounts/93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1/intraday-transactions?size=10";

    @InjectMocks
    private IntradayTransactionServiceImpl transactionService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.IsabelConnect, "account", "intradayTransactions"))
                .thenReturn(INTRADAY_TRANSACTIONS_ENDPOINT);
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_INTRADAY_TRANSACTIONS_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/isabel-connect/intraday_transactions.json"));

        IsabelCollection<IntradayTransaction> actual = transactionService.list(IntradayTransactionsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountId(ACCOUNT_ID)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected());
    }

    private IntradayTransaction createExpected() {
        return IntradayTransaction.builder()
                .id(fromString("14e2bff5-e365-4bc7-bf48-76b7bcd464e9").toString())
                .amount(new BigDecimal(80000))
                .counterpartName("MYBESTCLIENT")
                .counterpartAccountReference("BE21210123456703")
                .counterpartFinancialInstitutionBic("GEBABEBB")
                .endToEndId("UNIQUE CODE CUSTOMER")
                .executionDate(LocalDate.parse("2018-10-15"))
                .remittanceInformation("123456789002")
                .internalId("UNIQUE CODE BANK")
                .remittanceInformationType("structured-be")
                .status("Booked")
                .valueDate(LocalDate.parse("2018-10-15"))
                .build();
    }
}
