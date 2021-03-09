package com.ibanity.apis.client.jsonapi.isabel_connect;

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
    private String type;

    @Builder.Default
    private Map<String, Object> attributes = emptyMap();
}
