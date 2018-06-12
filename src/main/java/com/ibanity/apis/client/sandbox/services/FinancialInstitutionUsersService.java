package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;

import java.util.List;
import java.util.UUID;

/**
 * Service for Users related APIs
 */
public interface FinancialInstitutionUsersService {

    /**
     * Returns the list of all financial institution users
     * @return list of financial institution users
     */
    List<FinancialInstitutionUser> getFinancialInstitutionUsers();

    /**
     * Returns the list of all financial institution users based on the provided paging specification
     * @param pagingSpec The paging specification to be used for tuning the resulting list
     * @return list of financial institutions users
     */
    List<FinancialInstitutionUser> getFinancialInstitutionUsers(IbanityPagingSpec pagingSpec);

    /**
     * Get a specific financial institution user
     * @param financialInstitutionUserId the UUID of the financial institution user
     * @return the financial institution user
     * @throws ResourceNotFoundException when the provided ID is not known
     */
    FinancialInstitutionUser getFinancialInstitutionUser(UUID financialInstitutionUserId) throws ResourceNotFoundException;

    /**
     * Create a new financial institution user
     * @param financialInstitutionUser the details of the financial institution user
     * @return the newly created financial institution user details
     */
    FinancialInstitutionUser createFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser);

    /**
     * Update an existing financial institution user
     * @param financialInstitutionUser to details of the financial institution user to be updated
     * @return the updated version
     */
    FinancialInstitutionUser updateFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser);

    /**
     * Delete a financial institution user
     * @param financialInstitutionUserId the id of the financial institution user
     * @throws ResourceNotFoundException when the provided ID is not known
     */
    void deleteFinancialInstitutionUser(UUID financialInstitutionUserId) throws ResourceNotFoundException;
}
