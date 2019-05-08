package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.ibanity.apis.client.models.CustomerAccessToken.RESOURCE_PATH;
import static com.ibanity.apis.client.models.CustomerAccessToken.RESOURCE_TYPE;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class CustomerAccessToken extends AbstractModel {

    public static final String RESOURCE_TYPE    = "customerAccessToken";
    public static final String RESOURCE_PATH    = "customer-access-tokens";

    private String token;
    private String applicationCustomerReference;

}
