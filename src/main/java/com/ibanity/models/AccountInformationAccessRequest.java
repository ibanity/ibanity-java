package com.ibanity.models;

import com.ibanity.models.links.AccountInformationAccessLinks;
import io.crnk.core.resource.annotations.JsonApiLinksInformation;
import io.crnk.core.resource.annotations.JsonApiResource;
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

    public AccountInformationAccessRequest() {
        super();
    }

    public AccountInformationAccessRequest(UUID id) {
        super(id);
        this.links = new AccountInformationAccessLinks();
    }

    public AccountInformationAccessRequest(UUID id, String redirectUri, String consentReference) {
        super(id);
        this.redirectUri = redirectUri;
        this.consentReference = consentReference;
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
                .append("id", getId())
                .append("links", links)
                .append("redirectUri", redirectUri)
                .toString();
    }
}
