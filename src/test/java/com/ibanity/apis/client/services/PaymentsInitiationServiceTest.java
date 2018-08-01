package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.services.impl.PaymentsInitiationServiceImpl;
import org.apache.http.HttpStatus;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentsInitiationServiceTest extends AbstractServiceTest {
    private final PaymentsInitiationService paymentsInitiationService = new PaymentsInitiationServiceImpl();
    final Random random = new Random();

    @BeforeEach
    public void beforeEach() {
        initPublicAPIEnvironment();
    }

    @AfterEach
    public void afterEach() {
        cleanPublicAPIEnvironment();
    }

    protected PaymentInitiationRequest initializePaymentInitiationRequest() {
        PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();

        paymentInitiationRequest.setRedirectUri(fakeTppPaymentInitiationRedirectUrl);
        paymentInitiationRequest.setConsentReference(UUID.randomUUID().toString());
        paymentInitiationRequest.setEndToEndId(UUID.randomUUID().toString().replace("-",""));
        paymentInitiationRequest.setProductType("sepa-credit-transfer");

        paymentInitiationRequest.setRemittanceInformationType("unstructured");
        paymentInitiationRequest.setRemittanceInformation("Payment initiation test");

        paymentInitiationRequest.setAmount(50.4);
        paymentInitiationRequest.setCurrency("EUR");

        paymentInitiationRequest.setCreditorName("Fake Creditor Name");
        paymentInitiationRequest.setCreditorAccountReference(Iban.random(CountryCode.BE).toString());
        paymentInitiationRequest.setCreditorAccountReferenceType("IBAN");

        return paymentInitiationRequest;
    }

    private PaymentInitiationRequest submitPaymentInitiationRequest(FinancialInstitution financialInstitution){
        Objects.requireNonNull(financialInstitution, "Financial Institution cannot be null");

        return paymentsInitiationService.create(
                generatedCustomerAccessToken.getToken(), financialInstitution, initializePaymentInitiationRequest());
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
                paymentsInitiationService.create(generatedCustomerAccessToken.getToken(), this.financialInstitution,
                        initializePaymentInitiationRequest(), UUID.randomUUID());
        assertNotNull(resultingPaymentInitiationRequest.getLinks().getRedirect(), "Missing 'redirect' attribute in 'links'");
        assertFalse(resultingPaymentInitiationRequest.getLinks().getRedirect().isEmpty(), "'redirect' attribute is empty in 'links'");
    }

    @Test
    void findPaymentInitiationRequest() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest(this.financialInstitution);
        PaymentInitiationRequest paymentInitiationRequestReceived = paymentsInitiationService.find(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), resultingPaymentInitiationRequest.getId());
        assertEquals(paymentInitiationRequestReceived.getId(), resultingPaymentInitiationRequest.getId(), "PaymentInitiationRequestId is not the same");
    }

    @Test
    void findPaymentInitiationRequestWithWrongId() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest(this.financialInstitution);
        try {
            paymentsInitiationService.find(generatedCustomerAccessToken.getToken(), UUID.randomUUID(), UUID.randomUUID());
            fail("Expected paymentsInitiationService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }

    // Uncomment when PL-452 has been fixed
//    @Test
//    void getPaymentInitiationRequestWithWrongPaymentRequestID() throws Exception {
//        try {
//            paymentsInitiationService.getPaymentInitiationRequest(generatedCustomerAccessToken,financialInstitution.getId(),UUID.randomUUID());
//        } catch (apiErrorsException apiErrorsException) {
//            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
//            assertTrue(apiErrorsException.getMessage().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND));
//            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
//            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
//        }
//    }
}