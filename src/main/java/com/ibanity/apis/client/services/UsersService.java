package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionUser;
import com.ibanity.apis.client.paging.IBanityPagingSpec;

import java.util.List;
import java.util.UUID;

/**
 * Service for Users related APIs
 */
public interface UsersService {

    /**
     * Returns the list of all Sandbox Financial Institutions Users
     * @return list of Sandbox Financial Institutions Users
     */
    List<FinancialInstitutionUser> getSandboxFinancialInstitutionUsers();

    /**
     * Returns the list of all Sandbox Financial Institutions Users based on the provided paging specification
     * @param pagingSpec The paging specification to be used for tuning the resulting list
     * @return list of Sandbox Financial Institutions Users
     */
    List<FinancialInstitutionUser> getSandboxFinancialInstitutionUsers(IBanityPagingSpec pagingSpec);

    /**
     * Get a specific Sandbox Financial Institutions User
     * @param sandboxFinancialInstitutionUserId the UUID of the Sandbox Financial Institutions User
     * @return the Financial Institutions User
     * @throws ResourceNotFoundException when the provided ID is not known
     */
    FinancialInstitutionUser getSandboxFinancialInstitutionUser(UUID sandboxFinancialInstitutionUserId) throws ResourceNotFoundException;

    /**
     * Create a new Sandbox Financial Institution User
     * @param sandboxFinancialInstitutionUser the details of the financial institution user
     * @return the newly created financial institution user details
     */
    FinancialInstitutionUser createSandboxFinancialInstitutionUser(FinancialInstitutionUser sandboxFinancialInstitutionUser);

    /**
     * Update an existing Sandbox Financial Institution User
     * @param sandboxFinancialInstitutionUser to details of the financial institution user to be updated
     * @return the updated version
     */
    FinancialInstitutionUser updateSandboxFinancialInstitutionUser(FinancialInstitutionUser sandboxFinancialInstitutionUser);

    /**
     * Delete an Sandbox Financial Institution User
     * @param sandboxFinancialInstitutionUserId the id of the Financial Institution User
     * @throws ResourceNotFoundException when the provided ID is not known
     */
    void deleteSandboxFinancialInstitutionUser(UUID sandboxFinancialInstitutionUserId) throws ResourceNotFoundException;
}
