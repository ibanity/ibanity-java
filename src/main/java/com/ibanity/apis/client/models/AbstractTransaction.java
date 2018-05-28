package com.ibanity.apis.client.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionAccount;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

public abstract class AbstractTransaction extends AbstractModel{

    private Double amount;
    private String currency;

    @JsonProperty("value_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "UTC")
    private Instant valueDate;
    @JsonProperty("execution_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "UTC")
    private Instant executionDate;

    private String description;

    @JsonProperty("remittance_information_type")
    private String remittanceInformationType;
    @JsonProperty("remittance_information")

    private String remittanceInformation;

    @JsonProperty("counterpart_name")
    private String counterpartName;

    @JsonProperty("counterpart_reference")
    private String counterpartReference;


    @JsonApiRelation(lookUp= LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL,serialize= SerializeType.ONLY_ID)
    private FinancialInstitutionAccount account;

    public AbstractTransaction(UUID id, Double amount, String currency, Instant valueDate, Instant executionDate, String description, String remittanceInformationType, String remittanceInformation, String counterpartName, String counterpartReference) {
        super(id);
        this.amount = amount;
        this.currency = currency;
        this.valueDate = valueDate;
        this.executionDate = executionDate;
        this.description = description;
        this.remittanceInformationType = remittanceInformationType;
        this.remittanceInformation = remittanceInformation;
        this.counterpartName = counterpartName;
        this.counterpartReference = counterpartReference;
        this.account = new FinancialInstitutionAccount();
    }

    public AbstractTransaction(UUID id,  UUID financialInstitutionAccountId, UUID financialInstitutionId ) {
        super(id);
        this.account = new FinancialInstitutionAccount(financialInstitutionAccountId, financialInstitutionId);
    }

    public AbstractTransaction() {
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getValueDate() {
        return valueDate;
    }

    public void setValueDate(Instant valueDate) {
        this.valueDate = valueDate;
    }

    public Instant getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Instant executionDate) {
        this.executionDate = executionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemittanceInformationType() {
        return remittanceInformationType;
    }

    public void setRemittanceInformationType(String remittanceInformationType) {
        this.remittanceInformationType = remittanceInformationType;
    }

    public String getRemittanceInformation() {
        return remittanceInformation;
    }

    public void setRemittanceInformation(String remittanceInformation) {
        this.remittanceInformation = remittanceInformation;
    }

    public String getCounterpartName() {
        return counterpartName;
    }

    public void setCounterpartName(String counterpartName) {
        this.counterpartName = counterpartName;
    }

    public String getCounterpartReference() {
        return counterpartReference;
    }

    public void setCounterpartReference(String counterpartReference) {
        this.counterpartReference = counterpartReference;
    }

    public FinancialInstitutionAccount getAccount() {
        return account;
    }

    public void setAccount(FinancialInstitutionAccount account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AbstractTransaction)) return false;

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
                .append(getAccount(), that.getAccount())
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
                .append(getAccount())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("account", account)
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
