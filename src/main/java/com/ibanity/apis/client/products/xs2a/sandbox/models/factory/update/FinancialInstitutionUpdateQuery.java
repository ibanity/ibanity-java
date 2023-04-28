package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update;

import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionUpdateQuery {
    private UUID financialInstitutionId;

    private String name;
    private String bic;
    private String logoUrl;
    private String country;
    private boolean financialInstitutionCustomerReferenceRequired;
    private String sharedBrandReference;
    private String sharedBrandName;
    private boolean pendingTransactionsAvailable;
    private String timeZone;

    public static FinancialInstitutionUpdateQueryBuilder from(final FinancialInstitution financialInstitution) {
        Objects.requireNonNull(financialInstitution, "Missing required 'financialInstitution'");

        return new FinancialInstitutionUpdateQueryBuilder()
                .financialInstitutionId(financialInstitution.getId())
                .name(financialInstitution.getName())
                .pendingTransactionsAvailable(financialInstitution.isPendingTransactionsAvailable());
    }

    public static class FinancialInstitutionUpdateQueryBuilder {}
}
