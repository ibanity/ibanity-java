package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.services.impl.CustomerAccessTokensServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CustomerAccessTokensServiceImpl Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 14, 2018</pre>
 */
public class CustomerAccessTokensServiceTest {

    private static CustomerAccessTokensService customerAccessTokensService = new CustomerAccessTokensServiceImpl();

    @BeforeEach
    public void before() {
    }

    @AfterEach
    public void after() {
    }

    /**
     * Method: createCustomerAccessToken(CustomerAccessToken customerAccessToken)
     */
    @Test
    public void testCreateCustomerAccessToken() throws Exception {
        UUID applicationCustomerReference = UUID.randomUUID();
        CustomerAccessToken customerAccessToken = getCustomerAccessToken(applicationCustomerReference.toString());
        assertNotNull(customerAccessToken.getToken());
        assertTrue(customerAccessToken.getToken().length() > 20);
        assertNotNull(customerAccessToken.getId());
    }

    public static CustomerAccessToken getCustomerAccessToken(String applicationCustomerReference){
        CustomerAccessToken customerAccessTokenRequest = new CustomerAccessToken(UUID.randomUUID());
        customerAccessTokenRequest.setApplicationCustomerReference(applicationCustomerReference);
        return customerAccessTokensService.createCustomerAccessToken(customerAccessTokenRequest);
    }

} 
