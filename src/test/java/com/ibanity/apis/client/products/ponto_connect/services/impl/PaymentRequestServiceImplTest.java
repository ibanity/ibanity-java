package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentRequestCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.PaymentRequestDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.PaymentRequestReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
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
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentRequestServiceImplTest {

    private static final String PAYMENT_REQUEST_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/{accountId}/payment-requests/{paymentRequestId}";
    private static final String PAYMENT_REQUEST_ENDPOINT_FOR_FIND = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/payment-requests/6c0350b5-a724-42e5-b4ce-ca3a6994df3d";
    private static final String PAYMENT_REQUEST_ENDPOINT_FOR_CREATE = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/payment-requests";
    private static final UUID ACCOUNT_ID = UUID.fromString("8804e34f-12b0-4b70-86bf-265f013ca232");
    private static final UUID PAYMENT_REQUEST_ID = UUID.fromString("6c0350b5-a724-42e5-b4ce-ca3a6994df3d");
    private static final String ACCESS_TOKEN = "anAccessToken";

    @InjectMocks
    private PaymentRequestServiceImpl paymentRequestService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "paymentRequests"))
                .thenReturn(PAYMENT_REQUEST_ENDPOINT);
    }

    @Test
    void find() throws IOException {
        PaymentRequestReadQuery paymentRequestReadQuery = PaymentRequestReadQuery.builder()
                .accountId(ACCOUNT_ID)
                .paymentRequestId(PAYMENT_REQUEST_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.get(buildUri(PAYMENT_REQUEST_ENDPOINT_FOR_FIND), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/payment_request.json"));

        PaymentRequest actual = paymentRequestService.find(paymentRequestReadQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected(null));
    }

    @Test
    void create() throws IOException {
        PaymentRequestCreateQuery paymentRequestCreateQuery = PaymentRequestCreateQuery.builder()
                .accountId(ACCOUNT_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.post(eq(buildUri(PAYMENT_REQUEST_ENDPOINT_FOR_CREATE)), any(),eq(emptyMap()), eq(ACCESS_TOKEN)))
                .thenReturn(loadHttpResponse("json/ponto-connect/payment_request_create.json"));

        PaymentRequest actual = paymentRequestService.create(paymentRequestCreateQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected("https://sandbox-pay.myponto.com/organizations/5fa92700-7416-4fd4-a8f4-fbdb2a533d11/payment-requests/6c0350b5-a724-42e5-b4ce-ca3a6994df3d"));
    }

    @Test
    void delete() throws IOException {
        PaymentRequestDeleteQuery paymentRequestDeleteQuery = PaymentRequestDeleteQuery.builder()
                .paymentRequestId(PAYMENT_REQUEST_ID)
                .accountId(ACCOUNT_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.delete(buildUri(PAYMENT_REQUEST_ENDPOINT_FOR_FIND), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/payment_request.json"));

        PaymentRequest actual = paymentRequestService.delete(paymentRequestDeleteQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected(null));
    }

    private PaymentRequest createExpected(String redirect) {
        return PaymentRequest.builder()
                .amount(ONE)
                .creditorAccountReference("BE84732645584359")
                .creditorAccountReferenceType("IBAN")
                .debtorAccountReferenceType("IBAN")
                .currency("EUR")
                .id(PAYMENT_REQUEST_ID)
                .remittanceInformation("ThisIsATest")
                .remittanceInformationType("unstructured")
                .redirectLink(redirect)
                .endToEndId("4874366da78549e0b3014a86cd646dc4")
                .build();
    }
}
