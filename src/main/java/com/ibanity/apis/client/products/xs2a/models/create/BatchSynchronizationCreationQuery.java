package com.ibanity.apis.client.products.xs2a.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class BatchSynchronizationCreationQuery {

    private String[] subtypes;
    private String resourceType;
    private LocalDate cancelAfter;
    private LocalDate unlessSynchronizedAfter;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
