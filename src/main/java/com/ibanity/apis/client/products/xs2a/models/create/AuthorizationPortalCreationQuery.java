package com.ibanity.apis.client.products.xs2a.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class AuthorizationPortalCreationQuery {

    private String financialInstitutionPrimaryColor;
    private String financialInstitutionSecondaryColor;
    private String disclaimerTitle;
    private String disclaimerContent;

}
