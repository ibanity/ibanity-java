package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;

import java.util.UUID;

public interface PaymentsInitiationService {

    PaymentInitiationRequest create(String customerAccessToken, FinancialInstitution financialInstitution,
                                    PaymentInitiationRequest paymentInitiationRequest);

    PaymentInitiationRequest create(String customerAccessToken, FinancialInstitution financialInstitution,
                                    PaymentInitiationRequest paymentInitiationRequest, UUID idempotencyKey);

    PaymentInitiationRequest find(String customerAccessToken, UUID financialInstitutionId, UUID paymentInitiationRequestId);
}
