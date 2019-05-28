package com.ibanity.samples;


import com.ibanity.apis.client.builders.IbanityServiceBuilder;
import com.ibanity.apis.client.builders.OptionalPropertiesBuilder;
import com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys;
import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.samples.sandbox.FinancialInstitutionAccountSample;
import com.ibanity.samples.sandbox.FinancialInstitutionSample;
import com.ibanity.samples.sandbox.FinancialInstitutionTransactionSample;
import com.ibanity.samples.sandbox.FinancialInstitutionUserSample;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.cert.CertificateException;

import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.IBANITY_CLIENT_SSL_CA_CERTIFICATE_PATH_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.IBANITY_CLIENT_SSL_CLIENT_CERTIFICATE_PATH_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PATH_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_CERTIFICATE_PATH_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PATH_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.getConfiguration;
import static com.ibanity.samples.helper.SampleHelper.loadCa;
import static com.ibanity.samples.helper.SampleHelper.loadCertificate;
import static com.ibanity.samples.helper.SampleHelper.loadPrivateKey;

public class ClientSandboxSample {
    private static final Logger LOGGER = LogManager.getLogger(ClientSandboxSample.class);

    // Sample services
    private final FinancialInstitutionSample financialInstitutionSample;
    private final FinancialInstitutionUserSample financialInstitutionUserSample;
    private final FinancialInstitutionAccountSample financialInstitutionAccountSample;
    private final FinancialInstitutionTransactionSample financialInstitutionTransactionSample;

    public ClientSandboxSample(IbanityService ibanityService) {
        financialInstitutionSample = new FinancialInstitutionSample(ibanityService);
        financialInstitutionUserSample = new FinancialInstitutionUserSample(ibanityService);
        financialInstitutionAccountSample = new FinancialInstitutionAccountSample(ibanityService);
        financialInstitutionTransactionSample = new FinancialInstitutionTransactionSample(ibanityService);
    }

    public static void main(String[] args) throws CertificateException, IOException {
        String passphrase = getConfiguration(IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY);
        OptionalPropertiesBuilder ibanityServiceBuilder = IbanityServiceBuilder.builder()
                .ibanityApiEndpoint(getConfiguration(IBANITY_API_ENDPOINT_PROPERTY_KEY))
                .applicationPrivateKey(loadPrivateKey(getConfiguration(IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PATH_PROPERTY_KEY), passphrase))
                .passphrase(passphrase)
                .applicationCertificate(loadCertificate(getConfiguration(IBANITY_CLIENT_SSL_CLIENT_CERTIFICATE_PATH_PROPERTY_KEY)))
                .caCertificate(loadCa(getConfiguration(IBANITY_CLIENT_SSL_CA_CERTIFICATE_PATH_PROPERTY_KEY)));
        if (getConfiguration(IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY) != null) {
            String signaturePassphrase = getConfiguration(IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY, "");
            ibanityServiceBuilder
                    .requestSignaturePrivateKey(loadPrivateKey(getConfiguration(IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PATH_PROPERTY_KEY), signaturePassphrase))
                    .requestSignaturePassphrase(signaturePassphrase)
                    .requestSignatureCertificate(loadCertificate(getConfiguration(IBANITY_CLIENT_SIGNATURE_CERTIFICATE_PATH_PROPERTY_KEY)))
                    .signatureCertificateId(getConfiguration(IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY));
        }

        IbanityService ibanityService = ibanityServiceBuilder.build();
        ClientSandboxSample clientSandboxSample = new ClientSandboxSample(ibanityService);
        clientSandboxSample.financialInstitutionSamples();
        clientSandboxSample.financialInstitutionUserSamples();
        clientSandboxSample.financialInstitutionAccountSamples();
        clientSandboxSample.financialInstitutionTransactionSamples();

        LOGGER.info("Samples end");
    }

    public void financialInstitutionSamples() {
        LOGGER.info("Financial Institution samples");

        FinancialInstitution financialInstitution = financialInstitutionSample.create();
        financialInstitutionSample.update(financialInstitution);
        financialInstitutionSample.find(financialInstitution.getId());
        financialInstitutionSample.delete(financialInstitution);
    }

    public void financialInstitutionUserSamples() {
        LOGGER.info("Financial Institution User samples");

        FinancialInstitutionUser financialInstitutionUser = financialInstitutionUserSample.create();
        financialInstitutionUserSample.update(financialInstitutionUser);
        financialInstitutionUserSample.find(financialInstitutionUser.getId());
        financialInstitutionUserSample.delete(financialInstitutionUser);
    }

    public void financialInstitutionAccountSamples() {
        LOGGER.info("Financial Institution Account samples");

        // create related objects
        FinancialInstitution financialInstitution = financialInstitutionSample.create();
        FinancialInstitutionUser financialInstitutionUser = financialInstitutionUserSample.create();

        FinancialInstitutionAccount financialInstitutionAccount =
                financialInstitutionAccountSample.create(financialInstitution, financialInstitutionUser);

        financialInstitutionAccountSample.find(financialInstitution, financialInstitutionUser, financialInstitutionAccount.getId());

        financialInstitutionAccountSample.delete(financialInstitution, financialInstitutionUser, financialInstitutionAccount);

        // clean related objects
        financialInstitutionSample.delete(financialInstitution);
        financialInstitutionUserSample.delete(financialInstitutionUser);
    }

    public void financialInstitutionTransactionSamples() {
        LOGGER.info("Financial Institution Transaction samples");

        // create related objects
        FinancialInstitution financialInstitution = financialInstitutionSample.create();
        FinancialInstitutionUser financialInstitutionUser = financialInstitutionUserSample.create();
        FinancialInstitutionAccount financialInstitutionAccount =
                financialInstitutionAccountSample.create(financialInstitution, financialInstitutionUser);

        FinancialInstitutionTransaction financialInstitutionTransaction =
                this.financialInstitutionTransactionSample.create(financialInstitution, financialInstitutionUser, financialInstitutionAccount);

        this.financialInstitutionTransactionSample.find(financialInstitution, financialInstitutionUser,
                financialInstitutionAccount, financialInstitutionTransaction.getId());

        this.financialInstitutionTransactionSample.delete(financialInstitution, financialInstitutionUser,
                financialInstitutionAccount, financialInstitutionTransaction);

        // clean related objects
        this.financialInstitutionAccountSample.delete(financialInstitution, financialInstitutionUser, financialInstitutionAccount);
        this.financialInstitutionSample.delete(financialInstitution);
        this.financialInstitutionUserSample.delete(financialInstitutionUser);
    }

}