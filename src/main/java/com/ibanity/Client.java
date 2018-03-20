package com.ibanity;

import com.ibanity.api.*;
import com.ibanity.api.configuration.IBanityConfiguration;
import com.ibanity.api.impl.*;
import com.ibanity.exceptions.ResourceNotFoundException;
import com.ibanity.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Client {
    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    private static final String IBANITY_API_ENDPOINT = IBanityConfiguration.getConfiguration().getString(IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "api.endpoint");

    //Don't forget to configure the REDIRECT URL in your Application configuration on the iBanity Developper Portal
    private static final String FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL = "https://faketpp.com/accounts-access-granted";
    private static final String FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL = "https://faketpp2.com/payment-initiation-redirect";

    private FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();
    private CustomerAccessTokensService customerAccessTokensService = new CustomerAccessTokensServiceImpl();
    private AccountsService accountsService = new AccountsServiceImpl();
    private TransactionsService transactionsService = new TransactionsServiceImpl();
    private PaymentsService paymentsService = new PaymentsServiceImpl();

    public static void main(String[] args){
        Client client  = new Client();
        client.startFlow1();
    }

    public void startFlow1(){
        AtomicReference<FinancialInstitution> inUseFinancialInstitution = new AtomicReference();
        financialInstitutionsService.getFinancialInstitutions().stream().forEach(financialInstitution -> {
                                                            inUseFinancialInstitution.set(financialInstitution);
                                                            LOGGER.debug(financialInstitution.toString());}
                                                            );
        LOGGER.debug("Start : Customer Access Token Request");
        CustomerAccessToken customerAccessTokenRequest = new CustomerAccessToken("application_customer_reference");
        CustomerAccessToken generatedCustomerAccessToken = customerAccessTokensService.createCustomerAccessToken(customerAccessTokenRequest);
        LOGGER.debug(generatedCustomerAccessToken);
        LOGGER.debug("End : Customer Access Token Request");

        LOGGER.debug("Start : Accounts Information Access Request");
        AccountInformationAccessRequest accountInformationAccessRequest = new AccountInformationAccessRequest();
        accountInformationAccessRequest.setConsentReference(UUID.randomUUID().toString());
        accountInformationAccessRequest.setRedirectUri(FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL);
        accountInformationAccessRequest.setFinancialInstitution(inUseFinancialInstitution.get());
        AccountInformationAccessRequest resultingAccountInformationAccessRequest = accountsService.getAccountsInformationAccessRedirectUrl(generatedCustomerAccessToken, accountInformationAccessRequest);
        LOGGER.debug("AccountInformationAccessRequest:"+resultingAccountInformationAccessRequest.toString());
        LOGGER.debug("Accounts Information Access Request: End-User to be redirected to :\n"+resultingAccountInformationAccessRequest.getLinks().getRedirect());
        LOGGER.debug("in order to specify which Financial Institution's accounts will be authorised to be accessed through the TPP.");
        LOGGER.debug("End : Account Information Access Request");

        Scanner s = new Scanner(System.in);
        System.out.println("Please use the URL provided here above (End-User to be redirected to:...) in order to authorize accounts then, once authorization done, Press ENTER to proceed......");
        s.nextLine();

        LOGGER.debug("Start : AccountInformationAccessAuthorization");
        AccountInformationAccessAuthorization accountInformationAccessAuthorization = new AccountInformationAccessAuthorization();
        AtomicReference<AccountInformationAccessAuthorization> inUseAccountInformationAccessAuthorization = new AtomicReference();
        List<AccountInformationAccessAuthorization> accountsAuthorizations = accountsService.getAccountsInformationAccessAuthorizations(generatedCustomerAccessToken, resultingAccountInformationAccessRequest);
        accountsAuthorizations.stream().forEach(authorization -> {inUseAccountInformationAccessAuthorization.set(authorization); LOGGER.debug(authorization.toString());});
        LOGGER.debug("END : AccountInformationAccessAuthorization");


        AtomicReference<Account> inUseAccount = new AtomicReference();
        LOGGER.debug("Start : Accounts details");
        accountsService.getCustomerAccounts(generatedCustomerAccessToken).forEach(account -> LOGGER.debug(account.toString()));
        accountsService.getCustomerAccounts(generatedCustomerAccessToken,inUseFinancialInstitution.get().getId()).forEach(account -> {inUseAccount.set(account); LOGGER.debug(account.toString());});
        LOGGER.debug("End : Accounts details");

        LOGGER.debug("Start : Transactions details");
        transactionsService.getAccountTransactions(generatedCustomerAccessToken, inUseAccount.get()).forEach(transaction -> LOGGER.debug(transaction.toString()));
        LOGGER.debug("End : Transactions details");

        LOGGER.debug("Start : Remove Account Access Authorization");
        accountsService.revokeAccountsAccessAuthorization(generatedCustomerAccessToken, inUseFinancialInstitution.get().getId(), inUseAccountInformationAccessAuthorization.get());
        LOGGER.debug("Stop : Remove Account Access Authorization");

        LOGGER.debug("Start : Payment Initiation Request");
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
        LOGGER.debug("Payment Initiation: End User to be redirected to :\n"+resultingPaymentInitiationRequest.getLinks().getRedirect()+":\n in order to complete/proceed with the payment process.");
        LOGGER.debug("End : Payment Initiation Request");

    }

    public void getFinancialInstitutions() {
        financialInstitutionsService.getFinancialInstitutions().stream().forEach(financialInstitution -> LOGGER.debug(financialInstitution.toString()));
    }

    public void getFinancialInstitution(UUID id){
        try {
            LOGGER.debug(financialInstitutionsService.getFinancialInstitution(id).toString());
        } catch (ResourceNotFoundException e) {
            LOGGER.debug(e.getMessage());
        }
    }

    public void getCustomerAccessToken(){
        CustomerAccessToken customerAccessToken = new CustomerAccessToken("application_customer_reference");
        LOGGER.debug(customerAccessTokensService.createCustomerAccessToken(customerAccessToken));
    }
}
