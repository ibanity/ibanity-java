package com.ibanity.apis.client.products.xs2a.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class BatchSynchronizationCreationQuery {

    private String resourceType;
    private Instant cancelAfter;
    private Instant unlessSynchronizedAfter;

    @Builder.Default
    private List<String> subtypes = Collections.emptyList();

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
