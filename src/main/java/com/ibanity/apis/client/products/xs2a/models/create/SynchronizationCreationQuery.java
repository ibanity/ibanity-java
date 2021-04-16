package com.ibanity.apis.client.products.xs2a.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SynchronizationCreationQuery {

    private String subtype;
    private String resourceId;
    private String resourceType;
    private Boolean customerOnline;
    private String customerIpAddress;
    private String customerAccessToken;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
