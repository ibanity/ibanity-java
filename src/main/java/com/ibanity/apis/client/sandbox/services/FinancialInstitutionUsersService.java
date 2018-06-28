package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionUsersService {

    List<FinancialInstitutionUser> getFinancialInstitutionUsers();

    List<FinancialInstitutionUser> getFinancialInstitutionUsers(IbanityPagingSpec pagingSpec);

    FinancialInstitutionUser getFinancialInstitutionUser(UUID financialInstitutionUserId) throws ApiErrorsException;

    FinancialInstitutionUser createFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser);

    FinancialInstitutionUser createFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser, UUID idempotency);

    FinancialInstitutionUser updateFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser);

    FinancialInstitutionUser updateFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser, UUID idempotency);

    void deleteFinancialInstitutionUser(UUID financialInstitutionUserId) throws ApiErrorsException;
}
