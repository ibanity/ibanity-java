package com.ibanity.apis.client.products.xs2a.sandbox.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class FinancialInstitutionUser implements IbanityModel {

    public static final String RESOURCE_TYPE = "financialInstitutionUser";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private String requestId;

    private String password;
    private String firstName;
    private String lastName;
    private String login;

    private Instant createdAt;
    private Instant updatedAt;
}
