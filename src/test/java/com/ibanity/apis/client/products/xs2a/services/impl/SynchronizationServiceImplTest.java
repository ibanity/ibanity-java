package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.factory.read.SynchronizationReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SynchronizationServiceImplTest {

    private static final UUID SYNCHRONIZATION_ID = fromString("04e98b21-8213-4aec-b373-13eb51f948e9");
    private static final String ACCOUNT_ID = "d0027fc6-0097-41f7-a9bb-3c400b5a3e2a";
    private static final String SYNCHRONIZATION_ENDPOINT = "https://api.ibanity.com/xs2a/customer/synchronizations";
    private static final String CUSTOMER_ACCESS_TOKEN = "itsme";

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private SynchronizationServiceImpl synchronizationService;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "synchronizations")).thenReturn(SYNCHRONIZATION_ENDPOINT);
    }

    @Test
    void create() throws Exception {
        SynchronizationReadQuery synchronizationReadQuery =
                SynchronizationReadQuery.builder()
                        .resourceType("account")
                        .subtype("accountDetails")
                        .resourceId(ACCOUNT_ID)
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();

        when(ibanityHttpClient.post(new URI(SYNCHRONIZATION_ENDPOINT), createRequest(synchronizationReadQuery), CUSTOMER_ACCESS_TOKEN)).thenReturn(IbanityTestHelper.loadFile("json/create_synchronization.json"));

        Synchronization actual = synchronizationService.create(synchronizationReadQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected("pending"));
    }

    @Test
    void find() throws Exception {
        SynchronizationReadQuery synchronizationReadQuery =
                SynchronizationReadQuery.builder()
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .synchronizationId(SYNCHRONIZATION_ID)
                        .build();

        when(ibanityHttpClient.get(new URI(SYNCHRONIZATION_ENDPOINT + "/" + SYNCHRONIZATION_ID), CUSTOMER_ACCESS_TOKEN)).thenReturn(IbanityTestHelper.loadFile("json/synchronization.json"));

        Synchronization actual = synchronizationService.find(synchronizationReadQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected("success"));
    }

    private RequestApiModel createRequest(SynchronizationReadQuery synchronizationReadQuery) {
        Synchronization synchronization = Synchronization.builder()
                .resourceId(synchronizationReadQuery.getResourceId())
                .resourceType(synchronizationReadQuery.getResourceType())
                .subType(synchronizationReadQuery.getSubtype())
                .build();
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .attributes(synchronization)
                                .type(Synchronization.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private Synchronization createExpected(String status) {
        return Synchronization.builder()
                .id(SYNCHRONIZATION_ID)
                .resourceId(ACCOUNT_ID)
                .resourceType("account")
                .status(status)
                .subType("accountDetails")
                .createdAt(Instant.parse("2019-05-09T09:18:58.358Z"))
                .updatedAt(Instant.parse("2019-05-09T09:18:59.012Z"))
                .build();
    }
}