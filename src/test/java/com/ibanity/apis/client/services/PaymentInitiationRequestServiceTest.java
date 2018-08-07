package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.factory.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.models.factory.read.PaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.services.impl.PaymentInitiationRequestServiceImpl;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentInitiationRequestServiceTest extends AbstractServiceTest {
    private final PaymentInitiationRequestService paymentInitiationRequestService = new PaymentInitiationRequestServiceImpl();
    final Random random = new Random();

    @BeforeEach
    public void beforeEach() {
        initPublicAPIEnvironment();
    }

    @AfterEach
    public void afterEach() {
        cleanPublicAPIEnvironment();
    }

    protected PaymentInitiationRequestCreationQuery initializePaymentInitiationRequest(
            String customerAccessToken, FinancialInstitution financialInstitution
    ) {
        return this.initializePaymentInitiationRequest(customerAccessToken, financialInstitution, null);
    }

    protected PaymentInitiationRequestCreationQuery initializePaymentInitiationRequest(
            String customerAccessToken, FinancialInstitution financialInstitution, UUID idempotencyKey
    ) {
        return PaymentInitiationRequestCreationQuery.builder()
                .redirectUri(fakeTppPaymentInitiationRedirectUrl)
                .consentReference(UUID.randomUUID().toString())
                .endToEndId(UUID.randomUUID().toString().replace("-",""))
                .productType("sepa-credit-transfer")
                .remittanceInformationType("unstructured")
                .remittanceInformation("Payment initiation test")
                .amount(50.4)
                .currency("EUR")
                .creditorName("Fake Creditor Name")
                .creditorAccountReference(Iban.random(CountryCode.BE).toString())
                .creditorAccountReferenceType("IBAN")
                .customerAccessToken(customerAccessToken)
                .financialInstitutionId(financialInstitution.getId())
                .idempotencyKey(idempotencyKey)
                .build();
    }

    private PaymentInitiationRequest submitPaymentInitiationRequest(FinancialInstitution financialInstitution) {
        Objects.requireNonNull(financialInstitution, "Financial Institution cannot be null");

        return paymentInitiationRequestService.create(
                initializePaymentInitiationRequest(generatedCustomerAccessToken.getToken(), financialInstitution));
    }

    @Test
    void createForFinanciaInstitution() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest(this.financialInstitution);
        assertNotNull(resultingPaymentInitiationRequest.getLinks().getRedirect());
        assertTrue(!resultingPaymentInitiationRequest.getLinks().getRedirect().isEmpty());
    }

    @Test
    void createForFinanciaInstitutionWithIdempotencyKey() {
        PaymentInitiationRequest resultingPaymentInitiationRequest =
                paymentInitiationRequestService.create(
                        initializePaymentInitiationRequest(generatedCustomerAccessToken.getToken(), this.financialInstitution, UUID.randomUUID()));
        assertNotNull(resultingPaymentInitiationRequest.getLinks().getRedirect(), "Missing 'redirect' attribute in 'links'");
        assertFalse(resultingPaymentInitiationRequest.getLinks().getRedirect().isEmpty(), "'redirect' attribute is empty in 'links'");
    }

    @Test
    void findPaymentInitiationRequest() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest(this.financialInstitution);

        PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery =
                PaymentInitiationRequestReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .paymentInitiationRequestId(resultingPaymentInitiationRequest.getId())
                .build();

        PaymentInitiationRequest paymentInitiationRequestReceived =
                paymentInitiationRequestService.find(paymentInitiationRequestReadQuery);

        assertEquals(paymentInitiationRequestReceived.getId(), resultingPaymentInitiationRequest.getId(), "PaymentInitiationRequestId is not the same");
    }

    @Test
    void findPaymentInitiationRequestWithWrongId() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest(this.financialInstitution);

        PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery =
                PaymentInitiationRequestReadQuery.builder()
                        .customerAccessToken(generatedCustomerAccessToken.getToken())
                        .financialInstitutionId(UUID.randomUUID())
                        .paymentInitiationRequestId(UUID.randomUUID())
                        .build();

        try {
            paymentInitiationRequestService.find(paymentInitiationRequestReadQuery);
            fail("Expected paymentInitiationRequestService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }

    // Uncomment when PL-452 has been fixed
//    @Test
//    void getPaymentInitiationRequestWithWrongPaymentRequestID() throws Exception {
//        try {
//            paymentInitiationRequestService.getPaymentInitiationRequest(generatedCustomerAccessToken,financialInstitution.getId(),UUID.randomUUID());
//        } catch (apiErrorsException apiErrorsException) {
//            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
//            assertTrue(apiErrorsException.getMessage().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND));
//            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
//            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
//        }
//    }
}