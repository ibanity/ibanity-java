package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionUserCreationQuery {
    private String password;
    private String firstName;
    private String lastName;
    private String login;
}
