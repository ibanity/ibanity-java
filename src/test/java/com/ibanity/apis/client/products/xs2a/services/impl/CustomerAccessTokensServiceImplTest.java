package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.factory.create.CustomerAccessTokenCreationQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerAccessTokensServiceImplTest {

    private static final String APPLICATION_REFERENCE = "666";
    private static final String CUSTOMER_ACCESS_TOKEN_ENDPOINT = "https://api.ibanity.com/xs2a/customer-access-tokens";

    @InjectMocks
    private CustomerAccessTokensServiceImpl customerAccessTokensService;

    @Mock
    private IbanityHttpClient httpClient;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Test
    void create() throws Exception {
        CustomerAccessTokenCreationQuery creationQuery =
                CustomerAccessTokenCreationQuery.builder()
                        .applicationCustomerReference(APPLICATION_REFERENCE)
                        .build();

        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customerAccessTokens")).thenReturn(CUSTOMER_ACCESS_TOKEN_ENDPOINT);
        when(httpClient.post(new URI(CUSTOMER_ACCESS_TOKEN_ENDPOINT), buildRequest(), null)).thenReturn(loadFile("json/customerAccessToken.json"));

        CustomerAccessToken actual = customerAccessTokensService.create(creationQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    private RequestApiModel buildRequest() {
        CustomerAccessToken request = CustomerAccessToken.builder().applicationCustomerReference(APPLICATION_REFERENCE).build();
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .attributes(request)
                                .type(CustomerAccessToken.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private CustomerAccessToken createExpected() {
        return CustomerAccessToken.builder()
                .id(UUID.fromString("10afe599-bb51-49f0-974c-4d396a1af5d0"))
                .token("eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIxMGFmZTU5OS1iYjUxLTQ5ZjAtOTc0Yy00ZDM5NmExYWY1ZDAiLCJpc3MiOiIyZTM2YTVlNy0wYjFlLTRkYjctOGEwMy03OTAyZjc4NWI0MTYiLCJhdWQiOiJ1c2VyX2FjY291bnRfYWNjZXNzIiwic3ViIjoiNzY1MGE1ZmYtNzY1Ni00OGEwLTg3MDAtMDdjNzAwMmFlMDcxIiwiZGF0YSI6e319.nG2A4HbF0JCNCabmMIn4u7XqQI2P9xNFPfC2VuqfP8zgi-sIS8rjUFKaLG6cUjvAAIbRgxkLCGq2KsegPEgXIavfnGq7FV6POF-pAyvdOfGtqP08wmkdrXzD5GHsQngWr_1sMv7oCsTrxDtZQxx7Gv1UOTnJYUxJEBirCgeAm0oPjRr5V97BN4Bh4XB3FhLnLZGqlO01-sDykdWY764BYQM-76WElXP2c6CNFF_ZklsL5LRzJ4tHhV5F5jYf7o5s6cxEQnC7sAuwVV1YXoKMxlwYbPhr64rB3MJBmBlAMkHJO4wkyV2eTpb7pIvq0jRbK-2CNWhCYqPi93NnXcpxQw")
                .build();
    }
}