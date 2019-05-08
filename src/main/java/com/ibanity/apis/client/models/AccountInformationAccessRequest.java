package com.ibanity.apis.client.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.links.AccountInformationAccessLinks;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiLinksInformation;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.ibanity.apis.client.models.AccountInformationAccessRequest.RESOURCE_PATH;
import static com.ibanity.apis.client.models.AccountInformationAccessRequest.RESOURCE_TYPE;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class AccountInformationAccessRequest extends AbstractModel {

    public static final String RESOURCE_TYPE    = "accountInformationAccessRequest";
    public static final String RESOURCE_PATH    = "account-information-access-requests";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    private String redirectUri;
    private String consentReference;

    @JsonApiLinksInformation
    private AccountInformationAccessLinks links;

    @JsonApiRelation(lookUp = LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL, serialize = SerializeType.LAZY)
    private FinancialInstitution financialInstitution;

}
