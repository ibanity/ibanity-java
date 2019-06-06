package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionUserUpdateQuery {
    private UUID financialInstitutionUserId;
    private String login;
    private String password;
    private String firstName;
    private String lastName;

}
