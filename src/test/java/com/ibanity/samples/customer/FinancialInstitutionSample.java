package com.ibanity.samples.customer;

import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionsReadQuery;
import com.ibanity.apis.client.services.FinancialInstitutionsService;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;

import java.util.List;

public class FinancialInstitutionSample {
    private final FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl(IbanityService.apiUrlProvider(), IbanityService.ibanityHttpClient());

    public List<FinancialInstitution> list() {
        return financialInstitutionsService.list(FinancialInstitutionsReadQuery.builder().build()).getItems();
    }
}
