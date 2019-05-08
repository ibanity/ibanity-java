package com.ibanity.apis.client.models;

import com.ibanity.apis.client.annotations.InstantJsonFormat;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

import static com.ibanity.apis.client.models.Synchronization.RESOURCE_PATH;
import static com.ibanity.apis.client.models.Synchronization.RESOURCE_TYPE;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@JsonApiResource(type = RESOURCE_TYPE, resourcePath = RESOURCE_PATH, pagingBehavior = IbanityPagingBehavior.class)
public class Synchronization extends AbstractModel {

    public static final String RESOURCE_TYPE    = "synchronization";
    public static final String RESOURCE_PATH    = "synchronizations";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    private List<String> errors;
    private String status;
    private String subtype;
    private String resourceId;
    private String resourceType;

    @InstantJsonFormat
    private Instant createdAt;
    @InstantJsonFormat
    private Instant updatedAt;

}

