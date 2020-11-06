package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.models.Collection;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitutionCountry;
import com.ibanity.apis.client.products.xs2a.models.read.FinancialInstitutionCountriesReadQuery;

public interface FinancialInstitutionCountriesService {

    Collection<FinancialInstitutionCountry> list(FinancialInstitutionCountriesReadQuery financialInstitutionCountriesReadQuery);
}
