package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.configuration.ApiIUrls;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiServiceTest extends AbstractServiceTest {

    private static ApiIUrls apiUrls;

    @BeforeAll
    static void beforeAll(){
        apiUrls = IbanityConfiguration.getApiIUrls();
    }

    @Test
    void testValidateCustomerAccessTokensApiUrl() {
        assertTrue(apiUrls.getCustomerAccessTokens().contains(CustomerAccessToken.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getCustomerAccessTokens().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateFinancialInstitutionsApiUrl() {
        assertTrue(apiUrls.getFinancialInstitutions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getFinancialInstitutions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getFinancialInstitutions().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateSandboxFinancialInstitutionsApiUrl() {
        assertTrue(apiUrls.getSandbox().getFinancialInstitutions().contains(IbanityConfiguration.SANBOX_PREFIX_PATH));
        assertTrue(apiUrls.getSandbox().getFinancialInstitutions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getSandbox().getFinancialInstitutions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getSandbox().getFinancialInstitutions().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateSandboxFinancialInstitutionUsersApiUrl() {
        // FinancialInstitutionUsers
        assertTrue(apiUrls.getSandbox().getFinancialInstitutionUsers().contains(IbanityConfiguration.SANBOX_PREFIX_PATH));
        assertTrue(apiUrls.getSandbox().getFinancialInstitutionUsers().contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getSandbox().getFinancialInstitutionUsers().contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getSandbox().getFinancialInstitutionUsers().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }


    @Test
    void testValidateSandboxFinancialInstitutionFinancialInstitutionUserFinancialInstitutionAccountApiUrl() {
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts().contains(IbanityConfiguration.SANBOX_PREFIX_PATH));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts().contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts().contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts().contains(FinancialInstitutionAccount.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts().contains(FinancialInstitutionAccount.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateSandboxFinancialInstitutionFinancialInstitutionUserFinancialInstitutionAccountFinancialInstitutionTransactionApiUrl() {
        // FinancialInstitutionTransactions
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(IbanityConfiguration.SANBOX_PREFIX_PATH));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(FinancialInstitutionUser.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(FinancialInstitutionAccount.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(FinancialInstitutionAccount.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(FinancialInstitutionTransaction.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(FinancialInstitutionTransaction.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateCustomerFinancialInstitutionsApiUrl() {
        assertTrue(apiUrls.getCustomer().getFinancialInstitutions().contains(IbanityConfiguration.CUSTOMER_PREFIX_PATH));
        assertTrue(apiUrls.getCustomer().getFinancialInstitutions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertFalse(apiUrls.getCustomer().getFinancialInstitutions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitutions().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateCustomerAccountsApiUrl() {
        // Accounts
        assertTrue(apiUrls.getCustomer().getAccounts().contains(IbanityConfiguration.CUSTOMER_PREFIX_PATH));
        assertTrue(apiUrls.getCustomer().getAccounts().contains(Account.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertFalse(apiUrls.getCustomer().getAccounts().contains(Account.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getAccounts().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateFinancialInstitutionInformationAccessRequestsApiUrl() {
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccountInformationAccessRequests().contains(IbanityConfiguration.CUSTOMER_PREFIX_PATH));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccountInformationAccessRequests().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccountInformationAccessRequests().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccountInformationAccessRequests().contains(AccountInformationAccessRequest.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertFalse(apiUrls.getCustomer().getFinancialInstitution().getAccountInformationAccessRequests().contains(AccountInformationAccessRequest.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccountInformationAccessRequests().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateFinancialInstitutionPaymentInitiationRequestsApiUrl() {
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getPaymentInitiationRequests().contains(IbanityConfiguration.CUSTOMER_PREFIX_PATH));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getPaymentInitiationRequests().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getPaymentInitiationRequests().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getPaymentInitiationRequests().contains(PaymentInitiationRequest.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getPaymentInitiationRequests().contains(PaymentInitiationRequest.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getPaymentInitiationRequests().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateFinancialInstitutionAccountsApiUrl() {
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccounts().contains(IbanityConfiguration.CUSTOMER_PREFIX_PATH));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccounts().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccounts().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccounts().contains(Account.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccounts().contains(Account.class.getAnnotation(JsonApiResource.class).type() + URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getAccounts().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }

    @Test
    void testValidateFinancialInstitutionAccountsTransactionApiUrl() {
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getTransactions().contains(IbanityConfiguration.CUSTOMER_PREFIX_PATH));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getTransactions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getTransactions().contains(FinancialInstitution.class.getAnnotation(JsonApiResource.class).type()+URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getTransactions().contains(Account.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getTransactions().contains(Account.class.getAnnotation(JsonApiResource.class).type()+URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getTransactions().contains(Transaction.class.getAnnotation(JsonApiResource.class).resourcePath()));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getTransactions().contains(Transaction.class.getAnnotation(JsonApiResource.class).type()+URL_PARAMETER_ID_POSTFIX));
        assertTrue(apiUrls.getCustomer().getFinancialInstitution().getTransactions().contains(IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY)));
    }
}