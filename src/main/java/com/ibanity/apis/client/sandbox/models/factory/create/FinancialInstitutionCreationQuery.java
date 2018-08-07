package com.ibanity.apis.client.sandbox.models.factory.create;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionCreationQuery {
    private String name;

    private UUID idempotencyKey;
}
