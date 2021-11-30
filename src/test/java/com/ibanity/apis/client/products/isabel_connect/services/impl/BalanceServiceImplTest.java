package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Balance;
import com.ibanity.apis.client.products.isabel_connect.models.read.BalancesReadQuery;
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
import java.time.LocalDateTime;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BalanceServiceImplTest {
    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final String ACCOUNT_ID = "93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1";
    private static final String BALANCES_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/accounts/{accountId}/balances";
    private static final String LIST_BALANCES_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/accounts/93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1/balances?size=20";

    @InjectMocks
    private BalanceServiceImpl balanceService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.IsabelConnect, "account", "balances"))
                .thenReturn(BALANCES_ENDPOINT);
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_BALANCES_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/isabel-connect/balances.json"));

        IsabelCollection<Balance> actual = balanceService.list(BalancesReadQuery.builder()
                .accountId(ACCOUNT_ID)
                .accessToken(ACCESS_TOKEN)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPagingOffset()).isEqualTo(0);
        assertThat(actual.getPagingTotal()).isEqualTo(4);
    }

    private Balance createExpected() {
        return Balance.builder()
                .datetime(LocalDateTime.parse("2018-10-12T22:46:32.417"))
                .amount(BigDecimal.valueOf(123456.78))
                .subtype("CLBD")
                .build();
    }
}
