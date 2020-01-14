package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequestAuthorization;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestAuthorizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.PaymentInitiationRequestAuthorizationLinks;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.Maps.newHashMap;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.util.Collections.emptyMap;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentInitiationRequestAuthorizationsServiceImplTest {

    private static final UUID FINANCIAL_INSTITUTION_ID = fromString("4876fdd6-7333-4f9f-b142-ba520ca497b1");
    private static final UUID PAYMENT_INITIATION_REQUEST_ID = fromString("cd273ba1-cb2a-464d-b85d-62c9fc4dc8d9");
    private static final UUID AUTHORIZATION_ID = fromString("cd273ba1-cb2a-464d-b85d-62c9fc4dc8d9");
    private static final String AUTHORIZATION_CODE = "fndsfhskdfslfjhdskfjdsn";
    private static final String CUSTOMER_ACCESS_TOKEN = "kdsfldsfmnvlds;md,vms.kvmdsk.vmd";

    private static final String AUTHORIZATION_ENDPOINT = "https://api.ibanity.com/xs2a/customer/financial-institutions/{financialInstitutionId}/payment-initiation-requests/{paymentInitiationRequestId}/authorizations/{authorizationId}";
    private static final String AUTHORIZATION_ENDPOINT_FOR_CREATE = "https://api.ibanity.com/xs2a/customer/financial-institutions/4876fdd6-7333-4f9f-b142-ba520ca497b1/payment-initiation-requests/cd273ba1-cb2a-464d-b85d-62c9fc4dc8d9/authorizations";

    @InjectMocks
    private PaymentInitiationRequestAuthorizationsServiceImpl authorizationsService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @Captor
    private ArgumentCaptor<RequestApiModel> argumentCaptor;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "paymentInitiationRequest", "authorizations"))
                .thenReturn(AUTHORIZATION_ENDPOINT);
    }

    @Test
    void create() throws IOException {
        Map<String, String> queryParams = newHashMap();
        queryParams.put("code", AUTHORIZATION_CODE);
        PaymentInitiationRequestAuthorizationCreationQuery creationQuery = PaymentInitiationRequestAuthorizationCreationQuery.builder()
                .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                .queryParameters(queryParams)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .paymentInitiationRequestId(PAYMENT_INITIATION_REQUEST_ID)
                .build();

        when(ibanityHttpClient.post(eq(buildUri(AUTHORIZATION_ENDPOINT_FOR_CREATE)), argumentCaptor.capture(), eq(emptyMap()), eq(CUSTOMER_ACCESS_TOKEN))).thenReturn(loadFile("json/createPIRAuthorization.json"));

        PaymentInitiationRequestAuthorization authorization = authorizationsService.create(creationQuery);

        assertThat(authorization).isEqualTo(expected());
        PaymentInitiationRequestAuthorizationsServiceImpl.PaymentInitiationRequestAuthorization authorizationRequest =
                (PaymentInitiationRequestAuthorizationsServiceImpl.PaymentInitiationRequestAuthorization) argumentCaptor.getValue().getData().getAttributes();
        assertThat(authorizationRequest.getQueryParameters().get("code")).isEqualTo(AUTHORIZATION_CODE);
    }

    private PaymentInitiationRequestAuthorization expected() {
        return PaymentInitiationRequestAuthorization.builder()
                .id(AUTHORIZATION_ID)
                .status("succeeded")
                .links(PaymentInitiationRequestAuthorizationLinks.builder()
                        .nextRedirect("https://www.bnpp.com/next-step")
                        .build())
                .build();
    }
}
