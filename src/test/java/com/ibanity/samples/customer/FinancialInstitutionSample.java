package com.ibanity.samples.customer;

import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.factory.read.FinancialInstitutionsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.FinancialInstitutionsService;

import java.util.List;

public class FinancialInstitutionSample {

    private final FinancialInstitutionsService financialInstitutionsService;

    public FinancialInstitutionSample(IbanityService ibanityService) {
        financialInstitutionsService = ibanityService.xs2aService().financialInstitutionsService();
    }

    public List<FinancialInstitution> list() {
        return financialInstitutionsService.list(FinancialInstitutionsReadQuery.builder().build()).getItems();
    }
}
