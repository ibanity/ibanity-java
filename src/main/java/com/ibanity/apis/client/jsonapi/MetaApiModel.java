package com.ibanity.apis.client.jsonapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MetaApiModel {

    @Builder.Default
    private PagingApiModel paging = new PagingApiModel();
}
