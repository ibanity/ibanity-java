package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionTransactionsService {

    /**
     * Get a specific financial institution transaction
     * @param financialInstitutionId The Id of the financial institution
     * @param financialInstitutionUserId The Id of the financial institution user to which the financial institution account is linked
     * @param financialInstitutionAccountId The id of the financial institution account to which the transaction is linked to
     * @param financialInstitutionTransactionId the UUID of the financial institution transaction
     * @return the financial institution transaction
     * @throws ResourceNotFoundException when the provided IDs are not known
     */
    FinancialInstitutionTransaction getFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) throws ResourceNotFoundException;

    /**
     * Get the list of financial institution transaction for a specific account
     * @param financialInstitutionId The Id of the financial institution
     * @param financialInstitutionUserId The Id of the financial institution user to which the financial institution account is linked
     * @param financialInstitutionAccountId The id of the financial institution account to which the transactions are linked to
     * @return the list of financial institution transactions
     * @throws ResourceNotFoundException when the provided IDs are not known
     */
    List<FinancialInstitutionTransaction> getFinancialInstitutionAccountTransactions(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException;

    /**
     * Create a new financial institution transaction in the financial institution, linked to the provide sfinancial institution user
     * @param financialInstitutionId The Id of the financial institution in which we want to create a new account linked to the provide financialInstitutionUserId
     * @param financialInstitutionUserId The Id of the financial institution user for which we want to link a new financial institution account
     * @param financialInstitutionAccountId The id of the financial institution account to which the transaction will be linked to
     * @param financialInstitutionTransaction The details of the financial institution transaction
     * @return the created financial institution transaction
     */
    FinancialInstitutionTransaction createFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction);


    /**
     * Delete a financial institution transaction
     * @param financialInstitutionId The Id of the financial institution in which we want to create a new financial institution account linked to the provide financialInstitutionUserId
     * @param financialInstitutionUserId The Id of the financial institution user for which we want to link a new financial institution account
     * @param financialInstitutionAccountId The id of the financial institution account to which the financial institution transaction will be linked to
     * @param financialInstitutionTransactionId The id of the financial institution transaction to delete
     * @throws ResourceNotFoundException when the provided IDs are not known
     */
    void deleteFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) throws ResourceNotFoundException;
}
