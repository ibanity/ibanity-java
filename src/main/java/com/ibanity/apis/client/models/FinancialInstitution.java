package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.IBanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@JsonApiResource(type = FinancialInstitution.RESOURCE_TYPE, resourcePath = "financial-institutions", pagingBehavior = IBanityPagingBehavior.class)
public class FinancialInstitution extends AbstractModel {

    public static final String RESOURCE_TYPE = "financialInstitution";
    private Boolean sandbox;
    private String name;

    public FinancialInstitution(UUID id) {
        super(id);
    }

    public FinancialInstitution(UUID id, Boolean sandbox, String name) {
        super(id);
        this.sandbox = sandbox;
        this.name = name;
    }

    public FinancialInstitution() {
        super();
    }

    public Boolean getSandbox() {
        return sandbox;
    }

    public void setSandbox(Boolean sandbox) {
        this.sandbox = sandbox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof FinancialInstitution)) return false;

        FinancialInstitution that = (FinancialInstitution) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getSandbox(), that.getSandbox())
                .append(getName(), that.getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getSandbox())
                .append(getName())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sandbox", sandbox)
                .append("name", name)
                .append("id", getId())
                .toString();
    }
}
