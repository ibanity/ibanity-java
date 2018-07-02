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
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PaymentsInitiationServiceTest extends AbstractServiceTest {
    private PaymentsInitiationService paymentsInitiationService = new PaymentsInitiationServiceImpl();
    Random random = new Random();

    @BeforeEach
    public void beforeEach() throws Exception {
        initPublicAPIEnvironment();
    }

    @AfterEach
    public void afterEach() throws Exception {
        cleanPublicAPIEnvironment();
    }

    protected PaymentInitiationRequest initializePaymentInitiationRequest() {
        PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();
        paymentInitiationRequest.setRedirectUri(FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL);
        paymentInitiationRequest.setFinancialInstitutionId(financialInstitution.getId());
        paymentInitiationRequest.setConsentReference(UUID.randomUUID().toString());
        paymentInitiationRequest.setEndToEndId(UUID.randomUUID().toString());
        paymentInitiationRequest.setCustomerIp(InetAddresses.fromInteger(random.nextInt()).getHostAddress());
        paymentInitiationRequest.setCustomerAgent("Mozilla");
        paymentInitiationRequest.setProductType("sepa-credit-transfer");
        paymentInitiationRequest.setRemittanceInformationType("unstructured");
        paymentInitiationRequest.setCurrency("EUR");
        paymentInitiationRequest.setAmount(Double.valueOf(50.4));
        paymentInitiationRequest.setCreditorName("Fake Creditor Name");
        paymentInitiationRequest.setCreditorAccountReference("BE23947805459949");
        paymentInitiationRequest.setCreditorAccountReferenceType("IBAN");
        return paymentInitiationRequest;
    }

    protected PaymentInitiationRequest submitPaymentInitiationRequest(){
        return paymentsInitiationService.createForFinanciaInstitution(generatedCustomerAccessToken.getToken(), initializePaymentInitiationRequest());
    }

    @Test
    void createForFinanciaInstitution() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest();
        assertTrue(resultingPaymentInitiationRequest.getLinks().getRedirect() != null);
        assertTrue(!resultingPaymentInitiationRequest.getLinks().getRedirect().isEmpty());
    }

    @Test
    void createForFinanciaInstitutionWithIdempotencyKey() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = paymentsInitiationService.createForFinanciaInstitution(generatedCustomerAccessToken.getToken(), initializePaymentInitiationRequest(), UUID.randomUUID());
        assertTrue(resultingPaymentInitiationRequest.getLinks().getRedirect() != null);
        assertTrue(!resultingPaymentInitiationRequest.getLinks().getRedirect().isEmpty());
    }

    @Test
    void findPaymentInitiationRequest() throws Exception {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest();
        PaymentInitiationRequest paymentInitiationRequestReceived = paymentsInitiationService.find(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), resultingPaymentInitiationRequest.getId());
        assertTrue(paymentInitiationRequestReceived.getId().equals(resultingPaymentInitiationRequest.getId()));
    }

    @Test
    void findPaymentInitiationRequestWithWrongId() throws Exception {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest();
        try {
            paymentsInitiationService.find(generatedCustomerAccessToken.getToken(), UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count() == 1);
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