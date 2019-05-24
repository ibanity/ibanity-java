package com.ibanity.samples;


import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.samples.sandbox.FinancialInstitutionAccountSample;
import com.ibanity.samples.sandbox.FinancialInstitutionSample;
import com.ibanity.samples.sandbox.FinancialInstitutionTransactionSample;
import com.ibanity.samples.sandbox.FinancialInstitutionUserSample;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientSandboxSample {
    private static final Logger LOGGER = LogManager.getLogger(ClientSandboxSample.class);

    // Sample services
    private final FinancialInstitutionSample financialInstitutionSample = new FinancialInstitutionSample();
    private final FinancialInstitutionUserSample financialInstitutionUserSample = new FinancialInstitutionUserSample();
    private final FinancialInstitutionAccountSample financialInstitutionAccountSample = new FinancialInstitutionAccountSample();
    private final FinancialInstitutionTransactionSample financialInstitutionTransactionSample = new FinancialInstitutionTransactionSample();

    public static void main(String[] args) {
        IbanityService.apiUrlProvider().loadApiSchema();
        ClientSandboxSample clientSandboxSample = new ClientSandboxSample();
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