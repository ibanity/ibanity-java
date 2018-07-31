package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionUsersServiceImpl;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FinancialInstitutionUsersServiceTest extends AbstractServiceTest {

    private static final FinancialInstitutionUsersService financialInstitutionUsersService = new FinancialInstitutionUsersServiceImpl();

    @Test
    public void testGetFinancialInstitutionUsers() throws ApiErrorsException {
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);
        List<FinancialInstitutionUser> financialInstitutionUsers = financialInstitutionUsersService.list();
        assertTrue(financialInstitutionUsers.size() > 0);
        financialInstitutionUsersService.delete(financialInstitutionUser.getId());
    }

    @Test
    public void testGetFinancialInstitutionUsersPagingSpec() throws ApiErrorsException {
        List<FinancialInstitutionUser> financialInstitutionUsers =  new ArrayList<>();
        for( int index = 0; index < 3; index ++){
            financialInstitutionUsers.add(createFinancialInstitutionUser(null));
        }

        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(3L);
        List<FinancialInstitutionUser> financialInstitutionUsersList = financialInstitutionUsersService.list(pagingSpec);
        assertEquals(3, financialInstitutionUsersList.size());
        for (FinancialInstitutionUser financialInstitutionUser :  financialInstitutionUsers){
            financialInstitutionUsersService.delete(financialInstitutionUser.getId());
        }
    }

    @Test
    public void testGetFinancialInstitutionUser() throws ApiErrorsException {
        getFinancialInstutionUser(null);
    }

    private void getFinancialInstutionUser(UUID indempotency) throws ApiErrorsException {
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(indempotency);
        FinancialInstitutionUser financialInstitutionUserGet = financialInstitutionUsersService.find(financialInstitutionUser.getId());
        financialInstitutionUsersService.delete(financialInstitutionUserGet.getId());
        assertEquals(financialInstitutionUserGet, financialInstitutionUser);
    }

    @Test
    public void testGetFinancialInstitutionUserUnknown() throws ApiErrorsException {
        try {
            financialInstitutionUsersService.find(UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitutionUser.RESOURCE_TYPE)).count());
        }
    }

    @Test
    public void testCreateFinancialInstitutionUser() {
        getFinancialInstutionUser(null);
    }

    @Test
    public void testCreateFinancialInstitutionUserIdenmpotency() {
        getFinancialInstutionUser(UUID.randomUUID());
    }

    @Test
    public void testUpdateFinancialInstitutionUser() throws ApiErrorsException {
        updateFinancialInstitutionUser(null);
    }

    @Test
    public void testUpdateFinancialInstitutionUserIndempotency() throws ApiErrorsException {
        updateFinancialInstitutionUser(UUID.randomUUID());
    }

    private void updateFinancialInstitutionUser(UUID idempotencyKey) throws ApiErrorsException {
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(idempotencyKey);
        financialInstitutionUser.setPassword("Password");
        FinancialInstitutionUser updatedFinancialInstitutionUser;
        if (idempotencyKey == null) {
            updatedFinancialInstitutionUser = financialInstitutionUsersService.update(financialInstitutionUser);
        } else {
            updatedFinancialInstitutionUser = financialInstitutionUsersService.update(financialInstitutionUser, idempotencyKey);
        }
        assertEquals(financialInstitutionUser.getPassword(), updatedFinancialInstitutionUser.getPassword());
        assertEquals(financialInstitutionUser.getFirstName(), updatedFinancialInstitutionUser.getFirstName());
        assertEquals(financialInstitutionUser.getLastName(), updatedFinancialInstitutionUser.getLastName());
        assertEquals(financialInstitutionUser.getLogin(), updatedFinancialInstitutionUser.getLogin());
        assertEquals(financialInstitutionUser.getCreatedAt(), updatedFinancialInstitutionUser.getCreatedAt());
        assertNotEquals(financialInstitutionUser.getUpdatedAt(), updatedFinancialInstitutionUser.getUpdatedAt());
        assertTrue(updatedFinancialInstitutionUser.getUpdatedAt().isAfter(financialInstitutionUser.getUpdatedAt()));
        assertNull(updatedFinancialInstitutionUser.getDeletedAt());
        financialInstitutionUsersService.delete(financialInstitutionUser.getId());
    }

    @Test
    public void testDeleteFinancialInstitutionUser() {
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);
        financialInstitutionUsersService.delete(financialInstitutionUser.getId());
    }

    @Test
    public void testDeleteUnknownFinancialInstitutionUser() {
        try {
            financialInstitutionUsersService.delete(UUID.randomUUID());
            fail("testDeleteUnknownFinancialInstitutionUser didn't raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitutionUser.RESOURCE_TYPE)).count());
        }
    }
}
