package com.ibanity.apis.client.models;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMeta {

    private FinancialInstitutionResponse financialInstitutionResponse;
}
