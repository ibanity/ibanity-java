package com.ibanity.apis.client.products.xs2a.sandbox.services;

import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update.FinancialInstitutionUpdateQuery;
import com.ibanity.apis.client.products.xs2a.services.FinancialInstitutionsService;

public interface SandboxFinancialInstitutionsService extends FinancialInstitutionsService {

    FinancialInstitution create(FinancialInstitutionCreationQuery financialInstitutionCreationQuery);

    FinancialInstitution update(FinancialInstitutionUpdateQuery financialInstitutionUpdateQuery);

    FinancialInstitution delete(FinancialInstitutionDeleteQuery financialInstitutionDeleteQuery);
}
