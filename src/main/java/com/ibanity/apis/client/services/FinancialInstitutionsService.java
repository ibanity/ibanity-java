package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionsService {

    List<FinancialInstitution> list(String customerAccessToken);

    List<FinancialInstitution> list(String customerAccessToken, IbanityPagingSpec pagingSpec);

    List<FinancialInstitution> list();

    List<FinancialInstitution> list(IbanityPagingSpec pagingSpec);

    FinancialInstitution find(UUID financialInstitutionId);
}
