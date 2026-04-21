/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.JsonNode;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonNodeUtilsTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // --- stripNulls ---

    @Test
    void stripNulls_removesTopLevelNulls() throws Exception {
        JsonNode node = MAPPER.readTree("{\"a\":1,\"b\":null,\"c\":\"hello\"}");
        JsonNode result = JsonNodeUtils.stripNulls(node);
        assertEquals(MAPPER.readTree("{\"a\":1,\"c\":\"hello\"}"), result);
    }

    @Test
    void stripNulls_removesNestedNulls() throws Exception {
        JsonNode node = MAPPER.readTree("{\"outer\":{\"keep\":true,\"drop\":null}}");
        JsonNode result = JsonNodeUtils.stripNulls(node);
        assertEquals(MAPPER.readTree("{\"outer\":{\"keep\":true}}"), result);
    }

    @Test
    void stripNulls_leavesArrayElementsIntact() throws Exception {
        // Nulls inside arrays are kept (they're positional)
        JsonNode node = MAPPER.readTree("{\"arr\":[1,null,3]}");
        JsonNode result = JsonNodeUtils.stripNulls(node);
        assertEquals(MAPPER.readTree("{\"arr\":[1,null,3]}"), result);
    }

    @Test
    void stripNulls_removesNullsInsideArrayObjects() throws Exception {
        JsonNode node = MAPPER.readTree("[{\"a\":1,\"b\":null},{\"c\":null}]");
        JsonNode result = JsonNodeUtils.stripNulls(node);
        assertEquals(MAPPER.readTree("[{\"a\":1},{}]"), result);
    }

    @Test
    void stripNulls_noOpOnCleanTree() throws Exception {
        JsonNode node = MAPPER.readTree("{\"a\":1,\"b\":\"two\"}");
        JsonNode result = JsonNodeUtils.stripNulls(node);
        assertEquals(MAPPER.readTree("{\"a\":1,\"b\":\"two\"}"), result);
    }

    // --- diffNodes ---

    @Test
    void diffNodes_identicalTrees_noDiffs() throws Exception {
        JsonNode a = MAPPER.readTree("{\"x\":1,\"y\":\"hello\"}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", a, a.deepCopy(), diffs);
        assertTrue(diffs.isEmpty());
    }

    @Test
    void diffNodes_changedValue() throws Exception {
        JsonNode expected = MAPPER.readTree("{\"x\":1}");
        JsonNode actual = MAPPER.readTree("{\"x\":2}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", expected, actual, diffs);
        assertEquals(1, diffs.size());
        assertTrue(diffs.get(0).startsWith("CHANGED .x"));
    }

    @Test
    void diffNodes_missingField() throws Exception {
        JsonNode expected = MAPPER.readTree("{\"a\":1,\"b\":2}");
        JsonNode actual = MAPPER.readTree("{\"a\":1}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", expected, actual, diffs);
        assertEquals(1, diffs.size());
        assertTrue(diffs.get(0).contains("MISSING") && diffs.get(0).contains(".b"), "got: " + diffs.get(0));
    }

    @Test
    void diffNodes_addedField() throws Exception {
        JsonNode expected = MAPPER.readTree("{\"a\":1}");
        JsonNode actual = MAPPER.readTree("{\"a\":1,\"b\":2}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", expected, actual, diffs);
        assertEquals(1, diffs.size(), "diffs: " + diffs);
        assertTrue(diffs.get(0).contains("ADDED") && diffs.get(0).contains(".b"), "got: " + diffs.get(0));
    }

    @Test
    void diffNodes_nestedObjectDiff() throws Exception {
        JsonNode expected = MAPPER.readTree("{\"outer\":{\"inner\":\"old\"}}");
        JsonNode actual = MAPPER.readTree("{\"outer\":{\"inner\":\"new\"}}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", expected, actual, diffs);
        assertEquals(1, diffs.size());
        assertTrue(diffs.get(0).contains(".outer.inner"));
    }

    @Test
    void diffNodes_arrayElementDiff() throws Exception {
        JsonNode expected = MAPPER.readTree("{\"arr\":[1,2,3]}");
        JsonNode actual = MAPPER.readTree("{\"arr\":[1,99,3]}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", expected, actual, diffs);
        assertEquals(1, diffs.size());
        assertTrue(diffs.get(0).contains("[1]"));
    }

    @Test
    void diffNodes_arrayLengthMismatch() throws Exception {
        JsonNode expected = MAPPER.readTree("{\"arr\":[1,2]}");
        JsonNode actual = MAPPER.readTree("{\"arr\":[1,2,3]}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", expected, actual, diffs);
        assertEquals(1, diffs.size());
        assertTrue(diffs.get(0).contains("ADDED"), "got: " + diffs.get(0));
    }

    // --- areSameDateTime (tested indirectly via diffNodes) ---

    @Test
    void diffNodes_equivalentDateTimes_noDiff() throws Exception {
        // "+0000" vs "Z" — same instant, different format
        JsonNode expected = MAPPER.readTree("{\"t\":\"2020-03-12T19:03:58.000+0000\"}");
        JsonNode actual = MAPPER.readTree("{\"t\":\"2020-03-12T19:03:58.000Z\"}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", expected, actual, diffs);
        assertTrue(diffs.isEmpty(), "Expected no diffs for equivalent datetimes, got: " + diffs);
    }

    @Test
    void diffNodes_differentDateTimes_hasDiff() throws Exception {
        JsonNode expected = MAPPER.readTree("{\"t\":\"2020-03-12T19:03:58.000Z\"}");
        JsonNode actual = MAPPER.readTree("{\"t\":\"2021-01-01T00:00:00.000Z\"}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", expected, actual, diffs);
        assertEquals(1, diffs.size());
    }

    @Test
    void diffNodes_nonDateStrings_notTreatedAsDates() throws Exception {
        JsonNode expected = MAPPER.readTree("{\"s\":\"hello\"}");
        JsonNode actual = MAPPER.readTree("{\"s\":\"world\"}");
        List<String> diffs = new ArrayList<>();
        JsonNodeUtils.diffNodes("", expected, actual, diffs);
        assertEquals(1, diffs.size());
        assertTrue(diffs.get(0).startsWith("CHANGED .s"));
    }
}
