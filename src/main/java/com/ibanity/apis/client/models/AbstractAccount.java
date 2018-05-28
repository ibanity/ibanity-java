package com.ibanity.apis.client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

public abstract class AbstractAccount extends AbstractModel{
    @JsonProperty("subtype")
    private String subType;
    private String currency;
    private String description;
    private String reference;
    @JsonProperty("reference_type")
    private String referenceType;
    private Double currentBalance;
    private Double availableBalance;

    @JsonApiRelation(lookUp=LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL,serialize=SerializeType.ONLY_ID)
    private FinancialInstitution financialInstitution;

    public AbstractAccount(UUID id, String subType, String currency, String description, String reference, String referenceType, Double currentBalance, Double availableBalance) {
        super(id);
        this.subType = subType;
        this.currency = currency;
        this.description = description;
        this.reference = reference;
        this.referenceType = referenceType;
        this.currentBalance = currentBalance;
        this.availableBalance = availableBalance;
    }

    public AbstractAccount() {
        this.financialInstitution = new FinancialInstitution();
    }

    public AbstractAccount(UUID id, UUID financialInstitutionId) {
        super(id);
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
    }

    public FinancialInstitution getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
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

        if (!(o instanceof AbstractAccount)) return false;

        AbstractAccount that = (AbstractAccount) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getSubType(), that.getSubType())
                .append(getCurrency(), that.getCurrency())
                .append(getDescription(), that.getDescription())
                .append(getReference(), that.getReference())
                .append(getReferenceType(), that.getReferenceType())
                .append(getCurrentBalance(), that.getCurrentBalance())
                .append(getAvailableBalance(), that.getAvailableBalance())
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
                .append(super.toString())
                .append("availableBalance", availableBalance)
                .append("currency", currency)
                .append("currentBalance", currentBalance)
                .append("description", description)
                .append("reference", reference)
                .append("referenceType", referenceType)
                .append("subType", subType)
                .toString();
    }
}
