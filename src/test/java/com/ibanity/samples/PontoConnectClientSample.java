package com.ibanity.samples;

import com.ibanity.apis.client.builders.IbanityServiceBuilder;
import com.ibanity.apis.client.builders.OptionalPropertiesBuilder;
import com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.models.*;
import com.ibanity.apis.client.products.ponto_connect.models.create.BulkPaymentCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.create.SynchronizationCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.create.TokenCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.BulkPaymentDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.PaymentDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.*;
import com.ibanity.apis.client.products.ponto_connect.models.refresh.TokenRefreshQuery;
import com.ibanity.apis.client.products.ponto_connect.models.revoke.TokenRevokeQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.update.FinancialInstitutionTransactionUpdateQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.SandboxService;
import com.ibanity.apis.client.products.ponto_connect.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.getConfiguration;
import static com.ibanity.samples.helper.SampleHelper.loadCertificate;
import static com.ibanity.samples.helper.SampleHelper.loadPrivateKey;

public class PontoConnectClientSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(PontoConnectClientSample.class);

    private static final String pontoConnectRedirectUrl = getConfiguration("ponto-connect.oauth2.redirect-url");
    private static final String clientId = getConfiguration("ponto-connect.oauth2.client_id");
    private static final String clientSecret = getConfiguration("ponto-connect.oauth2.client_secret");
    private static final String codeVerifier = getConfiguration("ponto-connect.oauth2.code_verifier");
    private static final String authorizationCode = getConfiguration("ponto-connect.oauth2.authorization_code");

    public static void main(String[] args) throws CertificateException, IOException {
        String passphrase = getConfiguration(IBANITY_CLIENT_TLS_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY);
        OptionalPropertiesBuilder ibanityServiceBuilder = IbanityServiceBuilder.builder()
                .ibanityApiEndpoint(getConfiguration(IBANITY_API_ENDPOINT_PROPERTY_KEY))
                .tlsPrivateKey(loadPrivateKey(getConfiguration(IBANITY_CLIENT_TLS_PRIVATE_KEY_PATH_PROPERTY_KEY), passphrase))
                .passphrase(passphrase)
                .tlsCertificate(loadCertificate(getConfiguration(IBANITY_CLIENT_TLS_CERTIFICATE_PATH_PROPERTY_KEY)))
                .caCertificate(loadCertificate(getConfiguration(IBANITY_CLIENT_TLS_CA_CERTIFICATE_PATH_PROPERTY_KEY)));

        if (getConfiguration(IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY) != null) {
            String signaturePassphrase = getConfiguration(IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY, "");
            ibanityServiceBuilder
                    .requestSignaturePrivateKey(loadPrivateKey(getConfiguration(IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PATH_PROPERTY_KEY), signaturePassphrase))
                    .requestSignaturePassphrase(signaturePassphrase)
                    .requestSignatureCertificate(loadCertificate(getConfiguration(IBANITY_CLIENT_SIGNATURE_CERTIFICATE_PATH_PROPERTY_KEY)))
                    .signatureCertificateId(getConfiguration(IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY));
        }

        PontoConnectService pontoConnectService = ibanityServiceBuilder
                .pontoConnectOauth2ClientId(clientId)
                .build()
                .pontoConnectService();


        String accessToken = createToken(pontoConnectService.tokenService());

        List<FinancialInstitution> financialInstitutions = financialInstitutions(pontoConnectService.financialInstitutionService(), accessToken);
        LOGGER.info("List of financialInstitutions {}", financialInstitutions);

        Userinfo userinfo = userinfo(pontoConnectService.userinfoService(), accessToken);
        LOGGER.info("Userinfo {}", userinfo);

        sampleSandbox(pontoConnectService.sandboxService(), financialInstitutions.stream().map(FinancialInstitution::getId).findFirst().orElseThrow(RuntimeException::new), accessToken);

        List<Account> accounts = accounts(pontoConnectService.accountService(), accessToken);
        LOGGER.info("List of accounts {}", accounts);

        UUID accountId = accounts.stream().map(Account::getId).findFirst().orElseThrow(RuntimeException::new);

        Synchronization synchronization = synchronization(pontoConnectService.synchronizationService(), accountId, accessToken);
        LOGGER.info("Synchronization {}", synchronization);

        List<Transaction> transactions = transactions(pontoConnectService.transactionService(), accountId, accessToken);
        LOGGER.info("List of transactions {}", transactions);
        Payment payment = payments(pontoConnectService.paymentService(), accountId, accessToken);
        LOGGER.info("payment {}", payment);
        BulkPayment bulkPayment = bulkPayments(pontoConnectService.bulkPaymentService(), accountId, accessToken);
        LOGGER.info("bulk payment {}", bulkPayment);

        revokeToken(pontoConnectService.tokenService(), accessToken);
    }

    private static Userinfo userinfo(UserinfoService userinfoService, String accessToken) {
        return userinfoService.getUserinfo(UserinfoReadQuery.builder()
                .accessToken(accessToken)
                .build());
    }

    private static void sampleSandbox(SandboxService sandboxService, UUID financialInstitutionId, String accessToken) {
        List<FinancialInstitutionAccount> financialInstitutionAccounts = financialInstitutionAccounts(sandboxService.financialInstitutionAccountsService(), financialInstitutionId, accessToken);
        LOGGER.info("List of financialInstitutionAccounts {}", financialInstitutionAccounts);

        UUID financialInstitutionAccountId = financialInstitutionAccounts.stream().map(FinancialInstitutionAccount::getId).findFirst().orElseThrow(RuntimeException::new);
        List<FinancialInstitutionTransaction> financialInstitutionAccountTransaction = financialInstitutionTransactions(sandboxService.financialInstitutionTransactionsService(), financialInstitutionId, financialInstitutionAccountId, accessToken);
        LOGGER.info("List of financialInstitutionAccountTransaction {}", financialInstitutionAccountTransaction);
    }

    private static List<FinancialInstitutionTransaction> financialInstitutionTransactions(FinancialInstitutionTransactionsService financialInstitutionTransactionsService, UUID financialInstitutionId, UUID financialInstitutionAccountId, String accessToken) {
        UUID financialInstitutionTransactionId = financialInstitutionTransactionsService.create(FinancialInstitutionTransactionCreationQuery.builder()
                .accessToken(accessToken)
                .remittanceInformationType("unstructured")
                .remittanceInformation("NEW SHOES")
                .description("Small Cotton Shoes")
                .currency("EUR")
                .counterpartName("Otro Bank")
                .purposeCode("DEBIT")
                .counterpartReference("BE9786154282554")
                .amount(new BigDecimal("84.42"))
                .valueDate(Instant.parse("2020-05-22T00:00:00Z"))
                .executionDate(Instant.parse("2020-05-25T00:00:00Z"))
                .financialInstitutionAccountId(financialInstitutionAccountId)
                .financialInstitutionId(financialInstitutionId)
                .build()).getId();

        financialInstitutionTransactionsService.update(FinancialInstitutionTransactionUpdateQuery.builder()
                .accessToken(accessToken)
                .financialInstitutionId(financialInstitutionId)
                .financialInstitutionAccountId(financialInstitutionAccountId)
                .financialInstitutionTransactionId(financialInstitutionTransactionId)
                .remittanceInformation("NEW SHOES")
                .description("Hole foods")
                .bankTransactionCode("PMNT-IRCT-ESCT")
                .proprietaryBankTransactionCode("12267")
                .additionalInformation("Online payment on fake-tpp.com")
                .creditorId("123498765421")
                .mandateId("234")
                .purposeCode("CASH")
                .endToEndId("ref.243435343")
                .build());

        financialInstitutionTransactionsService.find(FinancialInstitutionTransactionReadQuery.builder()
                .accessToken(accessToken)
                .financialInstitutionId(financialInstitutionId)
                .financialInstitutionAccountId(financialInstitutionAccountId)
                .financialInstitutionTransactionId(financialInstitutionTransactionId)
                .build());

        return financialInstitutionTransactionsService.list(FinancialInstitutionTransactionsReadQuery.builder()
                .accessToken(accessToken)
                .financialInstitutionId(financialInstitutionId)
                .financialInstitutionAccountId(financialInstitutionAccountId)
                .build()).getItems();
    }

    private static List<FinancialInstitutionAccount> financialInstitutionAccounts(FinancialInstitutionAccountsService financialInstitutionAccountsService, UUID financialInstitutionId, String accessToken) {
        IbanityCollection<FinancialInstitutionAccount> list = financialInstitutionAccountsService.list(FinancialInstitutionAccountsReadQuery.builder()
                .accessToken(accessToken)
                .financialInstitutionId(financialInstitutionId)
                .build());

        UUID financialInstitutionAccountId = list.getItems().stream().map(FinancialInstitutionAccount::getId).findFirst().orElseThrow(RuntimeException::new);
        financialInstitutionAccountsService.find(FinancialInstitutionAccountReadQuery.builder()
                .accessToken(accessToken)
                .financialInstitutionId(financialInstitutionId)
                .financialInstitutionAccountId(financialInstitutionAccountId)
                .build());
        return list.getItems();
    }

    private static void revokeToken(TokenService tokenService, String accessToken) {
        tokenService.revoke(TokenRevokeQuery.builder()
                .token(accessToken)
                .clientSecret(clientSecret)
                .build());
    }

    private static String createToken(TokenService tokenService) {
        LOGGER.info("Token samples");

        Token token = tokenService.create(TokenCreateQuery.builder()
                .authorizationCode(authorizationCode)
                .clientSecret(clientSecret)
                .codeVerifier(codeVerifier)
                .redirectUri(pontoConnectRedirectUrl)
                .build());
        LOGGER.info("Token {}", token);

        token = tokenService.refresh(TokenRefreshQuery.builder()
                .clientSecret(clientSecret)
                .refreshToken(token.getRefreshToken())
                .build());
        LOGGER.info("Token {}", token);

        return token.getAccessToken();
    }

    private static Synchronization synchronization(SynchronizationService synchronizationService, UUID accountId, String accessToken) {
        LOGGER.info("Synchronization samples");

        Synchronization synchronization = synchronizationService.create(SynchronizationCreateQuery.builder()
                .accessToken(accessToken)
                .resourceId(accountId.toString())
                .resourceType("account")
                .subtype("accountDetails")
                .build());
        return synchronizationService.find(SynchronizationReadQuery.builder()
                .synchronizationId(synchronization.getId())
                .accessToken(accessToken)
                .build());
    }

    private static List<FinancialInstitution> financialInstitutions(FinancialInstitutionService financialInstitutionService, String accessToken) {
        LOGGER.info("Transactions samples");

        IbanityCollection<FinancialInstitution> financialInstitutions = financialInstitutionService.list(OrganizationFinancialInstitutionsReadQuery.builder()
                .accessToken(accessToken)
                .build());

        FinancialInstitution financialInstitution = financialInstitutions.getItems().stream().findFirst().orElseThrow(RuntimeException::new);

        financialInstitutionService.find(OrganizationFinancialInstitutionReadQuery.builder()
                .financialInstitutionId(financialInstitution.getId())
                .accessToken(accessToken).build());

        return financialInstitutions.getItems();
    }

    private static List<Transaction> transactions(TransactionService transactionService, UUID accountId, String accessToken) {
        LOGGER.info("Transactions samples");

        IbanityCollection<Transaction> transactions = transactionService.list(TransactionsReadQuery.builder()
                .accessToken(accessToken)
                .accountId(accountId)
                .build());

        UUID transactionId = transactions.getItems().stream().map(Transaction::getId).findFirst().orElseThrow(RuntimeException::new);

        transactionService.find(TransactionReadQuery.builder()
                .accessToken(accessToken)
                .accountId(accountId)
                .transactionId(transactionId)
                .build());

        return transactions.getItems();
    }

    private static List<Account> accounts(AccountService accountService, String accessToken) {
        LOGGER.info("Accounts samples");
        IbanityCollection<Account> accounts = accountService.list(AccountsReadQuery.builder()
                .accessToken(accessToken)
                .build());

        UUID accountId = accounts.getItems().stream().map(Account::getId).findFirst().orElseThrow(RuntimeException::new);
        accountService.find(AccountReadQuery.builder()
                .accessToken(accessToken)
                .accountId(accountId)
                .build());

        return accounts.getItems();
    }

    private static Payment payments(PaymentService paymentService, UUID accountId, String accessToken) {
        LOGGER.info("Payments samples");
        Payment payment = paymentService.create(PaymentCreateQuery.builder()
                .accessToken(accessToken)
                .accountId(accountId)
                .remittanceInformation("payment")
                .remittanceInformationType("unstructured")
                .requestedExecutionDate(LocalDate.now().plusDays(1))
                .currency("EUR")
                .amount(BigDecimal.valueOf(59))
                .creditorName("Alex Creditor")
                .creditorAccountReference("BE55732022998044")
                .creditorAccountReferenceType("IBAN")
                .creditorAgent("NBBEBEBB203")
                .creditorAgentType("BIC")
                .build());

        payment = paymentService.find(PaymentReadQuery.builder()
                .accessToken(accessToken)
                .accountId(accountId)
                .paymentId(payment.getId())
                .build());

        paymentService.delete(PaymentDeleteQuery.builder()
                .accessToken(accessToken)
                .accountId(accountId)
                .paymentId(payment.getId())
                .build());

        return payment;
    }

    private static BulkPayment bulkPayments(BulkPaymentService bulkPaymentService, UUID accountId, String accessToken) {
        LOGGER.info("BulkPayments samples");
        BulkPayment bulkPayment = bulkPaymentService.create(BulkPaymentCreateQuery.builder()
                .accessToken(accessToken)
                .batchBookingPreferred(true)
                .reference("myReference")
                .requestedExecutionDate(LocalDate.now().plusDays(1))
                .redirectUri(pontoConnectRedirectUrl)
                .accountId(accountId)
                .payments(Collections.singletonList(createPayment()))
                .build());

        bulkPayment = bulkPaymentService.find(BulkPaymentReadQuery.builder()
                .accessToken(accessToken)
                .accountId(accountId)
                .bulkPaymentId(bulkPayment.getId())
                .build());

        bulkPaymentService.delete(BulkPaymentDeleteQuery.builder()
                .accessToken(accessToken)
                .accountId(accountId)
                .bulkPaymentId(bulkPayment.getId())
                .build());

        return bulkPayment;
    }

    private static BulkPaymentCreateQuery.Payment createPayment() {
        return BulkPaymentCreateQuery.Payment.builder()
                .remittanceInformation("payment")
                .remittanceInformationType("unstructured")
                .currency("EUR")
                .amount(BigDecimal.valueOf(59))
                .creditorName("Alex Creditor")
                .creditorAccountReference("BE55732022998044")
                .creditorAccountReferenceType("IBAN")
                .creditorAgent("NBBEBEBB203")
                .creditorAgentType("BIC")
                .build();
    }

}
