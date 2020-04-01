package com.ibanity.apis.client.jsonapi;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMetaApiModel {

    private FinancialInstitutionResponseApiModel financialInstitutionResponse;
}
