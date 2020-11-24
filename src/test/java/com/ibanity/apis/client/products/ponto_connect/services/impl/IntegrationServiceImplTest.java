package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Integration;
import com.ibanity.apis.client.products.ponto_connect.models.delete.OrganizationIntegrationDeleteQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntegrationServiceImplTest {

    private static final String INTEGRATION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/organizations/{organizationId}/integration";
    private static final String DELETE_INTEGRATION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/organizations/6680437c-8ed8-425b-84b7-2c31e5ca625d/integration";
    private static final UUID INTEGRATION_ID = UUID.fromString("8804e34f-12b0-4b70-86bf-265f013ca232");
    private static final UUID ORGANISATION_ID = UUID.fromString("6680437c-8ed8-425b-84b7-2c31e5ca625d");
    private static final String ACCESS_TOKEN = "anAccessToken";

    @InjectMocks
    private IntegrationServiceImpl integrationService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "organizations", "integration"))
                .thenReturn(INTEGRATION_ENDPOINT);
    }

    @Test
    void delete() throws IOException, URISyntaxException {
        OrganizationIntegrationDeleteQuery deleteQuery = OrganizationIntegrationDeleteQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .organizationId(ORGANISATION_ID)
                .build();

        when(ibanityHttpClient.delete(new URI(DELETE_INTEGRATION_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/delete_organization_integration.json"));
        Integration integration = integrationService.delete(deleteQuery);
        assertThat(integration.getId()).isEqualTo(INTEGRATION_ID);
    }
}
