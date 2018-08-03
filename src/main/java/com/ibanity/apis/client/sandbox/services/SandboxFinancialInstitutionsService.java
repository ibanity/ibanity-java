package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.update.FinancialInstitutionUpdateQuery;
import com.ibanity.apis.client.services.FinancialInstitutionsService;

public interface SandboxFinancialInstitutionsService extends FinancialInstitutionsService {

    FinancialInstitution create(FinancialInstitutionCreationQuery financialInstitutionCreationQuery);

    FinancialInstitution update(FinancialInstitutionUpdateQuery financialInstitutionUpdateQuery);

    void delete(FinancialInstitutionDeleteQuery financialInstitutionDeleteQuery);
}
