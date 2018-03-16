package com.ibanity.models;

import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

@JsonApiResource(type = "account-information-access-authorizations")
public class AccountInformationAccessAuthorization extends AbstractModel{
    private String accountReference;
    private Instant revokedAt;

    public AccountInformationAccessAuthorization() {
    }

    public AccountInformationAccessAuthorization(UUID id, String accountReference, Instant revokedAt) {
        super(id);
        this.accountReference = accountReference;
        this.revokedAt = revokedAt;
    }

    public AccountInformationAccessAuthorization(UUID id) {
        super(id);
    }

    public String getAccountReference() {
        return accountReference;
    }

    public void setAccountReference(String accountReference) {
        this.accountReference = accountReference;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AccountInformationAccessAuthorization)) return false;

        AccountInformationAccessAuthorization that = (AccountInformationAccessAuthorization) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getAccountReference(), that.getAccountReference())
                .append(getRevokedAt(), that.getRevokedAt())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getAccountReference())
                .append(getRevokedAt())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accountReference", accountReference)
                .append("id", getId())
                .append("revokedAt", revokedAt)
                .toString();
    }
}
