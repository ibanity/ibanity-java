package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.ReauthorizationRequest;
import com.ibanity.apis.client.products.ponto_connect.models.create.ReauthorizationRequestCreateQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReauthorizationRequestServiceImplTest {

    private static final String REAUTHORIZATION_REQUEST_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/{accountId}/reauthorization-requests/{reauthorizationRequestId}";
    private static final String REAUTHORIZATION_REQUEST_ENDPOINT_FOR_CREATE = "https://api.ibanity.localhost/ponto-connect/accounts/44f261ec-2cc9-47f8-8cad-bcd6994629ed/reauthorization-requests";
    private static final UUID ACCOUNT_ID = UUID.fromString("44f261ec-2cc9-47f8-8cad-bcd6994629ed");
    private static final UUID REAUTHORIZATION_REQUEST_ID = UUID.fromString("7f4a4447-eadf-4529-9145-0b3060c260c8");
    private static final String ACCESS_TOKEN = "anAccessToken";

    @InjectMocks
    private ReauthorizationRequestServiceImpl reauthorizationRequestService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "reauthorizationRequests"))
                .thenReturn(REAUTHORIZATION_REQUEST_ENDPOINT);
    }

    @Test
    void create() throws IOException {
        ReauthorizationRequestCreateQuery reauthorizationRequestCreateQuery = ReauthorizationRequestCreateQuery.builder()
                .accountId(ACCOUNT_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.post(eq(buildUri(REAUTHORIZATION_REQUEST_ENDPOINT_FOR_CREATE)), any(),eq(emptyMap()), eq(ACCESS_TOKEN)))
                .thenReturn(loadHttpResponse("json/ponto-connect/create_reauthorization_request.json"));

        ReauthorizationRequest actual = reauthorizationRequestService.create(reauthorizationRequestCreateQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected("https://authorize.myponto.com/organizations/6680437c-8ed8-425b-84b7-2c31e5ca625d/sandbox/integrations/236d8f5c-9e19-45c7-8138-1a50910020ae/accounts/44f261ec-2cc9-47f8-8cad-bcd6994629ed/reauthorization-requests/7f4a4447-eadf-4529-9145-0b3060c260c8"));
    }

    private ReauthorizationRequest createExpected(String redirect) {
        return ReauthorizationRequest.builder()
                .id(REAUTHORIZATION_REQUEST_ID)
                .redirectLink(redirect)
                .build();
    }
}
