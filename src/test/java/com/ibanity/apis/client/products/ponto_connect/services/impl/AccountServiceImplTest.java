package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Account;
import com.ibanity.apis.client.products.ponto_connect.models.Synchronization;
import com.ibanity.apis.client.products.ponto_connect.models.delete.AccountDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.AccountReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.AccountsReadQuery;
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
public class AccountServiceImplTest {

    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final UUID ACCOUNT_ID = fromString("8804e34f-12b0-4b70-86bf-265f013ca232");
    private static final UUID FINANCIAL_INSTITUTION_ID = fromString("953934eb-229a-4fd2-8675-07794078cc7d");
    private static final String ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/{accountId}";
    private static final String GET_ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232";
    private static final String LIST_ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts?page%5Blimit%5D=10";

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "accounts")).thenReturn(ACCOUNT_ENDPOINT);
    }

    @Test
    public void find() throws Exception {
        when(ibanityHttpClient.get(new URI(GET_ACCOUNT_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/account.json"));

        Account actual = accountService.find(AccountReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountId(ACCOUNT_ID)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    public void delete() throws Exception {
        when(ibanityHttpClient.delete(new URI(GET_ACCOUNT_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/account_delete.json"));

        Account actual = accountService.delete(AccountDeleteQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountId(ACCOUNT_ID)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(Account.builder()
                .id(ACCOUNT_ID)
                .build());
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_ACCOUNT_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/accounts.json"));

        IbanityCollection<Account> actual = accountService.list(AccountsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    private Account createExpected() {
        return Account.builder()
                .id(ACCOUNT_ID)
                .referenceType("IBAN")
                .reference("BE53586671384412")
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .description("Repellat voluptatem aut.")
                .currentBalance(new BigDecimal("6386.04"))
                .currency("EUR")
                .availableBalance(new BigDecimal("6386.04"))
                .subtype("checking")
                .deprecated(false)
                .synchronizedAt(parse("2019-09-02T11:28:36.551Z"))
                .latestSynchronization(createSynchronization())
                .authorizedAt(parse("2020-07-15T12:01:44.166Z"))
                .authorizationExpirationExpectedAt(parse("2020-10-13T12:01:44.166Z"))
                .availableBalanceReferenceDate(parse("2020-06-20T00:00:00Z"))
                .availableBalanceChangedAt(parse("2020-09-17T12:30:23.405Z"))
                .availableBalanceVariationObservedAt(parse("2020-09-17T12:30:23.467Z"))
                .currentBalanceVariationObservedAt(parse("2020-09-17T12:30:23.467Z"))
                .currentBalanceReferenceDate(parse("2020-06-20T00:00:00Z"))
                .currentBalanceChangedAt(parse("2020-09-17T12:30:23.405Z"))
                .holderName("Malvina Dibbert")
                .product("Current account")
                .internalReference("99f0c729-c51a-4b4c-a4e4-abb8a52c1e12")
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
