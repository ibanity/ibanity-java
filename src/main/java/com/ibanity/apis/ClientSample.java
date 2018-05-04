package com.ibanity.apis;

import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessAuthorization;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.Transaction;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ClientSample {
    private static final Logger LOGGER = LogManager.getLogger(ClientSample.class);

    private static final String IBANITY_API_ENDPOINT = IBanityConfiguration.getConfiguration().getString(IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "services.endpoint");

    private static final String FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL = IBanityConfiguration.getConfiguration().getString(IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.accounts.information.access.result.redirect.url");
    private static final String FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL = IBanityConfiguration.getConfiguration().getString(IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.payments.initiation.result.redirect.url");

    private FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();
    private CustomerAccessTokensService customerAccessTokensService = new CustomerAccessTokensServiceImpl();
    private AccountsService accountsService = new AccountsServiceImpl();
    private TransactionsService transactionsService = new TransactionsServiceImpl();
    private PaymentsService paymentsService = new PaymentsServiceImpl();

    public static void main(String[] args){
        ClientSample client  = new ClientSample();
        client.startFlow();
    }

    public void startFlow(){

        LOGGER.info("Start : List of Financial Institutions: starting with 1 FI");

        AtomicReference<FinancialInstitution> inUseFinancialInstitution = new AtomicReference();
        PagingSpec pagingSpec = new PagingSpec();
        pagingSpec.setLimit(1);
        financialInstitutionsService.getFinancialInstitutions(pagingSpec).stream().forEach(financialInstitution -> {
                                                            inUseFinancialInstitution.set(financialInstitution);
                                                            LOGGER.info(financialInstitution.toString());}
                                                            );
        LOGGER.info("END : List of Financial Institutions: starting with 1 FI");
        LOGGER.info("Start : List of Financial Institutions: after:"+inUseFinancialInstitution.get().getId()+":");
        pagingSpec.setAfter(inUseFinancialInstitution.get().getId());
        pagingSpec.setLimit(10);
        financialInstitutionsService.getFinancialInstitutions(pagingSpec).stream().forEach(financialInstitution -> {
            inUseFinancialInstitution.set(financialInstitution);
            LOGGER.info(financialInstitution.toString());}
        );
        LOGGER.info("END : List of Financial Institutions: after:");


        LOGGER.info("Start : Customer Access Token Request");
        CustomerAccessToken customerAccessTokenRequest = new CustomerAccessToken("application_customer_reference");
        CustomerAccessToken generatedCustomerAccessToken = customerAccessTokensService.createCustomerAccessToken(customerAccessTokenRequest);
        LOGGER.info(generatedCustomerAccessToken);
        LOGGER.info("End : Customer Access Token Request");

        LOGGER.info("Start : Accounts Information Access Request");
        AccountInformationAccessRequest accountInformationAccessRequest = new AccountInformationAccessRequest();
        accountInformationAccessRequest.setConsentReference(UUID.randomUUID().toString());
        accountInformationAccessRequest.setRedirectUri(FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL);
        accountInformationAccessRequest.setFinancialInstitution(inUseFinancialInstitution.get());
        AccountInformationAccessRequest resultingAccountInformationAccessRequest = accountsService.getAccountsInformationAccessRedirectUrl(generatedCustomerAccessToken, accountInformationAccessRequest);
        LOGGER.info("AccountInformationAccessRequest:"+resultingAccountInformationAccessRequest.toString());
        LOGGER.warn("#######################################");
        LOGGER.warn("Accounts Information Access Request: End-User to be redirected to :\n"+resultingAccountInformationAccessRequest.getLinks().getRedirect());
        LOGGER.warn("#######################################");
        LOGGER.info("in order to specify which Financial Institution's accounts will be authorised to be accessed through the TPP.");
        LOGGER.info("End : Account Information Access Request");

        Scanner s = new Scanner(System.in);
        System.out.println("Please use the URL provided here above (End-User to be redirected to:...) in order to authorize accounts then, once authorization done, Press ENTER to proceed......");
        s.nextLine();

        LOGGER.info("Start : AccountInformationAccessAuthorization");
        AccountInformationAccessAuthorization accountInformationAccessAuthorization = new AccountInformationAccessAuthorization();
        AtomicReference<AccountInformationAccessAuthorization> inUseAccountInformationAccessAuthorization = new AtomicReference();
        List<AccountInformationAccessAuthorization> accountsAuthorizations = accountsService.getAccountsInformationAccessAuthorizations(generatedCustomerAccessToken, resultingAccountInformationAccessRequest);
        accountsAuthorizations.stream().forEach(authorization -> {inUseAccountInformationAccessAuthorization.set(authorization); LOGGER.info(authorization.toString());});
        LOGGER.info("END : AccountInformationAccessAuthorization");


        AtomicReference<Account> inUseAccount = new AtomicReference();
        LOGGER.info("Start : get All Accounts");
        List<Account> accounts = accountsService.getCustomerAccounts(generatedCustomerAccessToken);
        accounts.forEach(account -> LOGGER.info(account.toString()));
        LOGGER.info("End : get All Accounts");
        LOGGER.info("Start : get All Accounts for financial institution:"+inUseFinancialInstitution.get().getId()+":");
        accountsService.getCustomerAccounts(generatedCustomerAccessToken,inUseFinancialInstitution.get().getId(), pagingSpec).forEach(account -> {inUseAccount.set(account); LOGGER.info(account.toString());});
        LOGGER.info("End : get All Accounts for financial institution:"+inUseFinancialInstitution.get().getId()+":");

        pagingSpec = new PagingSpec();
        pagingSpec.setLimit(2);
        LOGGER.info("Start : Accounts details 2 of them");
        accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec).forEach(account -> {inUseAccount.set(account); LOGGER.info(account.toString());});
        LOGGER.info("End : Accounts details 2 of them");

        UUID afterUUID = inUseAccount.get().getId();
        pagingSpec.setAfter(afterUUID);
        LOGGER.info("Start : Accounts details next 2");
        accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec).forEach(account ->{inUseAccount.set(account); LOGGER.info(account.toString());});
        LOGGER.info("End : Accounts details next 2");

        UUID beforeUUID = inUseAccount.get().getId();

        pagingSpec = new PagingSpec();
        pagingSpec.setAfter(beforeUUID);
        pagingSpec.setLimit(100);
        LOGGER.info("Start : Accounts details all the rest");
        accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec).forEach(account -> {inUseAccount.set(account); LOGGER.info(account.toString());});
        LOGGER.info("End : Accounts details all the rest");


        LOGGER.info("Start : Transactions details");
        List<Transaction> transactionsList = transactionsService.getAccountTransactions(generatedCustomerAccessToken, inUseAccount.get());
        transactionsList.stream().forEach(transaction -> LOGGER.info(transaction.toString()));

        transactionsList = transactionsService.getAccountTransactions(generatedCustomerAccessToken, inUseAccount.get());
        transactionsList.stream().forEach(transaction -> LOGGER.info(transaction.toString()));
        LOGGER.info("End : Transactions details");

        LOGGER.info("Start : Remove Account Access Authorization");
        accountsService.revokeAccountsAccessAuthorization(generatedCustomerAccessToken, inUseFinancialInstitution.get().getId(), inUseAccountInformationAccessAuthorization.get());
        LOGGER.info("Stop : Remove Account Access Authorization");

        LOGGER.info("Start : Payment Initiation Request");
        PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();
        paymentInitiationRequest.setRedirectUri(FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL);
        paymentInitiationRequest.setFinancialInstitution(inUseFinancialInstitution.get());
        paymentInitiationRequest.setConsentReference(UUID.randomUUID().toString());
        paymentInitiationRequest.setEndToEndId(UUID.randomUUID().toString());
        paymentInitiationRequest.setCustomerIp("192.168.0.1");
        paymentInitiationRequest.setCustomerAgent("Mozilla");
        paymentInitiationRequest.setProductType("sepa-credit-transfer");
        paymentInitiationRequest.setRemittanceInformationType("unstructured");
        paymentInitiationRequest.setCurrency("EUR");
        paymentInitiationRequest.setAmount(new Double(50.4));
        paymentInitiationRequest.setCreditorName("Fake Creditor Name");
        paymentInitiationRequest.setCreditorAccountReference("BE23947805459949");
        paymentInitiationRequest.setCreditorAccountReferenceType("IBAN");
        PaymentInitiationRequest resultingPaymentInitiationRequest = paymentsService.initiatePaymentRequest(generatedCustomerAccessToken, paymentInitiationRequest);
        LOGGER.warn("#######################################");
        LOGGER.warn("Payment Initiation: End User to be redirected to :\n"+resultingPaymentInitiationRequest.getLinks().getRedirect()+":\n in order to complete/proceed with the payment process.");
        LOGGER.warn("#######################################");
        LOGGER.info("End : Payment Initiation Request");
    }
}
