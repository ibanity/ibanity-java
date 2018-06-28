package com.ibanity.apis.client.sandbox.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.AbstractAccount;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import com.ibanity.apis.client.sandbox.annotations.InstantFormatAnnotation;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount.RESOURCE_PATH;
import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount.RESOURCE_TYPE;

@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class FinancialInstitutionAccount extends AbstractAccount {

    public static final String RESOURCE_TYPE    = "financialInstitutionAccount";
    public static final String RESOURCE_PATH    = "financial-institution-accounts";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    @InstantFormatAnnotation
    private Instant createdAt = null;
    @InstantFormatAnnotation
    private Instant updatedAt = null;
    @InstantFormatAnnotation
    private Instant deletedAt = null;

    public FinancialInstitutionAccount(final UUID id) {
        super(id);
    }

    public FinancialInstitutionAccount() {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("createdAt", getCreatedAt())
                .append("deletedAt", getDeletedAt())
                .append("updatedAt", getUpdatedAt())
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FinancialInstitutionAccount)) {
            return false;
        }

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
