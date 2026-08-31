package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.PaymentNotification;
import com.ibanity.apis.client.products.isabel_connect.models.delete.PaymentNotificationDeleteQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.PaymentNotificationsReadQuery;
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
import java.time.Instant;
import java.util.Collections;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.createHttpResponse;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentNotificationServiceImplTest {
    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final String BPIR_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/bulk-payment-initiation-requests/{bulkPaymentInitiationRequestId}";
    private static final String LIST_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/bulk-payment-initiation-requests/notifications?size=20";
    private static final String NOTIFICATION_ID = "14e2bff5-e365-4bc7-bf48-76b7bcd464e9";
    private static final String DELETE_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/bulk-payment-initiation-requests/notifications/" + NOTIFICATION_ID;

    @InjectMocks
    private PaymentNotificationServiceImpl service;

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
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_ENDPOINT), Collections.emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/isabel-connect/payment_notifications.json"));

        IsabelCollection<PaymentNotification> actual = service.list(PaymentNotificationsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .build());

        assertThat(actual.getItems().size()).isEqualTo(1);
        assertThat(actual.getPagingOffset()).isEqualTo(0);
        assertThat(actual.getPagingTotal()).isEqualTo(1);

        PaymentNotification notification = actual.getItems().get(0);
        assertThat(notification.getId()).isEqualTo(NOTIFICATION_ID);
        assertThat(notification.getNotificationType()).isEqualTo("payment.status.updated");
        assertThat(notification.getCreatedAt()).isEqualTo(Instant.parse("2026-06-04T14:30:00.000Z"));
        assertThat(notification.getPaymentId()).isEqualTo("90000036388319");
    }

    @Test
    public void delete() throws Exception {
        when(ibanityHttpClient.delete(new URI(DELETE_ENDPOINT), Collections.emptyMap(), ACCESS_TOKEN))
                .thenReturn(createHttpResponse(""));

        service.delete(PaymentNotificationDeleteQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .paymentNotificationId(NOTIFICATION_ID)
                .build());

        verify(ibanityHttpClient).delete(new URI(DELETE_ENDPOINT), Collections.emptyMap(), ACCESS_TOKEN);
    }
}
