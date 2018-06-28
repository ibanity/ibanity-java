package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AccountsServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 14, 2018</pre>
 */
public class AccountsServiceTest extends AbstractServiceTest {

    @BeforeEach
    public void beforeEach() throws Exception {
        initSelenium();
        initPublicAPIEnvironment();
    }

    @AfterEach
    public void afterEach() throws Exception {
        exitSelenium();
        cleanPublicAPIEnvironment();
    }

    /**
     * Method: getCustomerAccount(CustomerAccessToken customerAccessToken, UUID accountId, UUID financialInstitutionId)
     */
    @Test
    public void testGetCustomerAccount() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, financialInstitution.getId());
        for (Account account : accountsList) {
            Account accountResult = accountsService.getCustomerAccount(generatedCustomerAccessToken, account.getId(), financialInstitution.getId());
            assertTrue(account.getReference().equals(accountResult.getReference()));
            assertTrue(account.getReferenceType().equals(accountResult.getReferenceType()));
            assertTrue(account.getCurrency().equals(accountResult.getCurrency()));
            assertTrue(account.getSubType().equals(accountResult.getSubType()));
            assertTrue(account.getAvailableBalance().equals(accountResult.getAvailableBalance()));
            assertTrue(account.getCurrentBalance().equals(accountResult.getCurrentBalance()));
        }
    }

    @Test
    public void testGetCustomerAccountWithWrongIDs() throws Exception {
        assertThrows(ApiErrorsException.class, () -> accountsService.getCustomerAccount(generatedCustomerAccessToken, UUID.randomUUID(), UUID.randomUUID()));
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken)
     */
    @Test
    public void testGetCustomerAccountsCustomerAccessToken() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(50L);
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec);
        assertTrue(financialInstitutionAccounts.size() == accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessTokenDefaultPagingSpec() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken);
        assertTrue(financialInstitutionAccounts.size() == accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessTokenNoAccountsAuthorized() throws Exception {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(50L);
        List<Account> accountsList = accountsService.getCustomerAccounts(getCustomerAccessToken(UUID.randomUUID().toString()), pagingSpec);
        assertTrue(accountsList.isEmpty());
        assertFalse(financialInstitutionAccounts.size() == accountsList.size());
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenPagingSpec() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec);
        assertTrue(accountsList.size() == 1);
        Account account = accountsList.get(0);
        assertTrue(financialInstitutionAccounts.stream().filter(financialInstitutionAccount -> financialInstitutionAccount.getReference().equals(account.getReference())).collect(Collectors.toList()).size() == 1);
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId)
     */
    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionId() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, financialInstitution.getId());
        assertTrue(accountsList.size() == financialInstitutionAccounts.size());
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenUnknownFinancialInstitutionId() throws Exception {
        try {
            accountsService.getCustomerAccounts(generatedCustomerAccessToken, UUID.randomUUID());
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionIdPagingSpec() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, financialInstitution.getId(), pagingSpec);
        Account account = accountsList.get(0);
        assertTrue(financialInstitutionAccounts.stream().filter(financialInstitutionAccount -> financialInstitutionAccount.getReference().equals(account.getReference())).collect(Collectors.toList()).size() == 1);
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenUnknownFinancialInstitutionIdPagingSpecErrorsContent() throws Exception {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        try {
            accountsService.getCustomerAccounts(generatedCustomerAccessToken, UUID.randomUUID(), pagingSpec);
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }

    /**
     * Method: getAccountInformationAccessRequest(CustomerAccessToken customerAccessToken, AccountInformationAccessRequest accountInformationAccessRequest)
     */
    @Test
    public void testGetAccountInformationAccessRequest() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        assertNotNull(accountInformationAccessRequest.getLinks().getRedirect());
        assertNotNull(URI.create(accountInformationAccessRequest.getLinks().getRedirect()));
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accounts = accountsService.getCustomerAccounts(generatedCustomerAccessToken, financialInstitution.getId());
        assertTrue(accounts.size() > 0);
    }
}
