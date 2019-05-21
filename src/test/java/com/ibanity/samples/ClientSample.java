package com.ibanity.samples;

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
        ClientSample clientSample = new ClientSample();

        clientSample.customerAccessTokenSamples();
        clientSample.financialInstitutionSamples();
        clientSample.accountInformationAccessRequestSamples();
        clientSample.accountSamples();
        clientSample.transactionSamples();
        clientSample.paymentInitiationRequestSamples();
        clientSample.synchronizationSamples();

        LOGGER.info("Samples end");
    }

    private void synchronizationSamples() {
        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        CustomerAccessToken customerAccessToken = customerAccessTokenSample.create(consentReference);
        FinancialInstitution financialInstitution = financialInstitutionSample.list().get(0);

        AccountInformationAccessRequest accountInformationAccessRequest =
                this.accountInformationAccessRequestSample.create(financialInstitution, customerAccessToken,
                        consentReference, fakeTppAccountInformationAccessRedirectUrl);
        SampleHelper.waitForAuthorizationWebFlow(accountInformationAccessRequest);
        Account account = accountSample.list(customerAccessToken, financialInstitution).getItems().get(0);

        Synchronization synchronization = synchronizationSample.create(customerAccessToken, account.getId());
        synchronization = synchronizationSample.find(customerAccessToken, synchronization.getId());
    }

    public void customerAccessTokenSamples() {
        LOGGER.info("Customer Access Token samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        CustomerAccessToken customerAccessToken = customerAccessTokenSample.create(consentReference);
    }

    public void financialInstitutionSamples() {
        LOGGER.info("Financial Institution samples");

        List<FinancialInstitution> financialInstitutions = financialInstitutionSample.list();
    }

    public void accountInformationAccessRequestSamples() {
        LOGGER.info("Account Information Access Request samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        List<FinancialInstitution> financialInstitutions = financialInstitutionSample.list();
        CustomerAccessToken customerAccessToken = customerAccessTokenSample.create(consentReference);

        AccountInformationAccessRequest accountInformationAccessRequest =
                this.accountInformationAccessRequestSample.create(
                        financialInstitutions.get(0), customerAccessToken,
                        consentReference, fakeTppAccountInformationAccessRedirectUrl);

        SampleHelper.waitForAuthorizationWebFlow(accountInformationAccessRequest);
    }

    public void accountSamples() {
        LOGGER.info("Accounts samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        CustomerAccessToken customerAccessToken = customerAccessTokenSample.create(consentReference);
        FinancialInstitution financialInstitution = financialInstitutionSample.list().get(0);

        AccountInformationAccessRequest accountInformationAccessRequest =
                this.accountInformationAccessRequestSample.create(financialInstitution, customerAccessToken,
                        consentReference, fakeTppAccountInformationAccessRedirectUrl);
        SampleHelper.waitForAuthorizationWebFlow(accountInformationAccessRequest);

        List<Account> accounts = accountSample.list(customerAccessToken, financialInstitution).getItems();

        Account account = accountSample.get(customerAccessToken, financialInstitution, accounts.get(0).getId());
    }

    public void transactionSamples() {
        LOGGER.info("Transactions samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        CustomerAccessToken customerAccessToken = customerAccessTokenSample.create(consentReference);
        FinancialInstitution financialInstitution = financialInstitutionSample.list().get(0);

        AccountInformationAccessRequest accountInformationAccessRequest =
                this.accountInformationAccessRequestSample.create(financialInstitution, customerAccessToken,
                        consentReference, fakeTppAccountInformationAccessRedirectUrl);
        SampleHelper.waitForAuthorizationWebFlow(accountInformationAccessRequest);
        Account account = accountSample.list(customerAccessToken, financialInstitution).getItems().get(0);

        List<Transaction> transactions = transactionSample.list(customerAccessToken, financialInstitution, account);

        Transaction transaction = transactionSample.get(customerAccessToken, financialInstitution, account, transactions.get(0).getId());
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
