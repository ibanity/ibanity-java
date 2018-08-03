package com.ibanity.apis.client.models.factory.read;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PaymentInitiationRequestReadQuery {
    private String customerAccessToken;
    private UUID financialInstitutionId;
    private UUID paymentInitiationRequestId;
}
