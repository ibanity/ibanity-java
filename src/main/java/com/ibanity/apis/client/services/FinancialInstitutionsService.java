package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import io.crnk.core.resource.list.ResourceList;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionsService {

    ResourceList<FinancialInstitution> getFinancialInstitutions(CustomerAccessToken customerAccessToken);

    List<FinancialInstitution> getFinancialInstitutions(CustomerAccessToken customerAccessToken, IbanityPagingSpec pagingSpec);

    List<FinancialInstitution> getFinancialInstitutions();

    List<FinancialInstitution> getFinancialInstitutions(IbanityPagingSpec pagingSpec);

    FinancialInstitution getFinancialInstitution(UUID financialInstitutionId) throws ApiErrorsException;
}
