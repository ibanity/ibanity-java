package com.ibanity.apis.client.products.ponto_connect.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OnboardingDetails implements IbanityModel {

    public static final String RESOURCE_TYPE = "onboardingDetails";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private String requestId;

    private String vatNumber;
    private String phoneNumber;
    private String organizationName;
    private String lastName;
    private String firstName;
    private String enterpriseNumber;
    private String email;
    private String addressStreetAddress;
    private String addressPostalCode;
    private String addressCountry;
    private String addressCity;
    private UUID initialFinancialInstitutionId;
    private Boolean automaticSubmissionOnCompletedForms;
    private String preferredOtpMethod;
    private UUID requestedOrganisationId;
    private String organizationType;
}
