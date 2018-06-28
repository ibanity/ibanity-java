package com.ibanity.apis.client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static com.ibanity.apis.client.models.CustomerAccessToken.RESOURCE_PATH;
import static com.ibanity.apis.client.models.CustomerAccessToken.RESOURCE_TYPE;

@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class CustomerAccessToken extends AbstractModel {

    public static final String RESOURCE_TYPE    = "customerAccessToken";
    public static final String RESOURCE_PATH    = "customer-access-tokens";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    private String token;

    @JsonProperty("applicationCustomerReference")
    private String applicationCustomerReference;

    public CustomerAccessToken(final UUID id) {
        super(id);
    }

    public CustomerAccessToken() {
        super();
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getApplicationCustomerReference() {
        return applicationCustomerReference;
    }

    public void setApplicationCustomerReference(final String applicationCustomerReference) {
        this.applicationCustomerReference = applicationCustomerReference;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CustomerAccessToken)) {
            return false;
        }

        CustomerAccessToken that = (CustomerAccessToken) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getToken(), that.getToken())
                .append(getApplicationCustomerReference(), that.getApplicationCustomerReference())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getToken())
                .append(getApplicationCustomerReference())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("token", token)
                .append("applicationCustomerReference", applicationCustomerReference)
                .append("id", getId())
                .toString();
    }
}
