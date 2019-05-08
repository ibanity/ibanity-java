package com.ibanity.apis.client.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.links.PaymentAccessLinks;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiLinksInformation;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.ibanity.apis.client.models.PaymentInitiationRequest.RESOURCE_PATH;
import static com.ibanity.apis.client.models.PaymentInitiationRequest.RESOURCE_TYPE;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
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

    @JsonApiLinksInformation
    private PaymentAccessLinks links;

    @JsonApiRelation(lookUp = LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL, serialize = SerializeType.LAZY)
    private FinancialInstitution financialInstitution;

}
