package com.ibanity.apis.client.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

import static com.ibanity.apis.client.models.AccountInformationAccessAuthorization.RESOURCE_PATH;
import static com.ibanity.apis.client.models.AccountInformationAccessAuthorization.RESOURCE_TYPE;

@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class AccountInformationAccessAuthorization extends AbstractModel {

    public static final String RESOURCE_TYPE    = "account-information-access-authorizations";
    public static final String RESOURCE_PATH    = "authorizations";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    private String accountReference;
    private Instant revokedAt;

    @JsonApiRelation(lookUp = LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL, serialize = SerializeType.LAZY)
    private AccountInformationAccessRequest accountInformationAccessRequest = null;

    public AccountInformationAccessAuthorization() {
        super();
    }

    public AccountInformationAccessAuthorization(final UUID id, final String accountReference, final Instant revokedAt, final AccountInformationAccessRequest accountInformationAccessRequest) {
        super(id);
        this.accountReference = accountReference;
        this.revokedAt = revokedAt;
        setAccountInformationAccessRequest(accountInformationAccessRequest);
    }

    public AccountInformationAccessAuthorization(final UUID id, final AccountInformationAccessRequest accountInformationAccessRequest) {
        super(id);
        setAccountInformationAccessRequest(accountInformationAccessRequest);
    }

    public AccountInformationAccessRequest getAccountInformationAccessRequest() {
        return accountInformationAccessRequest;
    }

    public void setAccountInformationAccessRequest(final AccountInformationAccessRequest accountInformationAccessRequest) {
        this.accountInformationAccessRequest = accountInformationAccessRequest;
    }

    public String getAccountReference() {
        return accountReference;
    }

    public void setAccountReference(final String accountReference) {
        this.accountReference = accountReference;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(final Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AccountInformationAccessAuthorization)) {
            return false;
        }

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
