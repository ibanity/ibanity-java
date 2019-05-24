package com.ibanity.samples;

import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.Synchronization;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.samples.customer.AccountInformationAccessRequestSample;
import com.ibanity.samples.customer.AccountSample;
import com.ibanity.samples.customer.CustomerAccessTokenSample;
import com.ibanity.samples.customer.FinancialInstitutionSample;
import com.ibanity.samples.customer.PaymentInitiationRequestSample;
import com.ibanity.samples.customer.SynchronizationSample;
import com.ibanity.samples.customer.TransactionSample;
import com.ibanity.samples.helper.SampleHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;

public class ClientSample {
    private static final Logger LOGGER = LogManager.getLogger(ClientSample.class);

    // Sample services
    private final CustomerAccessTokenSample customerAccessTokenSample = new CustomerAccessTokenSample();
    private final FinancialInstitutionSample financialInstitutionSample = new FinancialInstitutionSample();
    private final AccountInformationAccessRequestSample accountInformationAccessRequestSample = new AccountInformationAccessRequestSample();
    private final AccountSample accountSample = new AccountSample();
    private final TransactionSample transactionSample = new TransactionSample();
    private final PaymentInitiationRequestSample paymentInitiationRequestSample = new PaymentInitiationRequestSample();
    private final SynchronizationSample synchronizationSample = new SynchronizationSample();

    // Configurations
    private final String fakeTppAccountInformationAccessRedirectUrl = getConfiguration("tpp.accounts.information.access.result.redirect.url");
    private final String fakeTppPaymentInitiationRedirectUrl = getConfiguration("tpp.payments.initiation.result.redirect.url");

    public static void main(String[] args) {
        IbanityService.apiUrlProvider().loadApiSchema();
        ClientSample clientSample = new ClientSample();

        CustomerAccessToken customerAccessToken = clientSample.customerAccessTokenSamples();
        List<FinancialInstitution> financialInstitutions = clientSample.financialInstitutionSamples();

        clientSample.accountInformationAccessRequestSamples(customerAccessToken, financialInstitutions);

        List<Account> accounts = clientSample.accountSamples(customerAccessToken, financialInstitutions);
        List<Transaction> transactions = clientSample.transactionSamples(customerAccessToken, financialInstitutions, accounts);
        Synchronization synchronization = clientSample.synchronizationSamples(customerAccessToken, accounts);

        LOGGER.info("List of financialInstitutions: {}", financialInstitutions);
        LOGGER.info("List of accounts: {}", accounts);
        LOGGER.info("List of transactions: {}", transactions);
        LOGGER.info("synchronization: {}", synchronization);


        clientSample.paymentInitiationRequestSamples();

        LOGGER.info("Samples end");
    }

    private Synchronization synchronizationSamples(CustomerAccessToken customerAccessToken, List<Account> accounts) {
        Account account = accounts.get(0);

        Synchronization synchronization = synchronizationSample.create(customerAccessToken, account.getId());
        synchronization = synchronizationSample.find(customerAccessToken, synchronization.getId());
        return synchronization;
    }

    public CustomerAccessToken customerAccessTokenSamples() {
        LOGGER.info("Customer Access Token samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        return customerAccessTokenSample.create(consentReference);
    }

    public List<FinancialInstitution> financialInstitutionSamples() {
        LOGGER.info("Financial Institution samples");

        return financialInstitutionSample.list();
    }

    public void accountInformationAccessRequestSamples(CustomerAccessToken customerAccessToken, List<FinancialInstitution> financialInstitutions) {
        LOGGER.info("Account Information Access Request samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        AccountInformationAccessRequest accountInformationAccessRequest =
                this.accountInformationAccessRequestSample.create(
                        financialInstitutions.get(0), customerAccessToken,
                        consentReference, fakeTppAccountInformationAccessRedirectUrl);

        SampleHelper.waitForAuthorizationWebFlow(accountInformationAccessRequest);
    }

    public List<Account> accountSamples(CustomerAccessToken customerAccessToken, List<FinancialInstitution> financialInstitutions) {
        LOGGER.info("Accounts samples");

        FinancialInstitution financialInstitution = financialInstitutions.get(0);
        List<Account> accounts = accountSample.list(customerAccessToken, financialInstitution).getItems();

        accountSample.get(customerAccessToken, financialInstitution, accounts.get(0).getId());

        return accounts;
    }

    public List<Transaction> transactionSamples(CustomerAccessToken customerAccessToken, List<FinancialInstitution> financialInstitutionList, List<Account> accountList) {
        LOGGER.info("Transactions samples");

        FinancialInstitution financialInstitution = financialInstitutionList.get(0);

        Account account = accountList.get(0);

        List<Transaction> transactions = transactionSample.list(customerAccessToken, financialInstitution, account);

        transactionSample.get(customerAccessToken, financialInstitution, account, transactions.get(0).getId());

        return transactions;
    }

    public void paymentInitiationRequestSamples() {
        LOGGER.info("Payment Initiation Request samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        CustomerAccessToken customerAccessToken = customerAccessTokenSample.create(consentReference);
        FinancialInstitution financialInstitution = financialInstitutionSample.list().get(0);

        PaymentInitiationRequest paymentInitiationRequest =
                paymentInitiationRequestSample.create(financialInstitution, customerAccessToken,
                        fakeTppPaymentInitiationRedirectUrl);
    }

}
