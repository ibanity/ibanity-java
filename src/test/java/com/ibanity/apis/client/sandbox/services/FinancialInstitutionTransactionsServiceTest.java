package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionTransactionDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionTransactionsServiceImpl;
import org.apache.commons.math3.util.Precision;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FinancialInstitutionTransactionsServiceTest extends AbstractServiceTest {

    private static final FinancialInstitutionTransactionsService financialInstitutionTransactionsService = new FinancialInstitutionTransactionsServiceImpl();

    @Test
    public void testGetFinancialInstitutionTransaction() {
        getFinancialInstitutionTransaction(null);
    }

    private void getFinancialInstitutionTransaction(UUID idempotencyKey) {
        FinancialInstitution financialInstitution = createFinancialInstitution(idempotencyKey);
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(idempotencyKey);
        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), idempotencyKey);

        FinancialInstitutionTransaction financialInstitutionTransaction =
                createFinancialInstitutionTransaction(financialInstitutionUser.getId(), financialInstitutionAccount, idempotencyKey);

        FinancialInstitutionTransactionReadQuery transactionsReadQuery =
                FinancialInstitutionTransactionReadQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionTransactionId(financialInstitutionTransaction.getId())
                        .build();

        FinancialInstitutionTransaction financialInstitutionTransactionGet =
                financialInstitutionTransactionsService.find(transactionsReadQuery);

        assertEquals(financialInstitutionTransaction.getCounterpartName(), financialInstitutionTransactionGet.getCounterpartName());
        assertEquals(financialInstitutionTransaction.getCounterpartReference(), financialInstitutionTransactionGet.getCounterpartReference());
        assertEquals(financialInstitutionTransaction.getCurrency(), financialInstitutionTransactionGet.getCurrency());
        assertEquals(financialInstitutionTransaction.getDescription(), financialInstitutionTransactionGet.getDescription());
        assertEquals(financialInstitutionTransaction.getRemittanceInformation(), financialInstitutionTransactionGet.getRemittanceInformation());
        assertEquals(financialInstitutionTransaction.getRemittanceInformationType(), financialInstitutionTransactionGet.getRemittanceInformationType());
        assertEquals(financialInstitutionTransaction.getAmount(), financialInstitutionTransactionGet.getAmount());
        assertEquals(financialInstitutionTransaction.getExecutionDate(), financialInstitutionTransactionGet.getExecutionDate());
        assertEquals(financialInstitutionTransaction.getValueDate(), financialInstitutionTransactionGet.getValueDate());

        deleteFinancialInstitutionTransaction(financialInstitution.getId(), financialInstitutionUser.getId(),
                financialInstitutionAccount.getId(),financialInstitutionTransaction.getId());
        deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(),
                financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionTransactionWithWrongIDs() {
        try {
            FinancialInstitutionTransactionReadQuery transactionReadQuery =
                    FinancialInstitutionTransactionReadQuery.builder()
                            .financialInstitutionId(UUID.randomUUID())
                            .financialInstitutionAccountId(UUID.randomUUID())
                            .financialInstitutionTransactionId(UUID.randomUUID())
                            .financialInstitutionUserId(UUID.randomUUID())
                            .build();

            financialInstitutionTransactionsService.find(transactionReadQuery);
            fail("Expected financialInstitutionTransactionsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, Transaction.RESOURCE_TYPE);
        }
    }

    @Test
    public void testGetFinancialInstitutionAccountTransactions() {
        List<FinancialInstitutionTransaction> financialInstitutionTransactions = new ArrayList<>();
        FinancialInstitution financialInstitution = createFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);
        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);
        financialInstitutionTransactions.add(
                createFinancialInstitutionTransaction(financialInstitutionUser.getId(), financialInstitutionAccount, null));
        financialInstitutionTransactions.add(
                createFinancialInstitutionTransaction(financialInstitutionUser.getId(), financialInstitutionAccount, null));
        financialInstitutionTransactions.add(
                createFinancialInstitutionTransaction(financialInstitutionUser.getId(), financialInstitutionAccount, null));

        FinancialInstitutionTransactionsReadQuery transactionsReadQuery =
                FinancialInstitutionTransactionsReadQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .build();

        List<FinancialInstitutionTransaction> financialInstitutionTransactionsList =
                financialInstitutionTransactionsService.list(transactionsReadQuery);
        assertTrue (financialInstitutionTransactionsList.containsAll(financialInstitutionTransactions));

        for (FinancialInstitutionTransaction financialInstitutionTransaction: financialInstitutionTransactions) {
            deleteFinancialInstitutionTransaction(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId(),financialInstitutionTransaction.getId());
        }
        deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionAccountTransactionsWithWrongIds() {
        try {
            FinancialInstitutionTransactionsReadQuery transactionsReadQuery =
                    FinancialInstitutionTransactionsReadQuery.builder()
                            .financialInstitutionId(UUID.randomUUID())
                            .financialInstitutionAccountId(UUID.randomUUID())
                            .financialInstitutionUserId(UUID.randomUUID())
                            .build();

            financialInstitutionTransactionsService.list(transactionsReadQuery );
            fail("Expected financialInstitutionTransactionsService.list to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, Transaction.RESOURCE_TYPE);
        }
    }

    @Test
    public void testCreateFinancialInstitutionTransaction() {
        getFinancialInstitutionTransaction(null);
    }

    @Test
    public void testCreateFinancialInstitutionTransactionIdempotency() {
        getFinancialInstitutionTransaction(UUID.randomUUID());
    }

    @Test
    public void testCreateFinancialInstitutionTransactionWithWrongIds() {
        try {
            FinancialInstitutionTransactionCreationQuery transactionCreationQuery =
                    FinancialInstitutionTransactionCreationQuery.builder()
                            .financialInstitutionId(UUID.randomUUID())
                            .financialInstitutionAccountId(UUID.randomUUID())
                            .financialInstitutionUserId(UUID.randomUUID())
                            .build();

            financialInstitutionTransactionsService.create(transactionCreationQuery);
            fail("Expected financialInstitutionTransactionsService.create to rais an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, Transaction.RESOURCE_TYPE);
        }
    }

    @Test
    public void testDeleteFinancialInstitutionTransaction() {
        testGetFinancialInstitutionTransaction();
    }

    @Test
    public void testDeleteFinancialInstitutionTransactionWithWrongIds() {
        try {
            deleteFinancialInstitutionTransaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            fail("Expected deleteFinancialInstitutionTransaction to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, Transaction.RESOURCE_TYPE);
        }
    }

    public static void deleteFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) {
        FinancialInstitutionTransactionDeleteQuery transactionDeleteQuery =
                FinancialInstitutionTransactionDeleteQuery.builder()
                        .financialInstitutionId(financialInstitutionId)
                        .financialInstitutionAccountId(financialInstitutionAccountId)
                        .financialInstitutionUserId(financialInstitutionUserId)
                        .financialInstitutionTransactionId(financialInstitutionTransactionId)
                        .build();

        financialInstitutionTransactionsService.delete(transactionDeleteQuery);
    }

    public static FinancialInstitutionTransaction createFinancialInstitutionTransaction(UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount, UUID idempotencyKey) {
        Instant now = Instant.now();
        Random random = new Random();

        Instant executionDate = now.plus(3, ChronoUnit.DAYS);
        Instant valueDate = now.minus(1, ChronoUnit.DAYS);

        FinancialInstitutionTransactionCreationQuery transactionCreationQuery =
                FinancialInstitutionTransactionCreationQuery.builder()
                        .financialInstitutionId(financialInstitutionAccount.getFinancialInstitution().getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .financialInstitutionUserId(financialInstitutionUserId)
                        .idempotencyKey(idempotencyKey)
                        .amount(Precision.round(random.doubles(10,1000).findFirst().getAsDouble() * (random .nextBoolean() ? -1 : 1), 2))
                        .counterpartName("Stroman, Hettinger and Swift")
                        .counterpartReference(Iban.random(CountryCode.BE).getAccountNumber())
                        .currency("EUR")
                        .description("Car rental")
                        .executionDate(executionDate)
                        .remittanceInformation("Aspernatur et quibusdam.")
                        .remittanceInformationType("unstructured")
                        .valueDate(valueDate)
                .build();

        return financialInstitutionTransactionsService.create(transactionCreationQuery);
    }

} 
