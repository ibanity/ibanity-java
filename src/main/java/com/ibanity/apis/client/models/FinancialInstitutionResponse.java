package com.ibanity.apis.client.models;

import lombok.*;

import java.time.Instant;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FinancialInstitutionResponse {

    private String body;
    private String requestId;
    private Instant timestamp;
    private String requestUri;
    private Integer statusCode;
    private String responseId;
}
