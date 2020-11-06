package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.Collection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitutionCountry;
import com.ibanity.apis.client.products.xs2a.models.read.FinancialInstitutionCountriesReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialInstitutionCountriesServiceImplTest {

    private static final String FINANCIAL_INSTITUTION_COUNTRIES_API = "https://api.ibanity.localhost/xs2a/financial-institution-countries";

    @InjectMocks
    private FinancialInstitutionCountriesServiceImpl countriesService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "financialInstitutionCountries")).thenReturn(FINANCIAL_INSTITUTION_COUNTRIES_API);
    }

    @Test
    void list() throws URISyntaxException, IOException {
        FinancialInstitutionCountriesReadQuery readQuery =
                FinancialInstitutionCountriesReadQuery.builder()
                        .pagingSpec(IbanityPagingSpec.builder()
                                .after("AU")
                                .build())
                        .build();

        when(ibanityHttpClient.get(new URI(FINANCIAL_INSTITUTION_COUNTRIES_API + "?page%5Bafter%5D=AU&page%5Blimit%5D=10"), emptyMap(), null))
                .thenReturn(loadHttpResponse("json/financial_institution_countries.json"));

        Collection<FinancialInstitutionCountry> actual = countriesService.list(readQuery);

        assertThat(actual.getItems()).containsExactly(createExpected("BE"), createExpected("NL"));
        assertThat(actual.getPageLimit()).isEqualTo(10);
    }

    private FinancialInstitutionCountry createExpected(String country) {
        return FinancialInstitutionCountry.builder().id(country).build();
    }
}
