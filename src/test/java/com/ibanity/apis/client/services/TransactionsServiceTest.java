package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.models.factory.read.AccountsReadQuery;
import com.ibanity.apis.client.models.factory.read.TransactionReadQuery;
import com.ibanity.apis.client.models.factory.read.TransactionsReadQuery;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsServiceTest;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.services.impl.TransactionsServiceImpl;
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
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
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
        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .build();

        for (Account account : accountsService.list(accountsReadQuery)) {
            TransactionsReadQuery transactionsReadQuery = TransactionsReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(financialInstitution.getId())
                    .accountId(account.getId())
                    .build();

            List<Transaction> transactionsList = transactionsService.list(transactionsReadQuery);
            assertEquals(transactionsList.size(), financialInstitutionTransactions.size());
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountIdDefaultPagingSpec() {
        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .build();

        for (Account account : accountsService.list(accountsReadQuery)) {
            TransactionsReadQuery transactionsReadQuery = TransactionsReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(financialInstitution.getId())
                    .accountId(account.getId())
                    .pagingSpec(new IbanityPagingSpec())
                    .build();

            List<Transaction> transactionsList = transactionsService.list(transactionsReadQuery);
            assertEquals(transactionsList.size(), financialInstitutionTransactions.size());
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenAndWrongFinancialInstitutionIdAccountId() {
        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .build();

        for (Account account : accountsService.list(accountsReadQuery)) {
            try {
                TransactionsReadQuery transactionsReadQuery = TransactionsReadQuery.builder()
                        .customerAccessToken(generatedCustomerAccessToken.getToken())
                        .financialInstitutionId(UUID.randomUUID())
                        .accountId(UUID.randomUUID())
                        .build();

                transactionsService.list(transactionsReadQuery);
                fail("Expected transactionsService.list to raise an ApiErrorsException");
            } catch (ApiErrorsException apiErrorsException) {
                super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
            }
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenAndWrongFinancialInstitutionIdAccountIdPaginSpec() {
        try {
            TransactionsReadQuery transactionsReadQuery = TransactionsReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(UUID.randomUUID())
                    .accountId(UUID.randomUUID())
                    .pagingSpec(new IbanityPagingSpec())
                    .build();

            transactionsService.list(transactionsReadQuery);
            fail("Expected transactionsService.list to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }

    @Test
    public void testGetAccountTransactionsForCustomerAccessTokenFinancialInstitutionIdAccountIdPagingSpec() {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);

        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .build();

        for (Account account : accountsService.list(accountsReadQuery)) {
            TransactionsReadQuery transactionsReadQuery = TransactionsReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(financialInstitution.getId())
                    .accountId(account.getId())
                    .pagingSpec(pagingSpec)
                    .build();

            List<Transaction> transactionsList = transactionsService.list(transactionsReadQuery);
            assertEquals(1, transactionsList.size());
        }
    }

    @Test
    public void testGetAccountTransaction() {
        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(generatedCustomerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .build();

        for (Account account : accountsService.list(accountsReadQuery)) {
            TransactionsReadQuery transactionsReadQuery = TransactionsReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(financialInstitution.getId())
                    .accountId(account.getId())
                    .build();

            List<Transaction> transactionsList = transactionsService.list(transactionsReadQuery);

            for (Transaction transaction : transactionsList) {
                TransactionReadQuery transactionReadQuery = TransactionReadQuery.builder()
                        .customerAccessToken(generatedCustomerAccessToken.getToken())
                        .financialInstitutionId(financialInstitution.getId())
                        .accountId(account.getId())
                        .transactionId(transaction.getId())
                        .build();

                Transaction transactionReceived = transactionsService.find(transactionReadQuery);
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
            TransactionReadQuery transactionReadQuery = TransactionReadQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(UUID.randomUUID())
                    .accountId(UUID.randomUUID())
                    .transactionId(UUID.randomUUID())
                    .build();

            transactionsService.find(transactionReadQuery);
            fail("Expected transactionsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, Transaction.RESOURCE_TYPE);
        }
    }
}
