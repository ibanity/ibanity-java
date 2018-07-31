package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FinancialInstitutionsServiceTest extends AbstractServiceTest {

    private final FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();

    @BeforeEach
    public void before() {
        initPublicAPIEnvironment();
    }

    @AfterEach
    public void after() {
        cleanPublicAPIEnvironment();
    }

    @Test
    public void testGetFinancialInstitutions() {
        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.list();
        assertTrue(financialInstitutionsList.size() > 0);
    }

    @Test
    public void testGetFinancialInstitutionsCustomerAccessToken() {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.list(generatedCustomerAccessToken.getToken());
        assertTrue(financialInstitutionsList.size() > 0);
    }

    @Test
    public void testGetFinancialInstitutionsCustomerAccessTokenIbanityPagingSpec() {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec ibanityPagingSpec = new IbanityPagingSpec();
        ibanityPagingSpec.setLimit(1L);
        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.list(generatedCustomerAccessToken.getToken(), ibanityPagingSpec);
        assertEquals(1, financialInstitutionsList.size());
    }

    @Test
    public void testGetFinancialInstitutionsPagingSpec() {
        IbanityPagingSpec ibanityPagingSpec = new IbanityPagingSpec();
        ibanityPagingSpec.setLimit(3L);
        List<FinancialInstitution> createdFinancialInstitutionsList = new ArrayList<>();
        for (int index = 0; index < 5; index++){
            createdFinancialInstitutionsList.add(createFinancialInstitution(null));
        }

        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.list(ibanityPagingSpec);
        assertEquals(3, financialInstitutionsList.size());

        for (FinancialInstitution financialInstitution : createdFinancialInstitutionsList){
            deleteFinancialInstitution(financialInstitution.getId());
        }
    }

    @Test
    public void testGetFinancialInstitution() {
        FinancialInstitution financialInstitutionReceived = financialInstitutionsService.find(financialInstitution.getId());
        assertEquals(financialInstitutionReceived.getId(), financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionWrongId() {
        try {
            financialInstitutionsService.find(UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(apiErrorsException.getHttpStatus(), HttpStatus.SC_NOT_FOUND);
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }
}
