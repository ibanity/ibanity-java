package com.ibanity.samples;

import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.services.AccountsService;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import com.ibanity.apis.client.services.FinancialInstitutionsService;
import com.ibanity.apis.client.services.PaymentsInitiationService;
import com.ibanity.apis.client.services.TransactionsService;
import com.ibanity.apis.client.services.impl.AccountInformationAccessRequestsServiceImpl;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.services.impl.CustomerAccessTokensServiceImpl;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import com.ibanity.apis.client.services.impl.PaymentsInitiationServiceImpl;
import com.ibanity.apis.client.services.impl.TransactionsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;

public class ClientSample {
    private static final Logger LOGGER = LogManager.getLogger(ClientSample.class);

    // Configurations
    private final String fakeTppAccountInformationAccessRedirectUrl = getConfiguration("tpp.accounts.information.access.result.redirect.url");
    private final String fakeTppPaymentInitiationRedirectUrl = getConfiguration("tpp.payments.initiation.result.redirect.url");

    // Services
    private final CustomerAccessTokensService customerAccessTokensService = new CustomerAccessTokensServiceImpl();
    private final FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();
    private final AccountsService accountsService = new AccountsServiceImpl();
    private final AccountInformationAccessRequestsService accountInformationAccessRequestsService =
            new AccountInformationAccessRequestsServiceImpl();
    private final TransactionsService transactionsService = new TransactionsServiceImpl();
    private final PaymentsInitiationService paymentsService = new PaymentsInitiationServiceImpl();

    public void createPaymentInitiationRequest(CustomerAccessToken customerAccessToken,
                                               FinancialInstitution financialInstitution,
                                               PaymentInitiationRequest paymentInitiationRequest) {
        paymentsService.create(customerAccessToken.getToken(), financialInstitution, paymentInitiationRequest);
    }

    public List<Transaction> listTransactionsOfOneAccount(CustomerAccessToken customerAccessToken, Account account) {
        return transactionsService.list(
                customerAccessToken.getToken(),
                account.getFinancialInstitution().getId(),
                account.getId());
    }

    public Account getOneSpecificAccount(CustomerAccessToken customerAccessToken,
                                         FinancialInstitution financialInstitution, Account account) {
        return accountsService.find(customerAccessToken.getToken(), account.getId(), financialInstitution.getId());
    }

    public List<Account> listCustomerAccounts(CustomerAccessToken customerAccessToken) {
        return accountsService.list(customerAccessToken.getToken());
    }

    public void waitForAuthorizationWebFlow(AccountInformationAccessRequest accountInformationAccessRequest) {
        LOGGER.info("Open the following URL in your browser and follow the web flow:");
        LOGGER.info("   " + accountInformationAccessRequest.getLinks().getRedirect());
        LOGGER.info("Once the authorization is done, press ENTER to continue...");

        new Scanner(System.in).nextLine();
    }

    public AccountInformationAccessRequest createAccountInformationAccessRequest(
            String consentReference, CustomerAccessToken customerAccessToken, FinancialInstitution financialInstitution) {
        return accountInformationAccessRequestsService.create(
                customerAccessToken.getToken(),
                financialInstitution.getId(),
                fakeTppAccountInformationAccessRedirectUrl,
                consentReference);
    }

    public List<FinancialInstitution> listAvailableFinancialInstitutions() {
        return financialInstitutionsService.list();
    }

    public CustomerAccessToken createCustomerAccessToken(String consentReference) {
        return customerAccessTokensService.create(consentReference);
    }

    public PaymentInitiationRequest generateRandomPaymentInitiationRequest(FinancialInstitution financialInstitution) {
        PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();

        paymentInitiationRequest.setRedirectUri(fakeTppPaymentInitiationRedirectUrl);
        paymentInitiationRequest.setConsentReference(UUID.randomUUID().toString());
        paymentInitiationRequest.setEndToEndId(generateRandomEnd2EndId());
        paymentInitiationRequest.setProductType("sepa-credit-transfer");

        paymentInitiationRequest.setAmount(Double.valueOf(50.4));
        paymentInitiationRequest.setCurrency("EUR");

        paymentInitiationRequest.setCreditorName("Fake Creditor Name");
        paymentInitiationRequest.setCreditorAccountReference(Iban.random(CountryCode.BE).toString());
        paymentInitiationRequest.setCreditorAccountReferenceType("IBAN");

        paymentInitiationRequest.setRemittanceInformationType("unstructured");
        paymentInitiationRequest.setRemittanceInformation("Payment initiation sample");
        return paymentInitiationRequest;
    }

    private String generateRandomEnd2EndId() {
        // End-2-End-Id length must be maximum 35 chars
        return UUID.randomUUID().toString().replace("-", "");
    }
}
