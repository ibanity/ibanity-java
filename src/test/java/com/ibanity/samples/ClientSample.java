package com.ibanity.samples;

import com.ibanity.apis.client.builders.IbanityServiceBuilder;
import com.ibanity.apis.client.builders.OptionalPropertiesBuilder;
import com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys;
import com.ibanity.apis.client.products.xs2a.models.*;
import com.ibanity.apis.client.services.IbanityService;
import com.ibanity.samples.customer.*;
import com.ibanity.samples.helper.SampleHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.getConfiguration;
import static com.ibanity.samples.helper.SampleHelper.*;

public class ClientSample {

    private static final Logger LOGGER = LogManager.getLogger(ClientSample.class);

    private final CustomerAccessTokenSample customerAccessTokenSample;
    private final FinancialInstitutionSample financialInstitutionSample;
    private final AccountInformationAccessRequestSample accountInformationAccessRequestSample;
    private final AccountSample accountSample;
    private final TransactionSample transactionSample;
    private final PaymentInitiationRequestSample paymentInitiationRequestSample;
    private final SynchronizationSample synchronizationSample;
    private final CustomerSample customerSample;
    private final AuthorizationSample authorizationSample;
    private final HoldingSample holdingSample;

    // Configurations
    private final String fakeTppAccountInformationAccessRedirectUrl = getConfiguration("tpp.account-information-access-result.redirect-url");
    private final String fakeTppPaymentInitiationRedirectUrl = getConfiguration("tpp.payment-initiation-result.redirect-url");

    public ClientSample(IbanityService ibanityService) {
        customerAccessTokenSample = new CustomerAccessTokenSample(ibanityService);
        financialInstitutionSample = new FinancialInstitutionSample(ibanityService);
        accountInformationAccessRequestSample = new AccountInformationAccessRequestSample(ibanityService);
        accountSample = new AccountSample(ibanityService);
        transactionSample = new TransactionSample(ibanityService);
        paymentInitiationRequestSample = new PaymentInitiationRequestSample(ibanityService);
        synchronizationSample = new SynchronizationSample(ibanityService);
        customerSample = new CustomerSample(ibanityService);
        authorizationSample = new AuthorizationSample(ibanityService);
        holdingSample = new HoldingSample(ibanityService);
    }

    public static void main(String[] args) throws CertificateException, IOException {
        String passphrase = getConfiguration(IBANITY_CLIENT_TLS_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY);
        PrivateKey privateKey = loadPrivateKey(getConfiguration(IBANITY_CLIENT_TLS_PRIVATE_KEY_PATH_PROPERTY_KEY), passphrase);
        X509Certificate certificate = loadCertificate(getConfiguration(IBANITY_CLIENT_TLS_CERTIFICATE_PATH_PROPERTY_KEY));
        OptionalPropertiesBuilder ibanityServiceBuilder = IbanityServiceBuilder.builder()
                .ibanityApiEndpoint(getConfiguration(IBANITY_API_ENDPOINT_PROPERTY_KEY))
                .tlsPrivateKey(privateKey)
                .passphrase(passphrase)
                .tlsCertificate(certificate)
                .withHttpRequestInterceptors((request, context) -> LOGGER.info("This is a HttpRequestInterceptor"))
                .withHttpResponseInterceptors((response, context) -> LOGGER.info("This is a HttpResponseInterceptor"))
                ;

        if(getConfiguration(IBANITY_CLIENT_TLS_CA_CERTIFICATE_PATH_PROPERTY_KEY) != null) {
            ibanityServiceBuilder.caCertificate(loadCa(getConfiguration(IBANITY_CLIENT_TLS_CA_CERTIFICATE_PATH_PROPERTY_KEY)));
        }

        if (getConfiguration(IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY) != null) {
            String signaturePassphrase = getConfiguration(IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY, "");
            ibanityServiceBuilder
                    .requestSignaturePrivateKey(loadPrivateKey(getConfiguration(IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PATH_PROPERTY_KEY), signaturePassphrase))
                    .requestSignaturePassphrase(signaturePassphrase)
                    .requestSignatureCertificate(loadCertificate(getConfiguration(IBANITY_CLIENT_SIGNATURE_CERTIFICATE_PATH_PROPERTY_KEY)))
                    .signatureCertificateId(getConfiguration(IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY));
        }

        IbanityService ibanityService = ibanityServiceBuilder.build();

        ClientSample clientSample = new ClientSample(ibanityService);

        CustomerAccessToken customerAccessToken = clientSample.customerAccessTokenSamples();
        List<FinancialInstitution> financialInstitutions = clientSample.financialInstitutionSamples();

        clientSample.accountInformationAccessRequestSamples(customerAccessToken, financialInstitutions);

        Authorization authorization = clientSample.authorizationSamples(customerAccessToken, financialInstitutions);
        LOGGER.info("Authorization: {}", authorization);

        List<Account> accounts = clientSample.accountSamples(customerAccessToken, financialInstitutions);
        List<Transaction> transactions = clientSample.transactionSamples(customerAccessToken, financialInstitutions, accounts);
        List<Holding> holdings = clientSample.holdingSample(customerAccessToken, financialInstitutions, accounts);
        Synchronization synchronization = clientSample.synchronizationSamples(customerAccessToken, accounts);
        PaymentInitiationRequest paymentInitiationRequest = clientSample.paymentInitiationRequestSamples();

        LOGGER.info("List of financialInstitutions: {}", financialInstitutions);
        LOGGER.info("List of accounts: {}", accounts);
        LOGGER.info("List of transactions: {}", transactions);
        LOGGER.info("List of holdings: {}", holdings);
        LOGGER.info("synchronization: {}", synchronization);
        LOGGER.info("paymentInitiationRequest: {}", paymentInitiationRequest);

        clientSample.customerSamples(customerAccessToken);

        LOGGER.info("Samples end");
    }

    private Customer customerSamples(CustomerAccessToken customerAccessToken) {
        return customerSample.delete(customerAccessToken);
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

    public AccountInformationAccessRequest accountInformationAccessRequestSamples(CustomerAccessToken customerAccessToken, List<FinancialInstitution> financialInstitutions) {
        LOGGER.info("Account Information Access Request samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        AccountInformationAccessRequest accountInformationAccessRequest =
                this.accountInformationAccessRequestSample.create(
                        financialInstitutions.get(0), customerAccessToken,
                        consentReference, fakeTppAccountInformationAccessRedirectUrl);

        SampleHelper.waitForAuthorizationWebFlow(accountInformationAccessRequest);

        accountInformationAccessRequest = accountInformationAccessRequestSample.find(accountInformationAccessRequest, financialInstitutions.get(0), customerAccessToken);
        return accountInformationAccessRequest;
    }

    public Authorization authorizationSamples(CustomerAccessToken customerAccessToken, List<FinancialInstitution> financialInstitutions) {
        LOGGER.info("Authorization samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();
        FinancialInstitution financialInstitution = financialInstitutions.get(0);

        AccountInformationAccessRequest accountInformationAccessRequest =
                authorizationSample.createAiar(financialInstitution, customerAccessToken, consentReference, fakeTppAccountInformationAccessRedirectUrl);

        String code = waitForAuthorizationWebFlow(accountInformationAccessRequest);

        return authorizationSample.create(accountInformationAccessRequest, financialInstitution, customerAccessToken, code);
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

    public List<Holding> holdingSample(CustomerAccessToken customerAccessToken, List<FinancialInstitution> financialInstitutionList, List<Account> accountList) {
        LOGGER.info("Holdings samples");

        FinancialInstitution financialInstitution = financialInstitutionList.get(0);

        Account account = accountList.stream().filter(account1 -> account1.getSubtype().equalsIgnoreCase("securities")).findFirst().get();

        List<Holding> holdings = holdingSample.list(customerAccessToken, financialInstitution, account);

        holdingSample.get(customerAccessToken, financialInstitution, account, holdings.get(0).getId());

        return holdings;
    }

    public PaymentInitiationRequest paymentInitiationRequestSamples() {
        LOGGER.info("Payment Initiation Request samples");

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        CustomerAccessToken customerAccessToken = customerAccessTokenSample.create(consentReference);
        FinancialInstitution financialInstitution = financialInstitutionSample.list().get(0);

        return paymentInitiationRequestSample.create(financialInstitution, customerAccessToken, fakeTppPaymentInitiationRedirectUrl);
    }

}
