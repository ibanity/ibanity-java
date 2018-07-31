package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionTransactionsService {

    FinancialInstitutionTransaction find(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId);

    List<FinancialInstitutionTransaction> list(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId);

    FinancialInstitutionTransaction create(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction);

    FinancialInstitutionTransaction create(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction, UUID idempotencyKey);

    void delete(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId);
}
