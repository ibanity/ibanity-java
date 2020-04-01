package com.ibanity.apis.client.jsonapi;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IbanityErrorApiModel {

    private String code;
    private String detail;
    private ErrorMetaApiModel meta;
}
