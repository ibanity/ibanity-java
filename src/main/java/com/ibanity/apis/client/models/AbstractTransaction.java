package com.ibanity.apis.client.models;

import com.ibanity.apis.client.annotations.InstantJsonFormat;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

public abstract class AbstractTransaction extends AbstractModel {

    private Double amount;
    private String currency;

    @InstantJsonFormat
    private Instant valueDate;

    @InstantJsonFormat
    private Instant executionDate;

    private String description;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String counterpartName;
    private String counterpartReference;

    public AbstractTransaction() {
    }

    public AbstractTransaction(final UUID id) {
        super(id);
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public Instant getValueDate() {
        return valueDate;
    }

    public void setValueDate(final Instant valueDate) {
        this.valueDate = valueDate;
    }

    public Instant getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(final Instant executionDate) {
        this.executionDate = executionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getRemittanceInformationType() {
        return remittanceInformationType;
    }

    public void setRemittanceInformationType(final String remittanceInformationType) {
        this.remittanceInformationType = remittanceInformationType;
    }

    public String getRemittanceInformation() {
        return remittanceInformation;
    }

    public void setRemittanceInformation(final String remittanceInformation) {
        this.remittanceInformation = remittanceInformation;
    }

    public String getCounterpartName() {
        return counterpartName;
    }

    public void setCounterpartName(final String counterpartName) {
        this.counterpartName = counterpartName;
    }

    public String getCounterpartReference() {
        return counterpartReference;
    }

    public void setCounterpartReference(final String counterpartReference) {
        this.counterpartReference = counterpartReference;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AbstractTransaction)) {
            return false;
        }

        AbstractTransaction that = (AbstractTransaction) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getAmount(), that.getAmount())
                .append(getCurrency(), that.getCurrency())
                .append(getValueDate(), that.getValueDate())
                .append(getExecutionDate(), that.getExecutionDate())
                .append(getDescription(), that.getDescription())
                .append(getRemittanceInformationType(), that.getRemittanceInformationType())
                .append(getRemittanceInformation(), that.getRemittanceInformation())
                .append(getCounterpartName(), that.getCounterpartName())
                .append(getCounterpartReference(), that.getCounterpartReference())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getAmount())
                .append(getCurrency())
                .append(getValueDate())
                .append(getExecutionDate())
                .append(getDescription())
                .append(getRemittanceInformationType())
                .append(getRemittanceInformation())
                .append(getCounterpartName())
                .append(getCounterpartReference())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("amount", amount)
                .append("counterpartName", counterpartName)
                .append("counterpartReference", counterpartReference)
                .append("currency", currency)
                .append("description", description)
                .append("executionDate", executionDate)
                .append("remittanceInformation", remittanceInformation)
                .append("remittanceInformationType", remittanceInformationType)
                .append("valueDate", valueDate)
                .toString();
    }
}
