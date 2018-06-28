package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FinancialInstitutionAccountsServiceImpl Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 13, 2018</pre>
 */
public class FinancialInstitutionAccountsServiceTest extends AbstractServiceTest {

    @BeforeEach
    public void before() {
    }

    @AfterEach
    public void after() {
    }

    /**
     * Method: getFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId)
     */
    @Test
    public void testGetFinancialInstitutionAccount() throws Exception {
        FinancialInstitution financialInstitution = createFinancialInstitution(null);
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);

        FinancialInstitutionAccount financialInstitutionAccountGet = financialInstitutionAccountsService.getFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());

        assertTrue(financialInstitutionAccountGet.equals(financialInstitutionAccount));

        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionAccountWithWrongIDs() throws Exception {
        try {
            financialInstitutionAccountsService.getFinancialInstitutionAccount(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }

    /**
     * Method: getFinancialInstitutionUserAccounts(UUID financialInstitutionId, UUID financialInstitutionUserId)
     */
    @Test
    public void testGetFinancialInstitutionUserAccounts() throws Exception {
        FinancialInstitution financialInstitution = createFinancialInstitution(null);
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);

        List<FinancialInstitutionAccount> financialInstitutionAccounts = financialInstitutionAccountsService.getFinancialInstitutionUserAccounts(financialInstitution.getId(), financialInstitutionUser.getId());

        assertTrue(financialInstitutionAccounts.size() > 0);

        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionUserAccountsWithWrongIDs() throws Exception {
        try {
            financialInstitutionAccountsService.getFinancialInstitutionUserAccounts(UUID.randomUUID(), UUID.randomUUID());
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }

    /**
     * Method: createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount)
     */
    @Test
    public void testCreateFinancialInstitutionAccount() throws Exception {
        createFinancialInstitutionAccount(null);
    }

    @Test
    public void testCreateFinancialInstitutionAccountIdempotency() throws Exception {
        createFinancialInstitutionAccount(UUID.randomUUID());
    }

    private void createFinancialInstitutionAccount(UUID idempotency) throws Exception {
        FinancialInstitution financialInstitution = createFinancialInstitution(idempotency);
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(idempotency);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), idempotency);
        FinancialInstitutionAccount financialInstitutionGet = financialInstitutionAccountsService.getFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());

        assertTrue(financialInstitutionAccount.getCurrency().equals(financialInstitutionGet.getCurrency()));
        assertTrue(financialInstitutionAccount.getReference().equals(financialInstitutionGet.getReference()));
        assertTrue(financialInstitutionAccount.getDescription().equals(financialInstitutionGet.getDescription()));
        assertTrue(financialInstitutionAccount.getReferenceType().equals(financialInstitutionGet.getReferenceType()));
        assertTrue(financialInstitutionAccount.getSubType().equals(financialInstitutionGet.getSubType()));
        assertTrue(financialInstitutionAccount.getFinancialInstitution().getId().equals(financialInstitution.getId()));

        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());

    }

    /**
     * Method: createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount)
     */
    @Test
    public void testCreateFinancialInstitutionAccountWithWrongIDs() throws Exception {
        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setId(UUID.randomUUID());
        try {
            createFinancialInstitutionAccount(financialInstitution, UUID.randomUUID(), null);
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }

    /**
     * Method: deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId)
     */
    @Test
    public void testDeleteFinancialInstitutionAccount() throws Exception {
        FinancialInstitution financialInstitution = createFinancialInstitution(null);
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);

        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    /**
     * Method: deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId)
     */
    @Test
    public void testDeleteFinancialInstitutionAccountWithWrongIDs() throws Exception {
        try {
            financialInstitutionAccountsService.deleteFinancialInstitutionAccount(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }
}
