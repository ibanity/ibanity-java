package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionAccountDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;
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

        FinancialInstitutionAccountReadQuery accountReadQuery =
                FinancialInstitutionAccountReadQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .build();

        FinancialInstitutionAccount financialInstitutionAccountGet =
                financialInstitutionAccountsService.find(accountReadQuery);

        assertEquals(financialInstitutionAccount, financialInstitutionAccountGet);

        FinancialInstitutionAccountDeleteQuery accountDeleteQuery =
                FinancialInstitutionAccountDeleteQuery.builder()
                .financialInstitutionId(financialInstitution.getId())
                .financialInstitutionUserId(financialInstitutionUser.getId())
                .financialInstitutionAccountId(financialInstitutionAccount.getId())
                .build();

        financialInstitutionAccountsService.delete(accountDeleteQuery);
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionAccountWithWrongIDs() {
        FinancialInstitutionAccountReadQuery accountReadQuery =
                FinancialInstitutionAccountReadQuery.builder()
                        .financialInstitutionId(UUID.randomUUID())
                        .financialInstitutionUserId(UUID.randomUUID())
                        .financialInstitutionAccountId(UUID.randomUUID())
                        .build();

        try {
            financialInstitutionAccountsService.find(accountReadQuery);
            fail("Expected financialInstitutionAccountsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }

    @Test
    public void testGetFinancialInstitutionUserAccounts() {
        FinancialInstitution financialInstitution = createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);

        FinancialInstitutionAccountsReadQuery accountsReadQuery =
                FinancialInstitutionAccountsReadQuery.builder()
                .financialInstitutionId(financialInstitution.getId())
                .financialInstitutionUserId(financialInstitutionUser.getId())
                .build();

        List<FinancialInstitutionAccount> financialInstitutionAccounts =
                        financialInstitutionAccountsService.list(accountsReadQuery);

        assertTrue(financialInstitutionAccounts.size() > 0);

        FinancialInstitutionAccountDeleteQuery accountDeleteQuery =
                FinancialInstitutionAccountDeleteQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .build();

        financialInstitutionAccountsService.delete(accountDeleteQuery);
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionUserAccountsWithWrongIDs() {
        try {
            FinancialInstitutionAccountsReadQuery accountsReadQuery =
                    FinancialInstitutionAccountsReadQuery.builder()
                            .financialInstitutionId(UUID.randomUUID())
                            .financialInstitutionUserId(UUID.randomUUID())
                            .build();

            financialInstitutionAccountsService.list(accountsReadQuery);
            fail("Expected financialInstitutionAccountsService.list to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
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

        FinancialInstitutionAccountReadQuery accountReadQuery =
                FinancialInstitutionAccountReadQuery.builder()
                .financialInstitutionId(financialInstitution.getId())
                .financialInstitutionUserId(financialInstitutionUser.getId())
                .financialInstitutionAccountId(financialInstitutionAccount.getId())
                .build();

        FinancialInstitutionAccount financialInstitutionGet =
                financialInstitutionAccountsService.find(accountReadQuery);

        assertEquals(financialInstitutionAccount.getCurrency(), financialInstitutionGet.getCurrency());
        assertEquals(financialInstitutionAccount.getReference(), financialInstitutionGet.getReference());
        assertEquals(financialInstitutionAccount.getDescription(), financialInstitutionGet.getDescription());
        assertEquals(financialInstitutionAccount.getReferenceType(), financialInstitutionGet.getReferenceType());
        assertEquals(financialInstitutionAccount.getSubType(), financialInstitutionGet.getSubType());
        assertEquals(financialInstitution.getId(), financialInstitutionAccount.getFinancialInstitution().getId());

        FinancialInstitutionAccountDeleteQuery accountDeleteQuery =
                FinancialInstitutionAccountDeleteQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .build();

        financialInstitutionAccountsService.delete(accountDeleteQuery);
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());

    }

    @Test
    public void testCreateFinancialInstitutionAccountWithWrongIDs() {
        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setId(UUID.randomUUID());
        try {
            createFinancialInstitutionAccount(financialInstitution, UUID.randomUUID(), null);
            fail("Expected createFinancialInstitutionAccount to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }

    @Test
    public void testDeleteFinancialInstitutionAccount() {
        FinancialInstitution financialInstitution = createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);

        FinancialInstitutionAccountDeleteQuery accountDeleteQuery =
                FinancialInstitutionAccountDeleteQuery.builder()
                .financialInstitutionId(financialInstitution.getId())
                .financialInstitutionUserId(financialInstitutionUser.getId())
                .financialInstitutionAccountId(financialInstitutionAccount.getId())
                .build();

        financialInstitutionAccountsService.delete(accountDeleteQuery);
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testDeleteFinancialInstitutionAccountWithWrongIDs() {
        FinancialInstitutionAccountDeleteQuery accountDeleteQuery =
                FinancialInstitutionAccountDeleteQuery.builder()
                        .financialInstitutionId(UUID.randomUUID())
                        .financialInstitutionUserId(UUID.randomUUID())
                        .financialInstitutionAccountId(UUID.randomUUID())
                        .build();

        try {
            financialInstitutionAccountsService.delete(accountDeleteQuery);
            fail("Expected financialInstitutionAccountsService.delete to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }
}
