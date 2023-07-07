package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.BatchTransactionDeleteRequest;
import com.ibanity.apis.client.products.xs2a.models.create.BatchTransactionDeleteRequestCreationQuery;
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

import static java.util.Collections.emptyMap;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchTransactionDeleteRequestServiceImplTest {

    private static final UUID BATCH_TRANSACTION_DELETE_REQUEST_ID = fromString("04e98b21-8213-4aec-b373-13eb51f948e9");
    private static final String BATCH_TRANSACTION_DELETE_REQUEST_ENDPOINT = "https://api.ibanity.com/xs2a/batch-transaction-delete-requests";
    private static final Instant BATCH_TRANSACTION_DELETE_REQUEST_X_DATE = Instant.parse("2022-05-17T00:00:00.000Z");

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private BatchTransactionDeleteRequestServiceImpl batchTransactionDeleteRequestService;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "batchTransactionDeleteRequests")).thenReturn(BATCH_TRANSACTION_DELETE_REQUEST_ENDPOINT);
    }

    @Test
    void create() throws Exception {
        BatchTransactionDeleteRequestCreationQuery batchTransactionDeleteRequestCreationQuery =
                BatchTransactionDeleteRequestCreationQuery.builder()
                        .beforeDate(BATCH_TRANSACTION_DELETE_REQUEST_X_DATE)
                        .build();

        when(ibanityHttpClient.post(new URI(BATCH_TRANSACTION_DELETE_REQUEST_ENDPOINT), createRequest(batchTransactionDeleteRequestCreationQuery), emptyMap(), null))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/createBatchTransactionDeleteRequest.json"));

        BatchTransactionDeleteRequest actual = batchTransactionDeleteRequestService.create(batchTransactionDeleteRequestCreationQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    private RequestApiModel createRequest(BatchTransactionDeleteRequestCreationQuery batchTransactionDeleteRequestCreationQuery) {
        BatchTransactionDeleteRequest batchTransactionDeleteRequest = BatchTransactionDeleteRequest.builder()
                .beforeDate(batchTransactionDeleteRequestCreationQuery.getBeforeDate())
                .build();
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .attributes(batchTransactionDeleteRequest)
                                .type(BatchTransactionDeleteRequest.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private BatchTransactionDeleteRequest createExpected() {
        BatchTransactionDeleteRequest.BatchTransactionDeleteRequestBuilder batchTransactionDeleteRequestBuilder = BatchTransactionDeleteRequest.builder()
                .id(BATCH_TRANSACTION_DELETE_REQUEST_ID)
                .beforeDate(BATCH_TRANSACTION_DELETE_REQUEST_X_DATE);

        return batchTransactionDeleteRequestBuilder
                .build();
    }
}
