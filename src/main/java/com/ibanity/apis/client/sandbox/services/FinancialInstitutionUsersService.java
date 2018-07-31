package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionUsersService {

    List<FinancialInstitutionUser> list();

    List<FinancialInstitutionUser> list(IbanityPagingSpec pagingSpec);

    FinancialInstitutionUser find(UUID financialInstitutionUserId);

    FinancialInstitutionUser create(String login, String password, String lastName, String firstName);

    FinancialInstitutionUser create(String login, String password, String lastName, String firstName, UUID idempotencyKey);

    FinancialInstitutionUser update(FinancialInstitutionUser financialInstitutionUser);

    FinancialInstitutionUser update(FinancialInstitutionUser financialInstitutionUser, UUID idempotencyKey);

    void delete(UUID financialInstitutionUserId);
}
