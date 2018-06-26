package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionsService {

    List<FinancialInstitution> getFinancialInstitutions();

    List<FinancialInstitution> getFinancialInstitutions(IbanityPagingSpec pagingSpec);

    FinancialInstitution getFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException;
}
