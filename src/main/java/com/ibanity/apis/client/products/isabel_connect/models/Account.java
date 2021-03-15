package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account extends IsabelModel<String> {
    public static final String RESOURCE_TYPE = "account";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private String country;
    private String currency;
    private String description;
    private String financialInstitutionBic;
    private String holderAddress;
    private String holderAddressCountry;
    private String holderName;
    private String reference;
    private String referenceType;
}
