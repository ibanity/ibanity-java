package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.Customer;
import com.ibanity.apis.client.products.xs2a.models.delete.CustomerDeleteQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private static final String CUSTOMER_ACCESS_TOKEN = "itsme";
    private static final String CUSTOMER_API = "https://api.ibanity.localhost/xs2a/customer";
    private static final UUID CUSTOMER_ID = UUID.fromString("894be17a-aa66-47f8-8447-b352338eacbd");

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "self")).thenReturn(CUSTOMER_API);
    }

    @Test
    void delete() throws Exception {
        CustomerDeleteQuery customerDeleteQuery =
                CustomerDeleteQuery.builder()
                        .customerAccessToken(CUSTOMER_ACCESS_TOKEN)
                        .build();

        when(ibanityHttpClient.delete(new URI(CUSTOMER_API), emptyMap(), CUSTOMER_ACCESS_TOKEN))
                .thenReturn(loadFile("json/customer.json"));

        Customer actual = customerService.delete(customerDeleteQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpected());
    }

    private Customer createExpected() {
        return Customer.builder()
                .id(CUSTOMER_ID)
                .build();
    }
}