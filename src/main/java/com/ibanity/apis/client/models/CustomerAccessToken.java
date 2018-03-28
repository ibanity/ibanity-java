package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.PagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@JsonApiResource(type = "customer-access-tokens", pagingBehavior = PagingBehavior.class)
public class CustomerAccessToken extends AbstractModel{
    private String token;
    private String application_customer_reference;

    public CustomerAccessToken(UUID id) {
        super(id);
    }

    public CustomerAccessToken(String application_customer_reference) {
        super();
        this.application_customer_reference = application_customer_reference;
    }

    public CustomerAccessToken() {
        super();
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApplication_customer_reference() {
        return application_customer_reference;
    }

    public void setApplication_customer_reference(String application_customer_reference) {
        this.application_customer_reference = application_customer_reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CustomerAccessToken)) return false;

        CustomerAccessToken that = (CustomerAccessToken) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getToken(), that.getToken())
                .append(getApplication_customer_reference(), that.getApplication_customer_reference())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getToken())
                .append(getApplication_customer_reference())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("token", token)
                .append("application_customer_reference", application_customer_reference)
                .append("id", getId())
                .toString();
    }
}
