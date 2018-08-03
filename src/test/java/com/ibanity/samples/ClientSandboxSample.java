package com.ibanity.samples;


import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsService;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionAccountsServiceImpl;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionTransactionsServiceImpl;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionUsersServiceImpl;
import com.ibanity.apis.client.sandbox.services.impl.SandboxFinancialInstitutionsServiceImpl;
import org.apache.commons.math3.util.Precision;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;

public class ClientSandboxSample {

    private final SandboxFinancialInstitutionsService sandBoxFinancialInstitutionsService = new SandboxFinancialInstitutionsServiceImpl();
    private final FinancialInstitutionAccountsService financialInstitutionAccountsService = new FinancialInstitutionAccountsServiceImpl();
    private final FinancialInstitutionTransactionsService financialInstitutionTransactionsService = new FinancialInstitutionTransactionsServiceImpl();
    private final FinancialInstitutionUsersService financialInstitutionUsersService = new FinancialInstitutionUsersServiceImpl();

    public void financialInstitutionRequetSamples(){
        FinancialInstitution financialInstitution = this.createSandboxFinancialInstitution();

        financialInstitution.setName("New FI name-"+Instant.now());
        sandBoxFinancialInstitutionsService.update(financialInstitution);

        sandBoxFinancialInstitutionsService.find(financialInstitution.getId());

        sandBoxFinancialInstitutionsService.delete(financialInstitution.getId());
    }

    public void financialInstitutionUserRequestSamples() {
        FinancialInstitutionUser financialInstitutionUser = this.createFinancialInstitutionUser();

        financialInstitutionUser.setPassword("new password");
        financialInstitutionUser = financialInstitutionUsersService.update(financialInstitutionUser);

        financialInstitutionUsersService.find(financialInstitutionUser.getId());

        financialInstitutionUsersService.delete(financialInstitutionUser.getId());
    }

    public void financialInstitutionAccountRequestSamples() {
        FinancialInstitution financialInstitution = this.createSandboxFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = this.createFinancialInstitutionUser();

        FinancialInstitutionAccount financialInstitutionAccount = this.generateRandomAccount(financialInstitution.getId());

        financialInstitutionAccount = financialInstitutionAccountsService.create(
                financialInstitution.getId(),
                financialInstitutionUser.getId(),
                financialInstitutionAccount);

        financialInstitutionAccount = financialInstitutionAccountsService.find(
                financialInstitution.getId(),
                financialInstitutionUser.getId(),
                financialInstitutionAccount.getId());

        financialInstitutionAccountsService.delete(
                financialInstitution.getId(),
                financialInstitutionUser.getId(),
                financialInstitutionAccount.getId());

        // clean temporary objects
        this.sandBoxFinancialInstitutionsService.delete(financialInstitution.getId());
        this.financialInstitutionUsersService.delete(financialInstitutionUser.getId());
    }

    public void financialInstitutionTransactionRequestSamples() {
        FinancialInstitution financialInstitution = this.createSandboxFinancialInstitution();
        FinancialInstitutionUser financialInstitutionUser = this.createFinancialInstitutionUser();
        FinancialInstitutionAccount financialInstitutionAccount = this.generateRandomAccount(financialInstitution.getId());

        FinancialInstitutionTransaction createdFinancialInstitutionTransaction = financialInstitutionTransactionsService.create(
                financialInstitution.getId(),
                financialInstitutionUser.getId(),
                financialInstitutionAccount.getId(),
                this.generateRandomTransaction(financialInstitutionAccount)
        );

        financialInstitutionTransactionsService.find(financialInstitution.getId(),
                financialInstitutionUser.getId(), financialInstitutionAccount.getId(),
                createdFinancialInstitutionTransaction.getId());

        financialInstitutionTransactionsService.delete(financialInstitution.getId(),
                financialInstitutionUser.getId(), financialInstitutionAccount.getId(),
                createdFinancialInstitutionTransaction.getId());

        // clean temporary objects
        this.financialInstitutionAccountsService.delete(financialInstitution.getId(),
                financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        this.sandBoxFinancialInstitutionsService.delete(financialInstitution.getId());
        this.financialInstitutionUsersService.delete(financialInstitutionUser.getId());
    }

    private FinancialInstitution createSandboxFinancialInstitution(){
        return sandBoxFinancialInstitutionsService.create("FI name-"+Instant.now());
    }

    private FinancialInstitutionUser createFinancialInstitutionUser() {
        return financialInstitutionUsersService.create(
                "Login-"+ Instant.now(), "Password",
                "LastName", "FirstName"
        );
    }

    private FinancialInstitutionAccount generateRandomAccount(UUID financialInstitutionId) {
        FinancialInstitutionAccount financialInstitutionAccount = new FinancialInstitutionAccount();

        financialInstitutionAccount.setSubType("checking");
        financialInstitutionAccount.setReference(Iban.random(CountryCode.BE).toString());
        financialInstitutionAccount.setReferenceType("IBAN");
        financialInstitutionAccount.setDescription("Checking Account");
        financialInstitutionAccount.setCurrency("EUR");
        financialInstitutionAccount.setCurrentBalance(this.generateRandomAmount());
        financialInstitutionAccount.setAvailableBalance(this.generateRandomAmount());
        financialInstitutionAccount.setFinancialInstitutionId(financialInstitutionId);

        return financialInstitutionAccount;
    }

    private FinancialInstitutionTransaction generateRandomTransaction(FinancialInstitutionAccount createdFinancialInstitutionAccount){
        Instant now = Instant.now();

        Instant executionDate = now.plus(3, ChronoUnit.DAYS);
        Instant valueDate = now.minus(1, ChronoUnit.DAYS);
        FinancialInstitutionTransaction financialInstitutionTransaction = new FinancialInstitutionTransaction();
        financialInstitutionTransaction.setFinancialInstitutionAccount(createdFinancialInstitutionAccount);
        financialInstitutionTransaction.setAmount(generateRandomAmount());
        financialInstitutionTransaction.setCounterpartName("Stroman, Hettinger and Swift");
        financialInstitutionTransaction.setCounterpartReference(Iban.random(CountryCode.BE).getAccountNumber());
        financialInstitutionTransaction.setCurrency("EUR");
        financialInstitutionTransaction.setDescription("Car rental");
        financialInstitutionTransaction.setExecutionDate(executionDate);
        financialInstitutionTransaction.setRemittanceInformation("Aspernatur et quibusdam.");
        financialInstitutionTransaction.setRemittanceInformationType("unstructured");
        financialInstitutionTransaction.setValueDate(valueDate);
        return financialInstitutionTransaction;
    }

    private double generateRandomAmount() {
        Random random = new Random();

        int randomDebitCreditSign = random.nextBoolean() ? -1 : 1;

        return Precision.round(
                random.doubles(10,1000)
                        .findFirst().getAsDouble() * randomDebitCreditSign, 2);
    }
}