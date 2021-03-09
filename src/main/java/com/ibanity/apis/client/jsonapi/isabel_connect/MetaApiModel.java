package com.ibanity.apis.client.jsonapi.isabel_connect;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MetaApiModel {
    private PagingApiModel paging;

    public Integer getPaginationOffset() {
        return paging.getOffset();
    }

    public Integer getPaginationTotal() {
        return paging.getTotal();
    }
}
