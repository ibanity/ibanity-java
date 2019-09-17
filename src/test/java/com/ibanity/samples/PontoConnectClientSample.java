package com.ibanity.samples;

import com.ibanity.apis.client.builders.IbanityServiceBuilder;
import com.ibanity.apis.client.builders.OptionalPropertiesBuilder;
import com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.models.*;
import com.ibanity.apis.client.products.ponto_connect.models.create.SynchronizationCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.*;
import com.ibanity.apis.client.products.ponto_connect.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.getConfiguration;
import static com.ibanity.samples.helper.SampleHelper.loadCertificate;
import static com.ibanity.samples.helper.SampleHelper.loadPrivateKey;

public class PontoConnectClientSample {

    private static final Logger LOGGER = LogManager.getLogger(PontoConnectClientSample.class);

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

        List<Account> accounts = accounts(pontoConnectService.accountService(), accessToken);
        LOGGER.info("List of accounts {}", accounts);

        UUID accountId = accounts.stream().map(Account::getId).findFirst().orElseThrow(RuntimeException::new);

        Synchronization synchronization = synchronization(pontoConnectService.synchronizationService(), accountId, accessToken);
        LOGGER.info("Synchronization {}", synchronization);

        List<Transaction> transactions = transactions(pontoConnectService.transactionService(), accountId, accessToken);
        LOGGER.info("List of transactions {}", transactions);

        revokeToken(pontoConnectService.tokenService(), accessToken);
    }

    private static void revokeToken(TokenService tokenService, String accessToken) {
        tokenService.revoke(accessToken, clientSecret);
    }

    private static String createToken(TokenService tokenService) {
        LOGGER.info("Token samples");

        Token token = tokenService.create(authorizationCode, codeVerifier, pontoConnectRedirectUrl, clientSecret);
        LOGGER.info("Token {}", token);

        token = tokenService.refresh(token.getRefreshToken(), pontoConnectRedirectUrl, clientSecret);
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

        IbanityCollection<FinancialInstitution> financialInstitutions = financialInstitutionService.list(FinancialInstitutionsReadQuery.builder()
                .accessToken(accessToken)
                .build());

        FinancialInstitution financialInstitution = financialInstitutions.getItems().stream().findFirst().orElseThrow(RuntimeException::new);

        financialInstitutionService.find(FinancialInstitutionReadQuery.builder()
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

}
