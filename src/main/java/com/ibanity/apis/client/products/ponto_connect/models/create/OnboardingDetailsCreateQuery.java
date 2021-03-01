package com.ibanity.apis.client.products.ponto_connect.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class OnboardingDetailsCreateQuery {

    private String accessToken;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

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
}
