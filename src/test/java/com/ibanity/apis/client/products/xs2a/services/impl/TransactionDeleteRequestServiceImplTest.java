package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.TransactionDeleteRequest;
import com.ibanity.apis.client.products.xs2a.models.create.TransactionDeleteRequestCreationQuery;
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
class TransactionDeleteRequestServiceImplTest {

    private static final UUID BATCH_TRANSACTION_DELETE_REQUEST_ID = fromString("04e98b21-8213-4aec-b373-13eb51f948e9");
    private static final String BATCH_TRANSACTION_DELETE_REQUEST_ENDPOINT = "https://api.ibanity.com/xs2a/transaction-delete-requests";
    private static final Instant BATCH_TRANSACTION_DELETE_REQUEST_X_DATE = Instant.parse("2022-05-17T00:00:00.000Z");

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private TransactionDeleteRequestServiceImpl transactionDeleteRequestService;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "transactionDeleteRequests")).thenReturn(BATCH_TRANSACTION_DELETE_REQUEST_ENDPOINT);
    }

    @Test
    void create() throws Exception {
        TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery =
                TransactionDeleteRequestCreationQuery.builder()
                        .beforeDate(BATCH_TRANSACTION_DELETE_REQUEST_X_DATE)
                        .build();

        when(ibanityHttpClient.post(new URI(BATCH_TRANSACTION_DELETE_REQUEST_ENDPOINT), createRequest(transactionDeleteRequestCreationQuery), emptyMap(), null))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/createTransactionDeleteRequest.json"));

        TransactionDeleteRequest actual = transactionDeleteRequestService.create(transactionDeleteRequestCreationQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    private RequestApiModel createRequest(TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery) {
        TransactionDeleteRequest transactionDeleteRequest = TransactionDeleteRequest.builder()
                .beforeDate(transactionDeleteRequestCreationQuery.getBeforeDate())
                .build();
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .attributes(transactionDeleteRequest)
                                .type(TransactionDeleteRequest.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private TransactionDeleteRequest createExpected() {
        TransactionDeleteRequest.TransactionDeleteRequestBuilder transactionDeleteRequestBuilder = TransactionDeleteRequest.builder()
                .id(BATCH_TRANSACTION_DELETE_REQUEST_ID)
                .beforeDate(BATCH_TRANSACTION_DELETE_REQUEST_X_DATE);

        return transactionDeleteRequestBuilder
                .build();
    }
}
