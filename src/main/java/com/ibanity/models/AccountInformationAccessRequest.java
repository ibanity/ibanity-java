package com.ibanity.models;

import com.ibanity.models.links.AccountInformationAccessLinks;
import io.crnk.core.resource.annotations.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@JsonApiResource(type = "account-information-access-requests")
public class AccountInformationAccessRequest extends AbstractModel{
    private String redirectUri;
    private String consentReference;
    @JsonApiLinksInformation
    private AccountInformationAccessLinks links;

    @JsonApiRelation(lookUp= LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL,serialize= SerializeType.ONLY_ID)
    private FinancialInstitution financialInstitution = null;

    public AccountInformationAccessRequest() {
        super();
    }

    public AccountInformationAccessRequest(UUID id) {
        super(id);
        this.links = new AccountInformationAccessLinks();
    }

    public AccountInformationAccessRequest(UUID id, UUID financialInstitutionId) {
        super(id);
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
        this.links = new AccountInformationAccessLinks();
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getConsentReference() {
        return consentReference;
    }

    public void setConsentReference(String consentReference) {
        this.consentReference = consentReference;
    }

    public AccountInformationAccessLinks getLinks() {
        return links;
    }

    public void setLinks(AccountInformationAccessLinks links) {
        this.links = links;
    }

    public FinancialInstitution getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AccountInformationAccessRequest)) return false;

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
