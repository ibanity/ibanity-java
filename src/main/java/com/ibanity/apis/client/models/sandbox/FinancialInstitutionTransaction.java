package com.ibanity.apis.client.models.sandbox;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibanity.apis.client.models.AbstractTransaction;
import com.ibanity.apis.client.paging.PagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

@JsonApiResource(type = "financialInstitutionTransaction", resourcePath = "financial-institution-transactions", pagingBehavior = PagingBehavior.class)
public class FinancialInstitutionTransaction extends AbstractTransaction {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "UTC")
    Instant createdAt;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "UTC")
    Instant updatedAt;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "UTC")
    Instant deletedAt;

    public FinancialInstitutionTransaction() {
        super();
    }

    public FinancialInstitutionTransaction(UUID id,  UUID financialInstitutionAccountId, UUID financialInstitutionId) {
        super(id, financialInstitutionAccountId,financialInstitutionId);
    }

    public FinancialInstitutionTransaction(UUID id, Double amount, String currency, Instant valueDate, Instant executionDate, String description, String remittanceInformationType, String remittanceInformation, String counterpartName, String counterpartReference, Instant createdAt, Instant updatedAt, Instant deletedAt) {
        super(id, amount, currency, valueDate, executionDate, description, remittanceInformationType, remittanceInformation, counterpartName, counterpartReference);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
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

        if (!(o instanceof FinancialInstitutionTransaction)) return false;

        FinancialInstitutionTransaction that = (FinancialInstitutionTransaction) o;

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
