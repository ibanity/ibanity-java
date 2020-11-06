package com.ibanity.apis.client.products.xs2a.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FinancialInstitutionCountry {

    public static final String RESOURCE_TYPE = "financialInstitutionCountry";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + "Id}";

    private String id;
    private String requestId;

}
