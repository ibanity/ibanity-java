package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionUserReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionUsersReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.update.FinancialInstitutionUserUpdateQuery;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionUsersServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FinancialInstitutionUsersServiceTest extends AbstractServiceTest {
    private static Logger LOGGER = LogManager.getLogger(FinancialInstitutionUsersServiceTest.class);

    private static final FinancialInstitutionUsersService financialInstitutionUsersService = new FinancialInstitutionUsersServiceImpl();

    @Test
    public void testGetFinancialInstitutionUsers() {
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        FinancialInstitutionUsersReadQuery usersReadQuery =
                FinancialInstitutionUsersReadQuery.builder().build();

        List<FinancialInstitutionUser> financialInstitutionUsers =
                financialInstitutionUsersService.list(usersReadQuery);

        assertTrue(financialInstitutionUsers.size() > 0);

        super.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
    }

    @Test
    public void testGetFinancialInstitutionUsersPagingSpec() {
        List<FinancialInstitutionUser> financialInstitutionUsers =  new ArrayList<>();
        for( int index = 0; index < 3; index ++) {
            financialInstitutionUsers.add(createFinancialInstitutionUser(null));
        }

        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(3L);

        FinancialInstitutionUsersReadQuery usersReadQuery =
                FinancialInstitutionUsersReadQuery.builder()
                .pagingSpec(pagingSpec)
                .build();

        List<FinancialInstitutionUser> financialInstitutionUsersList =
                financialInstitutionUsersService.list(usersReadQuery);

        assertEquals(3, financialInstitutionUsersList.size());

        for (FinancialInstitutionUser financialInstitutionUser : financialInstitutionUsers) {
            super.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        }
    }

    @Test
    public void testGetFinancialInstitutionUser() {
        getFinancialInstutionUser(null);
    }

    private void getFinancialInstutionUser(UUID idempotencyKey) {
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(idempotencyKey);

        FinancialInstitutionUserReadQuery userReadQuery =
                FinancialInstitutionUserReadQuery.builder()
                .financialInstitutionUserId(financialInstitutionUser.getId())
                .build();

        FinancialInstitutionUser financialInstitutionUserGet = 
                financialInstitutionUsersService.find(userReadQuery);

        assertEquals(financialInstitutionUserGet, financialInstitutionUser);

        super.deleteFinancialInstitutionUser(financialInstitutionUserGet.getId());
    }

    @Test
    public void testGetFinancialInstitutionUserUnknown() {
        try {
            FinancialInstitutionUserReadQuery userReadQuery = 
                    FinancialInstitutionUserReadQuery.builder()
                    .financialInstitutionUserId(UUID.randomUUID())
                    .build();
            
            financialInstitutionUsersService.find(userReadQuery);
            fail("Expected financialInstitutionUsersService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }

    @Test
    public void testCreateFinancialInstitutionUser() {
        getFinancialInstutionUser(null);
    }

    @Test
    public void testCreateFinancialInstitutionUserIdempotency() {
        getFinancialInstutionUser(UUID.randomUUID());
    }

    @Test
    public void testUpdateFinancialInstitutionUser() {
        updateFinancialInstitutionUser(null);
    }

    @Test
    public void testUpdateFinancialInstitutionUserIndempotency() {
        updateFinancialInstitutionUser(UUID.randomUUID());
    }

    private void updateFinancialInstitutionUser(UUID idempotencyKey) {
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(idempotencyKey);

        FinancialInstitutionUserUpdateQuery financialInstitutionUserUpdateQuery =
                FinancialInstitutionUserUpdateQuery.from(financialInstitutionUser)
                        .password("New Password-" + UUID.randomUUID().toString())
                        .idempotencyKey(idempotencyKey)
                        .build();
        
        FinancialInstitutionUser updatedFinancialInstitutionUser =
                financialInstitutionUsersService.update(financialInstitutionUserUpdateQuery);

        assertNotEquals(financialInstitutionUser.getPassword(), updatedFinancialInstitutionUser.getPassword());
        assertEquals(financialInstitutionUser.getFirstName(), updatedFinancialInstitutionUser.getFirstName());
        assertEquals(financialInstitutionUser.getLastName(), updatedFinancialInstitutionUser.getLastName());
        assertEquals(financialInstitutionUser.getLogin(), updatedFinancialInstitutionUser.getLogin());
        assertEquals(financialInstitutionUser.getCreatedAt(), updatedFinancialInstitutionUser.getCreatedAt());
        assertNotEquals(financialInstitutionUser.getUpdatedAt(), updatedFinancialInstitutionUser.getUpdatedAt());
        assertTrue(updatedFinancialInstitutionUser.getUpdatedAt().isAfter(financialInstitutionUser.getUpdatedAt()));
        assertNull(updatedFinancialInstitutionUser.getDeletedAt());

        super.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
    }

    @Test
    public void testDeleteFinancialInstitutionUser() {
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        super.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
    }

    @Test
    public void testDeleteUnknownFinancialInstitutionUser() {
        try {
            super.deleteFinancialInstitutionUser(UUID.randomUUID());
            fail("testDeleteUnknownFinancialInstitutionUser didn't raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }
    
}
