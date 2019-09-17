package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.models.FinancialInstitution;
import com.ibanity.apis.client.products.ponto_connect.models.read.FinancialInstitutionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.FinancialInstitutionsReadQuery;

public interface FinancialInstitutionService {

    FinancialInstitution find(FinancialInstitutionReadQuery financialInstitutionReadQuery);

    IbanityCollection<FinancialInstitution> list(FinancialInstitutionsReadQuery financialInstitutionsReadQuery);
}
