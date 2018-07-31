package com.ibanity.apis.client.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.links.PaymentAccessLinks;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiLinksInformation;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static com.ibanity.apis.client.models.PaymentInitiationRequest.RESOURCE_PATH;
import static com.ibanity.apis.client.models.PaymentInitiationRequest.RESOURCE_TYPE;


@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class PaymentInitiationRequest extends AbstractModel {

    public static final String RESOURCE_TYPE    = "paymentInitiationRequest";
    public static final String RESOURCE_PATH    = "payment-initiation-requests";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

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

    @JsonApiRelation(lookUp = LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL, serialize = SerializeType.LAZY)
    private FinancialInstitution financialInstitution;

    public PaymentInitiationRequest() {
        super();
        this.financialInstitution = new FinancialInstitution();
    }

    public PaymentInitiationRequest(final UUID id, final UUID financialInstitutionId) {
        super(id);
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
    }

    public void setFinancialInstitutionId(final UUID financialInstitutionId) {
        this.financialInstitution = new FinancialInstitution(financialInstitutionId);
    }

    public String getConsentReference() {
        return consentReference;
    }

    public void setConsentReference(final String consentReference) {
        this.consentReference = consentReference;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public void setEndToEndId(final String endToEndId) {
        this.endToEndId = endToEndId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(final String productType) {
        this.productType = productType;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(final String debtorName) {
        this.debtorName = debtorName;
    }

    public String getDebtorAccountReference() {
        return debtorAccountReference;
    }

    public void setDebtorAccountReference(final String debtorAccountReference) {
        this.debtorAccountReference = debtorAccountReference;
    }

    public String getDebtorAccountReferenceType() {
        return debtorAccountReferenceType;
    }

    public void setDebtorAccountReferenceType(final String debtorAccountReferenceType) {
        this.debtorAccountReferenceType = debtorAccountReferenceType;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(final String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorAccountReference() {
        return creditorAccountReference;
    }

    public void setCreditorAccountReference(final String creditorAccountReference) {
        this.creditorAccountReference = creditorAccountReference;
    }

    public String getCreditorAccountReferenceType() {
        return creditorAccountReferenceType;
    }

    public void setCreditorAccountReferenceType(final String creditorAccountReferenceType) {
        this.creditorAccountReferenceType = creditorAccountReferenceType;
    }

    public String getCreditorAgent() {
        return creditorAgent;
    }

    public void setCreditorAgent(final String creditorAgent) {
        this.creditorAgent = creditorAgent;
    }

    public String getCreditorAgentType() {
        return creditorAgentType;
    }

    public void setCreditorAgentType(final String creditorAgentType) {
        this.creditorAgentType = creditorAgentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public FinancialInstitution getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(final FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public PaymentAccessLinks getLinks() {
        return links;
    }

    public void setLinks(final PaymentAccessLinks links) {
        this.links = links;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(final String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public void setCustomerIp(final String customerIp) {
        this.customerIp = customerIp;
    }

    public String getCustomerAgent() {
        return customerAgent;
    }

    public void setCustomerAgent(final String customerAgent) {
        this.customerAgent = customerAgent;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PaymentInitiationRequest)) {
            return false;
        }

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
