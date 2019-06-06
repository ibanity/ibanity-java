package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FinancialInstitutionUserCreationQuery {
    private String password;
    private String firstName;
    private String lastName;
    private String login;
}
