package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.factory.read.FinancialInstitutionReadQuery;
import com.ibanity.apis.client.products.xs2a.models.factory.read.FinancialInstitutionsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.UUID;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FinancialInstitutionsServiceImplTest {

    private static final UUID FINANCIAL_INSTITUTION_ID = UUID.fromString("268c9f39-736b-4a9a-b198-4191030c0e21");
    private static final String CUSTOMER_ACCESS_TOKEN = "itsme";
    private static final String FINANCIAL_INSTITUTION_CUSTOMER_ENDPOINT = "https://api.ibanity.com/xs2a/customer/financial-institutions";
    private static final String FINANCIAL_INSTITUTION_ENDPOINT = "https://api.ibanity.localhost/xs2a/financial-institutions/{financialInstitutionId}";
    private static final String FINANCIAL_INSTITUTION_ENDPOINT_ID = "https://api.ibanity.localhost/xs2a/financial-institutions/" + FINANCIAL_INSTITUTION_ID;

    @InjectMocks
    private FinancialInstitutionsServiceImpl financialInstitutionsService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "financialInstitutions")).thenReturn(FINANCIAL_INSTITUTION_ENDPOINT);
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitutions")).thenReturn(FINANCIAL_INSTITUTION_CUSTOMER_ENDPOINT);
    }


    @Test
    void list() throws Exception {
        FinancialInstitutionsReadQuery financialInstitutionsReadQuery = FinancialInstitutionsReadQuery.builder().build();

        when(ibanityHttpClient.get(buildUri("https://api.ibanity.localhost/xs2a/financial-institutions", IbanityPagingSpec.DEFAULT_PAGING_SPEC), null)).thenReturn(IbanityTestHelper.loadFile("json/financialInstitutions.json"));

        IbanityCollection<FinancialInstitution> actual = financialInstitutionsService.list(financialInstitutionsReadQuery);

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    @Test
    void list_whenCustomerAccessTokenProvided_fetchesFinancialInstitutionForCustomer() throws Exception {
        FinancialInstitutionsReadQuery financialInstitutionsReadQuery =
                FinancialInstitutionsReadQuery.builder()
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();

        when(ibanityHttpClient.get(buildUri(FINANCIAL_INSTITUTION_CUSTOMER_ENDPOINT, IbanityPagingSpec.DEFAULT_PAGING_SPEC), CUSTOMER_ACCESS_TOKEN)).thenReturn(IbanityTestHelper.loadFile("json/financialInstitutions.json"));

        IbanityCollection<FinancialInstitution> actual = financialInstitutionsService.list(financialInstitutionsReadQuery);

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    @Test
    void find() throws Exception {
        FinancialInstitutionReadQuery financialInstitutionsReadQuery = FinancialInstitutionReadQuery
                .builder()
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();

        when(ibanityHttpClient.get(buildUri(FINANCIAL_INSTITUTION_ENDPOINT_ID), null))
                .thenReturn(IbanityTestHelper.loadFile("json/financialInstitution.json"));

        FinancialInstitution actual = financialInstitutionsService.find(financialInstitutionsReadQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected()[0]);
    }

    private FinancialInstitution[] createExpected() {
        return new FinancialInstitution[]{
                FinancialInstitution.builder()
                        .id(FINANCIAL_INSTITUTION_ID)
                        .selfLink("https://api.ibanity.com/xs2a/financial-institutions/268c9f39-736b-4a9a-b198-4191030c0e21")
                        .bic("NBBEBEBB203")
                        .name("ABN AMRO FUND MANAGERS LIMITED 0")
                        .futureDatedPaymentsAllowed(true)
                        .logoUrl("https://s3.eu-central-1.amazonaws.com/ibanity-production-financial-institution-assets/sandbox.png")
                        .primaryColor("#7d39ff")
                        .requiresCredentialStorage(true)
                        .requiresCustomerIpAddress(true)
                        .secondaryColor("#3DF2C2")
                        .minRequestedAccountReferences(0L)
                        .sandbox(true)
                        .build(),
                FinancialInstitution.builder()
                        .id(UUID.fromString("c0f14bd6-ae49-4611-9709-0ea553464a1a"))
                        .selfLink("https://api.ibanity.com/xs2a/financial-institutions/c0f14bd6-ae49-4611-9709-0ea553464a1a")
                        .bic("NBBEBEBB203")
                        .name("PGMS (GLASGOW) LIMITED 2")
                        .logoUrl("https://s3.eu-central-1.amazonaws.com/ibanity-production-financial-institution-assets/sandbox.png")
                        .primaryColor("#7d39ff")
                        .maxRequestedAccountReferences(0L)
                        .secondaryColor("#3DF2C2")
                        .build()

        };
    }
}