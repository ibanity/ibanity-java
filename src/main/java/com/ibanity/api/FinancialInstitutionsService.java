package com.ibanity.api;

import com.ibanity.exceptions.ResourceNotFoundException;
import com.ibanity.models.FinancialInstitution;

import java.util.List;
import java.util.UUID;

/**
 * Service for Financial Institutions related APIs
 */
public interface FinancialInstitutionsService {
    /**
     * Get all Financial Institutions supported by iBanity
     * @return List of Financial Institutions
     */
    List<FinancialInstitution> getFinancialInstitutions();

    /**
     * Get the details of a specific Financial Institution
     * @param financialInstitutionId the iBanity UUID of the Financial Institution
     * @return The Financial Institution Details
     * @throws ResourceNotFoundException when the provided ID is not known
     */
    FinancialInstitution getFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException;
}
