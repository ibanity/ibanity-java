package com.ibanity.apis.client.services;

import com.google.common.net.InetAddresses;
import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.services.impl.PaymentsInitiationServiceImpl;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
        paymentInitiationRequest.setRedirectUri(FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL);
        paymentInitiationRequest.setFinancialInstitutionId(financialInstitution.getId());
        paymentInitiationRequest.setConsentReference(UUID.randomUUID().toString());
        paymentInitiationRequest.setEndToEndId(UUID.randomUUID().toString().replace("-",""));
        paymentInitiationRequest.setCustomerIp(InetAddresses.fromInteger(random.nextInt()).getHostAddress());
        paymentInitiationRequest.setCustomerAgent("Mozilla");
        paymentInitiationRequest.setProductType("sepa-credit-transfer");
        paymentInitiationRequest.setRemittanceInformationType("unstructured");
        paymentInitiationRequest.setCurrency("EUR");
        paymentInitiationRequest.setAmount(50.4);
        paymentInitiationRequest.setCreditorName("Fake Creditor Name");
        paymentInitiationRequest.setCreditorAccountReference("BE23947805459949");
        paymentInitiationRequest.setCreditorAccountReferenceType("IBAN");
        return paymentInitiationRequest;
    }

    protected PaymentInitiationRequest submitPaymentInitiationRequest(){
        return paymentsInitiationService.create(generatedCustomerAccessToken.getToken(), initializePaymentInitiationRequest());
    }

    @Test
    void createForFinanciaInstitution() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest();
        assertNotNull(resultingPaymentInitiationRequest.getLinks().getRedirect());
        assertTrue(!resultingPaymentInitiationRequest.getLinks().getRedirect().isEmpty());
    }

    @Test
    void createForFinanciaInstitutionWithIdempotencyKey() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = paymentsInitiationService.create(generatedCustomerAccessToken.getToken(), initializePaymentInitiationRequest(), UUID.randomUUID());
        assertNotNull(resultingPaymentInitiationRequest.getLinks().getRedirect(), "Missing 'redirect' in links");
        assertFalse(resultingPaymentInitiationRequest.getLinks().getRedirect().isEmpty(), "'redirect' is empty in links");
    }

    @Test
    void findPaymentInitiationRequest() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest();
        PaymentInitiationRequest paymentInitiationRequestReceived = paymentsInitiationService.find(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), resultingPaymentInitiationRequest.getId());
        assertEquals(paymentInitiationRequestReceived.getId(), resultingPaymentInitiationRequest.getId(), "PaymentInitiationRequestId is not the same");
    }

    @Test
    void findPaymentInitiationRequestWithWrongId() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest();
        try {
            paymentsInitiationService.find(generatedCustomerAccessToken.getToken(), UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
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