package com.ibanity.apis.client.models;

import com.ibanity.apis.client.models.links.PaymentAccessLinks;
import com.ibanity.apis.client.paging.IBanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiLinksInformation;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;


@JsonApiResource(type= "paymentInitiationRequest", resourcePath = "payment-initiation-requests", pagingBehavior = IBanityPagingBehavior.class)
public class PaymentInitiationRequest extends AbstractModel{
    private String consentReference;
    private String endToEndId;
    private String productType;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String currency;
    private Double amount;
    private String debtorName;
    private String debtorAccountReference;
    private String debtorAccountReferenceType;
    private String creditorName;
    private String creditorAccountReference;
    private String creditorAccountReferenceType;
    private String creditorAgent;
    private String creditorAgentType;
    private String status;
    private String redirectUri;
    private String customerIp;
    private String customerAgent;


    @JsonApiLinksInformation
    private PaymentAccessLinks links;

    @JsonApiRelation(lookUp= LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL,serialize= SerializeType.ONLY_ID)
    private FinancialInstitution financialInstitution = null;

    public PaymentInitiationRequest() {
        super();
        this.financialInstitution = new FinancialInstitution();
    }

    public PaymentInitiationRequest(UUID id, UUID financialInstitutionId) {
        super(id);
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
    }

    public void setFinancialInstitutionId(UUID financialInstitutionId){
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
    }

    public String getConsentReference() {
        return consentReference;
    }

    public void setConsentReference(String consentReference) {
        this.consentReference = consentReference;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public void setEndToEndId(String endToEndId) {
        this.endToEndId = endToEndId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getDebtorAccountReference() {
        return debtorAccountReference;
    }

    public void setDebtorAccountReference(String debtorAccountReference) {
        this.debtorAccountReference = debtorAccountReference;
    }

    public String getDebtorAccountReferenceType() {
        return debtorAccountReferenceType;
    }

    public void setDebtorAccountReferenceType(String debtorAccountReferenceType) {
        this.debtorAccountReferenceType = debtorAccountReferenceType;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorAccountReference() {
        return creditorAccountReference;
    }

    public void setCreditorAccountReference(String creditorAccountReference) {
        this.creditorAccountReference = creditorAccountReference;
    }

    public String getCreditorAccountReferenceType() {
        return creditorAccountReferenceType;
    }

    public void setCreditorAccountReferenceType(String creditorAccountReferenceType) {
        this.creditorAccountReferenceType = creditorAccountReferenceType;
    }

    public String getCreditorAgent() {
        return creditorAgent;
    }

    public void setCreditorAgent(String creditorAgent) {
        this.creditorAgent = creditorAgent;
    }

    public String getCreditorAgentType() {
        return creditorAgentType;
    }

    public void setCreditorAgentType(String creditorAgentType) {
        this.creditorAgentType = creditorAgentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FinancialInstitution getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public PaymentAccessLinks getLinks() {
        return links;
    }

    public void setLinks(PaymentAccessLinks links) {
        this.links = links;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public void setCustomerIp(String customerIp) {
        this.customerIp = customerIp;
    }

    public String getCustomerAgent() {
        return customerAgent;
    }

    public void setCustomerAgent(String customerAgent) {
        this.customerAgent = customerAgent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PaymentInitiationRequest)) return false;

        PaymentInitiationRequest that = (PaymentInitiationRequest) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getConsentReference(), that.getConsentReference())
                .append(getEndToEndId(), that.getEndToEndId())
                .append(getProductType(), that.getProductType())
                .append(getRemittanceInformationType(), that.getRemittanceInformationType())
                .append(getRemittanceInformation(), that.getRemittanceInformation())
                .append(getCurrency(), that.getCurrency())
                .append(getAmount(), that.getAmount())
                .append(getDebtorName(), that.getDebtorName())
                .append(getDebtorAccountReference(), that.getDebtorAccountReference())
                .append(getDebtorAccountReferenceType(), that.getDebtorAccountReferenceType())
                .append(getCreditorName(), that.getCreditorName())
                .append(getCreditorAccountReference(), that.getCreditorAccountReference())
                .append(getCreditorAccountReferenceType(), that.getCreditorAccountReferenceType())
                .append(getCreditorAgent(), that.getCreditorAgent())
                .append(getCreditorAgentType(), that.getCreditorAgentType())
                .append(getStatus(), that.getStatus())
                .append(getRedirectUri(), that.getRedirectUri())
                .append(getCustomerIp(), that.getCustomerIp())
                .append(getCustomerAgent(), that.getCustomerAgent())
                .append(getLinks(), that.getLinks())
                .append(getFinancialInstitution(), that.getFinancialInstitution())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getConsentReference())
                .append(getEndToEndId())
                .append(getProductType())
                .append(getRemittanceInformationType())
                .append(getRemittanceInformation())
                .append(getCurrency())
                .append(getAmount())
                .append(getDebtorName())
                .append(getDebtorAccountReference())
                .append(getDebtorAccountReferenceType())
                .append(getCreditorName())
                .append(getCreditorAccountReference())
                .append(getCreditorAccountReferenceType())
                .append(getCreditorAgent())
                .append(getCreditorAgentType())
                .append(getStatus())
                .append(getRedirectUri())
                .append(getCustomerIp())
                .append(getCustomerAgent())
                .append(getLinks())
                .append(getFinancialInstitution())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("amount", amount)
                .append("consentReference", consentReference)
                .append("creditorAccountReference", creditorAccountReference)
                .append("creditorAccountReferenceType", creditorAccountReferenceType)
                .append("creditorAgent", creditorAgent)
                .append("creditorAgentType", creditorAgentType)
                .append("creditorName", creditorName)
                .append("currency", currency)
                .append("customerAgent", customerAgent)
                .append("customerIp", customerIp)
                .append("debtorAccountReference", debtorAccountReference)
                .append("debtorAccountReferenceType", debtorAccountReferenceType)
                .append("debtorName", debtorName)
                .append("endToEndId", endToEndId)
                .append("financialInstitution", financialInstitution)
                .append("id", getId())
                .append("links", links)
                .append("productType", productType)
                .append("redirectUri", redirectUri)
                .append("remittanceInformation", remittanceInformation)
                .append("remittanceInformationType", remittanceInformationType)
                .append("status", status)
                .toString();
    }
}
