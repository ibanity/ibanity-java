package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityError;
import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Synchronization implements IbanityModel {

    public static final String RESOURCE_TYPE    = "synchronization";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String requestId;

    @Builder.Default
    private List<IbanityError> errors = Collections.emptyList();
    private String status;
    private String subtype;
    private String resourceId;
    private String resourceType;
    private String selfLink;

    private Instant createdAt;
    private Instant updatedAt;

}

