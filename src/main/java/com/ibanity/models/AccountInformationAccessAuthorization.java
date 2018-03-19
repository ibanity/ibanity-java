package com.ibanity.models;

import io.crnk.core.resource.annotations.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

@JsonApiResource(type = "account-information-access-authorizations")
public class AccountInformationAccessAuthorization extends AbstractModel{
    private String accountReference;
    private Instant revokedAt;

    @JsonApiRelation(lookUp = LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL, repositoryBehavior = RelationshipRepositoryBehavior.DEFAULT, serialize = SerializeType.ONLY_ID)
    private AccountInformationAccessRequest accountInformationAccessRequest = null;

    public AccountInformationAccessAuthorization() {
        super();
    }

    public AccountInformationAccessAuthorization(UUID id, String accountReference, Instant revokedAt, AccountInformationAccessRequest accountInformationAccessRequest) {
        super(id);
        this.accountReference = accountReference;
        this.revokedAt = revokedAt;
        setAccountInformationAccessRequest(accountInformationAccessRequest);
    }

    public AccountInformationAccessAuthorization(UUID id, AccountInformationAccessRequest accountInformationAccessRequest) {
        super(id);
        setAccountInformationAccessRequest(accountInformationAccessRequest);
    }

    public AccountInformationAccessRequest getAccountInformationAccessRequest() {
        return accountInformationAccessRequest;
    }

    public void setAccountInformationAccessRequest(AccountInformationAccessRequest accountInformationAccessRequest) {
        this.accountInformationAccessRequest = accountInformationAccessRequest;
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
                .append("accountInformationAccessRequest", accountInformationAccessRequest)
                .append("accountReference", accountReference)
                .append("id", getId())
                .append("revokedAt", revokedAt)
                .toString();
    }
}
