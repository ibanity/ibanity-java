package com.ibanity.apis.client.products.xs2a.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibanity.apis.client.models.IbanityModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Synchronization implements IbanityModel {

    public static final String RESOURCE_TYPE    = "synchronization";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    @Builder.Default
    private List<String> errors = Collections.emptyList();
    private String status;
    @JsonProperty("subtype")
    private String subType;
    private String resourceId;
    private String resourceType;
    private String selfLink;

    private Instant createdAt;
    private Instant updatedAt;

}

