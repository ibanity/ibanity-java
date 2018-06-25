package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionAccountsService {

    /**
     * Get a specific Financial Institution Customer's account
     * @param financialInstitutionId The Id of the financial institution
     * @param financialInstitutionUserId The Id of the financial institution user to which the financial institution account is linked
     * @param financialInstitutionAccountId the UUID of the FinancialInstitutionAccount
     * @return the financial institution account
     * @throws ResourceNotFoundException when a provided ID is not known
     */
    FinancialInstitutionAccount getFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException;

    /**
     * Get ALL FinancialInstitutionUser accounts
     * @param financialInstitutionId The Id of the financial institution
     * @param financialInstitutionUserId the UUID of the FinancialInstitutionUser
     * @return list of all user's accounts
     * @throws ResourceNotFoundException when a provided ID is not known
     */
    List<FinancialInstitutionAccount> getFinancialInstitutionUserAccounts(UUID financialInstitutionId, UUID financialInstitutionUserId) throws ResourceNotFoundException;

    /**
     * Create a new financial institution account in the financial institution, linked to the provide financial institution user
     * @param financialInstitutionId The Id Sandbox Financial Institution in which we want to create a new financial institution account linked to the provide financialInstitutionUserId
     * @param financialInstitutionUserId The Id of the financial institution user for which we want to link a new financial institution account
     * @param financialInstitutionAccount the financial institution account to be created
     * @return the created version of the sandbox Account
     * @throws ResourceNotFoundException when a provided ID is not known
     */
    FinancialInstitutionAccount createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount) throws ResourceNotFoundException;

    /**
     * Create a new financial institution account in the financial institution, linked to the provide financial institution user
     * @param financialInstitutionId The Id Sandbox Financial Institution in which we want to create a new financial institution account linked to the provide financialInstitutionUserId
     * @param financialInstitutionUserId The Id of the financial institution user for which we want to link a new financial institution account
     * @param financialInstitutionAccount the financial institution account to be created
     * @param idempotency to prevent the same request from being performed more than once
     * @return the created version of the sandbox Account
     * @throws ResourceNotFoundException when a provided ID is not known
     */
    FinancialInstitutionAccount createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount, UUID idempotency) throws ResourceNotFoundException;

    /**
     * Delete the financial institution account
     * @param financialInstitutionId The Id financial institution in which we want to create a new financial institution account linked to the provide financialInstitutionUserId
     * @param financialInstitutionUserId The Id of the financial institution user for which we want to delete the financial institution account
     * @param financialInstitutionAccountId the ID of the financial institution account to be deleted
     * @throws ResourceNotFoundException when a provided ID is not known
     */
    void deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException;

}
