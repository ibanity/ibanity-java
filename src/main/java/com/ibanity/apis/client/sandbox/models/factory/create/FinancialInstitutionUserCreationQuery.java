package com.ibanity.apis.client.sandbox.models.factory.create;


import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionUserCreationQuery {
    private String password;
    private String firstName;
    private String lastName;
    private String login;

    private UUID idempotencyKey;
}
