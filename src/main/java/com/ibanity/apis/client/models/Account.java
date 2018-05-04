package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.PagingBehavior;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@JsonApiResource(type = "account", resourcePath = "accounts", pagingBehavior = PagingBehavior.class)
public class Account extends AbstractModel {

    private String subType;
    private String currency;
    private String description;
    private String reference;
    private String referenceType;
    private Double currentBalance;
    private Double availableBalance;

    @JsonApiRelation(lookUp=LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL,serialize=SerializeType.ONLY_ID)
    private FinancialInstitution financialInstitution;

    public Account(UUID id, String subType, String currency, String description, String reference, String referenceType, Double currentBalance, Double availableBalance, UUID financialInstitutionId) {
        super(id);
        this.subType = subType;
        this.currency = currency;
        this.description = description;
        this.reference = reference;
        this.referenceType = referenceType;
        this.currentBalance = currentBalance;
        this.availableBalance = availableBalance;
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
    }

    public Account() {
        super();
        this.financialInstitution = new FinancialInstitution();
    }

    public Account(UUID id, UUID financialInstitutionId) {
        super(id);
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getSubType(), account.getSubType())
                .append(getCurrency(), account.getCurrency())
                .append(getDescription(), account.getDescription())
                .append(getReference(), account.getReference())
                .append(getReferenceType(), account.getReferenceType())
                .append(getCurrentBalance(), account.getCurrentBalance())
                .append(getAvailableBalance(), account.getAvailableBalance())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getSubType())
                .append(getCurrency())
                .append(getDescription())
                .append(getReference())
                .append(getReferenceType())
                .append(getCurrentBalance())
                .append(getAvailableBalance())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("availableBalance", availableBalance)
                .append("currency", currency)
                .append("currentBalance", currentBalance)
                .append("description", description)
                .append("financialInstitution", financialInstitution)
                .append("id", getId())
                .append("reference", reference)
                .append("referenceType", referenceType)
                .append("subType", subType)
                .toString();
    }

    public FinancialInstitution getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
    }
}
