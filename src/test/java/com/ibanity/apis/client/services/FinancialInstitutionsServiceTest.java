package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.helpers.DockerHelper;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionReadQuery;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionsReadQuery;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FinancialInstitutionsServiceTest extends AbstractServiceTest {

    private final FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();

    @BeforeAll
    public static void dockerPull() throws Exception{
        DockerHelper.pullImage();
    }

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
        List<FinancialInstitution> financialInstitutionsList =
                financialInstitutionsService.list(FinancialInstitutionsReadQuery.builder().build());
        assertTrue(financialInstitutionsList.size() > 0);
    }

    @Test
    public void testGetFinancialInstitutionsCustomerAccessToken() {
        FinancialInstitutionsReadQuery financialInstitutionsReadQuery =
                FinancialInstitutionsReadQuery.builder()
                        .customerAccessToken(generatedCustomerAccessToken.getToken())
                        .build();

        AccountInformationAccessRequest accountInformationAccessRequest = createAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.list(financialInstitutionsReadQuery);
        assertTrue(financialInstitutionsList.size() > 0);
    }

    @Test
    public void testGetFinancialInstitutionsCustomerAccessTokenIbanityPagingSpec() {
        AccountInformationAccessRequest accountInformationAccessRequest = createAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec ibanityPagingSpec = new IbanityPagingSpec();
        ibanityPagingSpec.setLimit(1L);

        FinancialInstitutionsReadQuery financialInstitutionsReadQuery =
                FinancialInstitutionsReadQuery.builder()
                        .customerAccessToken(generatedCustomerAccessToken.getToken())
                        .pagingSpec(ibanityPagingSpec)
                        .build();

        List<FinancialInstitution> financialInstitutionsList =
                financialInstitutionsService.list(financialInstitutionsReadQuery);
        assertEquals(1, financialInstitutionsList.size());
    }

    @Test
    public void testGetFinancialInstitutionsPagingSpec() {
        IbanityPagingSpec ibanityPagingSpec = new IbanityPagingSpec();
        ibanityPagingSpec.setLimit(3L);
        List<FinancialInstitution> createdFinancialInstitutionsList = new ArrayList<>();
        for (int index = 0; index < 5; index++) {
            createdFinancialInstitutionsList.add(createFinancialInstitution());
        }

        FinancialInstitutionsReadQuery financialInstitutionsReadQuery =
                FinancialInstitutionsReadQuery.builder()
                        .pagingSpec(ibanityPagingSpec)
                        .build();

        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.list(financialInstitutionsReadQuery);
        assertEquals(3, financialInstitutionsList.size());

        for (FinancialInstitution financialInstitution : createdFinancialInstitutionsList) {
            deleteFinancialInstitution(financialInstitution.getId());
        }
    }

    @Test
    public void testGetFinancialInstitution() {
        FinancialInstitution financialInstitutionReceived = financialInstitutionsService.find(
                FinancialInstitutionReadQuery.builder().financialInstitutionId(financialInstitution.getId()).build());
        assertEquals(financialInstitutionReceived.getId(), financialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionWrongId() {
        try {
            financialInstitutionsService.find(FinancialInstitutionReadQuery.builder().financialInstitutionId(UUID.randomUUID()).build());
            fail("Expected financialInstitutionsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }
}
