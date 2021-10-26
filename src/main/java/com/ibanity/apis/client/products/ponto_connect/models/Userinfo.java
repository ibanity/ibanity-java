package com.ibanity.apis.client.products.ponto_connect.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Userinfo {

    private String name;
    private String sub;
    private boolean paymentsActivated;
    private boolean onboardingComplete;
}
