package com.ibanity.samples;


import com.ibanity.apis.client.builders.IbanityServiceBuilder;
import com.ibanity.apis.client.builders.OptionalPropertiesBuilder;
import com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionHolding;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.services.IbanityService;
import com.ibanity.samples.sandbox.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.cert.CertificateException;

import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityClientSecuritySignaturePropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.getConfiguration;
import static com.ibanity.samples.helper.SampleHelper.*;

public class ClientSandboxSample {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSandboxSample.class);

    private final FinancialInstitutionSample financialInstitutionSample;
    private final FinancialInstitutionUserSample financialInstitutionUserSample;
    private final FinancialInstitutionAccountSample financialInstitutionAccountSample;
    private final FinancialInstitutionTransactionSample financialInstitutionTransactionSample;
    private final FinancialInstitutionHoldingSample financialInstitutionHoldingSample;

    public ClientSandboxSample(IbanityService ibanityService) {
        financialInstitutionSample = new FinancialInstitutionSample(ibanityService);
        financialInstitutionUserSample = new FinancialInstitutionUserSample(ibanityService);
        financialInstitutionAccountSample = new FinancialInstitutionAccountSample(ibanityService);
        financialInstitutionTransactionSample = new FinancialInstitutionTransactionSample(ibanityService);
        financialInstitutionHoldingSample = new FinancialInstitutionHoldingSample(ibanityService);
    }

    public static void main(String[] args) throws CertificateException, IOException {
        String passphrase = getConfiguration(IBANITY_CLIENT_TLS_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY);
        OptionalPropertiesBuilder ibanityServiceBuilder = IbanityServiceBuilder.builder()
                .ibanityApiEndpoint(getConfiguration(IBANITY_API_ENDPOINT_PROPERTY_KEY))
                .tlsPrivateKey(loadPrivateKey(getConfiguration(IBANITY_CLIENT_TLS_PRIVATE_KEY_PATH_PROPERTY_KEY), passphrase))
                .passphrase(passphrase)
                .tlsCertificate(loadCertificate(getConfiguration(IBANITY_CLIENT_TLS_CERTIFICATE_PATH_PROPERTY_KEY)))
                .caCertificate(loadCa(getConfiguration(IBANITY_CLIENT_TLS_CA_CERTIFICATE_PATH_PROPERTY_KEY)));
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
        FinancialInstitution financialInstitution = clientSandboxSample.financialInstitutionSamples();
        LOGGER.info("Financial institution {}", financialInstitution);

        FinancialInstitutionUser financialInstitutionUser = clientSandboxSample.financialInstitutionUserSamples();
        LOGGER.info("Financial institution user {}", financialInstitutionUser);

        FinancialInstitutionAccount financialInstitutionAccount = clientSandboxSample.financialInstitutionAccountSamples();
        LOGGER.info("Financial institution account {}", financialInstitutionAccount);

        FinancialInstitutionTransaction financialInstitutionTransaction = clientSandboxSample.financialInstitutionTransactionSamples();
        LOGGER.info("Financial institution transaction {}", financialInstitutionTransaction);

        FinancialInstitutionHolding financialInstitutionHoldingSamples = clientSandboxSample.financialInstitutionHoldingSamples();
        LOGGER.info("Financial institution holding {}", financialInstitutionHoldingSamples);

        LOGGER.info("Samples end");
    }

    public FinancialInstitution financialInstitutionSamples() {
        LOGGER.info("Financial Institution samples");

        FinancialInstitution financialInstitution = financialInstitutionSample.create();
        FinancialInstitution update = financialInstitutionSample.update(financialInstitution);
        financialInstitutionSample.find(financialInstitution.getId());
        deleteFinancialInstitution(financialInstitution);
        return financialInstitution;
    }

    private void deleteFinancialInstitution(FinancialInstitution financialInstitution) {
        financialInstitutionSample.delete(financialInstitution);
    }

    public FinancialInstitutionUser financialInstitutionUserSamples() {
        LOGGER.info("Financial Institution User samples");

        FinancialInstitutionUser financialInstitutionUser = financialInstitutionUserSample.create();
        FinancialInstitutionUser update = financialInstitutionUserSample.update(financialInstitutionUser);
        financialInstitutionUserSample.find(financialInstitutionUser.getId());
        financialInstitutionUserSample.delete(financialInstitutionUser);
        return financialInstitutionUser;
    }

    public FinancialInstitutionAccount financialInstitutionAccountSamples() {
        LOGGER.info("Financial Institution Account samples");

        // create related objects
        FinancialInstitution financialInstitution = financialInstitutionSample.create();
        FinancialInstitutionUser financialInstitutionUser = financialInstitutionUserSample.create();

        FinancialInstitutionAccount financialInstitutionAccount =
                financialInstitutionAccountSample.create(financialInstitution, financialInstitutionUser);

        financialInstitutionAccountSample.find(financialInstitution, financialInstitutionUser, financialInstitutionAccount.getId());

        FinancialInstitutionAccount delete = financialInstitutionAccountSample.delete(financialInstitution, financialInstitutionUser, financialInstitutionAccount);

        // clean related objects
        deleteFinancialInstitution(financialInstitution);
        financialInstitutionUserSample.delete(financialInstitutionUser);

        return financialInstitutionAccount;
    }

    public FinancialInstitutionTransaction financialInstitutionTransactionSamples() {
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
        deleteFinancialInstitution(financialInstitution);
        this.financialInstitutionUserSample.delete(financialInstitutionUser);

        return financialInstitutionTransaction;
    }

    public FinancialInstitutionHolding financialInstitutionHoldingSamples() {
        LOGGER.info("Financial Institution Holding samples");

        FinancialInstitution financialInstitution = financialInstitutionSample.create();
        FinancialInstitutionUser financialInstitutionUser = financialInstitutionUserSample.create();
        FinancialInstitutionAccount financialInstitutionAccount =
                financialInstitutionAccountSample.create(financialInstitution, financialInstitutionUser);

        FinancialInstitutionHolding financialInstitutionHolding =
                financialInstitutionHoldingSample.create(financialInstitution, financialInstitutionUser, financialInstitutionAccount);

        financialInstitutionHoldingSample.find(financialInstitution, financialInstitutionUser,
                financialInstitutionAccount, financialInstitutionHolding.getId());

        financialInstitutionHoldingSample.delete(financialInstitution, financialInstitutionUser,
                financialInstitutionAccount, financialInstitutionHolding);

        // clean related objects
        this.financialInstitutionAccountSample.delete(financialInstitution, financialInstitutionUser, financialInstitutionAccount);
        deleteFinancialInstitution(financialInstitution);
        this.financialInstitutionUserSample.delete(financialInstitutionUser);

        return financialInstitutionHolding;
    }

}
