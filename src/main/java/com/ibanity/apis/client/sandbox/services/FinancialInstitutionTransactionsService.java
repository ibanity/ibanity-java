package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionTransactionsService {

    FinancialInstitutionTransaction getFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) throws ApiErrorsException;

    List<FinancialInstitutionTransaction> getFinancialInstitutionAccountTransactions(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ApiErrorsException;

    FinancialInstitutionTransaction createFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction) throws ApiErrorsException;

    FinancialInstitutionTransaction createFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction, UUID idempotency) throws ApiErrorsException;

    void deleteFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) throws ApiErrorsException;
}
