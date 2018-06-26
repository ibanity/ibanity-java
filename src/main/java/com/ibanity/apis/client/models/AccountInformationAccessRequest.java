package com.ibanity.apis.client.models;

import com.ibanity.apis.client.models.links.AccountInformationAccessLinks;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiLinksInformation;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@JsonApiResource(type = "accountInformationAccessRequest", resourcePath = "account-information-access-requests", pagingBehavior = IbanityPagingBehavior.class)
public class AccountInformationAccessRequest extends AbstractModel {
    private String redirectUri;
    private String consentReference;
    @JsonApiLinksInformation
    private AccountInformationAccessLinks links;

    @JsonApiRelation(lookUp = LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL, serialize = SerializeType.ONLY_ID)
    private FinancialInstitution financialInstitution = null;

    public AccountInformationAccessRequest() {
        super();
    }

    public AccountInformationAccessRequest(final UUID id) {
        super(id);
        this.links = new AccountInformationAccessLinks();
    }

    public AccountInformationAccessRequest(final UUID id, final UUID financialInstitutionId) {
        super(id);
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
        this.links = new AccountInformationAccessLinks();
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(final String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getConsentReference() {
        return consentReference;
    }

    public void setConsentReference(final String consentReference) {
        this.consentReference = consentReference;
    }

    public AccountInformationAccessLinks getLinks() {
        return links;
    }

    public void setLinks(final AccountInformationAccessLinks links) {
        this.links = links;
    }

    public FinancialInstitution getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(final FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public void setFinancialInstitutionId(final UUID financialInstitutionId) {
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AccountInformationAccessRequest)) {
            return false;
        }

        AccountInformationAccessRequest that = (AccountInformationAccessRequest) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getRedirectUri(), that.getRedirectUri())
                .append(getConsentReference(), that.getConsentReference())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getRedirectUri())
                .append(getConsentReference())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("consentReference", consentReference)
                .append("financialInstitution", financialInstitution)
                .append("id", getId())
                .append("links", links)
                .append("redirectUri", redirectUri)
                .toString();
    }

}
