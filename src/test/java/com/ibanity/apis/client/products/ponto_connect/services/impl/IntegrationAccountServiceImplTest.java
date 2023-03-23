package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.ponto_connect.models.IntegrationAccount;
import com.ibanity.apis.client.products.ponto_connect.models.read.IntegrationAccountsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.net.URI;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.time.Instant.parse;
import static java.util.Collections.emptyMap;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IntegrationAccountServiceImplTest {

    private static final String INTEGRATION_ACCOUNT_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/integration-accounts";
    private static final String LIST_INTEGRATION_ACCOUNT = "https://api.ibanity.localhost/ponto-connect/integration-accounts?page%5Blimit%5D=2";
    private static final String CLIENT_TOKEN = "myClientToken";

    @InjectMocks
    private IntegrationAccountServiceImpl integrationAccountService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "integrationAccounts")).thenReturn(INTEGRATION_ACCOUNT_ENDPOINT);
    }

    @Test
    void list() throws IOException {
        when(ibanityHttpClient.get(URI.create(LIST_INTEGRATION_ACCOUNT), emptyMap(), CLIENT_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/integration_accounts.json"));

        IbanityCollection<IntegrationAccount> integrationAccounts = integrationAccountService.list(
                IntegrationAccountsReadQuery.builder()
                        .accessToken(CLIENT_TOKEN)
                        .pagingSpec(IbanityPagingSpec.builder().limit(2).build())
                        .build());

        assertThat(integrationAccounts.getItems()).hasSize(1);
        assertThat(integrationAccounts.getItems()).containsExactly(createExpected());
    }

    private IntegrationAccount createExpected() {
        return IntegrationAccount.builder()
                .id(fromString("1d676229-8462-478a-bcbd-92b5c0e447d4"))
                .createdAt(parse("2023-02-28T14:36:10.319029Z"))
                .lastAccessedAt(parse("2023-03-08T14:05:55.470695Z"))
                .accountId(fromString("cac6b18d-e21c-42e8-9e66-95c9a3fbb8ef"))
                .financialInstitutionId(fromString("953934eb-229a-4fd2-8675-07794078cc7d"))
                .organizationId(fromString("6616fe6e-3e1f-44dd-867e-7ff24d8c3799"))
                .build();
    }
}
