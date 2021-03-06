package com.ibanity.samples.customer;

import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.read.FinancialInstitutionsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.FinancialInstitutionsService;
import com.ibanity.apis.client.services.IbanityService;

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
