package com.ibanity.apis.client.jsonapi.isabel_connect;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PagingApiModel {
    private Integer offset;
    private Integer total;
}
