package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.PaymentRequestActivationRequest;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentRequestActivationRequestCreateQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.util.Collections.emptyMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentRequestActivationRequestServiceImplTest {

    private static final String PAYMENT_ACTIVATION_REQUEST_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/payment-request-activation-request";
    private static final UUID PAYMENT_ACTIVATION_REQUEST_ID = UUID.fromString("7e7fca4c-cc8e-4e23-9a5f-cfa4dc175a88");
    private static final String ACCESS_TOKEN = "anAccessToken";

    @InjectMocks
    private PaymentRequestActivationRequestServiceImpl paymentRequestActivationRequestService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "paymentRequestActivationRequests"))
                .thenReturn(PAYMENT_ACTIVATION_REQUEST_ENDPOINT);
    }

    @Test
    void create() throws IOException {
        PaymentRequestActivationRequestCreateQuery paymentRequestActivationRequestCreateQuery =
                PaymentRequestActivationRequestCreateQuery.builder()
                        .accessToken(ACCESS_TOKEN)
                        .redirectUri("https://example.localhost")
                        .build();

        when(ibanityHttpClient.post(eq(buildUri(PAYMENT_ACTIVATION_REQUEST_ENDPOINT)), any(),eq(emptyMap()), eq(ACCESS_TOKEN)))
                .thenReturn(loadHttpResponse("json/ponto-connect/create_payment_request_activation_request.json"));


        PaymentRequestActivationRequest actual = paymentRequestActivationRequestService.create(paymentRequestActivationRequestCreateQuery);

        Assertions.assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    private Object createExpected() {
        return PaymentRequestActivationRequest.builder()
                .id(PAYMENT_ACTIVATION_REQUEST_ID)
                .redirectLink("https://authorize.development.myponto.net/organizations/6680437c-8ed8-425b-84b7-2c31e5ca625d/sandbox/integrations/1f5caef0-7dcd-41d0-9318-24bf40ba9d16/payment-request-activation-requests/7e7fca4c-cc8e-4e23-9a5f-cfa4dc175a88")
                .build();
    }
}
