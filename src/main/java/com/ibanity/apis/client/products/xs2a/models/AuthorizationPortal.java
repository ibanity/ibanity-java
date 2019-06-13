package com.ibanity.apis.client.products.xs2a.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationPortal {

    private String financialInstitutionPrimaryColor;
    private String financialInstitutionSecondaryColor;
    private String disclaimerTitle;
    private String disclaimerContent;
}
