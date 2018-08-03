package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AccountsServiceTest extends AbstractServiceTest {

    @BeforeEach
    public void beforeEach() {
        initPublicAPIEnvironment();
    }

    @AfterEach
    public void afterEach() {
        cleanPublicAPIEnvironment();
    }

    @Test
    public void testGetCustomerAccount() {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accountsList = accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId());
        if (accountsList.size() == 0){
            fail("authorized accounts list size is 0");
        }
        for (Account account : accountsList) {
            Account accountResult = accountsService.find(generatedCustomerAccessToken.getToken(), account.getId(), financialInstitution.getId());
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
            accountsService.find(generatedCustomerAccessToken.getToken(), UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessToken() {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(50L);
        List<Account> accountsList = accountsService.list(generatedCustomerAccessToken.getToken(), pagingSpec);
        assertEquals(financialInstitutionAccounts.size(), accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessTokenDefaultPagingSpec() {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accountsList = accountsService.list(generatedCustomerAccessToken.getToken());
        assertEquals(financialInstitutionAccounts.size(), accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessTokenNoAccountsAuthorized() {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(50L);
        List<Account> accountsList = accountsService.list(getCustomerAccessToken(UUID.randomUUID().toString()).getToken(), pagingSpec);
        assertTrue(accountsList.isEmpty());
        assertFalse(financialInstitutionAccounts.size() == accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenPagingSpec() {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        List<Account> accountsList = accountsService.list(generatedCustomerAccessToken.getToken(), pagingSpec);
        assertEquals(1, accountsList.size());
        Account account = accountsList.get(0);
        assertEquals(1, financialInstitutionAccounts.stream().filter(financialInstitutionAccount -> financialInstitutionAccount.getReference().equals(account.getReference())).collect(Collectors.toList()).size());
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionId() {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accountsList = accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId());
        assertEquals(accountsList.size(), financialInstitutionAccounts.size());
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenUnknownFinancialInstitutionId() {
        try {
            accountsService.list(generatedCustomerAccessToken.getToken(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(apiErrorsException.getHttpStatus(), HttpStatus.SC_NOT_FOUND);
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }

    @Test
    @Disabled
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionIdPagingSpec() {
        // TODO : not working
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        List<Account> accountsList = accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), pagingSpec);
        Account account = accountsList.get(0);
        assertEquals(1, financialInstitutionAccounts.stream().filter(financialInstitutionAccount -> financialInstitutionAccount.getReference().equals(account.getReference())).collect(Collectors.toList()).size());
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenUnknownFinancialInstitutionIdPagingSpecErrorsContent() {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        try {
            accountsService.list(generatedCustomerAccessToken.getToken(), UUID.randomUUID(), pagingSpec);
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(apiErrorsException.getHttpStatus(), HttpStatus.SC_NOT_FOUND);
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }
}
