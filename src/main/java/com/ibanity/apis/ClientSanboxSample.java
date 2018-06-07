package com.ibanity.apis;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionAccount;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionTransaction;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionUser;
import com.ibanity.apis.client.paging.PagingSpec;
import com.ibanity.apis.client.services.AccountsService;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import com.ibanity.apis.client.services.FinancialInstitutionsService;
import com.ibanity.apis.client.services.TransactionsService;
import com.ibanity.apis.client.services.UsersService;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.services.impl.CustomerAccessTokensServiceImpl;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import com.ibanity.apis.client.services.impl.TransactionsServiceImpl;
import com.ibanity.apis.client.services.impl.UsersServiceImpl;
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

public class ClientSanboxSample {
    private static final Logger LOGGER = LogManager.getLogger(ClientSanboxSample.class);

    private static final Integer SANDBOX_ACCOUNTS_TO_CREATE = 5;
    private static final Integer SANDBOX_TRANSACTIONS_T0_CREATE_PER_ACCOUNT = 20;

    private FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();
    private CustomerAccessTokensService customerAccessTokensService = new CustomerAccessTokensServiceImpl();
    private AccountsService accountsService = new AccountsServiceImpl();
    private TransactionsService transactionsService = new TransactionsServiceImpl();
    private UsersService usersService = new UsersServiceImpl();

    public static void main(String[] args){
        ClientSanboxSample client  = new ClientSanboxSample();
        client.startFlow();
    }

    public void startFlow(){

        Instant now = Instant.now();

        LOGGER.info("Start : Creating a new Financial Institution");
        PagingSpec pagingSpec = new PagingSpec();
        pagingSpec.setLimit(1L);
        financialInstitutionsService.getFinancialInstitutions(pagingSpec).stream().forEach(financialInstitution -> {
                                                            LOGGER.info(financialInstitution.toString());}
                                                            );

        CustomerAccessToken customerAccessTokenRequest = new CustomerAccessToken("application_customer_reference");
        CustomerAccessToken generatedCustomerAccessToken = customerAccessTokensService.createCustomerAccessToken(customerAccessTokenRequest);

        FinancialInstitution newFinancialInstitution = new FinancialInstitution();
        newFinancialInstitution.setSandbox(Boolean.TRUE);
        newFinancialInstitution.setName("FI-"+now);

        newFinancialInstitution = financialInstitutionsService.createSandboxFinancialInstitution(newFinancialInstitution);

        LOGGER.info("New Sandbox Financial Institution created:"+newFinancialInstitution.toString());

        newFinancialInstitution.setName("FI-"+now);
        newFinancialInstitution = financialInstitutionsService.updateSandboxFinancialInstitution(newFinancialInstitution);
        LOGGER.info("New Sandbox Financial Institution updated:"+newFinancialInstitution.toString());

        try {
            financialInstitutionsService.deleteSandboxFinancialInstitution(newFinancialInstitution.getId());
        } catch (ResourceNotFoundException e) {
            LOGGER.info(e);
        }
        LOGGER.info("New Sandbox Financial Institution deleted");


        FinancialInstitutionUser financialInstitutionUser = new FinancialInstitutionUser();
        financialInstitutionUser.setFirstName("FirstName-"+now);
        financialInstitutionUser.setLastName("LastName-"+now);
        financialInstitutionUser.setLogin("Login-"+now);
        financialInstitutionUser.setPassword("Password-"+now);

        financialInstitutionUser = usersService.createSandboxFinancialInstitutionUser(financialInstitutionUser);
        LOGGER.info("New Sandbox Financial Institution User created:"+financialInstitutionUser.toString());

        financialInstitutionUser.setPassword("password");
        usersService.updateSandboxFinancialInstitutionUser(financialInstitutionUser);
        LOGGER.info("New Sandbox Financial Institution User updated:"+financialInstitutionUser.toString());

        usersService.getSandboxFinancialInstitutionUsers().stream().forEach(financialInstitutionUser1 -> LOGGER.info("Financial Institution User:"+financialInstitutionUser1.toString()));

        try {
            usersService.getSandboxFinancialInstitutionUser(financialInstitutionUser.getId());
        } catch (ResourceNotFoundException e) {
            LOGGER.info(e);
        }

        LOGGER.info("Got Sandbox Financial Institution User");

        try {
            usersService.deleteSandboxFinancialInstitutionUser(financialInstitutionUser.getId());
        } catch (ResourceNotFoundException e) {
            LOGGER.info(e);
        }

        LOGGER.info("Sandbox Financial Institution User deleted");

        newFinancialInstitution.setId(null);
        newFinancialInstitution = financialInstitutionsService.createSandboxFinancialInstitution(newFinancialInstitution);

        List<FinancialInstitutionAccount> sandboxAccounts = new ArrayList<>();

        financialInstitutionUser = usersService.createSandboxFinancialInstitutionUser(financialInstitutionUser);

        LOGGER.info("Start : Adding account to User's Financial Institution");
        FinancialInstitutionAccount sandboxAccount2 = new FinancialInstitutionAccount();
        sandboxAccount2.setSubType("checking");
        sandboxAccount2.setReference("BE02379129664149");
        sandboxAccount2.setReferenceType("IBAN");
        sandboxAccount2.setDescription("Checking Account");
        sandboxAccount2.setCurrency("EUR");

        sandboxAccount2.setFinancialInstitution(newFinancialInstitution);

        FinancialInstitutionAccount sandboxAccountToGet = accountsService.createSandBoxAccount(generatedCustomerAccessToken
                , newFinancialInstitution.getId()
                , financialInstitutionUser.getId()
                , sandboxAccount2);

        for (int index = 0 ; index < SANDBOX_ACCOUNTS_TO_CREATE; index++) {
            FinancialInstitutionAccount sandboxAccount = new FinancialInstitutionAccount();
            sandboxAccount.setSubType("checking");
            sandboxAccount.setReference(Iban.random(CountryCode.BE).toString());
            sandboxAccount.setReferenceType("IBAN");
            sandboxAccount.setDescription("Checking Account");
            sandboxAccount.setCurrency("EUR");
            sandboxAccount.setFinancialInstitution(newFinancialInstitution);

            FinancialInstitutionAccount createdSandboxAccount = accountsService.createSandBoxAccount(generatedCustomerAccessToken
                    , newFinancialInstitution.getId()
                    , financialInstitutionUser.getId()
                    , sandboxAccount);
            sandboxAccounts.add(createdSandboxAccount);
            LOGGER.info("Account:"+createdSandboxAccount.getReference()+":created.");
        }

        LOGGER.info("END : Adding account to User's Financial Institutions");

        LOGGER.info("Start : Adding transactions to to User's Financial Institutions's account");

        Random random = new Random();

        final UUID newFinancialInstitutionId  = newFinancialInstitution.getId();
        final UUID financialInstitutionUserId = financialInstitutionUser.getId();

        sandboxAccounts.stream().forEach(createdSandboxAccount -> {
            for (int index = 0; index < SANDBOX_TRANSACTIONS_T0_CREATE_PER_ACCOUNT ; index++) {
                Instant executionDate = now.plus(3, ChronoUnit.DAYS);
                Instant valueDate = now.minus(1, ChronoUnit.DAYS);
                FinancialInstitutionTransaction sandboxTransaction = new FinancialInstitutionTransaction();
                sandboxTransaction.setFinancialInstitutionAccount(createdSandboxAccount);
                sandboxTransaction.setAmount(Precision.round(random.doubles(10,1000).findFirst().getAsDouble() * (random .nextBoolean() ? -1 : 1), 2));
                sandboxTransaction.setCounterpartName("Stroman, Hettinger and Swift");
                sandboxTransaction.setCounterpartReference(Iban.random(CountryCode.BE).getAccountNumber());
                sandboxTransaction.setCurrency("EUR");
                sandboxTransaction.setDescription("Car rental");
                sandboxTransaction.setExecutionDate(executionDate);
                sandboxTransaction.setRemittanceInformation("Aspernatur et quibusdam.");
                sandboxTransaction.setRemittanceInformationType("unstructured");
                sandboxTransaction.setValueDate(valueDate);

                FinancialInstitutionTransaction createdSandboxTransaction = transactionsService.createSandBoxTransaction(
                        generatedCustomerAccessToken
                        , newFinancialInstitutionId
                        , financialInstitutionUserId
                        , createdSandboxAccount.getId()
                        , sandboxTransaction
                );
                LOGGER.info("SandboxAccount:"+createdSandboxAccount.getReference()+":Transaction:"+createdSandboxTransaction.getId()+":created");
            }
        });
        LOGGER.info("END : Adding transactions to to User's Financial Institutions's account");
    }
}
