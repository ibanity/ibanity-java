package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.PaymentInitiationRequest;

import java.util.UUID;

public interface PaymentsInitiationService {

    PaymentInitiationRequest create(String customerAccessToken, PaymentInitiationRequest paymentInitiationRequest);

    PaymentInitiationRequest create(String customerAccessToken, PaymentInitiationRequest paymentInitiationRequest, UUID idempotencyKey);

    PaymentInitiationRequest find(String customerAccessToken, UUID financialInstitutionId, UUID paymentInitiationRequestId);
}
