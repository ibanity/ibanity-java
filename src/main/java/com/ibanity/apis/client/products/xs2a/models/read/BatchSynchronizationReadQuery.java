package com.ibanity.apis.client.products.xs2a.models.read;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public final class BatchSynchronizationReadQuery {

    private UUID batchSynchronizationId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
