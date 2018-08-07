package com.ibanity.apis.client.sandbox.models.factory.update;

import com.ibanity.apis.client.models.FinancialInstitution;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionUpdateQuery {
    private UUID financialInstitutionId;
    private String name;

    private UUID idempotencyKey;

    public static FinancialInstitutionUpdateQueryBuilder from(final FinancialInstitution financialInstitution) {
        Objects.requireNonNull(financialInstitution, "Missing required 'financialInstitution'");

        return new FinancialInstitutionUpdateQueryBuilder()
                .financialInstitutionId(financialInstitution.getId())
                .name(financialInstitution.getName());
    }
}
