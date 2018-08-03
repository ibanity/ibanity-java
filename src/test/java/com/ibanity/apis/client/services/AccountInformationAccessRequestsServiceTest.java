package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.create.AccountInformationAccessRequestCreationQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class AccountInformationAccessRequestsServiceTest extends AbstractServiceTest {

    @BeforeEach
    void setUp() {
        initPublicAPIEnvironment();
    }

    @AfterEach
    void tearDown() {
        exitPublicApiEnvironment();
    }

    @Test
    void testCreateForFinancialInstitution() {
        AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery =
                AccountInformationAccessRequestCreationQuery.builder()
                        .customerAccessToken(generatedCustomerAccessToken.getToken())
                        .financialInstitutionId(financialInstitution.getId())
                        .redirectURI(fakeTppAccountInformationAccessRedirectUrl)
                        .consentReference(UUID.randomUUID().toString())
                        .build();

        AccountInformationAccessRequest accountInformationAccessRequest =
                accountInformationAccessRequestsService.create(accountInformationAccessRequestCreationQuery);

        assertNotNull(accountInformationAccessRequest.getLinks().getRedirect());
        assertNotNull(URI.create(accountInformationAccessRequest.getLinks().getRedirect()));
    }

    @Test
    void testCreateForFinancialInstitutionWithIdempotency() {
        AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery =
                AccountInformationAccessRequestCreationQuery.builder()
                        .customerAccessToken(generatedCustomerAccessToken.getToken())
                        .financialInstitutionId(financialInstitution.getId())
                        .redirectURI(fakeTppAccountInformationAccessRedirectUrl)
                        .consentReference(UUID.randomUUID().toString())
                        .idempotencyKey(UUID.randomUUID())
                        .build();

        AccountInformationAccessRequest accountInformationAccessRequest =
                accountInformationAccessRequestsService.create(accountInformationAccessRequestCreationQuery);
        assertNotNull(accountInformationAccessRequest.getLinks().getRedirect());
        assertNotNull(URI.create(accountInformationAccessRequest.getLinks().getRedirect()));
    }

    @Test
    void testCreateForUnknownFinancialInstitution() {
        AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery =
                AccountInformationAccessRequestCreationQuery.builder()
                        .customerAccessToken(generatedCustomerAccessToken.getToken())
                        .financialInstitutionId(UUID.randomUUID())
                        .redirectURI(fakeTppAccountInformationAccessRedirectUrl)
                        .consentReference(UUID.randomUUID().toString())
                        .build();

        try {
            accountInformationAccessRequestsService.create(accountInformationAccessRequestCreationQuery);
            fail("Expected accountInformationAccessRequestsService.create to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }
}