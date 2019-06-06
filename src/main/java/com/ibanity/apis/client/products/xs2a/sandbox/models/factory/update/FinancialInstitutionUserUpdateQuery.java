package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionUserUpdateQuery {
    private UUID financialInstitutionUserId;
    private String login;
    private String password;
    private String firstName;
    private String lastName;

}
