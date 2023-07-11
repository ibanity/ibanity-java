package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.TransactionDeleteRequest;
import com.ibanity.apis.client.products.xs2a.models.create.TransactionDeleteRequestCreationQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
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
class TransactionDeleteRequestsServiceImplTest {

    private static final UUID ACCOUNT_ID = UUID.fromString("1c020714-759c-4ee6-ae87-5ce667937e77");
    private static final UUID FINANCIAL_INSTITUTION_ID = UUID.fromString("99477654-a061-414c-afb4-19e37d13c5a3");
    private static final UUID TRANSACTION_DELETE_REQUEST_ID = fromString("04e98b21-8213-4aec-b373-13eb51f948e9");
    private static final String TRANSACTION_DELETE_REQUEST_FOR_APPLICATION_ENDPOINT = "https://api.ibanity.com/xs2a/transaction-delete-requests";
    private static final String TRANSACTION_DELETE_REQUEST_FOR_CUSTOMER_ENDPOINT = "https://api.ibanity.com/xs2a/customer/transaction-delete-requests";
    private static final String TRANSACTION_DELETE_REQUEST_FOR_ACCOUNT_ENDPOINT = "https://api.ibanity.com/xs2a/customer/financial-institutions/id/accounts/1c020714-759c-4ee6-ae87-5ce667937e77/transaction-delete-requests";
    private static final Instant TRANSACTION_DELETE_REQUEST_X_DATE = Instant.parse("2022-05-17T00:00:00.000Z");
    private static final String CUSTOMER_ACCESS_TOKEN = "itsme";

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private TransactionDeleteRequestsServiceImpl transactionDeleteRequestsService;

    @Test
    void createForApplication() throws Exception {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "transactionDeleteRequests")).thenReturn(TRANSACTION_DELETE_REQUEST_FOR_APPLICATION_ENDPOINT);

        TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery =
                TransactionDeleteRequestCreationQuery.builder()
                        .beforeDate(TRANSACTION_DELETE_REQUEST_X_DATE)
                        .build();

        when(ibanityHttpClient.post(new URI(TRANSACTION_DELETE_REQUEST_FOR_APPLICATION_ENDPOINT), createRequest(transactionDeleteRequestCreationQuery), emptyMap(), null))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/createTransactionDeleteRequest.json"));

        TransactionDeleteRequest actual = transactionDeleteRequestsService.createForApplication(transactionDeleteRequestCreationQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    @Test
    void createForCustomer() throws Exception {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "transactionDeleteRequests")).thenReturn(TRANSACTION_DELETE_REQUEST_FOR_CUSTOMER_ENDPOINT);

        TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery =
                TransactionDeleteRequestCreationQuery.builder()
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .beforeDate(TRANSACTION_DELETE_REQUEST_X_DATE)
                        .build();

        when(ibanityHttpClient.post(new URI(TRANSACTION_DELETE_REQUEST_FOR_CUSTOMER_ENDPOINT), createRequest(transactionDeleteRequestCreationQuery), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/createTransactionDeleteRequest.json"));

        TransactionDeleteRequest actual = transactionDeleteRequestsService.createForCustomer(transactionDeleteRequestCreationQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    @Test
    void createForAccount() throws Exception {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "account", "transactionDeleteRequests")).thenReturn(TRANSACTION_DELETE_REQUEST_FOR_ACCOUNT_ENDPOINT);

        TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery =
                TransactionDeleteRequestCreationQuery.builder()
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .accountId(ACCOUNT_ID)
                        .beforeDate(TRANSACTION_DELETE_REQUEST_X_DATE)
                        .build();

        when(ibanityHttpClient.post(new URI(TRANSACTION_DELETE_REQUEST_FOR_ACCOUNT_ENDPOINT), createRequest(transactionDeleteRequestCreationQuery), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/createTransactionDeleteRequest.json"));

        TransactionDeleteRequest actual = transactionDeleteRequestsService.createForAccount(transactionDeleteRequestCreationQuery);

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
                .id(TRANSACTION_DELETE_REQUEST_ID)
                .beforeDate(TRANSACTION_DELETE_REQUEST_X_DATE);

        return transactionDeleteRequestBuilder
                .build();
    }
}
