package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.OrganizationUsage;
import com.ibanity.apis.client.products.ponto_connect.models.read.OrganizationUsageReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static java.util.UUID.fromString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsageServiceImplTest {

    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final UUID ORGANIZATION_ID = fromString("953934eb-229a-4fd2-8675-07794078cc7d");
    private static final String MONTH = "2020-08";
    private static final String ORGANIZATION_ENDPOINT = "https://api.development.ibanity.net/ponto-connect/organizations/{organizationId}/usage/{month}";
    private static final String GET_ORGANIZATION_ENDPOINT = "https://api.development.ibanity.net/ponto-connect/organizations/" + ORGANIZATION_ID.toString() + "/usage/" + MONTH;

    @InjectMocks
    private UsageServiceImpl organizationUsageService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "organizations", "usage")).thenReturn(ORGANIZATION_ENDPOINT);
    }

    @Test
    void getOrganizationUsage() throws URISyntaxException, IOException {
        when(ibanityHttpClient.get(new URI(GET_ORGANIZATION_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/organizations_usage.json"));

        OrganizationUsageReadQuery readQuery = OrganizationUsageReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .month(MONTH)
                .organizationId(ORGANIZATION_ID)
                .build();
        OrganizationUsage actual = organizationUsageService.getOrganizationUsage(readQuery);

        Assertions.assertThat(actual).isEqualToComparingFieldByField(expected());
    }

    private OrganizationUsage expected() {
        return OrganizationUsage.builder()
                .accountCount(BigDecimal.ONE)
                .paymentAccountCount(BigDecimal.TEN)
                .bulkPaymentCount(new BigDecimal("1.5"))
                .id(MONTH)
                .paymentCount(new BigDecimal("1.1"))
                .build();
    }
}
