package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.PaymentInitiationRequest;

import java.util.UUID;

public interface PaymentsInitiationService {

    PaymentInitiationRequest createForFinanciaInstitution(String customerAccessToken, PaymentInitiationRequest paymentInitiationRequest);

    PaymentInitiationRequest createForFinanciaInstitution(String customerAccessToken, PaymentInitiationRequest paymentInitiationRequest, UUID idempotencyKey);

    PaymentInitiationRequest find(String customerAccessToken, UUID financialInstitutionId, UUID paymentInitiationRequestId) throws ApiErrorsException;
}
