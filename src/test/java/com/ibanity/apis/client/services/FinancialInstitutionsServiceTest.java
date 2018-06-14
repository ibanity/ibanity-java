package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsServiceTest;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FinancialInstitutionsServiceImpl Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 14, 2018</pre>
 */
public class FinancialInstitutionsServiceTest {

    private FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();
    private FinancialInstitution financialInstitution;

    @BeforeEach
    public void before() {
        financialInstitution = SandboxFinancialInstitutionsServiceTest.createFinancialInstitution();
    }

    @AfterEach
    public void after() throws Exception {
        SandboxFinancialInstitutionsServiceTest.deleteFinancialInstitution(financialInstitution.getId());
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
     * Method: getFinancialInstitutions(IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetFinancialInstitutionsPagingSpec() throws Exception {
        IbanityPagingSpec ibanityPagingSpec = new IbanityPagingSpec();
        ibanityPagingSpec.setLimit(3L);
        List<FinancialInstitution> createdFinancialInstitutionsList = new ArrayList<>();
        for (int index = 0; index < 5; index++){
            createdFinancialInstitutionsList.add(SandboxFinancialInstitutionsServiceTest.createFinancialInstitution());
        }

        List<FinancialInstitution> financialInstitutionsList = financialInstitutionsService.getFinancialInstitutions(ibanityPagingSpec);
        assertTrue(financialInstitutionsList.size() == 3);

        for (FinancialInstitution financialInstitution : createdFinancialInstitutionsList){
            SandboxFinancialInstitutionsServiceTest.deleteFinancialInstitution(financialInstitution.getId());
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
        assertThrows(ResourceNotFoundException.class, () -> financialInstitutionsService.getFinancialInstitution(UUID.randomUUID()));
    }
}
