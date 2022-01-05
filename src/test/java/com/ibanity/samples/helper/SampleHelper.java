package com.ibanity.samples.helper;

import com.ibanity.apis.client.helpers.KeyToolHelper;
import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionHoldingCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import org.apache.commons.math3.util.Precision;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import static java.math.BigDecimal.TEN;

public class SampleHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleHelper.class);

    public static BigDecimal generateRandomAmount() {
        Random random = new Random();

        int randomDebitCreditSign = random.nextBoolean() ? -1 : 1;

        return BigDecimal.valueOf(Precision.round(
                random.doubles(10, 100)
                        .findFirst().getAsDouble() * randomDebitCreditSign, 2));
    }

    public static String waitForAuthorizationWebFlow(AccountInformationAccessRequest accountInformationAccessRequest) {
        LOGGER.info("Open the following URL in your browser and follow the web flow:");
        LOGGER.info("   " + accountInformationAccessRequest.getAccountInformationAccessLinks().getRedirect());
        LOGGER.info("Once the authorization is done, press ENTER to continue...");
        LOGGER.info("Type in the code returned in the callback if you used skipIbanityCompletionCallback flag");

        return new Scanner(System.in).nextLine();
    }

    public static FinancialInstitutionTransactionCreationQuery generateRandomTransactionCreationQuery(
            FinancialInstitution financialInstitution,
            FinancialInstitutionUser financialInstitutionUser,
            FinancialInstitutionAccount financialInstitutionAccount) {

        return FinancialInstitutionTransactionCreationQuery.builder()
                .financialInstitutionId(financialInstitution.getId())
                .financialInstitutionUserId(financialInstitutionUser.getId())
                .financialInstitutionAccountId(financialInstitutionAccount.getId())

                .amount(generateRandomAmount())
                .currency("EUR")

                .description("Car rental")

                .counterpartName("Stroman, Hettinger and Swift")
                .counterpartReference(Iban.random(CountryCode.BE).getAccountNumber())

                .executionDate(Instant.now().plus(3, ChronoUnit.DAYS))
                .valueDate(Instant.now().minus(1, ChronoUnit.DAYS))

                .remittanceInformation("Aspernatur et quibusdam.")
                .remittanceInformationType("unstructured")

                .build();
    }

    public static PaymentInitiationRequestCreationQuery generateRandomPaymentInitiationRequestCreationQuery(
            FinancialInstitution financialInstitution, CustomerAccessToken customerAccessToken,
            String redirectUrl) {

        return PaymentInitiationRequestCreationQuery.builder()
                .customerAccessToken(customerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())

                .redirectUri(redirectUrl)
                .consentReference(UUID.randomUUID().toString())
                .endToEndId(generateRandomEnd2EndId())
                .productType("sepa-credit-transfer")

                .amount(generateRandomAmount())
                .currency("EUR")

                .creditorName("Fake Creditor Name")
                .creditorAccountReference(Iban.random(CountryCode.BE).toString())
                .creditorAccountReferenceType("IBAN")

                .remittanceInformationType("unstructured")
                .remittanceInformation("Payment initiation sample")
                .skipIbanityCompletionCallback(true)
                .allowFinancialInstitutionRedirectUri(true)
                .state("thisIsMyState")
                .build();
    }

    public static String generateRandomEnd2EndId() {
        // End-2-End-Id length must be maximum 35 chars
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Certificate loadCa(String certificatePath) throws CertificateException {
        if (certificatePath == null) {
            return null;
        }

        return KeyToolHelper.loadCertificate(certificatePath);
    }

    public static PrivateKey loadPrivateKey(String path, String passphrase) throws IOException {
        return KeyToolHelper.loadPrivateKey(
                path,
                passphrase);
    }

    public static X509Certificate loadCertificate(String path) throws CertificateException {
        return (X509Certificate) KeyToolHelper.loadCertificate(
                path);
    }

    public static FinancialInstitutionHoldingCreationQuery generateRandomHoldingCreationQuery(
            FinancialInstitution financialInstitution,
            FinancialInstitutionUser financialInstitutionUser,
            FinancialInstitutionAccount financialInstitutionAccount) {
        return FinancialInstitutionHoldingCreationQuery.builder()
                .financialInstitutionId(financialInstitution.getId())
                .financialInstitutionUserId(financialInstitutionUser.getId())
                .financialInstitutionAccountId(financialInstitutionAccount.getId())
                .name("Fake STOCK")
                .reference("ISIN456")
                .referenceType("ISIN")
                .subtype("STOCK")
                .quantity(TEN)
                .totalValuation(new BigDecimal("100"))
                .totalValuationCurrency("EUR")
                .lastValuation(TEN)
                .lastValuationCurrency("EUR")
                .lastValuationDate(LocalDate.now())
                .build();
    }
}
