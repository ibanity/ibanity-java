package com.ibanity.apis.client.jsonapi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static java.util.Collections.emptyList;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionApiModel {

    private MetaApiModel meta;
    private LinksApiModel links;

    @Builder.Default
    private List<DataApiModel> data = emptyList();
}

