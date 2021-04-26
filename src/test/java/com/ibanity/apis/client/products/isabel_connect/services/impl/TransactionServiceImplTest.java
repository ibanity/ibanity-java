package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Transaction;
import com.ibanity.apis.client.products.isabel_connect.models.read.TransactionsReadQuery;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceImplTest {
    private static final String ACCOUNT_ID = "93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1";
    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final String TRANSACTIONS_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/accounts/{accountId}/transactions";
    private static final String LIST_TRANSACTIONS_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/accounts/93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1/transactions?size=10";

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.IsabelConnect, "account", "transactions")).thenReturn(TRANSACTIONS_ENDPOINT);
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_TRANSACTIONS_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/isabel-connect/transactions.json"));

        IsabelCollection<Transaction> actual = transactionService.list(TransactionsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountId(ACCOUNT_ID)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected());
    }

    private Transaction createExpected() {
        return Transaction.builder()
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
