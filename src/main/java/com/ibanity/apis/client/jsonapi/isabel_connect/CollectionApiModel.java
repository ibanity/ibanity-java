package com.ibanity.apis.client.jsonapi.isabel_connect;

import lombok.*;

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

    @Builder.Default
    private List<DataApiModel> data = emptyList();

    public Integer getPaginationOffset() {
        return meta.getPaginationOffset();
    }

    public Integer getPaginationTotal() {
        return meta.getPaginationTotal();
    }
}