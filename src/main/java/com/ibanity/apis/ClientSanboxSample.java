package com.ibanity.apis;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsService;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionAccountsServiceImpl;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionTransactionsServiceImpl;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionUsersServiceImpl;
import com.ibanity.apis.client.sandbox.services.impl.SandboxFinancialInstitutionsServiceImpl;
import org.apache.commons.math3.util.Precision;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ClientSanboxSample {
    private static final Logger LOGGER = LogManager.getLogger(ClientSanboxSample.class);

    private static final Integer SANDBOX_ACCOUNTS_TO_CREATE = 5;
    private static final Integer SANDBOX_TRANSACTIONS_T0_CREATE_PER_ACCOUNT = 10;

    private SandboxFinancialInstitutionsService financialInstitutionsService = new SandboxFinancialInstitutionsServiceImpl();
    private FinancialInstitutionAccountsService financialInstitutionAccountsService = new FinancialInstitutionAccountsServiceImpl();
    private FinancialInstitutionTransactionsService financialInstitutionTransactionsService = new FinancialInstitutionTransactionsServiceImpl();
    private FinancialInstitutionUsersService financialInstitutionUsersService = new FinancialInstitutionUsersServiceImpl();

    public static void main(String[] args){
        ClientSanboxSample client  = new ClientSanboxSample();
        client.startFlow();
    }

    public void startFlow(){

        Instant now = Instant.now();

        LOGGER.info("Start : Creating a new Financial Institution");
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        financialInstitutionsService.getFinancialInstitutions(pagingSpec).stream().forEach(financialInstitution -> LOGGER.info(financialInstitution.toString()));

        FinancialInstitution newFinancialInstitution = new FinancialInstitution();
        newFinancialInstitution.setSandbox(Boolean.TRUE);
        newFinancialInstitution.setName("FI-"+now);

        newFinancialInstitution = financialInstitutionsService.createFinancialInstitution(newFinancialInstitution);

        LOGGER.info("New Financial Institution created:"+newFinancialInstitution.toString());

        newFinancialInstitution.setName("FI-"+now);
        newFinancialInstitution = financialInstitutionsService.updateFinancialInstitution(newFinancialInstitution);
        LOGGER.info("New Financial Institution updated:"+newFinancialInstitution.toString());

        try {
            financialInstitutionsService.getFinancialInstitution(newFinancialInstitution.getId());
            LOGGER.info("New Financial Institution found:"+newFinancialInstitution.toString());

        } catch (ResourceNotFoundException e) {
            LOGGER.error(e);
        }

        try {
            financialInstitutionsService.deleteFinancialInstitution(newFinancialInstitution.getId());
        } catch (ResourceNotFoundException e) {
            LOGGER.info(e);
        }
        LOGGER.info("New Sandbox Financial Institution deleted");


        FinancialInstitutionUser financialInstitutionUser = new FinancialInstitutionUser();
        financialInstitutionUser.setFirstName("FirstName-"+now);
        financialInstitutionUser.setLastName("LastName-"+now);
        financialInstitutionUser.setLogin("Login-"+now);
        financialInstitutionUser.setPassword("Password-"+now);

        financialInstitutionUser = financialInstitutionUsersService.createFinancialInstitutionUser(financialInstitutionUser);
        LOGGER.info("New Financial Institution User created:"+financialInstitutionUser.toString());

        financialInstitutionUser.setPassword("password");
        financialInstitutionUser = financialInstitutionUsersService.updateFinancialInstitutionUser(financialInstitutionUser);
        LOGGER.info("New Financial Institution User updated:"+financialInstitutionUser.toString());

        financialInstitutionUsersService.getFinancialInstitutionUsers().stream().forEach(financialInstitutionUser1 -> LOGGER.info("Financial Institution User:"+financialInstitutionUser1.toString()));

        try {
            financialInstitutionUsersService.getFinancialInstitutionUser(financialInstitutionUser.getId());
        } catch (ResourceNotFoundException e) {
            LOGGER.error(e);
        }

        LOGGER.info("Got Financial Institution User");

        try {
            financialInstitutionUsersService.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        } catch (ResourceNotFoundException e) {
            LOGGER.error(e);
        }

        LOGGER.info("Financial Institution User deleted");

        newFinancialInstitution.setId(null);
        newFinancialInstitution = financialInstitutionsService.createFinancialInstitution(newFinancialInstitution);

        List<FinancialInstitutionAccount> financialInstitutionAccounts = new ArrayList<>();

        financialInstitutionUser = financialInstitutionUsersService.createFinancialInstitutionUser(financialInstitutionUser);

        LOGGER.info("Start : Adding account to User's Financial Institution");

        for (int index = 0 ; index < SANDBOX_ACCOUNTS_TO_CREATE; index++) {
            FinancialInstitutionAccount financialInstitutionAccount = new FinancialInstitutionAccount();
            financialInstitutionAccount.setSubType("checking");
            financialInstitutionAccount.setReference(Iban.random(CountryCode.BE).toString());
            financialInstitutionAccount.setReferenceType("IBAN");
            financialInstitutionAccount.setDescription("Checking Account");
            financialInstitutionAccount.setCurrency("EUR");
            financialInstitutionAccount.setFinancialInstitution(newFinancialInstitution);

            financialInstitutionAccount = financialInstitutionAccountsService.createFinancialInstitutionAccount(
                    newFinancialInstitution.getId()
                    , financialInstitutionUser.getId()
                    , financialInstitutionAccount);
            financialInstitutionAccounts.add(financialInstitutionAccount);
            LOGGER.info("Account:"+financialInstitutionAccount.getReference()+":created.");
        }

        LOGGER.info("END : Adding account to User's Financial Institutions");


        FinancialInstitutionAccount financialInstitutionAccountToPlayWith = new FinancialInstitutionAccount();
        financialInstitutionAccountToPlayWith.setSubType("checking");
        financialInstitutionAccountToPlayWith.setReference("BE02379129664149");
        financialInstitutionAccountToPlayWith.setReferenceType("IBAN");
        financialInstitutionAccountToPlayWith.setDescription("Checking Account");
        financialInstitutionAccountToPlayWith.setCurrency("EUR");

        financialInstitutionAccountToPlayWith.setFinancialInstitution(newFinancialInstitution);

        financialInstitutionAccountToPlayWith = financialInstitutionAccountsService.createFinancialInstitutionAccount(
                newFinancialInstitution.getId()
                , financialInstitutionUser.getId()
                , financialInstitutionAccountToPlayWith);

        LOGGER.info("Start : Finding FinancialInstitutionAccount");

        try {
            financialInstitutionAccountToPlayWith = financialInstitutionAccountsService.getFinancialInstitutionAccount(newFinancialInstitution.getId(),financialInstitutionUser.getId(), financialInstitutionAccountToPlayWith.getId());
        } catch (ResourceNotFoundException e) {
            LOGGER.error(e);
        }

        LOGGER.info("End : Finding FinancialInstitutionAccount");

        LOGGER.info("Start : Deleting FinancialInstitutionAccount");

        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(
                newFinancialInstitution.getId()
                , financialInstitutionUser.getId()
                , financialInstitutionAccountToPlayWith.getId());

        LOGGER.info("End : Deleting FinancialInstitutionAccount");

        LOGGER.info("Start : Adding transactions to User's Financial Institutions's account");


        final UUID newFinancialInstitutionId  = newFinancialInstitution.getId();
        final UUID financialInstitutionUserId = financialInstitutionUser.getId();

        final AtomicReference<FinancialInstitutionAccount> inUseFinancialInstitutionAccount = new AtomicReference();

        financialInstitutionAccounts.stream().forEach(financialInstitutionAccount -> {
            for (int index = 0; index < SANDBOX_TRANSACTIONS_T0_CREATE_PER_ACCOUNT ; index++) {
                FinancialInstitutionTransaction financialInstitutionTransaction = generateNewTransaction(financialInstitutionAccount);
                FinancialInstitutionTransaction createdFinancialInstitutionTransaction = financialInstitutionTransactionsService.createFinancialInstitutionTransaction(
                          newFinancialInstitutionId
                        , financialInstitutionUserId
                        , financialInstitutionAccount.getId()
                        , financialInstitutionTransaction
                );
                LOGGER.info("FinancialInstitutionAccount:"+financialInstitutionAccount.getReference()+":Transaction:"+createdFinancialInstitutionTransaction.getId()+":created");
            }
            inUseFinancialInstitutionAccount.set(financialInstitutionAccount);
        });
        LOGGER.info("END : Adding transactions to User's Financial Institutions's account");

        FinancialInstitutionTransaction financialInstitutionTransaction = generateNewTransaction(inUseFinancialInstitutionAccount.get());
        FinancialInstitutionTransaction createdFinancialInstitutionTransaction = financialInstitutionTransactionsService.createFinancialInstitutionTransaction(
                  newFinancialInstitutionId
                , financialInstitutionUserId
                , inUseFinancialInstitutionAccount.get().getId()
                , financialInstitutionTransaction
        );
        LOGGER.info("Start : Finding transaction from User's Financial Institutions's account");
        try {
            financialInstitutionTransactionsService.getFinancialInstitutionTransaction(newFinancialInstitutionId, financialInstitutionUserId, inUseFinancialInstitutionAccount.get().getId(), createdFinancialInstitutionTransaction.getId());
        } catch (ResourceNotFoundException e) {
            LOGGER.error(e);
        }

        LOGGER.info("End : Finding transaction from User's Financial Institutions's account");


        LOGGER.info("Start : Deleting transaction from User's Financial Institutions's account");
        try {
            financialInstitutionTransactionsService.deleteFinancialInstitutionTransaction(newFinancialInstitutionId, financialInstitutionUserId, inUseFinancialInstitutionAccount.get().getId(), createdFinancialInstitutionTransaction.getId());
        } catch (ResourceNotFoundException e) {
            LOGGER.error(e);
        }

        LOGGER.info("End : Deleting transaction from User's Financial Institutions's account");
    }

    private FinancialInstitutionTransaction generateNewTransaction(FinancialInstitutionAccount createdFinancialInstitutionAccount){
        Instant now = Instant.now();
        Random random = new Random();

        Instant executionDate = now.plus(3, ChronoUnit.DAYS);
        Instant valueDate = now.minus(1, ChronoUnit.DAYS);
        FinancialInstitutionTransaction financialInstitutionTransaction = new FinancialInstitutionTransaction();
        financialInstitutionTransaction.setFinancialInstitutionAccount(createdFinancialInstitutionAccount);
        financialInstitutionTransaction.setAmount(Precision.round(random.doubles(10,1000).findFirst().getAsDouble() * (random .nextBoolean() ? -1 : 1), 2));
        financialInstitutionTransaction.setCounterpartName("Stroman, Hettinger and Swift");
        financialInstitutionTransaction.setCounterpartReference(Iban.random(CountryCode.BE).getAccountNumber());
        financialInstitutionTransaction.setCurrency("EUR");
        financialInstitutionTransaction.setDescription("Car rental");
        financialInstitutionTransaction.setExecutionDate(executionDate);
        financialInstitutionTransaction.setRemittanceInformation("Aspernatur et quibusdam.");
        financialInstitutionTransaction.setRemittanceInformationType("unstructured");
        financialInstitutionTransaction.setValueDate(valueDate);
        return financialInstitutionTransaction;
    }
}
