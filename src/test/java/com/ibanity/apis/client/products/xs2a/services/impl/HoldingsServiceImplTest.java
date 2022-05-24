package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.Holding;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.read.HoldingReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.HoldingsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HoldingsServiceImplTest {

    private static final UUID ACCOUNT_ID = UUID.fromString("9fe37230-9103-49bd-a432-2bad72d5abb2");
    private static final UUID HOLDING_ID = UUID.fromString("eb535c31-f619-4092-9db2-4db84149ddcb");
    private static final UUID FINANCIAL_INSTITUTION_ID = UUID.fromString("f252866e-638e-4d28-a575-20a406223777");

    private static final String CUSTOMER_ACCESS_TOKEN = UUID.randomUUID().toString();
    private static final String HOLDINGS_ENDPOINT = "https://api.ibanity.localhost/xs2a/customer/financial-institutions/{financialInstitutionId}/accounts/{accountId}/holdings/{holdingId}";
    private static final String HOLDING_URI = "https://api.ibanity.localhost/xs2a/customer/financial-institutions/f252866e-638e-4d28-a575-20a406223777/accounts/9fe37230-9103-49bd-a432-2bad72d5abb2/holdings/eb535c31-f619-4092-9db2-4db84149ddcb";
    private static final String HOLDINGS_URI = "https://api.ibanity.localhost/xs2a/customer/financial-institutions/f252866e-638e-4d28-a575-20a406223777/accounts/9fe37230-9103-49bd-a432-2bad72d5abb2/holdings?page%5Blimit%5D=10";
    private static final String FIRST_LINK = "https://api.ibanity.localhost/xs2a/customer/financial-institutions/f252866e-638e-4d28-a575-20a406223777/accounts/9fe37230-9103-49bd-a432-2bad72d5abb2/holdings";


    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private HoldingsServiceImpl holdingsService;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "holdings")).thenReturn(HOLDINGS_ENDPOINT);
    }

    @Test
    void list() throws URISyntaxException, IOException {
        IbanityCollection<Holding> expected =
                IbanityCollection.<Holding>builder()
                        .pageLimit(10)
                        .firstLink(FIRST_LINK)
                        .items(Collections.singletonList(createExpected()))
                        .latestSynchronization(Synchronization.builder()
                                .resourceId("9fe37230-9103-49bd-a432-2bad72d5abb2")
                                .resourceType("account")
                                .subtype("accountHoldings")
                                .status("success")
                                .id(UUID.fromString("b2d3b6ad-5299-40be-9829-d8abb69c58b6"))
                                .createdAt(Instant.parse("2020-02-04T14:07:41.089Z"))
                                .updatedAt(Instant.parse("2020-02-04T14:07:42.169Z"))
                                .build())
                        .build();

        when(ibanityHttpClient.get(new URI(HOLDINGS_URI), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/holdings.json"));


        IbanityCollection<Holding> holdings = holdingsService.list(HoldingsReadQuery.builder()
                .accountId(ACCOUNT_ID)
                .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build());

        Assertions.assertThat(holdings).isEqualToComparingFieldByFieldRecursively(expected);
    }

    private Holding createExpected() {
        return Holding.builder()
                .id(UUID.fromString("a5af9187-9d11-4c30-9263-7c35ff036f6c"))
                .selfLink("https://api.ibanity.localhost/xs2a/customer/financial-institutions/f252866e-638e-4d28-a575-20a406223777/accounts/9fe37230-9103-49bd-a432-2bad72d5abb2/holdings/a5af9187-9d11-4c30-9263-7c35ff036f6c")
                .lastValuation(TEN)
                .quantity(new BigDecimal("10.55"))
                .lastValuationCurrency("USD")
                .lastValuationDate(LocalDate.parse("2020-01-01"))
                .totalValuation(new BigDecimal("105.5"))
                .totalValuationCurrency("USD")
                .subtype("STOCK")
                .referenceType("ISIN")
                .reference("ISINBE6456789")
                .name("AAPL")
                .build();
    }

    @Test
    void find() throws Exception {
        when(ibanityHttpClient.get(new URI(HOLDING_URI), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(IbanityTestHelper.loadHttpResponse("json/holding.json"));


        Holding holding = holdingsService.find(HoldingReadQuery.builder()
                .accountId(ACCOUNT_ID)
                .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                .holdingId(HOLDING_ID)
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build());

        Assertions.assertThat(holding).isEqualToComparingFieldByFieldRecursively(createExpected());
    }
}
