package com.ibanity.api;

import com.ibanity.exceptions.ResourceNotFoundException;
import com.ibanity.models.AccountInformationAccessRequest;
import com.ibanity.models.CustomerAccessToken;
import com.ibanity.models.FinancialInstitution;

import java.util.List;
import java.util.UUID;

public interface FinancialInstitutionsService {
    List<FinancialInstitution> getFinancialInstitutions();
    FinancialInstitution getFinancialInstitution(UUID id) throws ResourceNotFoundException;
    AccountInformationAccessRequest getAccountInformationAccessRedirectUrl(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, AccountInformationAccessRequest accountInformationAccessRequest);
}
