package com.ibanity.apis.client.sandbox.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.AbstractTransaction;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import com.ibanity.apis.client.sandbox.annotations.InstantFormatAnnotation;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction.RESOURCE_PATH;
import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction.RESOURCE_TYPE;

@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class FinancialInstitutionTransaction extends AbstractTransaction {

    public static final String RESOURCE_TYPE    = "financialInstitutionTransaction";
    public static final String RESOURCE_PATH    = "financial-institution-transactions";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    @JsonApiRelation(lookUp = LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL, serialize = SerializeType.ONLY_ID)
    private FinancialInstitutionAccount financialInstitutionAccount;

    @InstantFormatAnnotation
    private Instant createdAt = null;
    @InstantFormatAnnotation
    private Instant updatedAt = null;
    @InstantFormatAnnotation
    private Instant deletedAt = null;

    public FinancialInstitutionTransaction(final UUID id) {
        super(id);
    }

    public FinancialInstitutionTransaction() {
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public FinancialInstitutionAccount getFinancialInstitutionAccount() {
        return financialInstitutionAccount;
    }

    public void setFinancialInstitutionAccount(final FinancialInstitutionAccount financialInstitutionAccount) {
        this.financialInstitutionAccount = financialInstitutionAccount;
    }

    public void setFinancialInstitutionAccountId(final UUID financialInstitutionAccountId) {
        this.financialInstitutionAccount = new FinancialInstitutionAccount(financialInstitutionAccountId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FinancialInstitutionTransaction)) {
            return false;
        }

        FinancialInstitutionTransaction that = (FinancialInstitutionTransaction) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getCreatedAt(), that.getCreatedAt())
                .append(getUpdatedAt(), that.getUpdatedAt())
                .append(getDeletedAt(), that.getDeletedAt())
                .append(getFinancialInstitutionAccount(), that.getFinancialInstitutionAccount())
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
                .append(getFinancialInstitutionAccount())
                .append(getCreatedAt())
                .append(getUpdatedAt())
                .append(getDeletedAt())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("financialInstitutionAccount", financialInstitutionAccount)
                .append("createdAt", getCreatedAt())
                .append("deletedAt", getDeletedAt())
                .append("updatedAt", getUpdatedAt())
                .toString();
    }
}
