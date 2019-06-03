package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.factory.read.AccountReadQuery;
import com.ibanity.apis.client.products.xs2a.models.factory.read.AccountsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountsServiceImplTest {

    private static final UUID ACCOUNT_ID = UUID.fromString("d6d2a2bc-6607-467b-ac78-86e4e19963ff");
    private static final UUID SYNCHRONIZATION_ID = UUID.fromString("d57d0a33-dcc0-4a45-b517-ca6a9d978fac");
    private static final UUID FINANCIAL_INSTITUTION_ID = UUID.fromString("43a02473-1023-4b70-a881-6c9b1857e8e6");
    private static final UUID ACCOUNT_INFORMATION_ACCESS_REQUEST_ID = UUID.fromString("4e4293b4-ffbd-4bc6-a02d-3899d199a6cc");

    private static final String CUSTOMER_ACCESS_TOKEN = "itsme";
    private static final String ACCOUNT_ENDPOINT = "https://api.ibanity.com/xs2a/customer/accounts/d6d2a2bc-6607-467b-ac78-86e4e19963ff";
    private static final String ACCOUNT_BY_AIAR_ENDPOINT = "https://api.ibanity.com/xs2a/customer/financial-institutions/43a02473-1023-4b70-a881-6c9b1857e8e6/account-information-access-requests/4e4293b4-ffbd-4bc6-a02d-3899d199a6cc/accounts";
    private static final String ACCOUNT_BY_FINANCIAL_INSTITUTION_ENDPOINT = "https://api.ibanity.com/xs2a/customer/financial-institutions/43a02473-1023-4b70-a881-6c9b1857e8e6/accounts/d6d2a2bc-6607-467b-ac78-86e4e19963ff";

    @InjectMocks
    private AccountsServiceImpl accountsService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "accounts")).thenReturn(ACCOUNT_ENDPOINT);
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accounts")).thenReturn(ACCOUNT_BY_FINANCIAL_INSTITUTION_ENDPOINT);
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accountInformationAccessRequest", "accounts")).thenReturn(ACCOUNT_BY_AIAR_ENDPOINT);
    }

    @Test
    void find() throws Exception {
        AccountReadQuery accountReadQuery =
                AccountReadQuery.builder()
                        .accountId(ACCOUNT_ID)
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .build();

        when(ibanityHttpClient.get(new URI(ACCOUNT_BY_FINANCIAL_INSTITUTION_ENDPOINT + "/" + ACCOUNT_ID), CUSTOMER_ACCESS_TOKEN)).thenReturn(loadFile("json/account.json"));

        Account actual = accountsService.find(accountReadQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    @Test
    void list_forAccountInformationAccessRequest() throws Exception {
        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .accountInformationAccessRequestId(ACCOUNT_INFORMATION_ACCESS_REQUEST_ID)
                .build();

        when(ibanityHttpClient.get(new URI(ACCOUNT_BY_AIAR_ENDPOINT + "?limit=10"), CUSTOMER_ACCESS_TOKEN)).thenReturn(loadFile("json/accounts.json"));

        IbanityCollection<Account> actual = accountsService.list(accountsReadQuery);

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    @Test
    void list_forFinancialInstitutions() throws Exception {
        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();

        when(ibanityHttpClient.get(new URI(ACCOUNT_BY_FINANCIAL_INSTITUTION_ENDPOINT + "?limit=10"), CUSTOMER_ACCESS_TOKEN)).thenReturn(loadFile("json/accounts.json"));
        IbanityCollection<Account> actual = accountsService.list(accountsReadQuery);

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    @Test
    void list() throws Exception {
        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                .build();

        when(ibanityHttpClient.get(new URI(ACCOUNT_ENDPOINT + "?limit=10"), CUSTOMER_ACCESS_TOKEN)).thenReturn(loadFile("json/accounts.json"));

        IbanityCollection<Account> actual = accountsService.list(accountsReadQuery);

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    private Account createExpected() {
        return Account.builder()
                .id(ACCOUNT_ID)
                .selfLink(ACCOUNT_BY_FINANCIAL_INSTITUTION_ENDPOINT)
                .referenceType("IBAN")
                .reference("BE0178572046576544")
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .description("Checking account")
                .currentBalance(0.0)
                .currency("EUR")
                .availableBalance(0.0)
                .subType("checking")
                .synchronizedAt(parse("2019-05-09T09:19:37.683Z"))
                .lastSynchronization(createSynchronization())
                .build();
    }

    private Synchronization createSynchronization() {
        return Synchronization.builder()
                .id(SYNCHRONIZATION_ID)
                .resourceId(ACCOUNT_ID.toString())
                .resourceType("account")
                .status("success")
                .subType("accountDetails")
                .createdAt(Instant.parse("2019-05-09T09:19:37.683Z"))
                .updatedAt(Instant.parse("2019-05-09T09:19:37.683Z"))
                .build();
    }
}