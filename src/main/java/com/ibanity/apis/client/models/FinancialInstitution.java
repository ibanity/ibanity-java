package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@JsonApiResource(type = FinancialInstitution.RESOURCE_TYPE, resourcePath = "financial-institutions", pagingBehavior = IbanityPagingBehavior.class)
public class FinancialInstitution extends AbstractModel {

    public static final String RESOURCE_TYPE = "financialInstitution";
    private Boolean sandbox;
    private String name;

    public FinancialInstitution(final UUID id) {
        super(id);
    }

    public FinancialInstitution(final UUID id, final Boolean sandbox, final String name) {
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

    public void setSandbox(final Boolean sandbox) {
        this.sandbox = sandbox;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FinancialInstitution)) {
            return false;
        }

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
