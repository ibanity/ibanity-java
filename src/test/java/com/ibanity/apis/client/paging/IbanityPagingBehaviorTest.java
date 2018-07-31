package com.ibanity.apis.client.paging;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IbanityPagingBehaviorTest {

    private static IbanityPagingSpec iBanityPagingSpec;
    private static IbanityPagingBehavior iBanityPagingBehavior;

    private static final String AFTER = "after";
    private static final String BEFORE = "before";
    private static final String LIMIT = "limit";
    private static final String LIMIT_VALUE = "10";

    @BeforeAll
    public static void beforeAll() {
        iBanityPagingBehavior = new IbanityPagingBehavior();
        iBanityPagingSpec = new IbanityPagingSpec();
        iBanityPagingSpec.setLimit(Long.valueOf(LIMIT_VALUE));
    }

    @Test
    public void testSerialize() {
        UUID afterUUID = UUID.randomUUID();
        UUID beforeUUID = UUID.randomUUID();
        iBanityPagingSpec.setAfter(afterUUID);
        iBanityPagingSpec.setBefore(beforeUUID);
        Map<String, Set<String>> values = iBanityPagingBehavior.serialize(iBanityPagingSpec, null);
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
        iBanityPagingSpec.setAfter(afterUUID);
        iBanityPagingSpec.setBefore(beforeUUID);
        Map<String, Set<String>> serializedValues = iBanityPagingBehavior.serialize(iBanityPagingSpec, null);
        IbanityPagingSpec iBanityPagingSpecResult = iBanityPagingBehavior.deserialize(serializedValues);
        assertEquals(iBanityPagingSpec, iBanityPagingSpecResult);
    }

    @Test
    public void testCreateEmptyPagingSpec() {
        IbanityPagingSpec iBanityPagingSpecResult = iBanityPagingBehavior.createEmptyPagingSpec();
        assertNull(iBanityPagingSpecResult.getAfter());
        assertNull(iBanityPagingSpecResult.getBefore());
        assertSame(iBanityPagingSpecResult.getLimit(), IbanityPagingSpec.LIMIT_DEFAULT);
    }

    @Test
    public void testCreateDefaultPagingSpec() {
        testCreateEmptyPagingSpec();
    }

    @Test
    public void testIsRequired() {
        IbanityPagingSpec iBanityPagingSpecResult = iBanityPagingBehavior.createEmptyPagingSpec();
        assertTrue(iBanityPagingBehavior.isRequired(iBanityPagingSpecResult));
    }
} 
