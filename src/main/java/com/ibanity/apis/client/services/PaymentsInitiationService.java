package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.PaymentInitiationRequest;

import java.util.UUID;

public interface PaymentsInitiationService {

    PaymentInitiationRequest initiatePaymentRequest(CustomerAccessToken customerAccessToken, PaymentInitiationRequest paymentInitiationRequest);

    PaymentInitiationRequest getPaymentInitiationRequest(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID paymentInitiationRequestId) throws ResourceNotFoundException;
}
