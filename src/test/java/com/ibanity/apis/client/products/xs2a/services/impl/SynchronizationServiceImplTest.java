package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.ErrorMeta;
import com.ibanity.apis.client.models.FinancialInstitutionResponse;
import com.ibanity.apis.client.models.IbanityError;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.create.SynchronizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.SynchronizationReadQuery;
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

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyMap;
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
        SynchronizationCreationQuery synchronizationCreationQuery =
                SynchronizationCreationQuery.builder()
                        .resourceType("account")
                        .subtype("accountDetails")
                        .resourceId(ACCOUNT_ID)
                        .customerOnline(true)
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();

        when(ibanityHttpClient.post(new URI(SYNCHRONIZATION_ENDPOINT), createRequest(synchronizationCreationQuery), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/create_synchronization.json"));

        Synchronization actual = synchronizationService.create(synchronizationCreationQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected("pending", "somehtml"));
    }

    @Test
    void find() throws Exception {
        SynchronizationReadQuery synchronizationReadQuery =
                SynchronizationReadQuery.builder()
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .synchronizationId(SYNCHRONIZATION_ID)
                        .build();

        when(ibanityHttpClient.get(new URI(SYNCHRONIZATION_ENDPOINT + "/" + SYNCHRONIZATION_ID), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/synchronization.json"));

        Synchronization actual = synchronizationService.find(synchronizationReadQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected("error", "somehtml"));
    }

    @Test
    void find_when_error_contains_json() throws Exception {
        SynchronizationReadQuery synchronizationReadQuery =
                SynchronizationReadQuery.builder()
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .synchronizationId(SYNCHRONIZATION_ID)
                        .build();

        when(ibanityHttpClient.get(new URI(SYNCHRONIZATION_ENDPOINT + "/" + SYNCHRONIZATION_ID), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/synchronization_with_json_error.json"));

        Synchronization actual = synchronizationService.find(synchronizationReadQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected("error", "{\"tppMessages\":[{\"category\":\"ERROR\",\"code\":\"NOT_FOUND\",\"text\":\"3.2 - Not Found\"}]}"));
    }

    private RequestApiModel createRequest(SynchronizationCreationQuery synchronizationCreationQuery) {
        Synchronization synchronization = Synchronization.builder()
                .resourceId(synchronizationCreationQuery.getResourceId())
                .resourceType(synchronizationCreationQuery.getResourceType())
                .subtype(synchronizationCreationQuery.getSubtype())
                .customerOnline(synchronizationCreationQuery.isCustomerOnline())
                .customerIpAddress(synchronizationCreationQuery.getCustomerIpAddress())
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

    private Synchronization createExpected(String status, String body) {
        Synchronization.SynchronizationBuilder synchronizationBuilder = Synchronization.builder()
                .id(SYNCHRONIZATION_ID)
                .resourceId(ACCOUNT_ID)
                .resourceType("account")
                .status(status)
                .subtype("accountDetails")
                .createdAt(Instant.parse("2019-05-09T09:18:58.358Z"))
                .updatedAt(Instant.parse("2019-05-09T09:18:59.012Z"));
        if ("error".equalsIgnoreCase(status)) {
            synchronizationBuilder = synchronizationBuilder.errors(newArrayList(IbanityError.builder()
                    .code("authorizationInvalid")
                    .detail("The authorization is invalid, you should ask the customer to reauthorize the account")
                    .meta(ErrorMeta.builder()
                            .financialInstitutionResponse(FinancialInstitutionResponse.builder()
                                    .body(body)
                                    .requestId("354fwfwef4w684")
                                    .statusCode(500)
                                    .timestamp(Instant.parse("2019-05-09T09:18:00.000Z"))
                                    .requestUri("http://google.com")
                                    .build())
                            .build())
                    .build()));

        }
        return synchronizationBuilder
                .build();
    }
}
