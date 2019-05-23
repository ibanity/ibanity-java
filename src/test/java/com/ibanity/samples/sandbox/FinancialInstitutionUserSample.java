package com.ibanity.samples.sandbox;

import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionUserDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionUserReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.update.FinancialInstitutionUserUpdateQuery;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionUsersServiceImpl;

import java.util.UUID;

public class FinancialInstitutionUserSample {
    private final FinancialInstitutionUsersService financialInstitutionUsersService = new FinancialInstitutionUsersServiceImpl(null, null);

    public FinancialInstitutionUser create(){
        FinancialInstitutionUserUpdateQuery userCreationQuery =
                FinancialInstitutionUserUpdateQuery.builder()
                        .login("Login-"+ UUID.randomUUID().toString())
                        .password("Password")
                        .lastName("Lastname")
                        .firstName("First name")
                        .build();
        return financialInstitutionUsersService.create(userCreationQuery);
    }

    public FinancialInstitutionUser update(FinancialInstitutionUser financialInstitutionUser) {
        FinancialInstitutionUserUpdateQuery financialInstitutionUserUpdateQuery =
                FinancialInstitutionUserUpdateQuery.from(financialInstitutionUser)
                        .password("new password")
                        .build();

        return financialInstitutionUsersService.update(
                financialInstitutionUserUpdateQuery);
    }

    public FinancialInstitutionUser find(UUID financialInstitutionUserId) {
        FinancialInstitutionUserReadQuery financialInstitutionUserReadQuery =
                FinancialInstitutionUserReadQuery.builder()
                        .financialInstitutionUserId(financialInstitutionUserId)
                        .build();

        return financialInstitutionUsersService.find(financialInstitutionUserReadQuery);
    }

    public void delete(FinancialInstitutionUser financialInstitutionUser){
        FinancialInstitutionUserDeleteQuery financialInstitutionUserDeleteQuery =
                FinancialInstitutionUserDeleteQuery.builder()
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .build();

        financialInstitutionUsersService.delete(financialInstitutionUserDeleteQuery);
    }
}
