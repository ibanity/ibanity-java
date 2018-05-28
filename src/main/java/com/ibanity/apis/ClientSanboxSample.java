package com.ibanity.apis;

import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionAccount;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionTransaction;
import com.ibanity.apis.client.paging.PagingSpec;
import com.ibanity.apis.client.services.AccountsService;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import com.ibanity.apis.client.services.FinancialInstitutionsService;
import com.ibanity.apis.client.services.PaymentsService;
import com.ibanity.apis.client.services.TransactionsService;
import com.ibanity.apis.client.services.configuration.IBanityConfiguration;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.services.impl.CustomerAccessTokensServiceImpl;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import com.ibanity.apis.client.services.impl.PaymentsServiceImpl;
import com.ibanity.apis.client.services.impl.TransactionsServiceImpl;
import org.apache.commons.math3.util.Precision;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ClientSanboxSample {
    private static final Logger LOGGER = LogManager.getLogger(ClientSanboxSample.class);

    private static final String IBANITY_API_ENDPOINT = IBanityConfiguration.getConfiguration().getString(IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "services.endpoint");

    private static final String IBANITY_CLIENT_SANDBOX_USER_ID = IBanityConfiguration.getConfiguration().getString(IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.sandox.user_id");

    private static final String FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL = IBanityConfiguration.getConfiguration().getString(IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.accounts.information.access.result.redirect.url");
    private static final String FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL = IBanityConfiguration.getConfiguration().getString(IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.payments.initiation.result.redirect.url");

    private static DecimalFormat df2 = new DecimalFormat(".##");


    private FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();
    private CustomerAccessTokensService customerAccessTokensService = new CustomerAccessTokensServiceImpl();
    private AccountsService accountsService = new AccountsServiceImpl();
    private TransactionsService transactionsService = new TransactionsServiceImpl();
    private PaymentsService paymentsService = new PaymentsServiceImpl();

    public static void main(String[] args){
        ClientSanboxSample client  = new ClientSanboxSample();
        client.startFlow();
    }

    public void startFlow(){

        LOGGER.info("Start : Adding account to User's Financial Institutions");

        AtomicReference<FinancialInstitution> inUseFinancialInstitution = new AtomicReference();
        PagingSpec pagingSpec = new PagingSpec();
        pagingSpec.setLimit(1);
        financialInstitutionsService.getFinancialInstitutions(pagingSpec).stream().forEach(financialInstitution -> {
                                                            inUseFinancialInstitution.set(financialInstitution);
                                                            LOGGER.info(financialInstitution.toString());}
                                                            );

        CustomerAccessToken customerAccessTokenRequest = new CustomerAccessToken("application_customer_reference");
        CustomerAccessToken generatedCustomerAccessToken = customerAccessTokensService.createCustomerAccessToken(customerAccessTokenRequest);

        List<FinancialInstitutionAccount> sandboxAccounts = new ArrayList<>();

        for (int index = 0 ; index < 30; index++) {
            FinancialInstitutionAccount sandboxAccount = new FinancialInstitutionAccount();
            sandboxAccount.setSubType("checking");
            sandboxAccount.setReference(Iban.random(CountryCode.BE).toString());
            sandboxAccount.setReferenceType("IBAN");
            sandboxAccount.setDescription("Checking Account");
            sandboxAccount.setCurrency("EUR");
            sandboxAccount.setFinancialInstitution(inUseFinancialInstitution.get());

            FinancialInstitutionAccount createdSandboxAccount = accountsService.createSandBoxAccount(generatedCustomerAccessToken
                    , inUseFinancialInstitution.get().getId()
                    , UUID.fromString(IBANITY_CLIENT_SANDBOX_USER_ID)
                    , sandboxAccount);
            sandboxAccounts.add(createdSandboxAccount);
            LOGGER.info("Account:"+createdSandboxAccount.getReference()+":created.");
        }

        LOGGER.info("END : Adding account to User's Financial Institutions");

        LOGGER.info("Start : Adding transactions to to User's Financial Institutions's account");

        Random random = new Random();

        sandboxAccounts.stream().forEach(createdSandboxAccount -> {
            for (int index = 0; index < 100 ; index++) {
                Instant now = Instant.now();
                Instant executionDate = now.plus(3, ChronoUnit.DAYS);
                Instant valueDate = now.minus(1, ChronoUnit.DAYS);
                FinancialInstitutionTransaction sandboxTransaction = new FinancialInstitutionTransaction();
                sandboxTransaction.setAccount(createdSandboxAccount);
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
                        , inUseFinancialInstitution.get().getId()
                        , UUID.fromString(IBANITY_CLIENT_SANDBOX_USER_ID)
                        , createdSandboxAccount.getId()
                        , sandboxTransaction
                );
                LOGGER.info("SandboxAccount:"+createdSandboxAccount.getReference()+":Transaction:"+createdSandboxTransaction.getId()+":created");
            }
        });
        LOGGER.info("END : Adding transactions to to User's Financial Institutions's account");
    }
}
