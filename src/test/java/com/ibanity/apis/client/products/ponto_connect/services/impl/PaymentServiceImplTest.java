package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Payment;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.PaymentDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.PaymentReadQuery;
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
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    private static final String PAYMENT_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/accounts/{accountId}/payments/{paymentId}";
    private static final String PAYMENT_ENDPOINT_FOR_FIND = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/payments/6c0350b5-a724-42e5-b4ce-ca3a6994df3d";
    private static final String PAYMENT_ENDPOINT_FOR_CREATE = "https://api.ibanity.localhost/ponto-connect/accounts/8804e34f-12b0-4b70-86bf-265f013ca232/payments";
    private static final UUID ACCOUNT_ID = UUID.fromString("8804e34f-12b0-4b70-86bf-265f013ca232");
    private static final UUID PAYMENT_ID = UUID.fromString("6c0350b5-a724-42e5-b4ce-ca3a6994df3d");
    private static final String ACCESS_TOKEN = "anAccessToken";

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "payments"))
                .thenReturn(PAYMENT_ENDPOINT);
    }

    @Test
    void find() throws IOException {
        PaymentReadQuery paymentReadQuery = PaymentReadQuery.builder()
                .accountId(ACCOUNT_ID)
                .paymentId(PAYMENT_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.get(buildUri(PAYMENT_ENDPOINT_FOR_FIND), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/payment.json"));

        Payment actual = paymentService.find(paymentReadQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected(null));
    }

    @Test
    void create() throws IOException {
        PaymentCreateQuery paymentCreateQuery = PaymentCreateQuery.builder()
                .accountId(ACCOUNT_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.post(eq(buildUri(PAYMENT_ENDPOINT_FOR_CREATE)), any(),eq(emptyMap()), eq(ACCESS_TOKEN)))
                .thenReturn(loadHttpResponse("json/ponto-connect/payment_create.json"));

        Payment actual = paymentService.create(paymentCreateQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected("https://authorize.myponto.com/organizations/6680437c-8ed8-425b-84b7-2c31e5ca625d/sandbox/integrations/236d8f5c-9e19-45c7-8138-1a50910020ae/accounts/44f261ec-2cc9-47f8-8cad-bcd6994629ed/payments/c3a13020-61a4-4d9e-ab57-1f0df1f20d7d"));
    }

    @Test
    void delete() throws IOException {
        PaymentDeleteQuery paymentDeleteQuery = PaymentDeleteQuery.builder()
                .paymentId(PAYMENT_ID)
                .accountId(ACCOUNT_ID)
                .accessToken(ACCESS_TOKEN)
                .build();
        when(ibanityHttpClient.delete(buildUri(PAYMENT_ENDPOINT_FOR_FIND), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/payment.json"));

        Payment actual = paymentService.delete(paymentDeleteQuery);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected(null));
    }

    private Payment createExpected(String redirect) {
        return Payment.builder()
                .amount(ONE)
                .creditorAccountReference("BE84732645584359")
                .creditorAccountReferenceType("IBAN")
                .creditorAgent("anAgent")
                .creditorAgentType("anAgentType")
                .creditorName("Alex Creditor")
                .currency("EUR")
                .id(PAYMENT_ID)
                .remittanceInformation("ThisIsATest")
                .remittanceInformationType("unstructured")
                .requestedExecutionDate(LocalDate.parse("2019-01-01"))
                .status("pending")
                .redirectLink(redirect)
                .endToEndId("1234567890")
                .build();
    }
}
