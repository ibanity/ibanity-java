package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.factory.create.CustomerAccessTokenCreationQuery;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerAccessTokensServiceTest extends AbstractServiceTest {

    @Test
    public void testCreateCustomerAccessToken() {
        UUID applicationCustomerReference = UUID.randomUUID();
        CustomerAccessToken customerAccessToken = createCustomerAccessToken(applicationCustomerReference.toString());
        assertNotNull(customerAccessToken.getToken());
        assertTrue(customerAccessToken.getToken().length() > 20);
        assertNotNull(customerAccessToken.getId());
    }

    @Test
    public void testCreateCustomerAccessTokenWithIdempotency() {
        CustomerAccessTokenCreationQuery customerAccessTokenCreationQuery =
                CustomerAccessTokenCreationQuery.builder()
                        .applicationCustomerReference(UUID.randomUUID().toString())
                        .idempotencyKey(UUID.randomUUID())
                        .build();

        CustomerAccessToken customerAccessToken = customerAccessTokensService.create(customerAccessTokenCreationQuery);
        assertNotNull(customerAccessToken.getToken());
        assertTrue(customerAccessToken.getToken().length() > 20);
        assertNotNull(customerAccessToken.getId());
    }
}
