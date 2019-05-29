package com.ibanity.apis.client.jsonapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CollectionApiModel {

    @Builder.Default
    private MetaApiModel meta = new MetaApiModel();
    @Builder.Default
    private LinksApiModel links = new LinksApiModel();
    private List<DataApiModel> data;
}
