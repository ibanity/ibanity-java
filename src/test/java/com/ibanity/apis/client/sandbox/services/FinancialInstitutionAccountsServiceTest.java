package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FinancialInstitutionAccountsServiceTest extends AbstractServiceTest {

    @Test
    public void testGetFinancialInstitutionAccount() {
        FinancialInstitution financialInstitution = createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);

        FinancialInstitutionAccount financialInstitutionAccountGet = financialInstitutionAccountsService.find(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());

        assertEquals(financialInstitutionAccount, financialInstitutionAccountGet);

        financialInstitutionAccountsService.delete(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionAccountWithWrongIDs() {
        try {
            financialInstitutionAccountsService.find(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }

    @Test
    public void testGetFinancialInstitutionUserAccounts() {
        FinancialInstitution financialInstitution = createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);

        List<FinancialInstitutionAccount> financialInstitutionAccounts = financialInstitutionAccountsService.list(financialInstitution.getId(), financialInstitutionUser.getId());

        assertTrue(financialInstitutionAccounts.size() > 0);

        financialInstitutionAccountsService.delete(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionUserAccountsWithWrongIDs() {
        try {
            financialInstitutionAccountsService.list(UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }

    @Test
    public void testCreateFinancialInstitutionAccount() {
        createFinancialInstitutionAccount(null);
    }

    @Test
    public void testCreateFinancialInstitutionAccountIdempotency() {
        createFinancialInstitutionAccount(UUID.randomUUID());
    }

    private void createFinancialInstitutionAccount(UUID idempotencyKey) {
        FinancialInstitution financialInstitution = createFinancialInstitution(idempotencyKey);
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(idempotencyKey);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), idempotencyKey);
        FinancialInstitutionAccount financialInstitutionGet = financialInstitutionAccountsService.find(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());

        assertEquals(financialInstitutionAccount.getCurrency(), financialInstitutionGet.getCurrency());
        assertEquals(financialInstitutionAccount.getReference(), financialInstitutionGet.getReference());
        assertEquals(financialInstitutionAccount.getDescription(), financialInstitutionGet.getDescription());
        assertEquals(financialInstitutionAccount.getReferenceType(), financialInstitutionGet.getReferenceType());
        assertEquals(financialInstitutionAccount.getSubType(), financialInstitutionGet.getSubType());
        assertEquals(financialInstitution.getId(), financialInstitutionAccount.getFinancialInstitution().getId());

        financialInstitutionAccountsService.delete(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());

    }

    @Test
    public void testCreateFinancialInstitutionAccountWithWrongIDs() {
        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setId(UUID.randomUUID());
        try {
            createFinancialInstitutionAccount(financialInstitution, UUID.randomUUID(), null);
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }

    @Test
    public void testDeleteFinancialInstitutionAccount() {
        FinancialInstitution financialInstitution = createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);

        financialInstitutionAccountsService.delete(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testDeleteFinancialInstitutionAccountWithWrongIDs() {
        try {
            financialInstitutionAccountsService.delete(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }
}
