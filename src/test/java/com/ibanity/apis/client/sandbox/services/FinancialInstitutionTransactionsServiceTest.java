package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionTransactionsServiceImpl;
import org.apache.commons.math3.util.Precision;
import org.apache.http.HttpStatus;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * FinancialInstitutionTransactionsServiceImpl Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 13, 2018</pre>
 */
public class FinancialInstitutionTransactionsServiceTest extends AbstractServiceTest {

    private static final FinancialInstitutionTransactionsService financialInstitutionTransactionsService = new FinancialInstitutionTransactionsServiceImpl();

    @BeforeEach
    public void before() {
    }

    @AfterEach
    public void after() {
    }

    /**
     * Method: getFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId)
     */
    @Test
    public void testGetFinancialInstitutionTransaction() throws Exception {
        getFinancialInstitutionTransaction(null);
    }

    private void getFinancialInstitutionTransaction(UUID idempotencyKey) throws Exception {
        FinancialInstitution financialInstitution = createFinancialInstitution(idempotencyKey);
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(idempotencyKey);
        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), idempotencyKey);
        FinancialInstitutionTransaction financialInstitutionTransaction = createFinancialInstitutionTransaction(financialInstitutionUser.getId(), financialInstitutionAccount, idempotencyKey);
        FinancialInstitutionTransaction financialInstitutionTransactionGet = financialInstitutionTransactionsService.find(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId(), financialInstitutionTransaction.getId());
        assertTrue(financialInstitutionTransactionGet.getCounterpartName().equals(financialInstitutionTransaction.getCounterpartName()));
        assertTrue(financialInstitutionTransactionGet.getCounterpartReference().equals(financialInstitutionTransaction.getCounterpartReference()));
        assertTrue(financialInstitutionTransactionGet.getCurrency().equals(financialInstitutionTransaction.getCurrency()));
        assertTrue(financialInstitutionTransactionGet.getDescription().equals(financialInstitutionTransaction.getDescription()));
        assertTrue(financialInstitutionTransactionGet.getRemittanceInformation().equals(financialInstitutionTransaction.getRemittanceInformation()));
        assertTrue(financialInstitutionTransactionGet.getRemittanceInformationType().equals(financialInstitutionTransaction.getRemittanceInformationType()));
        assertTrue(financialInstitutionTransactionGet.getAmount().equals(financialInstitutionTransaction.getAmount()));
        assertTrue(financialInstitutionTransactionGet.getExecutionDate().equals(financialInstitutionTransaction.getExecutionDate()));
        assertTrue(financialInstitutionTransactionGet.getValueDate().equals(financialInstitutionTransaction.getValueDate()));

        deleteFinancialInstitutionTransaction(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId(),financialInstitutionTransaction.getId());
        deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());

    }

    /**
     * Method: getFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId)
     */
    @Test
    public void testGetFinancialInstitutionTransactionWithWrongIDs() throws Exception {
        try {
            financialInstitutionTransactionsService.find(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count() == 1);
        }
    }

    /**
     * Method: getFinancialInstitutionAccountTransactions(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId)
     */
    @Test
    public void testGetFinancialInstitutionAccountTransactions() throws Exception {
        List<FinancialInstitutionTransaction> financialInstitutionTransactions = new ArrayList<>();
        FinancialInstitution financialInstitution = createFinancialInstitution(null);
        FinancialInstitutionUser financialInstitutionUser = createFinancialInstitutionUser(null);
        FinancialInstitutionAccount financialInstitutionAccount = createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null);
        financialInstitutionTransactions.add(createFinancialInstitutionTransaction(financialInstitutionUser.getId(), financialInstitutionAccount, null));
        financialInstitutionTransactions.add(createFinancialInstitutionTransaction(financialInstitutionUser.getId(), financialInstitutionAccount, null));
        financialInstitutionTransactions.add(createFinancialInstitutionTransaction(financialInstitutionUser.getId(), financialInstitutionAccount, null));

        List<FinancialInstitutionTransaction> financialInstitutionTransactionsList = financialInstitutionTransactionsService.list(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        assertTrue (financialInstitutionTransactionsList.containsAll(financialInstitutionTransactions));

        for (FinancialInstitutionTransaction financialInstitutionTransaction: financialInstitutionTransactions){
            deleteFinancialInstitutionTransaction(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId(),financialInstitutionTransaction.getId());
        }
        deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }


    /**
     * Method: getFinancialInstitutionAccountTransactions(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId)
     */
    @Test
    public void testGetFinancialInstitutionAccountTransactionsWithWrongIds() throws Exception {
        try {
            financialInstitutionTransactionsService.list(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count() == 1);
        }
    }

    /**
     * Method: createFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction)
     */
    @Test
    public void testCreateFinancialInstitutionTransaction() throws Exception {
        getFinancialInstitutionTransaction(null);
    }

    @Test
    public void testCreateFinancialInstitutionTransactionIdempotency() throws Exception {
        getFinancialInstitutionTransaction(UUID.randomUUID());
    }

    /**
     * Method: createFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction)
     */
    @Test
    public void testCreateFinancialInstitutionTransactionWithWrongIds() throws Exception {
        try {
            financialInstitutionTransactionsService.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new FinancialInstitutionTransaction());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count() == 1);
        }
    }

    /**
     * Method: deleteFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId)
     */
    @Test
    public void testDeleteFinancialInstitutionTransaction() throws Exception {
        testGetFinancialInstitutionTransaction();
    }

    /**
     * Method: deleteFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId)
     */
    @Test
    public void testDeleteFinancialInstitutionTransactionWithWrongIds() throws Exception {
        try {
            deleteFinancialInstitutionTransaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count() == 1);
        }
    }

    public static void deleteFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) throws ApiErrorsException {
        financialInstitutionTransactionsService.delete(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, financialInstitutionTransactionId);
    }

    public static FinancialInstitutionTransaction createFinancialInstitutionTransaction(UUID financialInstitutionUserId, FinancialInstitutionAccount financialInstitutionAccount, UUID idempotencyKey) throws ApiErrorsException {
        Instant now = Instant.now();
        Random random = new Random();

        Instant executionDate = now.plus(3, ChronoUnit.DAYS);
        Instant valueDate = now.minus(1, ChronoUnit.DAYS);
        FinancialInstitutionTransaction financialInstitutionTransaction = new FinancialInstitutionTransaction();
        financialInstitutionTransaction.setFinancialInstitutionAccount(financialInstitutionAccount);
        financialInstitutionTransaction.setAmount(Precision.round(random.doubles(10,1000).findFirst().getAsDouble() * (random .nextBoolean() ? -1 : 1), 2));
        financialInstitutionTransaction.setCounterpartName("Stroman, Hettinger and Swift");
        financialInstitutionTransaction.setCounterpartReference(Iban.random(CountryCode.BE).getAccountNumber());
        financialInstitutionTransaction.setCurrency("EUR");
        financialInstitutionTransaction.setDescription("Car rental");
        financialInstitutionTransaction.setExecutionDate(executionDate);
        financialInstitutionTransaction.setRemittanceInformation("Aspernatur et quibusdam.");
        financialInstitutionTransaction.setRemittanceInformationType("unstructured");
        financialInstitutionTransaction.setValueDate(valueDate);
        if (idempotencyKey == null) {
            return financialInstitutionTransactionsService.create(
                    financialInstitutionAccount.getFinancialInstitution().getId()
                    , financialInstitutionUserId
                    , financialInstitutionAccount.getId()
                    , financialInstitutionTransaction
            );
        } else {
            return financialInstitutionTransactionsService.create(
                    financialInstitutionAccount.getFinancialInstitution().getId()
                    , financialInstitutionUserId
                    , financialInstitutionAccount.getId()
                    , financialInstitutionTransaction
                    , idempotencyKey
            );
        }
    }

} 
