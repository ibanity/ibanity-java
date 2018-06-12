package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IBanityPagingSpec;

import java.util.List;
import java.util.UUID;

/**
 * Service for Financial Institutions related APIs
 */
public interface FinancialInstitutionsService {
    /**
     * Get all Financial Institutions supported by Ibanity
     * @return List of Financial Institutions
     */
    List<FinancialInstitution> getFinancialInstitutions();

    /**
     * Get all Financial Institutions supported by Ibanity using the provided PagingSpec
     * @param pagingSpec The paging specification to be used for gathering the financial institutions list
     * @return List of Financial Institutions
     */
    List<FinancialInstitution> getFinancialInstitutions(IBanityPagingSpec pagingSpec);

    /**
     * Get the details of a specific Financial Institution
     * @param financialInstitutionId the id of the Financial Institution
     * @return The Financial Institution Details
     * @throws ResourceNotFoundException when the provided ID is not known
     */
    FinancialInstitution getFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException;

    /**
     * Create a new Sandbox Financial Institution
     * @param financialInstitution the details of the financial institution
     * @return the newly created financial institution details
     */
    FinancialInstitution createSandboxFinancialInstitution(FinancialInstitution financialInstitution);

    /**
     * Update an existing Sandbox Financial Institution
     * @param financialInstitution to details of the financial institution to be updated
     * @return the updated version
     */
    FinancialInstitution updateSandboxFinancialInstitution(FinancialInstitution financialInstitution);

    /**
     * Delete an Sandbox Financial Institution
     * @param financialInstitutionId the id of the Financial Institution
     * @throws ResourceNotFoundException when the provided ID is not known
     */
    void deleteSandboxFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException;
}
