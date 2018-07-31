package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.AbstractServiceTest;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SandboxFinancialInstitutionsServiceTest extends AbstractServiceTest {

    @Test
    public void testCreateFinancialInstitution() {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
        assertNotNull(newFinancialInstitution.getId());
        assertEquals(name, newFinancialInstitution.getName());
        assertTrue(newFinancialInstitution.isSandbox());
        sandboxFinancialInstitutionsService.delete(newFinancialInstitution.getId());
    }

    @Test
    public void testCreateFinancialInstitutionIdempotency() {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(UUID.randomUUID());
        assertNotNull(newFinancialInstitution.getId());
        assertEquals(name, newFinancialInstitution.getName());
        assertTrue(newFinancialInstitution.isSandbox());
        sandboxFinancialInstitutionsService.delete(newFinancialInstitution.getId());
    }

    @Test
    public void testUpdateFinancialInstitution() {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
        newFinancialInstitution.setName(newFinancialInstitution.getName()+"-UPDATED");
        FinancialInstitution updatedFinancialInstitution = sandboxFinancialInstitutionsService.update(newFinancialInstitution, UUID.randomUUID());
        assertEquals(newFinancialInstitution.getName(), updatedFinancialInstitution.getName());
        assertEquals(newFinancialInstitution.getId(), updatedFinancialInstitution.getId());
        assertEquals(newFinancialInstitution.isSandbox(), updatedFinancialInstitution.isSandbox());
        sandboxFinancialInstitutionsService.delete(updatedFinancialInstitution.getId());
    }

    @Test
    public void testUpdateFinancialInstitutionIdempotency() {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
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
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
        sandboxFinancialInstitutionsService.delete(newFinancialInstitution.getId());
        try {
            sandboxFinancialInstitutionsService.find(newFinancialInstitution.getId());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }

    @Test
    public void testDeleteFinancialInstitutionWithWrongId() {
        try {
            sandboxFinancialInstitutionsService.delete(UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }

    @Test
    public void testGetFinancialInstitution() {
        FinancialInstitution newFinancialInstitution = createFinancialInstitution(null);
        FinancialInstitution getFinancialInstitution = sandboxFinancialInstitutionsService.find(newFinancialInstitution.getId());
        assertEquals(newFinancialInstitution, getFinancialInstitution);
        sandboxFinancialInstitutionsService.delete(newFinancialInstitution.getId());
    }

    @Test
    public void testGetFinancialInstitutionUnknownID() {
        try {
            sandboxFinancialInstitutionsService.find(UUID.randomUUID());
            fail("Should raise ApiErrorsException");
        } catch (ApiErrorsException apiErrorsException) {
            assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
            assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
        }
    }
}
