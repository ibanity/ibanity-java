package com.ibanity.samples;

import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;

import java.util.List;
import java.util.UUID;

public class SampleRunner {

    public static void main(String[] args) {
        ClientSample clientSample  = new ClientSample();

        String consentReference = "application_customer_reference-" + UUID.randomUUID().toString();

        CustomerAccessToken customerAccessToken = clientSample.createCustomerAccessToken(consentReference);

        List<FinancialInstitution> financialInstitutions = clientSample.listAvailableFinancialInstitutions();

        // Get the first financial-institution of the list to work with
        FinancialInstitution financialInstitution = financialInstitutions.get(0);

        AccountInformationAccessRequest accountInformationAccessRequest =
                clientSample.createAccountInformationAccessRequest(
                        consentReference, customerAccessToken, financialInstitution);

        clientSample.waitForAuthorizationWebFlow(accountInformationAccessRequest);

        List<Account> accounts = clientSample.listCustomerAccounts(customerAccessToken);

        // Get the first account of the list to work with
        Account account = accounts.get(0);

        // Other possibility to get one account
        // account = clientSample.getOneSpecificAccount(customerAccessToken, financialInstitution, account);

        clientSample.listTransactionsOfOneAccount(customerAccessToken, account);

        PaymentInitiationRequest paymentInitiationRequest = clientSample.generateRandomPaymentInitiationRequest(financialInstitution);

        clientSample.createPaymentInitiationRequest(customerAccessToken, financialInstitution, paymentInitiationRequest);

    }}
