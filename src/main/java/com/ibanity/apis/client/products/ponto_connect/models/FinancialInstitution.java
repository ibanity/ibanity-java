package com.ibanity.apis.client.products.ponto_connect.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FinancialInstitution implements IbanityModel {

    public static final String RESOURCE_TYPE = "financialInstitution";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;

    private String name;
    private String selfLink;
}

