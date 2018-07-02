package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.services.FinancialInstitutionsService;

import java.util.UUID;

public interface SandboxFinancialInstitutionsService extends FinancialInstitutionsService {

    FinancialInstitution create(String name);

    FinancialInstitution create(String name, UUID idempotencyKey);

    FinancialInstitution updateFinancialInstitution(FinancialInstitution financialInstitution);

    FinancialInstitution updateFinancialInstitution(FinancialInstitution financialInstitution, UUID idempotencyKey);

    void deleteFinancialInstitution(UUID financialInstitutionId) throws ApiErrorsException;
}
