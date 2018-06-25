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

    private static final String ERROR_RESOURCE_NOT_FOUND                        = "Resource with provided IDs not found";

    @Override
    public FinancialInstitutionAccount getFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException {
        ResourceRepositoryV2<FinancialInstitutionAccount, UUID> accountsRepo = getAccountsRepository(financialInstitutionId, financialInstitutionUserId, null);
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
    public List<FinancialInstitutionAccount> getFinancialInstitutionUserAccounts(UUID financialInstitutionId, UUID financialInstitutionUserId) throws ResourceNotFoundException{
        try {
            return getAccountsRepository(financialInstitutionId, financialInstitutionUserId, null).findAll(new QuerySpec(FinancialInstitutionAccount.class));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            LOGGER.debug(ERROR_RESOURCE_NOT_FOUND);
            throw new ResourceNotFoundException(ERROR_RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public FinancialInstitutionAccount createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount) throws ResourceNotFoundException {
        return create(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccount, null);
    }

    private FinancialInstitutionAccount create(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount, UUID idempotency) throws ResourceNotFoundException {
        try {
            return getAccountsRepository(financialInstitutionId, financialInstitutionUserId, idempotency).create(financialInstitutionAccount);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            LOGGER.debug(ERROR_RESOURCE_NOT_FOUND);
            throw new ResourceNotFoundException(ERROR_RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public FinancialInstitutionAccount createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount, UUID idempotency) throws ResourceNotFoundException {
        return create(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccount, idempotency);
    }

    @Override
    public void deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException {
        try {
            getAccountsRepository(financialInstitutionId, financialInstitutionUserId, null).delete(financialInstitutionAccountId);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            LOGGER.debug(ERROR_RESOURCE_NOT_FOUND);
            throw new ResourceNotFoundException(ERROR_RESOURCE_NOT_FOUND);
        }
    }

    protected ResourceRepositoryV2<FinancialInstitutionAccount, UUID> getAccountsRepository(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID idempotency){
        String correctPath = SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(USER_ID_TAG, financialInstitutionUserId.toString())
                ;
        return getApiClient(correctPath, null, idempotency).getRepositoryForType(FinancialInstitutionAccount.class);
    }
}
