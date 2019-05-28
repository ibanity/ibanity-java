package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update;

import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionUserUpdateQuery {
    private UUID financialInstitutionUserId;
    private String login;
    private String password;
    private String firstName;
    private String lastName;

    private UUID idempotencyKey;

    public static FinancialInstitutionUserUpdateQueryBuilder from(final FinancialInstitutionUser financialInstitutionUser) {
        Objects.requireNonNull(financialInstitutionUser, "Missing required 'financialInstitutionUser'");

        return new FinancialInstitutionUserUpdateQueryBuilder()
                .financialInstitutionUserId(financialInstitutionUser.getId())
                .firstName(financialInstitutionUser.getFirstName())
                .lastName(financialInstitutionUser.getLastName())
                .login(financialInstitutionUser.getLogin())
                .password(financialInstitutionUser.getPassword());
    }
}
