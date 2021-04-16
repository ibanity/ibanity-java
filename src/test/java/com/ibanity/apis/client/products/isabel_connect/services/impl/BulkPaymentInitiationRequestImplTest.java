package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.isabel_connect.models.BulkPaymentInitiationRequest;
import com.ibanity.apis.client.products.isabel_connect.models.read.BulkPaymentInitiationRequestReadQuery;
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
import java.util.Collections;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BulkPaymentInitiationRequestImplTest {
    public static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    public static final String BPIR_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/bulk-payment-initiation-requests/{bulkPaymentInitiationRequestId}";
    public static final String FIND_BPIR_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/bulk-payment-initiation-requests/93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1";

    @InjectMocks
    private BulkPaymentInitiationRequestServiceImpl service;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.IsabelConnect, "bulkPaymentInitiationRequests"))
            .thenReturn(BPIR_ENDPOINT);
    }

    @Test
    public void find() throws Exception {
        when(ibanityHttpClient.get(new URI(FIND_BPIR_ENDPOINT), Collections.emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/isabel-connect/bulk_payment_initiation_requests.json"));

        BulkPaymentInitiationRequest actual = service.find(BulkPaymentInitiationRequestReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .bulkPaymentInitiationRequestId("93ecb1fdbfb7848e7b7896c0f2d207aed3d8b4c1")
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    private BulkPaymentInitiationRequest createExpected() {
        return BulkPaymentInitiationRequest.builder()
                .status("processed-ok")
                .id("90000410224108")
                .build();
    }
}
