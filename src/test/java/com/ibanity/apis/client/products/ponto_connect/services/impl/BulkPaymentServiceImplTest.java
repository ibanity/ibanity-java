package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.BulkPayment;
import com.ibanity.apis.client.products.ponto_connect.models.create.BulkPaymentCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.BulkPaymentDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.BulkPaymentReadQuery;
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
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulkPaymentServiceImplTest {

    private static final String BULK_PAYMENT_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/{accountId}/bulk-payments/{bulkPaymentId}";
    private static final String BULK_PAYMENT_ENDPOINT_FOR_FIND = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/bulk-payments/6c0350b5-a724-42e5-b4ce-ca3a6994df3d";
    private static final String BULK_PAYMENT_ENDPOINT_FOR_CREATE = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/bulk-payments";
    private static final UUID ACCOUNT_ID = UUID.fromString("8804e34f-12b0-4b70-86bf-265f013ca232");
    private static final UUID BULK_PAYMENT_ID = UUID.fromString("6c0350b5-a724-42e5-b4ce-ca3a6994df3d");
    private static final String ACCESS_TOKEN = "anAccessToken";

    @InjectMocks
    private BulkPaymentServiceImpl bulkPaymentService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "bulkPayments"))
                .thenReturn(BULK_PAYMENT_ENDPOINT);
    }

    @Test
    void find() throws IOException {
        BulkPaymentReadQuery paymentReadQuery = BulkPaymentReadQuery.builder()
                .accountId(ACCOUNT_ID)
                .bulkPaymentId(BULK_PAYMENT_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.get(buildUri(BULK_PAYMENT_ENDPOINT_FOR_FIND), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/bulk_payment.json"));

        BulkPayment actual = bulkPaymentService.find(paymentReadQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected(null));
    }

    @Test
    void create() throws IOException {
        BulkPaymentCreateQuery paymentCreateQuery = BulkPaymentCreateQuery.builder()
                .accountId(ACCOUNT_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.post(eq(buildUri(BULK_PAYMENT_ENDPOINT_FOR_CREATE)), any(),eq(emptyMap()), eq(ACCESS_TOKEN)))
                .thenReturn(loadHttpResponse("json/ponto-connect/bulk_payment_create.json"));

        BulkPayment actual = bulkPaymentService.create(paymentCreateQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected("https://authorize.myponto.com/organizations/6680437c-8ed8-425b-84b7-2c31e5ca625d/sandbox/integrations/236d8f5c-9e19-45c7-8138-1a50910020ae/accounts/44f261ec-2cc9-47f8-8cad-bcd6994629ed/bulk-payments/e9607b48-7941-4470-8b5a-19ddfe62e67a"));
    }

    @Test
    void delete() throws IOException {
        BulkPaymentDeleteQuery paymentDeleteQuery = BulkPaymentDeleteQuery.builder()
                .bulkPaymentId(BULK_PAYMENT_ID)
                .accountId(ACCOUNT_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.delete(buildUri(BULK_PAYMENT_ENDPOINT_FOR_FIND), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/delete_bulk_payment.json"));

        BulkPayment actual = bulkPaymentService.delete(paymentDeleteQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createDeleted());
    }

    private BulkPayment createDeleted() {
        return BulkPayment.builder()
                .id(BULK_PAYMENT_ID)
                .build();
    }

    private BulkPayment createExpected(String redirect) {
        return BulkPayment.builder()
                .redirectLink(redirect)
                .id(BULK_PAYMENT_ID)
                .reference("Invoice collection")
                .batchBookingPreferred(true)
                .requestedExecutionDate(LocalDate.parse("2022-02-05"))
                .status("unsigned")
                .build();
    }
}
