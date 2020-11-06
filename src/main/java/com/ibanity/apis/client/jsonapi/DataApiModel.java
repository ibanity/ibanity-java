package com.ibanity.apis.client.jsonapi;

import lombok.*;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DataApiModel {

    private String id;
    private LinksApiModel links;
    private String type;
    private SynchronizationMetaApiModel meta;

    @Builder.Default
    private Map<String, Object> attributes = emptyMap();
    @Builder.Default
    private Map<String, RelationshipsApiModel> relationships = emptyMap();
}
