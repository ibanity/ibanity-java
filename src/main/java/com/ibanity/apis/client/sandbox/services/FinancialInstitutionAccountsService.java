package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionAccountsService {

    FinancialInstitutionAccount getFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException;

    List<FinancialInstitutionAccount> getFinancialInstitutionUserAccounts(UUID financialInstitutionId, UUID financialInstitutionUserId) throws ResourceNotFoundException;

    FinancialInstitutionAccount createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount) throws ResourceNotFoundException;

    FinancialInstitutionAccount createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount, UUID idempotency) throws ResourceNotFoundException;

    void deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException;

}
