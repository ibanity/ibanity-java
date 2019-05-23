package com.ibanity.apis.client.sandbox.models;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.IbanityModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialInstitutionUser implements IbanityModel {

    public static final String RESOURCE_TYPE = "financialInstitutionUser";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;

    private String selfLink;
    private String password;
    private String firstName;
    private String lastName;
    private String login;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
