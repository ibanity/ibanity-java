package com.ibanity.apis.client.sandbox.models;

import com.ibanity.apis.client.annotations.InstantJsonFormat;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.AbstractModel;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;

import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser.RESOURCE_PATH;
import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser.RESOURCE_TYPE;

@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class FinancialInstitutionUser extends AbstractModel {

    public static final String RESOURCE_TYPE    = "financialInstitutionUser";
    public static final String RESOURCE_PATH    = "financial-institution-users";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    @InstantJsonFormat
    private Instant createdAt = null;

    @InstantJsonFormat
    private Instant updatedAt = null;

    @InstantJsonFormat
    private Instant deletedAt = null;

    private String password;
    private String firstName;
    private String lastName;
    private String login;

    public FinancialInstitutionUser(final String password, final String firstName, final String lastName, final String login) {
        super();
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;

    }

    public FinancialInstitutionUser() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FinancialInstitutionUser)) {
            return false;
        }

        FinancialInstitutionUser that = (FinancialInstitutionUser) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getPassword(), that.getPassword())
                .append(getFirstName(), that.getFirstName())
                .append(getLastName(), that.getLastName())
                .append(getLogin(), that.getLogin())
                .append(getCreatedAt(), that.getCreatedAt())
                .append(getUpdatedAt(), that.getUpdatedAt())
                .append(getDeletedAt(), that.getDeletedAt())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getPassword())
                .append(getFirstName())
                .append(getLastName())
                .append(getLogin())
                .append(getCreatedAt())
                .append(getUpdatedAt())
                .append(getDeletedAt())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("login", login)
                .append("password", password)
                .append("createdAt", createdAt)
                .append("deletedAt", deletedAt)
                .append("updatedAt", updatedAt)
                .toString();
    }
}
