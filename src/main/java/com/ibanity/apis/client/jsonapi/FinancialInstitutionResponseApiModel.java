package com.ibanity.apis.client.jsonapi;

import lombok.*;

import java.time.Instant;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FinancialInstitutionResponseApiModel {

    private Object body;
    private String requestId;
    private Instant timestamp;
    private String requestUri;
    private Integer statusCode;
    private String responseId;
}
