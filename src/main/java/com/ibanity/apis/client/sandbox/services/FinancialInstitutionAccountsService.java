package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionAccountsService {

    FinancialInstitutionAccount getFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ApiErrorsException;

    List<FinancialInstitutionAccount> getFinancialInstitutionUserAccounts(UUID financialInstitutionId, UUID financialInstitutionUserId) throws ApiErrorsException;

    FinancialInstitutionAccount createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount) throws ApiErrorsException;

    FinancialInstitutionAccount createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount, UUID idempotency) throws ApiErrorsException;

    void deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ApiErrorsException;

}
