package com.ibanity.apis.client.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import lombok.Data;

import static com.ibanity.apis.client.models.Transaction.RESOURCE_PATH;
import static com.ibanity.apis.client.models.Transaction.RESOURCE_TYPE;

@Data
@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class Transaction extends AbstractTransaction {

    public static final String RESOURCE_TYPE    = "transaction";
    public static final String RESOURCE_PATH    = "transactions";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    @JsonApiRelation(lookUp = LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL, serialize = SerializeType.LAZY)
    private Account account;

}
