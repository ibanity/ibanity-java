package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.PagingBehavior;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

@JsonApiResource(type = "transactions", pagingBehavior = PagingBehavior.class)
public class Transaction extends AbstractModel{
    private Double amount;
    private String currency;

    private Instant valueDate;
    private Instant executionDate;
    private String description;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String counterpartName;
    private String counterpartReference;

    @JsonApiRelation(lookUp= LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL,serialize= SerializeType.ONLY_ID)
    private Account account;

    public Transaction(UUID id, Double amount, String currency, Instant valueDate, Instant executionDate, String description, String remittanceInformationType, String remittanceInformation, String counterpartName, String counterpartReference, UUID accountId, UUID financialInstitutionId) {
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
        this.account = new Account(accountId, financialInstitutionId);
    }

    public Transaction() {
        super();
        this.account = new Account();
    }

    public Transaction(UUID id, UUID accountId, UUID financialInstitutionId) {
        super(id);
        this.account = new Account(accountId, financialInstitutionId);
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

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
                .append("account", account)
                .append("amount", amount)
                .append("counterpartName", counterpartName)
                .append("counterpartReference", counterpartReference)
                .append("currency", currency)
                .append("description", description)
                .append("executionDate", executionDate)
                .append("id", getId())
                .append("remittanceInformation", remittanceInformation)
                .append("remittanceInformationType", remittanceInformationType)
                .append("valueDate", valueDate)
                .toString();
    }
}
