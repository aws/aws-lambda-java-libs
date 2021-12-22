package com.amazonaws.services.lambda.runtime.events;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HttpHeadersUtilTest {

    @Test
    public void testWhenFromObjNullThenNullReturned() {
        Map<String, String> result = HttpHeaders.mergeOrReplace(null);
        assertNull(result);
    }

    @Test
    public void testWhenFromObjEmptyThenEmptyHttpHeadersReturned() {
        Map<String, String> from = new HashMap<>();
        Map<String, String> result = HttpHeaders.mergeOrReplace(from);

        assertNotNull(result);
        assertInstanceOf(HttpHeaders.class, result);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testWhenFromObjIsHashmapThenNewHttpHeadersReturned() {
        HashMap<String, String> from = createHashMap("key", "value");
        Map<String, String> result = HttpHeaders.mergeOrReplace(from);

        assertNotNull(result);
        assertInstanceOf(HttpHeaders.class, result);
        assertEquals(from, result);
    }

    @Test
    public void testWhenFromObjIsHashmapMultiValueThenNewHttpHeadersReturned() {
        HashMap<String, String> from = createHashMap(
                "key", "value",
                "key2", "value2",
                "key3", "value3"
        );
        Map<String, String> result = HttpHeaders.mergeOrReplace(from);

        assertNotNull(result);
        assertInstanceOf(HttpHeaders.class, result);
        assertEquals(from, result);
    }

    @Test
    public void testWhenFromObjIsTreemapMultiValueThenNewHttpHeadersReturned() {
        TreeMap<String, String> from = createTreeMap(
                "key", "value",
                "key2", "value2",
                "key3", "value3"
        );
        Map<String, String> result = HttpHeaders.mergeOrReplace(from);

        assertNotNull(result);
        assertInstanceOf(HttpHeaders.class, result);
        assertEquals(from, result);
    }

    @Test
    public void testWhenFromObjIsDuplicatedKeyMultiValueTreeMapThenReturnedHttpHeadersContainOnlyFinalValue() {
        TreeMap<String, String> from = createTreeMap(
                "key", "value",
                "KEY", "value2",
                "kEy", "value3",
                Collections.reverseOrder()
        );
        Map<String, String> result = HttpHeaders.mergeOrReplace(from);

        assertNotNull(result);
        assertInstanceOf(HttpHeaders.class, result);
        assertEquals(1, result.size());
        Map.Entry<String, String> lastEntry = from.lastEntry();
        assertEquals(result.getOrDefault(lastEntry.getKey(), null), lastEntry.getValue());
    }

    @Test
    public void testWhenFromObjIsDuplicatedKeyMultiValueHashMapThenReturnedHttpHeadersContainOnlyOneValue() {
        HashMap<String, String> from = createHashMap(
                "key", "value",
                "KEY", "value2",
                "kEy", "value3"
        );
        Map<String, String> result = HttpHeaders.mergeOrReplace(from);

        assertNotNull(result);
        assertInstanceOf(HttpHeaders.class, result);
        assertEquals(1, result.size());

        Map.Entry<String, String> entry = from.entrySet().iterator().next();
        assertTrue(from.entrySet().stream()
                .anyMatch(e -> entry.getKey().equals(e.getKey()) && entry.getValue().equals(e.getValue())));
    }

    @Test
    public void testWhenFromObjAlreadyHttpHeadersThenSameInstanceReturned() {
        Map<String, String> from = new HttpHeaders<>();
        from.put("key", "value");

        Map<String, String> result = HttpHeaders.mergeOrReplace(from);
        assertSame(from, result);
    }

    @Test
    public void testWhenFromObjAlreadyEmptyHttpHeadersThenSameInstanceReturned() {
        Map<String, String> from = new HttpHeaders<>();

        Map<String, String> result = HttpHeaders.mergeOrReplace(from);
        assertSame(from, result);
    }

    private static <T> HashMap<String, T> createHashMap(String key1, T val1) {
        HashMap<String, T> map = new HashMap<>();
        map.put(key1, val1);
        return map;
    }

    private static <T> HashMap<String, T> createHashMap(String key1, T val1, String key2, T val2, String key3, T val3) {
        HashMap<String, T> map = new HashMap<>();
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
        return map;
    }

    private static <T> TreeMap<String, T> createTreeMap(String key1, T val1, String key2, T val2, String key3, T val3) {
        TreeMap<String, T> map = new TreeMap<>();
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
        return map;
    }

    private static <T> TreeMap<String, T> createTreeMap(String key1, T val1, String key2, T val2, String key3, T val3, java.util.Comparator<T> ordering) {
        TreeMap<String, T> map = new TreeMap<>();
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
        return map;
    }
}