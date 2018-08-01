package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.configuration.ApiUrls;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import io.crnk.core.resource.annotations.JsonApiResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.URL_PARAMETER_ID_POSTFIX;
import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiServiceTest extends AbstractServiceTest {

    private static ApiUrls apiUrls;

    public static final String FORWARD_SLASH        = "/";
    public static final String SANDBOX_PREFIX_PATH = FORWARD_SLASH + "sandbox";
    public static final String CUSTOMER_PREFIX_PATH = FORWARD_SLASH + "customer";

    @BeforeAll
    static void beforeAll(){
        apiUrls = IbanityConfiguration.getApiUrls();
    }

    @Test
    void testValidateCustomerAccessTokensApiUrl() {
        String customerAccessToken = apiUrls.getCustomerAccessTokens();

        assertTrue(customerAccessToken.contains(CustomerAccessToken.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(customerAccessToken.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateFinancialInstitutionsApiUrl() {
        String financialInstitutions = apiUrls.getFinancialInstitutions();

        assertTrue(financialInstitutions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutions.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateSandboxFinancialInstitutionsApiUrl() {
        String financialInstitutions = apiUrls.getSandbox().getFinancialInstitutions();

        assertTrue(financialInstitutions.contains(SANDBOX_PREFIX_PATH));
        assertTrue(financialInstitutions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutions.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateSandboxFinancialInstitutionUsersApiUrl() {
        String financialInstitutionUsers = apiUrls.getSandbox().getFinancialInstitutionUsers();

        assertTrue(financialInstitutionUsers.contains(SANDBOX_PREFIX_PATH));
        assertTrue(financialInstitutionUsers.contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutionUsers.contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutionUsers.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }


    @Test
    void testValidateSandboxFinancialInstitutionFinancialInstitutionAccountsApiUrl() {
        String financialInstitutionAccounts = apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts();

        assertTrue(financialInstitutionAccounts.contains(SANDBOX_PREFIX_PATH));
        assertTrue(financialInstitutionAccounts.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutionAccounts.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutionAccounts.contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutionAccounts.contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutionAccounts.contains(FinancialInstitutionAccount.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutionAccounts.contains(FinancialInstitutionAccount.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutionAccounts.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateSandboxFinancialInstitutionFinancialInstitutionAccountFinancialInstitutionTransactionsApiUrl() {
        String financialInstitutionTransactions = apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions();

        assertTrue(financialInstitutionTransactions.contains(SANDBOX_PREFIX_PATH));
        assertTrue(financialInstitutionTransactions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutionTransactions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutionTransactions.contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutionTransactions.contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutionTransactions.contains(FinancialInstitutionAccount.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutionTransactions.contains(FinancialInstitutionAccount.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutionTransactions.contains(FinancialInstitutionTransaction.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(financialInstitutionTransactions.contains(FinancialInstitutionTransaction.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutionTransactions.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateCustomerFinancialInstitutionsApiUrl() {
        String financialInstitutions = apiUrls.getCustomer().getFinancialInstitutions();

        assertTrue(financialInstitutions.contains(CUSTOMER_PREFIX_PATH));
        assertTrue(financialInstitutions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertFalse(financialInstitutions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(financialInstitutions.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateCustomerAccountsApiUrl() {
        String accounts = apiUrls.getCustomer().getAccounts();

        assertTrue(accounts.contains(CUSTOMER_PREFIX_PATH));
        assertTrue(accounts.contains(Account.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertFalse(accounts.contains(Account.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(accounts.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateCustomerFinancialInstitutionAccountInformationAccessRequestsApiUrl() {
        String accountInformationAccessRequestsApiUrl = apiUrls.getCustomer().getFinancialInstitution().getAccountInformationAccessRequests();

        assertTrue(accountInformationAccessRequestsApiUrl.contains(CUSTOMER_PREFIX_PATH));
        assertTrue(accountInformationAccessRequestsApiUrl.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(accountInformationAccessRequestsApiUrl.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(accountInformationAccessRequestsApiUrl.contains(AccountInformationAccessRequest.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertFalse(accountInformationAccessRequestsApiUrl.contains(AccountInformationAccessRequest.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(accountInformationAccessRequestsApiUrl.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateCustomerFinancialInstitutionPaymentInitiationRequestsApiUrl() {
        String paymentInitiationRequests = apiUrls.getCustomer().getFinancialInstitution().getPaymentInitiationRequests();

        assertTrue(paymentInitiationRequests.contains(CUSTOMER_PREFIX_PATH));
        assertTrue(paymentInitiationRequests.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(paymentInitiationRequests.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(paymentInitiationRequests.contains(PaymentInitiationRequest.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(paymentInitiationRequests.contains(PaymentInitiationRequest.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(paymentInitiationRequests.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateCustomerFinancialInstitutionFinancialInstitutionAccountsApiUrl() {
        String accounts = apiUrls.getCustomer().getFinancialInstitution().getAccounts();

        assertTrue(accounts.contains(CUSTOMER_PREFIX_PATH));
        assertTrue(accounts.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(accounts.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(accounts.contains(Account.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(accounts.contains(Account.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(accounts.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateCustomerFinancialInstitutionFinancialInstitutionTransactionApiUrl() {
        String transactions = apiUrls.getCustomer().getFinancialInstitution().getTransactions();

        assertTrue(transactions.contains(CUSTOMER_PREFIX_PATH));
        assertTrue(transactions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(transactions.contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type()+URL_PARAMETER_ID_POSTFIX));
        assertTrue(transactions.contains(Account.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(transactions.contains(Account.class.getAnnotation(JsonApiResource.class).type()+URL_PARAMETER_ID_POSTFIX));
        assertTrue(transactions.contains(Transaction.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(transactions.contains(Transaction.class.getAnnotation(JsonApiResource.class).type()+URL_PARAMETER_ID_POSTFIX));
        assertTrue(transactions.contains(getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }
}