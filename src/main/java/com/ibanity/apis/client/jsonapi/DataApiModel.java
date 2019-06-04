package com.ibanity.apis.client.jsonapi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DataApiModel {

    private UUID id;
    private LinksApiModel links;
    private String type;
    private SynchronizationMetaApiModel meta;

    @Builder.Default
    private Map<String, Object> attributes = emptyMap();
    @Builder.Default
    private Map<String, RelationshipsApiModel> relationships = emptyMap();
}
