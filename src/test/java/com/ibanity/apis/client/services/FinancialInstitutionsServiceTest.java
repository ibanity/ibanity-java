package com.ibanity.apis.client.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FinancialInstitutionsServiceImpl Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 14, 2018</pre>
 */
public class FinancialInstitutionsServiceTest extends AbstractServiceTest {

    private FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();

    @BeforeEach
    public void before() throws Exception{
        initPublicAPIEnvironment();
    }

    @AfterEach
    public void after() throws Exception {
        cleanPublicAPIEnvironment();
    }

    /**
     * Method: getFinancialInstitutions()
     */
    @Test
    public void testGetFinancialInstitutions() {
        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.getFinancialInstitutions();
        assertTrue(financialInstitutionsList.size() > 0);
    }

    /**
     * Method: getFinancialInstitutions(CustomerAccessToken customerAccessToke)
     */
    @Test
    public void testGetFinancialInstitutionsCustomerAccessToken() {
        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.getFinancialInstitutions(generatedCustomerAccessToken);
        assertTrue(financialInstitutionsList.size() > 0);
    }

    /**
     * Method: getFinancialInstitutions(CustomerAccessToken, IbanityPagingSpec)
     */
    @Test
    public void testGetFinancialInstitutionsCustomerAccessTokenIbanityPagingSpec() {
        IbanityPagingSpec ibanityPagingSpec = new IbanityPagingSpec();
        ibanityPagingSpec.setLimit(1L);
        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.getFinancialInstitutions(generatedCustomerAccessToken, ibanityPagingSpec);
        assertTrue(financialInstitutionsList.size() == 1);
    }

    /**
     * Method: getFinancialInstitutions(IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetFinancialInstitutionsPagingSpec() throws Exception {
        IbanityPagingSpec ibanityPagingSpec = new IbanityPagingSpec();
        ibanityPagingSpec.setLimit(3L);
        List<FinancialInstitution> createdFinancialInstitutionsList = new ArrayList<>();
        for (int index = 0; index < 5; index++){
            createdFinancialInstitutionsList.add(createFinancialInstitution(null));
        }

        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.getFinancialInstitutions(ibanityPagingSpec);
        assertTrue(financialInstitutionsList.size() == 3);

        for (FinancialInstitution financialInstitution : createdFinancialInstitutionsList){
            deleteFinancialInstitution(financialInstitution.getId());
        }
    }

    /**
     * Method: getFinancialInstitution(UUID financialInstitutionId)
     */
    @Test
    public void testGetFinancialInstitution() throws Exception {
        financialInstitutionsService.getFinancialInstitution(financialInstitution.getId());
    }

    /**
     * Method: getFinancialInstitution(UUID financialInstitutionId)
     */
    @Test
    public void testGetFinancialInstitutionWrongId() throws Exception {
        try {
            financialInstitutionsService.getFinancialInstitution(UUID.randomUUID());
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }
}
