package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionAccountsService {

    FinancialInstitutionAccount find(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ApiErrorsException;

    List<FinancialInstitutionAccount> list(UUID financialInstitutionId, UUID financialInstitutionUserId) throws ApiErrorsException;

    FinancialInstitutionAccount create(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount) throws ApiErrorsException;

    FinancialInstitutionAccount create(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount, UUID idempotencyKey) throws ApiErrorsException;

    void delete(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ApiErrorsException;

}
