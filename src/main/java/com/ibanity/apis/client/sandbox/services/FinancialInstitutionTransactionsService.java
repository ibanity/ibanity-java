package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionTransactionsService {

    FinancialInstitutionTransaction find(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) throws ApiErrorsException;

    List<FinancialInstitutionTransaction> list(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ApiErrorsException;

    FinancialInstitutionTransaction create(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction) throws ApiErrorsException;

    FinancialInstitutionTransaction create(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction, UUID idempotencyKey) throws ApiErrorsException;

    void delete(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) throws ApiErrorsException;
}
