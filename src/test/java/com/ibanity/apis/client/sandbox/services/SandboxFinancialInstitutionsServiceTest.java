package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
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
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();
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
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();
        newFinancialInstitution.setName(newFinancialInstitution.getName()+"-UPDATED");
        FinancialInstitution updatedFinancialInstitution = sandboxFinancialInstitutionsService.updateFinancialInstitution(newFinancialInstitution);
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
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(newFinancialInstitution.getId());
        assertThrows(ResourceNotFoundException.class, () -> sandboxFinancialInstitutionsService.getFinancialInstitution(newFinancialInstitution.getId()));
    }

    @Test
    public void testDeleteFinancialInstitutionWithWrongId() throws Exception {
        assertThrows(ResourceNotFoundException.class, () -> sandboxFinancialInstitutionsService.deleteFinancialInstitution(UUID.randomUUID()));
    }

    /**
     * Method: getFinancialInstitution(UUID financialInstitutionId)
     */
    @Test
    public void testGetFinancialInstitution() throws Exception {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();
        FinancialInstitution getFinancialInstitution = sandboxFinancialInstitutionsService.getFinancialInstitution(newFinancialInstitution.getId());
        assertTrue(newFinancialInstitution.equals(getFinancialInstitution));
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(newFinancialInstitution.getId());
    }

    /**
     * Method: getFinancialInstitution(UUID financialInstitutionId)
     */
    @Test
    public void testGetFinancialInstitutionUnknownID() throws Exception {
        assertThrows( ResourceNotFoundException.class, () -> sandboxFinancialInstitutionsService.getFinancialInstitution(UUID.randomUUID()));
    }
}
