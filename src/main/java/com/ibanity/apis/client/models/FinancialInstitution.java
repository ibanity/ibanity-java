package com.ibanity.apis.client.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

import static com.ibanity.apis.client.models.FinancialInstitution.RESOURCE_PATH;

@Data
@JsonApiResource(type = FinancialInstitution.RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class FinancialInstitution extends AbstractModel {

    public static final String RESOURCE_TYPE    = "financialInstitution";
    public static final String RESOURCE_PATH    = "financial-institutions";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    private boolean sandbox;
    private String name;

}
