package com.ibanity.apis.client.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static com.ibanity.apis.client.models.FinancialInstitution.RESOURCE_PATH;

@JsonApiResource(type = FinancialInstitution.RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class FinancialInstitution extends AbstractModel {

    public static final String RESOURCE_TYPE    = "financialInstitution";
    public static final String RESOURCE_PATH    = "financial-institutions";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    private boolean sandbox;
    private String name;

    public FinancialInstitution(final UUID id) {
        super(id);
    }

    public FinancialInstitution(final UUID id, final boolean sandbox, final String name) {
        super(id);
        this.sandbox = sandbox;
        this.name = name;
    }

    public FinancialInstitution() {
        super();
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(final boolean sandbox) {
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
                .append(isSandbox(), that.isSandbox())
                .append(getName(), that.getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(isSandbox())
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
