package com.ibanity.apis.client.jsonapi.isabel_connect;

import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ResourceApiModel {

    private DataApiModel data;
}
