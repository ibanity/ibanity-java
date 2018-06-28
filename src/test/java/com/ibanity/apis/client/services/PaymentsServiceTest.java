package com.ibanity.apis.client.services;

import com.google.common.net.InetAddresses;
import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.services.impl.PaymentsInitiationServiceImpl;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentsServiceTest extends AbstractServiceTest {
    private PaymentsInitiationService paymentsService = new PaymentsInitiationServiceImpl();
    Random random = new Random();

    @BeforeEach
    public void beforeEach() throws Exception {
        initPublicAPIEnvironment();
    }

    @AfterEach
    public void afterEach() throws Exception {
        cleanPublicAPIEnvironment();
    }

    protected PaymentInitiationRequest submitPaymentInitiationRequest(){
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
        return paymentsService.initiatePaymentRequest(generatedCustomerAccessToken, paymentInitiationRequest);
    }

    @Test
    void initiatePaymentRequest() {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest();
        assertTrue(resultingPaymentInitiationRequest.getLinks().getRedirect() != null);
        assertTrue(!resultingPaymentInitiationRequest.getLinks().getRedirect().isEmpty());
    }

    @Test
    void getPaymentInitiationRequest() throws Exception {
        PaymentInitiationRequest resultingPaymentInitiationRequest = submitPaymentInitiationRequest();
        paymentsService.getPaymentInitiationRequest(generatedCustomerAccessToken,financialInstitution.getId(),resultingPaymentInitiationRequest.getId());
    }

    // Uncomment when PL-452 has been fixed
//    @Test
//    void getPaymentInitiationRequestWithWrongPaymentRequestID() throws Exception {
//        try {
//            paymentsService.getPaymentInitiationRequest(generatedCustomerAccessToken,financialInstitution.getId(),UUID.randomUUID());
//        } catch (IbanityException ibanityException) {
//            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
//            assertTrue(ibanityException.getMessage().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND));
//            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
//            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
//        }
//    }

    @Test
    void getPaymentInitiationRequestWithWrongFinancialInstitionId() throws Exception {
        try {
            paymentsService.getPaymentInitiationRequest(generatedCustomerAccessToken,UUID.randomUUID(),UUID.randomUUID());
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }
}