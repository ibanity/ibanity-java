package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.helpers.DockerHelper;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.read.AccountReadQuery;
import com.ibanity.apis.client.models.factory.read.AccountsReadQuery;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AccountsServiceTest extends AbstractServiceTest {
    private static final Logger LOGGER = LogManager.getLogger(AccountsServiceTest.class);

    @BeforeEach
    public void createData(){
        this.initPublicAPIEnvironment();
    }

    @AfterEach
    public void deleteData(){
        this.cleanPublicAPIEnvironment();
    }

    @BeforeAll
    public static void dockerPull() throws Exception{
        DockerHelper.pullImage();
    }

    @Test
    public void testGetCustomerAccount() {
        AccountInformationAccessRequest accountInformationAccessRequest = createAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());

        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .build();

        List<Account> accountsList = accountsService.list(accountsReadQuery);
        if (accountsList.size() == 0) {
            fail("authorized accounts list size is 0");
        }
        for (Account account : accountsList) {
            AccountReadQuery accountReadQuery = AccountReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(financialInstitution.getId())
                    .accountId(account.getId())
                    .build();

            Account accountResult = accountsService.find(accountReadQuery);
            assertEquals(account.getReference(), accountResult.getReference());
            assertEquals(account.getReferenceType(), accountResult.getReferenceType());
            assertEquals(account.getCurrency(), accountResult.getCurrency());
            assertEquals(account.getSubType(), accountResult.getSubType());
            assertEquals(account.getAvailableBalance(), accountResult.getAvailableBalance());
            assertEquals(account.getCurrentBalance(), accountResult.getCurrentBalance());
        }
    }

    @Test
    public void testGetCustomerAccountWithWrongIDs() {
        try {
            AccountReadQuery accountReadQuery = AccountReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(UUID.randomUUID())
                    .accountId(UUID.randomUUID())
                    .build();
            accountsService.find(accountReadQuery);
            fail("Expected accountsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, Account.RESOURCE_TYPE);
        }
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessToken() {
        AccountInformationAccessRequest accountInformationAccessRequest = createAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(50L);

        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .pagingSpec(pagingSpec)
                .build();

        List<Account> accountsList = accountsService.list(accountsReadQuery);
        assertEquals(financialInstitutionAccounts.size(), accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessTokenDefaultPagingSpec() {
        AccountInformationAccessRequest accountInformationAccessRequest = createAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());

        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .build();

        List<Account> accountsList = accountsService.list(accountsReadQuery);
        assertEquals(financialInstitutionAccounts.size(), accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessTokenNoAccountsAuthorized() {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(50L);

        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(createCustomerAccessToken(UUID.randomUUID().toString()).getToken())
                .pagingSpec(pagingSpec)
                .build();

        List<Account> accountsList = accountsService.list(accountsReadQuery);
        assertTrue(accountsList.isEmpty());
        assertFalse(financialInstitutionAccounts.size() == accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessTokenPagingSpec() {
        AccountInformationAccessRequest accountInformationAccessRequest =
                createAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());

        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);

        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .pagingSpec(pagingSpec)
                .build();

        List<Account> accountsList = accountsService.list(accountsReadQuery);
        assertEquals(1, accountsList.size());
        Account account = accountsList.get(0);
        assertEquals(1, financialInstitutionAccounts.stream().filter(financialInstitutionAccount -> financialInstitutionAccount.getReference().equals(account.getReference())).collect(Collectors.toList()).size());
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionId() {
        AccountInformationAccessRequest accountInformationAccessRequest = createAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());

        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .build();

        List<Account> accountsList = accountsService.list(accountsReadQuery);
        assertEquals(financialInstitutionAccounts.size(), accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenUnknownFinancialInstitutionId() {
        try {
            AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(UUID.randomUUID())
                    .build();

            accountsService.list(accountsReadQuery);
            fail("Expected accountsService.list to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, Account.RESOURCE_TYPE);
        }
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionIdPagingSpec() {
        AccountInformationAccessRequest accountInformationAccessRequest = createAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);

        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .pagingSpec(pagingSpec)
                .build();

        List<Account> accountsList = accountsService.list(accountsReadQuery);
        Account account = accountsList.get(0);
        assertEquals(1, financialInstitutionAccounts.stream()
                .filter(financialInstitutionAccount -> financialInstitutionAccount.getReference().equals(account.getReference()))
                .collect(Collectors.toList()).size());
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenUnknownFinancialInstitutionIdPagingSpecErrorsContent() {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        try {
            AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(UUID.randomUUID())
                    .pagingSpec(pagingSpec)
                    .build();

            accountsService.list(accountsReadQuery);
            fail("Expected accountsService.list to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }
}
