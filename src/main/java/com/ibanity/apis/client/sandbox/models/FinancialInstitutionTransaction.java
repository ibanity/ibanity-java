package com.ibanity.apis.client.sandbox.models;

import com.ibanity.apis.client.annotations.InstantJsonFormat;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.AbstractTransaction;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;
import lombok.Data;

import java.time.Instant;

import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction.RESOURCE_PATH;
import static com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction.RESOURCE_TYPE;

@Data
@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class FinancialInstitutionTransaction extends AbstractTransaction {

    public static final String RESOURCE_TYPE    = "financialInstitutionTransaction";
    public static final String RESOURCE_PATH    = "financial-institution-transactions";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    @JsonApiRelation(lookUp = LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL, serialize = SerializeType.LAZY)
    private FinancialInstitutionAccount financialInstitutionAccount;

    @InstantJsonFormat
    private Instant createdAt = null;

    @InstantJsonFormat
    private Instant updatedAt = null;

    @InstantJsonFormat
    private Instant deletedAt = null;

}
