package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SandboxFinancialInstitutionsServiceTest extends AbstractServiceTest {

    @Test
    public void testCreateFinancialInstitution() {
        String financialInstitutionName = generateFinancialInstitutionName();

        FinancialInstitution newFinancialInstitution = createFinancialInstitution(
                financialInstitutionName, null);
        assertNotNull(newFinancialInstitution.getId());
        assertEquals(financialInstitutionName, newFinancialInstitution.getName());
        assertTrue(newFinancialInstitution.isSandbox());
        sandboxFinancialInstitutionsService.delete(newFinancialInstitution.getId());
    }

    @Test
    public void testCreateFinancialInstitutionIdempotency() {
        String financialInstitutionName = generateFinancialInstitutionName();

        FinancialInstitution newFinancialInstitution = createFinancialInstitution(financialInstitutionName);
        assertNotNull(newFinancialInstitution.getId());
        assertEquals(financialInstitutionName, newFinancialInstitution.getName());
        assertTrue(newFinancialInstitution.isSandbox());
        sandboxFinancialInstitutionsService.delete(newFinancialInstitution.getId());
    }

    @Test
    public void testUpdateFinancialInstitution() {
        String financialInstitutionName = generateFinancialInstitutionName();

        FinancialInstitution newFinancialInstitution = createFinancialInstitution(financialInstitutionName);
        newFinancialInstitution.setName(newFinancialInstitution.getName()+"-UPDATED");
        FinancialInstitution updatedFinancialInstitution = sandboxFinancialInstitutionsService.update(newFinancialInstitution, UUID.randomUUID());
        assertEquals(newFinancialInstitution.getName(), updatedFinancialInstitution.getName());
        assertEquals(newFinancialInstitution.getId(), updatedFinancialInstitution.getId());
        assertEquals(newFinancialInstitution.isSandbox(), updatedFinancialInstitution.isSandbox());
        sandboxFinancialInstitutionsService.delete(updatedFinancialInstitution.getId());
    }

    @Test
    public void testUpdateFinancialInstitutionIdempotency() {
        String financialInstitutionName = generateFinancialInstitutionName();

        FinancialInstitution newFinancialInstitution = createFinancialInstitution(financialInstitutionName);
        newFinancialInstitution.setName(newFinancialInstitution.getName()+"-UPDATED");
        FinancialInstitution updatedFinancialInstitution = sandboxFinancialInstitutionsService.update(newFinancialInstitution, UUID.randomUUID());
        assertEquals(newFinancialInstitution.getName(), updatedFinancialInstitution.getName());
        assertEquals(newFinancialInstitution.getId(), updatedFinancialInstitution.getId());
        assertTrue(newFinancialInstitution.isSandbox());
        assertTrue(updatedFinancialInstitution.isSandbox());
        sandboxFinancialInstitutionsService.delete(updatedFinancialInstitution.getId());
    }

    @Test
    public void testDeleteFinancialInstitution() {
        String financialInstitutionName = generateFinancialInstitutionName();

        FinancialInstitution newFinancialInstitution = createFinancialInstitution(financialInstitutionName);
        sandboxFinancialInstitutionsService.delete(newFinancialInstitution.getId());
        try {
            sandboxFinancialInstitutionsService.find(newFinancialInstitution.getId());
            fail("Expected sandboxFinancialInstitutionsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException);
        }
    }

    @Test
    public void testDeleteFinancialInstitutionWithWrongId() {
        try {
            sandboxFinancialInstitutionsService.delete(UUID.randomUUID());
            fail("Expected sandboxFinancialInstitutionsService.delete to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            super.assertResourceNotFoundException(apiErrorsException);
        }
    }

    @Test
    public void testGetFinancialInstitution() {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();
        FinancialInstitution getFinancialInstitution = sandboxFinancialInstitutionsService.find(newFinancialInstitution.getId());
        assertEquals(newFinancialInstitution, getFinancialInstitution);
        sandboxFinancialInstitutionsService.delete(newFinancialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionUnknownID() {
        try {
            sandboxFinancialInstitutionsService.find(UUID.randomUUID());
            fail("Expected sandboxFinancialInstitutionsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            super.assertResourceNotFoundException(apiErrorsException);
        }
    }
}
