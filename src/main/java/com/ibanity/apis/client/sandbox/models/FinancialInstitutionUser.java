package com.ibanity.apis.client.sandbox.models;

import com.ibanity.apis.client.annotations.InstantJsonFormat;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.AbstractModel;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

import java.time.Instant;

import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser.RESOURCE_PATH;
import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser.RESOURCE_TYPE;

@Data
@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class FinancialInstitutionUser extends AbstractModel {

    public static final String RESOURCE_TYPE    = "financialInstitutionUser";
    public static final String RESOURCE_PATH    = "financial-institution-users";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    @InstantJsonFormat
    private Instant createdAt;

    @InstantJsonFormat
    private Instant updatedAt;

    @InstantJsonFormat
    private Instant deletedAt;

    private String password;
    private String firstName;
    private String lastName;
    private String login;

}
