package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerAccessTokensServiceTest extends AbstractServiceTest {

    @Test
    public void testCreateCustomerAccessToken() {
        UUID applicationCustomerReference = UUID.randomUUID();
        CustomerAccessToken customerAccessToken = getCustomerAccessToken(applicationCustomerReference.toString());
        assertNotNull(customerAccessToken.getToken());
        assertTrue(customerAccessToken.getToken().length() > 20);
        assertNotNull(customerAccessToken.getId());
    }

    @Test
    public void testCreateCustomerAccessTokenWithIdempotency() {
        UUID applicationCustomerReference = UUID.randomUUID();
        CustomerAccessToken customerAccessToken = customerAccessTokensService.create(applicationCustomerReference.toString(), UUID.randomUUID());
        assertNotNull(customerAccessToken.getToken());
        assertTrue(customerAccessToken.getToken().length() > 20);
        assertNotNull(customerAccessToken.getId());
    }
}
