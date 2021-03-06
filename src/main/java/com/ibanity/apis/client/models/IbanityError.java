package com.ibanity.apis.client.models;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IbanityError {

    private String code;
    private String detail;
    private ErrorMeta meta;
}
