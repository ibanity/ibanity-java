package com.ibanity.api.impl;

import com.ibanity.api.FinancialInstitutionsService;
import com.ibanity.exceptions.ResourceNotFoundException;
import com.ibanity.models.AccountInformationAccessRequest;
import com.ibanity.models.CustomerAccessToken;
import com.ibanity.models.FinancialInstitution;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class FinancialInstitutionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionsService{
    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionsServiceImpl.class);

    private static final String ACCOUNT_INFORMATION_ACCESS_REQUEST_PATH = "/customer/financial-institutions/"+FINANCIAL_INSTITUTION_ID_TAG;

    private final ResourceRepositoryV2<FinancialInstitution, UUID> financialInstitutionsRepo;

    public FinancialInstitutionsServiceImpl() {
        super();
        financialInstitutionsRepo = getApiClient("/").getRepositoryForType(FinancialInstitution.class);
    }

    @Override
    public List<FinancialInstitution> getFinancialInstitutions() {
        return financialInstitutionsRepo.findAll(new QuerySpec(FinancialInstitution.class));
    }

    @Override
    public FinancialInstitution getFinancialInstitution(UUID id) throws ResourceNotFoundException {
        try {
            return financialInstitutionsRepo.findOne(id, new QuerySpec(FinancialInstitution.class));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+id+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public AccountInformationAccessRequest getAccountInformationAccessRedirectUrl(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, AccountInformationAccessRequest accountInformationAccessRequest) {
        String correctPath = ACCOUNT_INFORMATION_ACCESS_REQUEST_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());
        ResourceRepositoryV2<AccountInformationAccessRequest, UUID> accountInformationAccessRequestRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(AccountInformationAccessRequest.class);
        AccountInformationAccessRequest result = accountInformationAccessRequestRepo.create(accountInformationAccessRequest);
        return result;
    }
}
