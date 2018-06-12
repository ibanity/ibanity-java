package com.ibanity.apis;

import com.google.common.net.InetAddresses;
import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.AccountsService;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import com.ibanity.apis.client.services.FinancialInstitutionsService;
import com.ibanity.apis.client.services.PaymentsService;
import com.ibanity.apis.client.services.TransactionsService;
import com.ibanity.apis.client.services.configuration.IbanityConfiguration;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.services.impl.CustomerAccessTokensServiceImpl;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import com.ibanity.apis.client.services.impl.PaymentsServiceImpl;
import com.ibanity.apis.client.services.impl.TransactionsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ClientSample {
    private static final Logger LOGGER = LogManager.getLogger(ClientSample.class);

    private static final String FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.accounts.information.access.result.redirect.url");
    private static final String FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.payments.initiation.result.redirect.url");

    private static final String LOGGER_LINE_SEPARATOR = "#######################################";

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
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        financialInstitutionsService.getFinancialInstitutions(pagingSpec).stream().forEach(financialInstitution -> {
                                                            inUseFinancialInstitution.set(financialInstitution);
                                                            LOGGER.info(financialInstitution.toString());}
                                                            );
        LOGGER.info("END : List of Financial Institutions: starting with 1 FI");
        LOGGER.info("Start : List of Financial Institutions: after:"+inUseFinancialInstitution.get().getId()+":");
        pagingSpec.setAfter(inUseFinancialInstitution.get().getId());
        pagingSpec.setLimit(10L);
        financialInstitutionsService.getFinancialInstitutions(pagingSpec).stream().forEach(financialInstitution -> {
            inUseFinancialInstitution.set(financialInstitution);
            LOGGER.info(financialInstitution.toString());}
        );
        LOGGER.info("END : List of Financial Institutions: after:");


        LOGGER.info("Start : Customer Access Token Request");
        CustomerAccessToken customerAccessTokenRequest = new CustomerAccessToken(UUID.randomUUID());
        customerAccessTokenRequest.setApplicationCustomerReference("application_customer_reference");
        CustomerAccessToken generatedCustomerAccessToken = customerAccessTokensService.createCustomerAccessToken(customerAccessTokenRequest);
        LOGGER.info(generatedCustomerAccessToken);
        LOGGER.info("End : Customer Access Token Request");

        LOGGER.info("Start : Accounts Information Access Request");
        AccountInformationAccessRequest accountInformationAccessRequest = new AccountInformationAccessRequest();
        accountInformationAccessRequest.setConsentReference(UUID.randomUUID().toString());
        accountInformationAccessRequest.setRedirectUri(FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL);
        accountInformationAccessRequest.setFinancialInstitution(inUseFinancialInstitution.get());
        AccountInformationAccessRequest resultingAccountInformationAccessRequest = accountsService.getAccountsInformationAccessRedirectUrl(generatedCustomerAccessToken, accountInformationAccessRequest);
        resultingAccountInformationAccessRequest.setFinancialInstitution(inUseFinancialInstitution.get());
        LOGGER.info("AccountInformationAccessRequest:"+resultingAccountInformationAccessRequest.toString());
        LOGGER.warn(LOGGER_LINE_SEPARATOR);
        LOGGER.warn("Accounts Information Access Request: End-User to be redirected to :\n"+resultingAccountInformationAccessRequest.getLinks().getRedirect());
        LOGGER.warn(LOGGER_LINE_SEPARATOR);
        LOGGER.info("in order to specify which Financial Institution's accounts will be authorised to be accessed through the TPP.");
        LOGGER.info("End : Account Information Access Request");

        Scanner s = new Scanner(System.in);
        LOGGER.warn("Please use the URL provided here above (End-User to be redirected to:...) in order to authorize accounts then, once authorization done, Press ENTER to proceed......");
        s.nextLine();

        AtomicReference<Account> inUseAccount = new AtomicReference();
        LOGGER.info("Start : get All Accounts");
        List<Account> accounts = accountsService.getCustomerAccounts(generatedCustomerAccessToken);
        accounts.forEach(account -> LOGGER.info(account.toString()));
        LOGGER.info("End : get All Accounts");
        LOGGER.info("Start : get All Accounts for financial institution:"+inUseFinancialInstitution.get().getId()+":");
        accountsService.getCustomerAccounts(generatedCustomerAccessToken,inUseFinancialInstitution.get().getId(), pagingSpec).forEach(account -> {inUseAccount.set(account); LOGGER.info(account.toString());});
        LOGGER.info("End : get All Accounts for financial institution:"+inUseFinancialInstitution.get().getId()+":");

        pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(2L);
        LOGGER.info("Start : Accounts details 2 of them");
        accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec).forEach(account -> {inUseAccount.set(account); LOGGER.info(account.toString());});
        LOGGER.info("End : Accounts details 2 of them");

        UUID afterUUID = inUseAccount.get().getId();
        pagingSpec.setAfter(afterUUID);
        LOGGER.info("Start : Accounts details next 2");
        accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec).forEach(account ->{inUseAccount.set(account); LOGGER.info(account.toString());});
        LOGGER.info("End : Accounts details next 2");

        UUID beforeUUID = inUseAccount.get().getId();

        pagingSpec = new IbanityPagingSpec();
        pagingSpec.setAfter(beforeUUID);
        pagingSpec.setLimit(100L);
        LOGGER.info("Start : Accounts details all the rest");
        accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec).forEach(account -> {inUseAccount.set(account); LOGGER.info(account.toString());});
        LOGGER.info("End : Accounts details all the rest");

        LOGGER.info("Start : get Account with Id:"+beforeUUID+": from Financial Institution:" + inUseFinancialInstitution.get().getId());
        try {
            LOGGER.info("Account = "+ accountsService.getCustomerAccount(generatedCustomerAccessToken, beforeUUID, inUseFinancialInstitution.get().getId()).toString());
        } catch (ResourceNotFoundException e) {
            LOGGER.info(e);
        }
        LOGGER.info("END : get Account with Id:"+beforeUUID+": from Financial Institution:" + inUseFinancialInstitution.get().getId());


        LOGGER.info("Start : Transactions details");
        accounts.stream().forEach(account -> {
            LOGGER.info("Transactions Details of Account:"+account.getReference()+":");
            List<Transaction> transactionsList = transactionsService.getAccountTransactions(generatedCustomerAccessToken, account);
            transactionsList.stream().forEach(transaction -> LOGGER.info(transaction.toString()));
        });
        LOGGER.info("End : Transactions details");

        LOGGER.info("Start : Payment Initiation Request");
        Random random = new Random();
        PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();
        paymentInitiationRequest.setRedirectUri(FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL);
        paymentInitiationRequest.setFinancialInstitution(inUseFinancialInstitution.get());
        paymentInitiationRequest.setConsentReference(UUID.randomUUID().toString());
        paymentInitiationRequest.setEndToEndId(UUID.randomUUID().toString());
        paymentInitiationRequest.setCustomerIp(InetAddresses.fromInteger(random.nextInt()).getHostAddress());
        paymentInitiationRequest.setCustomerAgent("Mozilla");
        paymentInitiationRequest.setProductType("sepa-credit-transfer");
        paymentInitiationRequest.setRemittanceInformationType("unstructured");
        paymentInitiationRequest.setCurrency("EUR");
        paymentInitiationRequest.setAmount(Double.valueOf(50.4));
        paymentInitiationRequest.setCreditorName("Fake Creditor Name");
        paymentInitiationRequest.setCreditorAccountReference("BE23947805459949");
        paymentInitiationRequest.setCreditorAccountReferenceType("IBAN");
        PaymentInitiationRequest resultingPaymentInitiationRequest = paymentsService.initiatePaymentRequest(generatedCustomerAccessToken, paymentInitiationRequest);
        LOGGER.warn(LOGGER_LINE_SEPARATOR);
        LOGGER.warn("Payment Initiation: End User to be redirected to :\n"+resultingPaymentInitiationRequest.getLinks().getRedirect()+":\n in order to complete/proceed with the payment process.");
        LOGGER.warn(LOGGER_LINE_SEPARATOR);
        LOGGER.info("End : Payment Initiation Request");
    }
}
