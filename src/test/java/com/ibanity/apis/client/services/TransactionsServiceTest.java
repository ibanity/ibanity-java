package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsServiceTest;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.services.impl.TransactionsServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TransactionsServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 19, 2018</pre>
 */
public class TransactionsServiceTest {

    private static final TransactionsService transactionsService = new TransactionsServiceImpl();
    private static final AccountsService accountsService = new AccountsServiceImpl();

    private static List<FinancialInstitutionTransaction> financialInstitutionTransactions = new ArrayList<>();


    @BeforeAll
    public static void beforeAll() throws Exception {
        AccountsServiceTest.beforeAll();
        for (FinancialInstitutionAccount financialInstitutionAccount : AccountsServiceTest.financialInstitutionAccounts){
            for (int index = 0; index < 5; index++) {
                financialInstitutionTransactions.add(
                        FinancialInstitutionTransactionsServiceTest.createFinancialInstitutionTransaction(
                                AccountsServiceTest.financialInstitutionUser.getId()
                                ,financialInstitutionAccount
                        )
                );
            }
        }

    }

    @AfterAll
    public static void afterAll() throws Exception {
        for (FinancialInstitutionTransaction financialInstitutionTransaction : financialInstitutionTransactions) {
            FinancialInstitutionTransactionsServiceTest.deleteFinancialInstitutionTransaction(
                    AccountsServiceTest.financialInstitution.getId()
                    , AccountsServiceTest.financialInstitutionUser.getId()
                    , financialInstitutionTransaction.getFinancialInstitutionAccount().getId()
                    , financialInstitutionTransaction.getId()
            );
        }
        AccountsServiceTest.afterAll();
    }

    /**
     * Method: getAccountTransactions(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId)
     */
    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountId() throws Exception {
        for (Account account : accountsService.getCustomerAccounts(AccountsServiceTest.generatedCustomerAccessToken, AccountsServiceTest.financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.getAccountTransactions(
                    AccountsServiceTest.generatedCustomerAccessToken
                    , AccountsServiceTest.financialInstitution.getId()
                    , account.getId()
            );
            assertTrue(transactionsList.size() == financialInstitutionTransactions.size());
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenAndWrongFinancialInstitutionIdAccountId() throws Exception {
        for (Account account : accountsService.getCustomerAccounts(AccountsServiceTest.generatedCustomerAccessToken, AccountsServiceTest.financialInstitution.getId())){
            assertThrows(ResourceNotFoundException.class, () -> transactionsService.getAccountTransactions(
                    AccountsServiceTest.generatedCustomerAccessToken
                    , UUID.randomUUID()
                    , UUID.randomUUID()
            ));
        }
    }

    /**
     * Method: getAccountTransactions(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountIdPagingSpec() throws Exception {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        for (Account account : accountsService.getCustomerAccounts(AccountsServiceTest.generatedCustomerAccessToken, AccountsServiceTest.financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.getAccountTransactions(
                    AccountsServiceTest.generatedCustomerAccessToken
                    , AccountsServiceTest.financialInstitution.getId()
                    , account.getId()
                    , pagingSpec
            );
            assertTrue(transactionsList.size() == 1);
        }
    }

    /**
     * Method: getAccountTransaction(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, UUID transactionId)
     */
    @Test
    public void testGetAccountTransaction() throws Exception {
        for (Account account : accountsService.getCustomerAccounts(AccountsServiceTest.generatedCustomerAccessToken, AccountsServiceTest.financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.getAccountTransactions(
                    AccountsServiceTest.generatedCustomerAccessToken
                    , AccountsServiceTest.financialInstitution.getId()
                    , account.getId()
            );
            for (Transaction transaction : transactionsList){
                Transaction transactionReceived = transactionsService.getAccountTransaction(AccountsServiceTest.generatedCustomerAccessToken, AccountsServiceTest.financialInstitution.getId(), transaction.getAccount().getId(), transaction.getId());
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
        for (Account account : accountsService.getCustomerAccounts(AccountsServiceTest.generatedCustomerAccessToken, AccountsServiceTest.financialInstitution.getId())){
            List<Transaction> transactionsList = transactionsService.getAccountTransactions(
                    AccountsServiceTest.generatedCustomerAccessToken
                    , AccountsServiceTest.financialInstitution.getId()
                    , account.getId()
            );
            for (Transaction transaction : transactionsList){
                assertThrows(ResourceNotFoundException.class, () -> transactionsService.getAccountTransaction(AccountsServiceTest.generatedCustomerAccessToken, AccountsServiceTest.financialInstitution.getId(), transaction.getAccount().getId(), UUID.randomUUID()));
            }
        }
    }
}
