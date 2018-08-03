package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.update.FinancialInstitutionUpdateQuery;
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
        super.deleteFinancialInstitution(newFinancialInstitution.getId());
    }

    @Test
    public void testCreateFinancialInstitutionIdempotency() {
        String financialInstitutionName = generateFinancialInstitutionName();

        FinancialInstitution newFinancialInstitution = createFinancialInstitution(financialInstitutionName);
        assertNotNull(newFinancialInstitution.getId());
        assertEquals(financialInstitutionName, newFinancialInstitution.getName());
        assertTrue(newFinancialInstitution.isSandbox());

        super.deleteFinancialInstitution(newFinancialInstitution.getId());
    }

    @Test
    public void testUpdateFinancialInstitution() {
        String financialInstitutionName = generateFinancialInstitutionName();

        FinancialInstitution newFinancialInstitution = createFinancialInstitution(financialInstitutionName);

        FinancialInstitutionUpdateQuery financialInstitutionUpdateQuery =
                FinancialInstitutionUpdateQuery.from(newFinancialInstitution)
                .name(newFinancialInstitution.getName()+"-UPDATED")
                .build();

        FinancialInstitution updatedFinancialInstitution =
                sandboxFinancialInstitutionsService.update(financialInstitutionUpdateQuery);
        assertNotEquals(newFinancialInstitution.getName(), updatedFinancialInstitution.getName());
        assertEquals(newFinancialInstitution.getId(), updatedFinancialInstitution.getId());
        assertEquals(newFinancialInstitution.isSandbox(), updatedFinancialInstitution.isSandbox());

        super.deleteFinancialInstitution(updatedFinancialInstitution.getId());
    }

    @Test
    public void testUpdateFinancialInstitutionIdempotency() {
        String financialInstitutionName = generateFinancialInstitutionName();

        FinancialInstitution financialInstitution = createFinancialInstitution(financialInstitutionName);

        FinancialInstitutionUpdateQuery financialInstitutionUpdateQuery =
                FinancialInstitutionUpdateQuery.from(financialInstitution)
                        .name(financialInstitution.getName()+"-UPDATED")
                        .idempotencyKey(UUID.randomUUID())
                        .build();

        FinancialInstitution updatedFinancialInstitution =
                sandboxFinancialInstitutionsService.update(financialInstitutionUpdateQuery);
        assertNotEquals(financialInstitution.getName(), updatedFinancialInstitution.getName());
        assertEquals(financialInstitution.getId(), updatedFinancialInstitution.getId());
        assertTrue(financialInstitution.isSandbox());
        assertTrue(updatedFinancialInstitution.isSandbox());
        super.deleteFinancialInstitution(updatedFinancialInstitution.getId());
    }

    @Test
    public void testDeleteFinancialInstitution() {
        String financialInstitutionName = generateFinancialInstitutionName();

        FinancialInstitution newFinancialInstitution = createFinancialInstitution(financialInstitutionName);
        super.deleteFinancialInstitution(newFinancialInstitution.getId());
        try {
            sandboxFinancialInstitutionsService.find(
                    FinancialInstitutionReadQuery.builder()
                            .financialInstitutionId(newFinancialInstitution.getId())
                            .build());
            fail("Expected sandboxFinancialInstitutionsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }

    @Test
    public void testDeleteFinancialInstitutionWithWrongId() {
        try {
            super.deleteFinancialInstitution(UUID.randomUUID());
            fail("Expected sandboxFinancialInstitutionsService.delete to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }

    @Test
    public void testGetFinancialInstitution() {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution();

        FinancialInstitution getFinancialInstitution = sandboxFinancialInstitutionsService.find(
                FinancialInstitutionReadQuery.builder()
                        .financialInstitutionId(newFinancialInstitution.getId())
                        .build());
        assertEquals(newFinancialInstitution, getFinancialInstitution);
        super.deleteFinancialInstitution(newFinancialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionUnknownID() {
        try {
            sandboxFinancialInstitutionsService.find(
                    FinancialInstitutionReadQuery.builder()
                            .financialInstitutionId(UUID.randomUUID())
                            .build());

            fail("Expected sandboxFinancialInstitutionsService.find to raise an ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            super.assertResourceNotFoundException(apiErrorsException, FinancialInstitution.RESOURCE_TYPE);
        }
    }
}
