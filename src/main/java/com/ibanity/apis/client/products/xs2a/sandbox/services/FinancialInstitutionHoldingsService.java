package com.ibanity.apis.client.products.xs2a.sandbox.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionHolding;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionHoldingCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionHoldingDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionHoldingReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionHoldingsReadQuery;

public interface FinancialInstitutionHoldingsService {

    FinancialInstitutionHolding find(FinancialInstitutionHoldingReadQuery holdingReadQuery);

    IbanityCollection<FinancialInstitutionHolding> list(FinancialInstitutionHoldingsReadQuery holdingsReadQuery);

    FinancialInstitutionHolding create(FinancialInstitutionHoldingCreationQuery holdingCreationQuery);

    FinancialInstitutionHolding delete(FinancialInstitutionHoldingDeleteQuery holdingDeleteQuery);
}
