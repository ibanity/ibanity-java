package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.services.FinancialInstitutionsService;

import java.util.UUID;

/**
 * Service for Financial Institutions related APIs
 */
public interface SandboxFinancialInstitutionsService extends FinancialInstitutionsService {

    /**
     * Create a new financial institution
     * @param financialInstitution the details of the financial institution
     * @return the newly created financial institution details
     */
    FinancialInstitution createFinancialInstitution(FinancialInstitution financialInstitution);

    /**
     * Create a new financial institution
     * @param financialInstitution the details of the financial institution
     * @param idempotency to prevent the same request from being performed more than once
     * @return the newly created financial institution details
     */
    FinancialInstitution createFinancialInstitution(FinancialInstitution financialInstitution, UUID idempotency);

    /**
     * Update an existing financial institution
     * @param financialInstitution to details of the financial institution to be updated
     * @return the updated version
     */
    FinancialInstitution updateFinancialInstitution(FinancialInstitution financialInstitution);

    /**
     * Update an existing financial institution
     * @param financialInstitution to details of the financial institution to be updated
     * @param idempotency to prevent the same request from being performed more than once
     * @return the updated version
     */
    FinancialInstitution updateFinancialInstitution(FinancialInstitution financialInstitution, UUID idempotency);

    /**
     * Delete an financial institution
     * @param financialInstitutionId the id of the financial institution
     * @throws ResourceNotFoundException when the provided ID is not known
     */
    void deleteFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException;
}
