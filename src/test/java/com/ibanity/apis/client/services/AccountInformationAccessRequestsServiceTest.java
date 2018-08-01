package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
        AccountInformationAccessRequest accountInformationAccessRequest = accountInformationAccessRequestsService.create(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), fakeTppAccountInformationAccessRedirectUrl, UUID.randomUUID().toString());
        assertNotNull(accountInformationAccessRequest.getLinks().getRedirect());
        assertNotNull(URI.create(accountInformationAccessRequest.getLinks().getRedirect()));
    }

    @Test
    void testCreateForFinancialInstitutionWithIdempotency() {
        AccountInformationAccessRequest accountInformationAccessRequest = accountInformationAccessRequestsService.create(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), fakeTppAccountInformationAccessRedirectUrl, UUID.randomUUID().toString(), UUID.randomUUID());
        assertNotNull(accountInformationAccessRequest.getLinks().getRedirect());
        assertNotNull(URI.create(accountInformationAccessRequest.getLinks().getRedirect()));
    }

    @Test
    void testCreateForUnknownFinancialInstitution() {
        FinancialInstitution unknownFinancialInstitution = new FinancialInstitution();
        unknownFinancialInstitution.setId(UUID.randomUUID());
        try {
            AccountInformationAccessRequest accountInformationAccessRequest = accountInformationAccessRequestsService.create(generatedCustomerAccessToken.getToken(), unknownFinancialInstitution.getId(), fakeTppAccountInformationAccessRedirectUrl, UUID.randomUUID().toString());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }
}