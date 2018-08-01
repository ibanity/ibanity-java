package com.ibanity.apis.client.paging;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IbanityPagingBehaviorTest {

    private static IbanityPagingSpec ibanityPagingSpec;
    private static IbanityPagingBehavior ibanityPagingBehavior;

    private static final String AFTER = "after";
    private static final String BEFORE = "before";
    private static final String LIMIT = "limit";
    private static final String LIMIT_VALUE = "10";

    @BeforeAll
    public static void beforeAll() {
        ibanityPagingBehavior = new IbanityPagingBehavior();
        ibanityPagingSpec = new IbanityPagingSpec();
        ibanityPagingSpec.setLimit(Long.valueOf(LIMIT_VALUE));
    }

    @Test
    public void testSerialize() {
        UUID afterUUID = UUID.randomUUID();
        UUID beforeUUID = UUID.randomUUID();
        ibanityPagingSpec.setAfter(afterUUID);
        ibanityPagingSpec.setBefore(beforeUUID);
        Map<String, Set<String>> values = ibanityPagingBehavior.serialize(ibanityPagingSpec, null);
        assertEquals(3, values.size());
        assertTrue(values.containsKey(BEFORE));
        assertTrue(values.containsKey(AFTER));
        assertTrue(values.containsKey(LIMIT));
        assertEquals(1, values.get(LIMIT).size());
        assertEquals(1, values.get(AFTER).size());
        assertEquals(1, values.get(BEFORE).size());
        assertTrue(values.get(AFTER).contains(afterUUID.toString()));
        assertTrue(values.get(BEFORE).contains(beforeUUID.toString()));
        assertTrue(values.get(LIMIT).contains(LIMIT_VALUE));
    }

    @Test
    public void testDeserialize() {
        UUID afterUUID = UUID.randomUUID();
        UUID beforeUUID = UUID.randomUUID();
        ibanityPagingSpec.setAfter(afterUUID);
        ibanityPagingSpec.setBefore(beforeUUID);
        Map<String, Set<String>> serializedValues = ibanityPagingBehavior.serialize(ibanityPagingSpec, null);
        assertEquals(ibanityPagingSpec, ibanityPagingBehavior.deserialize(serializedValues));
    }

    @Test
    public void testCreateEmptyPagingSpec() {
        IbanityPagingSpec ibanityPagingSpecResult = ibanityPagingBehavior.createEmptyPagingSpec();
        assertNull(ibanityPagingSpecResult.getAfter());
        assertNull(ibanityPagingSpecResult.getBefore());
        assertSame(ibanityPagingSpecResult.getLimit(), IbanityPagingSpec.DEFAULT_PAGING_SPEC_LIMIT);
    }

    @Test
    public void testCreateDefaultPagingSpec() {
        testCreateEmptyPagingSpec();
    }

    @Test
    public void testIsRequired() {
        IbanityPagingSpec ibanityPagingSpecResult = ibanityPagingBehavior.createEmptyPagingSpec();
        assertTrue(ibanityPagingBehavior.isRequired(ibanityPagingSpecResult));
    }
} 
