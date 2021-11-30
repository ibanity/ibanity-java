package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Account;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountReadQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.net.URI;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountServiceImplTest {
    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final String ACCOUNT_ID = "93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1";
    private static final String ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/accounts/{accountId}";
    private static final String GET_ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/accounts/93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1";
    private static final String LIST_ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/accounts?size=20";

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.IsabelConnect, "accounts")).thenReturn(ACCOUNT_ENDPOINT);
    }

    @Test
    public void find() throws Exception {
        when(ibanityHttpClient.get(new URI(GET_ACCOUNT_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/isabel-connect/account.json"));

        Account actual = accountService.find(AccountReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountId(ACCOUNT_ID)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_ACCOUNT_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/isabel-connect/accounts.json"));

        IsabelCollection<Account> actual = accountService.list(AccountsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPagingOffset()).isEqualTo(10);
        assertThat(actual.getPagingTotal()).isEqualTo(11);
    }

    private Account createExpected() {
        return Account.builder()
                .id(ACCOUNT_ID)
                .country("BE")
                .currency("EUR")
                .description("current account")
                .financialInstitutionBic("KREDBEBB")
                .holderAddress("Avenue du Grisbi, 1200 Bruxelles")
                .holderAddressCountry("BE")
                .holderName("Flouze")
                .reference("BE96153112434405")
                .referenceType("IBAN")
                .build();
    }
}
