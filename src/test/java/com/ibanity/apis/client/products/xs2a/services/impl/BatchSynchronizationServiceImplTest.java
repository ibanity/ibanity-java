package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.BatchSynchronization;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.create.BatchSynchronizationCreationQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import static java.util.Collections.emptyMap;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchSynchronizationServiceImplTest {

    private static final UUID BATCH_SYNCHRONIZATION_ID = fromString("04e98b21-8213-4aec-b373-13eb51f948e9");
    private static final String BATCH_SYNCHRONIZATION_ENDPOINT = "https://api.ibanity.com/xs2a/batch-synchronizations";
    private static final Instant BATCH_SYNCHRONIZATION_X_DATE = Instant.parse("2022-05-17T00:00:00.000Z");

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private BatchSynchronizationServiceImpl batchSynchronizationService;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "batchSynchronizations")).thenReturn(BATCH_SYNCHRONIZATION_ENDPOINT);
    }

    @Test
    void create() throws Exception {
        BatchSynchronizationCreationQuery batchSynchronizationCreationQuery =
                BatchSynchronizationCreationQuery.builder()
                        .resourceType("account")
                        .cancelAfter(BATCH_SYNCHRONIZATION_X_DATE)
                        .unlessSynchronizedAfter(BATCH_SYNCHRONIZATION_X_DATE)
                        .subtypes(Arrays.asList("accountDetails", "accountTransactions"))
                        .build();

        when(ibanityHttpClient.post(new URI(BATCH_SYNCHRONIZATION_ENDPOINT), createRequest(batchSynchronizationCreationQuery), emptyMap(), null))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/createBatchSynchronization.json"));

        BatchSynchronization actual = batchSynchronizationService.create(batchSynchronizationCreationQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    private RequestApiModel createRequest(BatchSynchronizationCreationQuery batchSynchronizationCreationQuery) {
        BatchSynchronization batchSynchronization = BatchSynchronization.builder()
                .resourceType(batchSynchronizationCreationQuery.getResourceType())
                .subtypes(batchSynchronizationCreationQuery.getSubtypes())
                .cancelAfter(batchSynchronizationCreationQuery.getCancelAfter())
                .unlessSynchronizedAfter(batchSynchronizationCreationQuery.getUnlessSynchronizedAfter())
                .build();
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .attributes(batchSynchronization)
                                .type(Synchronization.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private BatchSynchronization createExpected() {
        BatchSynchronization.BatchSynchronizationBuilder batchSynchronizationBuilder = BatchSynchronization.builder()
                .id(BATCH_SYNCHRONIZATION_ID)
                .resourceType("account")
                .cancelAfter(BATCH_SYNCHRONIZATION_X_DATE)
                .unlessSynchronizedAfter(BATCH_SYNCHRONIZATION_X_DATE)
                .subtypes(Arrays.asList("accountDetails", "accountTransactions"));

        return batchSynchronizationBuilder
                .build();
    }
}
