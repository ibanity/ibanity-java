package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountInformationAccessRequestsServiceTest extends AbstractServiceTest {

    @BeforeEach
    void setUp() throws Exception {
        initPublicAPIEnvironment();
    }

    @AfterEach
    void tearDown() throws Exception {
        exitPublicApiEnvironment();
    }

    @Test
    void testCreateForFinancialInstitution() {
        AccountInformationAccessRequest accountInformationAccessRequest = accountInformationAccessRequestsService.createForFinancialInstitution(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL, UUID.randomUUID().toString());
        assertNotNull(accountInformationAccessRequest.getLinks().getRedirect());
        assertNotNull(URI.create(accountInformationAccessRequest.getLinks().getRedirect()));
    }

    @Test
    void testCreateForUnknownFinancialInstitution() {
        FinancialInstitution unknownFinancialInstitution = new FinancialInstitution();
        unknownFinancialInstitution.setId(UUID.randomUUID());
        try {
            AccountInformationAccessRequest accountInformationAccessRequest = accountInformationAccessRequestsService.createForFinancialInstitution(generatedCustomerAccessToken.getToken(), unknownFinancialInstitution.getId(), FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL, UUID.randomUUID().toString());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertTrue(apiErrorsException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count() == 1);
        }
    }
}