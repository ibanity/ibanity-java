package com.ibanity.apis.client.models.factory.read;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SynchronizationReadQuery {

    private String subtype;
    private String resourceId;
    private String resourceType;
    private UUID synchronizationId;
    private String customerAccessToken;
}
