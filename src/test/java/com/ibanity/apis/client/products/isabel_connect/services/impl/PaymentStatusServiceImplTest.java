package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.isabel_connect.models.read.PaymentStatusReadQuery;
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

import static com.ibanity.apis.client.helpers.IbanityTestHelper.createHttpResponse;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentStatusServiceImplTest {
    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final String BPIR_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/bulk-payment-initiation-requests/{bulkPaymentInitiationRequestId}";
    private static final String PAYMENT_ID = "90000036388323";
    private static final String FIND_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/bulk-payment-initiation-requests/" + PAYMENT_ID + "/payment-status";
    private static final String FIND_WITH_NOTIFICATION_ENDPOINT = FIND_ENDPOINT + "?notificationId=a";

    @InjectMocks
    private PaymentStatusServiceImpl service;

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
        String xmlContent = loadFile("xml/isabel-connect/payment_status.xml");
        when(ibanityHttpClient.get(new URI(FIND_ENDPOINT), Collections.emptyMap(), ACCESS_TOKEN))
                .thenReturn(createHttpResponse(xmlContent));

        String actual = service.find(PaymentStatusReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .bulkPaymentInitiationRequestId(PAYMENT_ID)
                .build());

        assertThat(actual).isEqualTo(xmlContent);
    }

    @Test
    public void findWithNotificationId() throws Exception {
        String xmlContent = loadFile("xml/isabel-connect/payment_status.xml");
        when(ibanityHttpClient.get(new URI(FIND_WITH_NOTIFICATION_ENDPOINT), Collections.emptyMap(), ACCESS_TOKEN))
                .thenReturn(createHttpResponse(xmlContent));

        String actual = service.find(PaymentStatusReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .bulkPaymentInitiationRequestId(PAYMENT_ID)
                .notificationId("a")
                .build());

        assertThat(actual).isEqualTo(xmlContent);
    }
}
