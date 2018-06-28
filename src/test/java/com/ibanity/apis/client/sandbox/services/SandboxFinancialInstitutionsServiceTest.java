package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SandboxFinancialInstitutionsServiceImpl Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 13, 2018</pre>
 */
public class SandboxFinancialInstitutionsServiceTest extends AbstractServiceTest {

    @BeforeAll
    public static void beforeAll()  {
    }

    @AfterAll
    public static void afterAll()  {
    }

    /**
     * Method: createFinancialInstitution(FinancialInstitution financialInstitution)
     */
    @Test
    public void testCreateFinancialInstitution() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
        assertTrue(newFinancialInstitution.getId() != null);
        assertTrue(name.equals(newFinancialInstitution.getName()));
        assertTrue(newFinancialInstitution.getSandbox());
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(newFinancialInstitution.getId());
    }

    @Test
    public void testCreateFinancialInstitutionIdempotency() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(UUID.randomUUID());
        assertTrue(newFinancialInstitution.getId() != null);
        assertTrue(name.equals(newFinancialInstitution.getName()));
        assertTrue(newFinancialInstitution.getSandbox());
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(newFinancialInstitution.getId());
    }
    /**
     * Method: updateFinancialInstitution(FinancialInstitution financialInstitution)
     */
    @Test
    public void testUpdateFinancialInstitution() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
        newFinancialInstitution.setName(newFinancialInstitution.getName()+"-UPDATED");
        FinancialInstitution updatedFinancialInstitution = sandboxFinancialInstitutionsService.updateFinancialInstitution(newFinancialInstitution);
        assertTrue((newFinancialInstitution.getName()).equals(updatedFinancialInstitution.getName()));
        assertTrue(newFinancialInstitution.getId().equals(updatedFinancialInstitution.getId()));
        assertTrue(newFinancialInstitution.getSandbox().equals(updatedFinancialInstitution.getSandbox()));
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(updatedFinancialInstitution.getId());
    }

    @Test
    public void testUpdateFinancialInstitutionIdempotency() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
        newFinancialInstitution.setName(newFinancialInstitution.getName()+"-UPDATED");
        FinancialInstitution updatedFinancialInstitution = sandboxFinancialInstitutionsService.updateFinancialInstitution(newFinancialInstitution, UUID.randomUUID());
        assertTrue((newFinancialInstitution.getName()).equals(updatedFinancialInstitution.getName()));
        assertTrue(newFinancialInstitution.getId().equals(updatedFinancialInstitution.getId()));
        assertTrue(newFinancialInstitution.getSandbox().equals(updatedFinancialInstitution.getSandbox()));
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(updatedFinancialInstitution.getId());
    }

    /**
     * Method: deleteFinancialInstitution(UUID financialInstitutionId)
     */
    @Test
    public void testDeleteFinancialInstitution() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(newFinancialInstitution.getId());
        try {
            sandboxFinancialInstitutionsService.getFinancialInstitution(newFinancialInstitution.getId());
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }

    @Test
    public void testDeleteFinancialInstitutionWithWrongId() throws Exception {
        assertThrows(ApiErrorsException.class, () -> sandboxFinancialInstitutionsService.deleteFinancialInstitution(UUID.randomUUID()));
    }

    /**
     * Method: getFinancialInstitution(UUID financialInstitutionId)
     */
    @Test
    public void testGetFinancialInstitution() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
        FinancialInstitution getFinancialInstitution = sandboxFinancialInstitutionsService.getFinancialInstitution(newFinancialInstitution.getId());
        assertTrue(newFinancialInstitution.equals(getFinancialInstitution));
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(newFinancialInstitution.getId());
    }

    /**
     * Method: getFinancialInstitution(UUID financialInstitutionId)
     */
    @Test
    public void testGetFinancialInstitutionUnknownID() throws Exception {
        try {
            sandboxFinancialInstitutionsService.getFinancialInstitution(UUID.randomUUID());
        } catch (ApiErrorsException ibanityException) {
            assertTrue(ibanityException.getHttpStatus() == HttpStatus.SC_NOT_FOUND);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count() == 1);
            assertTrue(ibanityException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals("financialInstitution")).count() == 1);
        }
    }
}
