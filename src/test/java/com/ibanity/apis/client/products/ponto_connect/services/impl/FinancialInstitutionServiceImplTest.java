package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Filter;
import com.ibanity.apis.client.products.ponto_connect.models.FinancialInstitution;
import com.ibanity.apis.client.products.ponto_connect.models.read.FinancialInstitutionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.FinancialInstitutionsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.OrganizationFinancialInstitutionsReadQuery;
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
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FinancialInstitutionServiceImplTest {

    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final UUID FINANCIAL_INSTITUTION_ID = fromString("953934eb-229a-4fd2-8675-07794078cc7d");
    private static final UUID FINANCIAL_INSTITUTION_ID_2 = fromString("953934eb-0002-4fd2-8675-07794078cc7d");
    private static final String FINANCIAL_INSTITUTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/financial-institutions/{financialInstitutionId}";
    private static final String GET_FINANCIAL_INSTITUTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/financial-institutions/953934eb-229a-4fd2-8675-07794078cc7d";
    private static final String LIST_ORGANIZATION_FINANCIAL_INSTITUTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/financial-institutions?limit=10";
    private static final String LIST_FINANCIAL_INSTITUTION_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/financial-institutions?limit=10&filter%5Bname%5D%5Bcontains%5D=Bank";

    @InjectMocks
    private FinancialInstitutionServiceImpl financialInstitutionService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "financialInstitutions")).thenReturn(FINANCIAL_INSTITUTION_ENDPOINT);
    }

    @Test
    public void find() throws Exception {
        when(ibanityHttpClient.get(new URI(GET_FINANCIAL_INSTITUTION_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/financial_institution.json"));

        FinancialInstitution actual = financialInstitutionService.find(FinancialInstitutionReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected(FINANCIAL_INSTITUTION_ID, "Fake Bank"));
    }

    @Test
    public void listForOrganization() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_ORGANIZATION_FINANCIAL_INSTITUTION_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/organization_financial_institutions.json"));

        IbanityCollection<FinancialInstitution> actual = financialInstitutionService.list(OrganizationFinancialInstitutionsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected(FINANCIAL_INSTITUTION_ID, "Fake Bank"));
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_FINANCIAL_INSTITUTION_ENDPOINT), emptyMap(), null))
                .thenReturn(loadHttpResponse("json/ponto-connect/financial_institutions.json"));

        IbanityCollection<FinancialInstitution> actual = financialInstitutionService.list(FinancialInstitutionsReadQuery.builder()
                .filters(newArrayList(Filter.builder()
                        .field("name")
                        .contains("Bank")
                        .build()))
                .build());

        assertThat(actual.getItems()).containsExactly(createExpected(FINANCIAL_INSTITUTION_ID, "Full Fake Bank"), createExpected(FINANCIAL_INSTITUTION_ID_2, "Full Fake Bank 2"));
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    private FinancialInstitution createExpected(UUID financialInstitutionId, String name) {
        return FinancialInstitution.builder()
                .id(financialInstitutionId)
                .name(name)
                .build();
    }
}
