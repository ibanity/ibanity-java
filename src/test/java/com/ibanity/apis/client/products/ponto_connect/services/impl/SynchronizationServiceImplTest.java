package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Synchronization;
import com.ibanity.apis.client.products.ponto_connect.models.create.SynchronizationCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.SynchronizationReadQuery;
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
import java.time.Instant;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SynchronizationServiceImplTest {

    private static final String SYNCHRONIZATION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/synchronizations/{synchronizationId}";
    private static final String CREATE_SYNCHRONIZATION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/synchronizations";
    private static final String FIND_SYNCHRONIZATION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/synchronizations/1b3e3011-d018-4785-bbba-9aa75ba14d45";
    private static final String SUBTYPE = "accountDetails";
    private static final String RESOURCE_ID = "8804e34f-12b0-4b70-86bf-265f013ca232";
    private static final String ACCESS_TOKEN = "anAccessToken";
    private static final String RESOURCE_TYPE = "account";
    public static final UUID SYNCHRONIZATION_ID = UUID.fromString("1b3e3011-d018-4785-bbba-9aa75ba14d45");

    @InjectMocks
    private SynchronizationServiceImpl synchronizationService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "synchronizations")).thenReturn(SYNCHRONIZATION_ENDPOINT);
    }

    @Test
    public void create() throws Exception {
        when(ibanityHttpClient.post(new URI(CREATE_SYNCHRONIZATION_ENDPOINT),
                buildRequest(Synchronization.RESOURCE_TYPE, Synchronization.builder()
                        .subtype(SUBTYPE)
                        .resourceId(RESOURCE_ID)
                        .resourceType(RESOURCE_TYPE)
                        .build()),
                emptyMap(),
                ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/synchronization.json"));

        Synchronization actual = synchronizationService.create(SynchronizationCreateQuery.builder()
                .subtype(SUBTYPE)
                .resourceId(RESOURCE_ID)
                .accessToken(ACCESS_TOKEN)
                .resourceType(RESOURCE_TYPE)
                .build());

        assertThat(actual).isEqualTo(createExpected());
    }

    @Test
    public void find() throws Exception {
        when(ibanityHttpClient.get(new URI(FIND_SYNCHRONIZATION_ENDPOINT),
                emptyMap(),
                ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/synchronization.json"));

        Synchronization actual = synchronizationService.find(SynchronizationReadQuery.builder()
                .synchronizationId(SYNCHRONIZATION_ID)
                .accessToken(ACCESS_TOKEN)
                .build());

        assertThat(actual).isEqualTo(createExpected());
    }

    private Synchronization createExpected() {
        return Synchronization.builder()
                .subtype(SUBTYPE)
                .resourceId(RESOURCE_ID)
                .resourceType(RESOURCE_TYPE)
                .id(SYNCHRONIZATION_ID)
                .updatedAt(Instant.parse("2019-09-02T11:28:35.971Z"))
                .status("pending")
                .createdAt(Instant.parse("2019-09-02T11:28:35.971Z"))
                .build();
    }

}
