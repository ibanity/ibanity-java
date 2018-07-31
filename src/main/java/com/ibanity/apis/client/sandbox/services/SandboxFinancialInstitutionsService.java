package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.services.FinancialInstitutionsService;

import java.util.UUID;

public interface SandboxFinancialInstitutionsService extends FinancialInstitutionsService {

    FinancialInstitution create(String name);

    FinancialInstitution create(String name, UUID idempotencyKey);

    FinancialInstitution update(FinancialInstitution financialInstitution);

    FinancialInstitution update(FinancialInstitution financialInstitution, UUID idempotencyKey);

    void delete(UUID financialInstitutionId);
}
