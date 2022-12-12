package com.ibanity.apis.client.products.ponto_connect.models;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Userinfo {

    private String name;
    private String sub;
    private String representativeOrganizationName;
    private UUID representativeOrganizationId;
    private boolean paymentsActivated;
    private boolean paymentsActivationRequested;
    private boolean onboardingComplete;
}
