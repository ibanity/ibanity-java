package com.ibanity.apis.client.jsonapi;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PagingApiModel {

    private long limit;
    private String before;
    private String after;
}
