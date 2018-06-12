package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.FORWARD_SLASH;
import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.SANBOX_PREFIX_PATH;

public class FinancialInstitutionAccountsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionAccountsService {

    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionAccountsServiceImpl.class);

    private static final String SANDBOX_ACCOUNTS_FI_REQUEST_PATH                = SANBOX_PREFIX_PATH+ FORWARD_SLASH + FINANCIAL_INSTITUTIONS_PATH + FORWARD_SLASH + FINANCIAL_INSTITUTION_ID_TAG;
    private static final String SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH           = SANDBOX_ACCOUNTS_FI_REQUEST_PATH + FORWARD_SLASH + "financial-institution-users" + FORWARD_SLASH + USER_ID_TAG;

    @Override
    public FinancialInstitutionAccount getFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException {
        ResourceRepositoryV2<FinancialInstitutionAccount, UUID> accountsRepo = getAccountsRepository(financialInstitutionId, financialInstitutionUserId);
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionAccount.class);
        try {
            return accountsRepo.findOne(financialInstitutionAccountId, querySpec);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+financialInstitutionAccountId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public List<FinancialInstitutionAccount> getFinancialInstitutionUserAccounts(UUID financialInstitutionId, UUID financialInstitutionUserId) {
        return getAccountsRepository(financialInstitutionId, financialInstitutionUserId).findAll(new QuerySpec(FinancialInstitutionAccount.class));
    }

    @Override
    public FinancialInstitutionAccount createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount){
        return getAccountsRepository(financialInstitutionId, financialInstitutionUserId).create(financialInstitutionAccount);
    }

    @Override
    public void deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) {
        getAccountsRepository(financialInstitutionId, financialInstitutionUserId).delete(financialInstitutionAccountId);
    }

    protected ResourceRepositoryV2<FinancialInstitutionAccount, UUID> getAccountsRepository(UUID financialInstitutionId, UUID financialInstitutionUserId){
        String correctPath = SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(USER_ID_TAG, financialInstitutionUserId.toString())
                ;
        return getApiClient(correctPath, null).getRepositoryForType(FinancialInstitutionAccount.class);
    }
}
