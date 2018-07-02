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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * TransactionsServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 19, 2018</pre>
 */
public class TransactionsServiceTest extends AbstractServiceTest {

    private final TransactionsService transactionsService = new TransactionsServiceImpl();
    private final AccountsService accountsService = new AccountsServiceImpl();

    private List<FinancialInstitutionTransaction> financialInstitutionTransactions = new ArrayList<>();


    @BeforeEach
    public void beforeEach() throws Exception {
        initSelenium();
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
    public void afterEach() throws Exception {
        for (FinancialInstitutionTransaction financialInstitutionTransaction : financialInstitutionTransactions) {
            FinancialInstitutionTransactionsServiceTest.deleteFinancialInstitutionTransaction(
                    financialInstitution.getId()
                    , financialInstitutionUser.getId()
                    , financialInstitutionTransaction.getFinancialInstitutionAccount().getId()
                    , financialInstitutionTransaction.getId()
            );
        }
        exitSelenium();
        cleanPublicAPIEnvironment();
    }

    /**
     * Method: getList(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId)
     */
    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountId() throws Exception {
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , financialInstitution.getId()
                    , account.getId()
            );
            assertTrue(transactionsList.size() == financialInstitutionTransactions.size());
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountIdDefaultPagingSpec() throws Exception {
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , financialInstitution.getId()
                    , account.getId()
                    , new IbanityPagingSpec()
            );
            assertTrue(transactionsList.size() == financialInstitutionTransactions.size());
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenAndWrongFinancialInstitutionIdAccountId() throws Exception {
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            try {
                transactionsService.list(generatedCustomerAccessToken.getToken(), UUID.randomUUID(), UUID.randomUUID());
                fail("Should raise ApiErrorsException");
            } catch (ApiErrorsException apiErrorsException) {
                assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
                assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
                assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
                assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count() == 1);
            }
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenAndWrongFinancialInstitutionIdAccountIdPaginSpec() throws Exception {
        try {
            transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , UUID.randomUUID()
                    , UUID.randomUUID()
                    , new IbanityPagingSpec()
            );
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count() == 1);
        }
    }

    /**
     * Method: getList(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountIdPagingSpec() throws Exception {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , financialInstitution.getId()
                    , account.getId()
                    , pagingSpec
            );
            assertTrue(transactionsList.size() == 1);
        }
    }

    /**
     * Method: find(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, UUID transactionId)
     */
    @Test
    public void testGetAccountTransaction() throws Exception {
        for (Account account : accountsService.list(generatedCustomerAccessToken.getToken(), financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.list(
                    generatedCustomerAccessToken.getToken()
                    , financialInstitution.getId()
                    , account.getId()
            );
            for (Transaction transaction : transactionsList){
                Transaction transactionReceived = transactionsService.find(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), transaction.getAccount().getId(), transaction.getId());
                assertTrue(transactionReceived.getAccount().equals(transaction.getAccount()));
                assertTrue(transactionReceived.getAmount().equals(transaction.getAmount()));
                assertTrue(transactionReceived.getCounterpartName().equals(transaction.getCounterpartName()));
                assertTrue(transactionReceived.getCounterpartReference().equals(transaction.getCounterpartReference()));
                assertTrue(transactionReceived.getCurrency().equals(transaction.getCurrency()));
                assertTrue(transactionReceived.getDescription().equals(transaction.getDescription()));
                assertTrue(transactionReceived.getExecutionDate().equals(transaction.getExecutionDate()));
                assertTrue(transactionReceived.getId().equals(transaction.getId()));
                assertTrue(transactionReceived.getRemittanceInformation().equals(transaction.getRemittanceInformation()));
                assertTrue(transactionReceived.getRemittanceInformationType().equals(transaction.getRemittanceInformationType()));
                assertTrue(transactionReceived.getValueDate().equals(transaction.getValueDate()));
            }
        }
    }
    @Test
    public void testGetAccountTransactionWithWrongId() throws Exception {
        try {
            transactionsService.find(generatedCustomerAccessToken.getToken(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count() == 1);
        }
    }
}
