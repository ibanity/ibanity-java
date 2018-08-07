package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionReadQuery;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionsReadQuery;

import java.util.List;

public interface FinancialInstitutionsService {

    List<FinancialInstitution> list(FinancialInstitutionsReadQuery financialInstitutionsReadQuery);

    FinancialInstitution find(FinancialInstitutionReadQuery financialInstitutionReadQuery);
}
