package com.ibanity.apis.client.paging;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * IBanityPagingBehavior Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 12, 2018</pre>
 */
public class IBanityPagingBehaviorTest {

    IBanityPagingSpec iBanityPagingSpec;
    IBanityPagingBehavior iBanityPagingBehavior;

    private static final String AFTER = "after";
    private static final String BEFORE = "before";
    private static final String LIMIT = "limit";
    private static final String LIMIT_VALUE = "10";

    @Before
    public void before() throws Exception {
        iBanityPagingBehavior = new IBanityPagingBehavior();
        iBanityPagingSpec = new IBanityPagingSpec();
        iBanityPagingSpec.setLimit(Long.valueOf(LIMIT_VALUE));
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: serialize(final OffsetLimitPagingSpec offsetLimitPagingSpec, final String resourceType)
     */
    @Test
    public void testSerialize() throws Exception {
        UUID afterUUID = UUID.randomUUID();
        UUID beforeUUID = UUID.randomUUID();
        iBanityPagingSpec.setAfter(afterUUID);
        iBanityPagingSpec.setBefore(beforeUUID);
        Map<String, Set<String>> values = iBanityPagingBehavior.serialize(iBanityPagingSpec, null);
        assertTrue(values.size() == 3);
        assertTrue(values.containsKey(BEFORE));
        assertTrue(values.containsKey(AFTER));
        assertTrue(values.containsKey(LIMIT));
        assertTrue(values.get(LIMIT).size() == 1);
        assertTrue(values.get(AFTER).size() == 1);
        assertTrue(values.get(BEFORE).size() == 1);
        assertTrue(values.get(AFTER).contains(afterUUID.toString()));
        assertTrue(values.get(BEFORE).contains(beforeUUID.toString()));
        assertTrue(values.get(LIMIT).contains(LIMIT_VALUE));
    }

    /**
     * Method: deserialize(final Map<String, Set<String>> parameters)
     */
    @Test
    public void testDeserialize() throws Exception {
        UUID afterUUID = UUID.randomUUID();
        UUID beforeUUID = UUID.randomUUID();
        iBanityPagingSpec.setAfter(afterUUID);
        iBanityPagingSpec.setBefore(beforeUUID);
        Map<String, Set<String>> serializedValues = iBanityPagingBehavior.serialize(iBanityPagingSpec, null);
        IBanityPagingSpec iBanityPagingSpecResult = iBanityPagingBehavior.deserialize(serializedValues);
        assertTrue(iBanityPagingSpec.equals(iBanityPagingSpecResult));
    }

    /**
     * Method: createEmptyPagingSpec()
     */
    @Test
    public void testCreateEmptyPagingSpec() throws Exception {
        IBanityPagingSpec iBanityPagingSpecResult = iBanityPagingBehavior.createEmptyPagingSpec();
        assertNull(iBanityPagingSpecResult.getAfter());
        assertNull(iBanityPagingSpecResult.getBefore());
        assertTrue(iBanityPagingSpecResult.getLimit() == IBanityPagingSpec.LIMIT_DEFAULT);
    }

    /**
     * Method: createDefaultPagingSpec()
     */
    @Test
    public void testCreateDefaultPagingSpec() throws Exception {
        testCreateEmptyPagingSpec();
    }

    /**
     * Method: isRequired(final OffsetLimitPagingSpec offsetLimitPagingSpec)
     */
    @Test
    public void testIsRequired() throws Exception {
        IBanityPagingSpec iBanityPagingSpecResult = iBanityPagingBehavior.createEmptyPagingSpec();
        assertTrue(iBanityPagingBehavior.isRequired(iBanityPagingSpecResult));
    }
} 
