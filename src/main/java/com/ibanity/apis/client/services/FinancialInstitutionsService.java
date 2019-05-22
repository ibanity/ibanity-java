package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionReadQuery;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionsReadQuery;

public interface FinancialInstitutionsService {

    IbanityCollection<FinancialInstitution> list(FinancialInstitutionsReadQuery financialInstitutionsReadQuery);

    FinancialInstitution find(FinancialInstitutionReadQuery financialInstitutionReadQuery);
}
