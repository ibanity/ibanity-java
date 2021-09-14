package com.ibanity.samples;

import com.ibanity.apis.client.builders.IbanityServiceBuilder;
import com.ibanity.apis.client.builders.OptionalPropertiesBuilder;
import com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.*;
import com.ibanity.apis.client.products.isabel_connect.models.create.*;
import com.ibanity.apis.client.products.isabel_connect.models.read.*;
import com.ibanity.apis.client.products.isabel_connect.services.*;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.time.LocalDate;

import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.getConfiguration;
import static com.ibanity.samples.helper.SampleHelper.loadCertificate;
import static com.ibanity.samples.helper.SampleHelper.loadPrivateKey;

public class IsabelConnectClientSample {
    private static final Logger LOGGER = LogManager.getLogger(IsabelConnectClientSample.class);

    private static final String clientId = getConfiguration("isabel-connect.oauth2.client_id");
    private static final String clientSecret = getConfiguration("isabel-connect.oauth2.client_secret");
    private static final String authorizationCode = getConfiguration("isabel-connect.oauth2.authorization_code");
    private static final String redirectUrl = getConfiguration("isabel-connect.oauth2.redirect_url");
    private static final String accountId = getConfiguration("isabel-connect.account_id");
    private static final String bulkPaymentFile = getConfiguration("isabel-connect.bulk_payment.file");
    private static final String bulkPaymentFilename = getConfiguration("isabel-connect.bulk_payment.file_name");
    private static final String reportId = getConfiguration("isabel-connect.report_id");

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

        IsabelConnectService isabelConnectService = ibanityServiceBuilder
                .isabelConnectOauth2ClientId(clientId)
                .build()
                .isabelConnectService();

        AccessToken token = createToken(isabelConnectService.tokenService());

        listAccountReports(isabelConnectService.accountReportService(), token);
        getAccountReport(isabelConnectService.accountReportService(), token);
        listAccounts(isabelConnectService.accountService(), token);
        getAccount(isabelConnectService.accountService(), token);
        listBalances(isabelConnectService.balanceService(), token);
        listTransactions(isabelConnectService.transactionService(), token);
        listIntradayTransactions(isabelConnectService.intradayTransactionService(), token);
        createBulkPaymentInitiationRequest(isabelConnectService.bulkPaymentInitiationRequestService(), token);
    }

    private static BulkPaymentInitiationRequest createBulkPaymentInitiationRequest(
            BulkPaymentInitiationRequestService bpir,
            AccessToken token) {
        LOGGER.info("Bulk payments");

        BulkPaymentInitiationRequestCreateQuery query = BulkPaymentInitiationRequestCreateQuery.builder()
                .accessToken(token.getAccessToken())
                .filename(bulkPaymentFilename)
                .file(new File(bulkPaymentFile))
                .build();

        return bpir.create(query);
    }

    private static AccessToken createToken(TokenService tokenService) {
        LOGGER.info("Token samples");

        InitialToken initialToken = tokenService.create(InitialTokenCreateQuery.builder()
                .authorizationCode(authorizationCode)
                .redirectUri(redirectUrl)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build());

        LOGGER.info("InitialToken {}", initialToken);

        AccessToken accessToken = tokenService.create(AccessTokenCreateQuery.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .refreshToken(initialToken.getRefreshToken())
                .build());

        LOGGER.info("AccessToken {}", accessToken);

        return accessToken;
    }

    private static void listAccountReports(AccountReportService accountReportService, AccessToken token) {
        LOGGER.info("List account reports");
        AccountReportsReadQuery query = AccountReportsReadQuery.builder()
                .accessToken(token.getAccessToken())
                .build();

        IsabelCollection<AccountReport> reports = accountReportService.list(query);
        LOGGER.info("Account reports {}", reports);
    }

    private static void getAccountReport(AccountReportService service, AccessToken token) {
        LOGGER.info("Account reports");

        AccountReportReadQuery query = AccountReportReadQuery.builder()
                .accessToken(token.getAccessToken())
                .accountReportId(reportId)
                .build();

        String content = service.find(query, resp -> {
            try {
                return IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });

        LOGGER.info("Account report {}", content);
    }

    private static void listAccounts(AccountService service, AccessToken token) {
        LOGGER.info("List accounts");
        AccountsReadQuery query = AccountsReadQuery.builder()
                .accessToken(token.getAccessToken())
                .build();

        IsabelCollection<Account> accounts = service.list(query);
        LOGGER.info("Accounts {}", accounts);
    }

    private static void getAccount(AccountService service, AccessToken token) {
        LOGGER.info("Get account");
        AccountReadQuery query = AccountReadQuery.builder()
                .accessToken(token.getAccessToken())
                .accountId(accountId)
                .build();

        Account account = service.find(query);
        LOGGER.info("Account {}", account);
    }

    private static void listBalances(BalanceService service, AccessToken token) {
        LOGGER.info("List balances");

        LocalDate today = LocalDate.now();
        BalancesReadQuery query = BalancesReadQuery.builder()
                .accessToken(token.getAccessToken())
                .accountId(accountId)
                .pagingSpec(IsabelPagingSpec.builder()
                        .from(today.minusDays(15))
                        .to(today).build())
                .build();
        IsabelCollection<Balance> balances = service.list(query);
        LOGGER.info("Balances: {}", balances);
    }

    private static void listTransactions(TransactionService service, AccessToken token) {
        LOGGER.info("List transactions");

        LocalDate now = LocalDate.now();
        TransactionsReadQuery query = TransactionsReadQuery.builder()
                .accessToken(token.getAccessToken())
                .accountId(accountId)
                .pagingSpec(IsabelPagingSpec.builder()
                        .from(now.minusDays(60))
                        .to(now).build())
                .build();
        IsabelCollection<Transaction> transactions = service.list(query);
        LOGGER.info("Transactions {}", transactions);
    }

    private static void listIntradayTransactions(IntradayTransactionService service, AccessToken token) {
        LOGGER.info("List transactions");

        IntradayTransactionsReadQuery query = IntradayTransactionsReadQuery.builder()
                .accessToken(token.getAccessToken())
                .accountId(accountId)
                .build();
        IsabelCollection<IntradayTransaction> transactions = service.list(query);
        LOGGER.info("Intraday transactions {}", transactions);
    }
}
