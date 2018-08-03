package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsServiceTest;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.services.impl.TransactionsServiceImpl;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TransactionsServiceTest extends AbstractServiceTest {

    private final TransactionsService transactionsService = new TransactionsServiceImpl();
    private final AccountsService accountsService = new AccountsServiceImpl();

    private final List<FinancialInstitutionTransaction> financialInstitutionTransactions = new ArrayList<>();


    @BeforeEach
    public void beforeEach() {
        initPublicAPIEnvironment();
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts){
            for (int index = 0; index < 5; index++) {
                financialInstitutionTransactions.add(
                        FinancialInstitutionTransactionsServiceTest.createFinancialInstitutionTransaction(
                                financialInstitutionUser.getId()
                                ,financialInstitutionAccount
                                , null
                        )
                );
            }
        }
    }

    @AfterEach
    public void afterEach() {
        for (FinancialInstitutionTransaction financialInstitutionTransaction : financialInstitutionTransactions) {
            FinancialInstitutionTransactionsServiceTest.deleteFinancialInstitutionTransaction(
                    financialInstitution.getId()
                    , financialInstitutionUser.getId()
                    , financialInstitutionTransaction.getFinancialInstitutionAccount().getId()
                    , financialInstitutionTransaction.getId()
            );
        }
        cleanPublicAPIEnvironment();
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountId() {
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , financialInstitution.getId()
                    , account.getId()
            );
            assertEquals(transactionsList.size(), financialInstitutionTransactions.size());
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountIdDefaultPagingSpec() {
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , financialInstitution.getId()
                    , account.getId()
                    , new IbanityPagingSpec()
            );
            assertEquals(transactionsList.size(), financialInstitutionTransactions.size());
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenAndWrongFinancialInstitutionIdAccountId() {
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            try {
                transactionsService.list(generatedCustomerAccessToken.getToken(), UUID.randomUUID(), UUID.randomUUID());
                fail("Expected transactionsService.list to raise an ApiErrorsException");
            } catch (ApiErrorsException apiErrorsException) {
                assertEquals(apiErrorsException.getHttpStatus(), HttpStatus.SC_NOT_FOUND);
                assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
                assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
                assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
            }
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenAndWrongFinancialInstitutionIdAccountIdPaginSpec() {
        try {
            transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , UUID.randomUUID()
                    , UUID.randomUUID()
                    , new IbanityPagingSpec()
            );
            fail("Expected transactionsService.list to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountIdPagingSpec() {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , financialInstitution.getId()
                    , account.getId()
                    , pagingSpec
            );
            assertEquals(1, transactionsList.size());
        }
    }

    @Test
    public void testGetAccountTransaction() {
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , financialInstitution.getId()
                    , account.getId()
            );
            for (Transaction transaction : transactionsList){
                Transaction transactionReceived = transactionsService.find(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), transaction.getAccount().getId(), transaction.getId());
                assertEquals(transactionReceived.getAccount(), transaction.getAccount());
                assertEquals(transactionReceived.getAmount(), transaction.getAmount());
                assertEquals(transactionReceived.getCounterpartName(), transaction.getCounterpartName());
                assertEquals(transactionReceived.getCounterpartReference(), transaction.getCounterpartReference());
                assertEquals(transactionReceived.getCurrency(), transaction.getCurrency());
                assertEquals(transactionReceived.getDescription(), transaction.getDescription());
                assertEquals(transactionReceived.getExecutionDate(), transaction.getExecutionDate());
                assertEquals(transactionReceived.getId(), transaction.getId());
                assertEquals(transactionReceived.getRemittanceInformation(), transaction.getRemittanceInformation());
                assertEquals(transactionReceived.getRemittanceInformationType(), transaction.getRemittanceInformationType());
                assertEquals(transactionReceived.getValueDate(), transaction.getValueDate());
            }
        }
    }
    @Test
    public void testGetAccountTransactionWithWrongId() {
        try {
            transactionsService.find(generatedCustomerAccessToken.getToken(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            fail("Expected transactionsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(apiErrorsException.getHttpStatus(), HttpStatus.SC_NOT_FOUND);
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }
}
