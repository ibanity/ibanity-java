package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionAccountsServiceImpl;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;

/**
 * FinancialInstitutionAccountsServiceImpl Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 13, 2018</pre>
 */
public class FinancialInstitutionAccountsServiceTest {

    private static final FinancialInstitutionAccountsService financialInstitutionAccountsService = new FinancialInstitutionAccountsServiceImpl();

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    /**
     * Method: getFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId)
     */
    @Test
    public void testGetFinancialInstitutionAccount() throws Exception {
        FinancialInstitution financialInstitution = SandboxFinancialInstitutionsServiceTest.createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = FinancialInstitutionUsersServiceTest.createFinancialInstitutionUser();

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId());

        FinancialInstitutionAccount financialInstitutionAccountGet = financialInstitutionAccountsService.getFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());

        assertTrue(financialInstitutionAccountGet.equals(financialInstitutionAccount));

        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        FinancialInstitutionUsersServiceTest.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        SandboxFinancialInstitutionsServiceTest.deleteFinancialInstitution(financialInstitution.getId());
    }

    /**
     * Method: getFinancialInstitutionUserAccounts(UUID financialInstitutionId, UUID financialInstitutionUserId)
     */
    @Test
    public void testGetFinancialInstitutionUserAccounts() throws Exception {
        FinancialInstitution financialInstitution = SandboxFinancialInstitutionsServiceTest.createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = FinancialInstitutionUsersServiceTest.createFinancialInstitutionUser();

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId());

        List<FinancialInstitutionAccount> financialInstitutionAccounts = financialInstitutionAccountsService.getFinancialInstitutionUserAccounts(financialInstitution.getId(), financialInstitutionUser.getId());
        
        assertTrue(financialInstitutionAccounts.size() > 0);

        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        FinancialInstitutionUsersServiceTest.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        SandboxFinancialInstitutionsServiceTest.deleteFinancialInstitution(financialInstitution.getId());
    }

    /**
     * Method: createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount)
     */
    @Test
    public void testCreateFinancialInstitutionAccount() throws Exception {
        FinancialInstitution financialInstitution = SandboxFinancialInstitutionsServiceTest.createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = FinancialInstitutionUsersServiceTest.createFinancialInstitutionUser();

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId());
        FinancialInstitutionAccount financialInstitutionGet = financialInstitutionAccountsService.getFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());

        assertTrue(financialInstitutionAccount.getCurrency().equals(financialInstitutionGet.getCurrency()));
        assertTrue(financialInstitutionAccount.getReference().equals(financialInstitutionGet.getReference()));
        assertTrue(financialInstitutionAccount.getDescription().equals(financialInstitutionGet.getDescription()));
        assertTrue(financialInstitutionAccount.getReferenceType().equals(financialInstitutionGet.getReferenceType()));
        assertTrue(financialInstitutionAccount.getSubType().equals(financialInstitutionGet.getSubType()));
        assertTrue(financialInstitutionAccount.getFinancialInstitution().getId().equals(financialInstitution.getId()));

        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        FinancialInstitutionUsersServiceTest.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        SandboxFinancialInstitutionsServiceTest.deleteFinancialInstitution(financialInstitution.getId());
    }

    /**
     * Method: createFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount)
     */
    @Test (expected = ResourceNotFoundException.class)
    public void testCreateFinancialInstitutionAccountWithWrongIDs() throws Exception {
        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setId(UUID.randomUUID());
        createFinancialInstitutionAccount(financialInstitution, UUID.randomUUID());
    }

    /**
     * Method: deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId)
     */
    @Test
    public void testDeleteFinancialInstitutionAccount() throws Exception {
        FinancialInstitution financialInstitution = SandboxFinancialInstitutionsServiceTest.createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = FinancialInstitutionUsersServiceTest.createFinancialInstitutionUser();

        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId());

        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        FinancialInstitutionUsersServiceTest.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        SandboxFinancialInstitutionsServiceTest.deleteFinancialInstitution(financialInstitution.getId());
    }

    /**
     * Method: deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId)
     */
    @Test (expected = ResourceNotFoundException.class)
    public void testDeleteFinancialInstitutionAccountWithWrongIDs() throws Exception {
        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    }

    public static void deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException {
        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId);
    }

    public static FinancialInstitutionAccount createFinancialInstitutionAccount(FinancialInstitution financialInstitution, UUID financialInstitutionUser) throws ResourceNotFoundException {
        FinancialInstitutionAccount financialInstitutionAccount = new FinancialInstitutionAccount();
        financialInstitutionAccount.setSubType("checking");
        financialInstitutionAccount.setReference(Iban.random(CountryCode.BE).toString());
        financialInstitutionAccount.setReferenceType("IBAN");
        financialInstitutionAccount.setDescription("Checking Account");
        financialInstitutionAccount.setCurrency("EUR");
        financialInstitutionAccount.setFinancialInstitution(financialInstitution);
        return financialInstitutionAccountsService.createFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser, financialInstitutionAccount);
    }
} 
