package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.services.FinancialInstitutionsService;

import java.util.UUID;

public interface SandboxFinancialInstitutionsService extends FinancialInstitutionsService {

    FinancialInstitution createFinancialInstitution(FinancialInstitution financialInstitution);

    FinancialInstitution createFinancialInstitution(FinancialInstitution financialInstitution, UUID idempotency);

    FinancialInstitution updateFinancialInstitution(FinancialInstitution financialInstitution);

    FinancialInstitution updateFinancialInstitution(FinancialInstitution financialInstitution, UUID idempotency);

    void deleteFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException;
}
