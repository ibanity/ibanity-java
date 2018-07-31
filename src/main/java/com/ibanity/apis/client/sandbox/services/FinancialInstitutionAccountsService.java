package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionAccountsService {

    FinancialInstitutionAccount find(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId);

    List<FinancialInstitutionAccount> list(UUID financialInstitutionId, UUID financialInstitutionUserId);

    FinancialInstitutionAccount create(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount);

    FinancialInstitutionAccount create(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount, UUID idempotencyKey);

    void delete(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId);

}
