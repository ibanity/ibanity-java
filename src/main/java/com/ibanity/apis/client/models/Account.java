package com.ibanity.apis.client.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

import static com.ibanity.apis.client.models.Account.RESOURCE_PATH;
import static com.ibanity.apis.client.models.Account.RESOURCE_TYPE;

@Data
@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class Account extends AbstractAccount {

    public static final String RESOURCE_TYPE    = "account";
    public static final String RESOURCE_PATH    = "accounts";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

}
