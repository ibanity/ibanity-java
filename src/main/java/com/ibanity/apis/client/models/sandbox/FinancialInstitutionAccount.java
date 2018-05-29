package com.ibanity.apis.client.models.sandbox;

import com.ibanity.apis.client.models.AbstractAccount;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.PagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

@JsonApiResource(type = "financialInstitutionAccount", resourcePath = "financial-institution-accounts", pagingBehavior = PagingBehavior.class)
public class FinancialInstitutionAccount extends AbstractAccount {

    Instant createdAt;
    Instant updatedAt;
    Instant deletedAt;

    public FinancialInstitutionAccount() {
        super();
    }

    public FinancialInstitutionAccount(UUID id, UUID financialInstitutionId) {
        super(id, financialInstitutionId);
    }

    public FinancialInstitutionAccount(UUID id, String subType, String currency, String description, String reference, String referenceType, Double currentBalance, Double availableBalance, String subType1, String currency1, String description1, String reference1, String referenceType1, Double currentBalance1, Double availableBalance1, FinancialInstitution financialInstitution) {
        super(id, subType, currency, description, reference, referenceType, currentBalance, availableBalance);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("createdAt", createdAt)
                .append("deletedAt", deletedAt)
                .append("updatedAt", updatedAt)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof FinancialInstitutionAccount)) return false;

        FinancialInstitutionAccount that = (FinancialInstitutionAccount) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getCreatedAt(), that.getCreatedAt())
                .append(getUpdatedAt(), that.getUpdatedAt())
                .append(getDeletedAt(), that.getDeletedAt())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getCreatedAt())
                .append(getUpdatedAt())
                .append(getDeletedAt())
                .toHashCode();
    }
}
