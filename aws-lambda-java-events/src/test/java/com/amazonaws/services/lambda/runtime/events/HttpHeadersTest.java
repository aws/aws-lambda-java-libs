package com.amazonaws.services.lambda.runtime.events;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HttpHeadersTest {

    @Test
    public void testHttpHeadersCanBeInit() {
        HttpHeaders<String> obj = new HttpHeaders<>();
    }

    @Test
    public void testValueCanBeAddedToHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        assertEquals(1, obj.size());
        assertEquals(obj.getOrDefault("key", null), "value");
    }

    @Test
    public void testValuesCanBeAddedToHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");
        obj.put("key3", "value3");

        assertEquals(3, obj.size());
        assertEquals(obj.getOrDefault("key", null), "value");
        assertEquals(obj.getOrDefault("key2", null), "value2");
        assertEquals(obj.getOrDefault("key3", null), "value3");
    }

    @Test
    public void testOverridingHttpHeadersKeysIsCaseInsensitive() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("KEY", "value2");
        obj.put("kEy", "value3");

        assertEquals(1, obj.size());
        assertEquals(obj.getOrDefault("key", null), "value3");
    }

    @Test
    public void testValuesCanBeRemovedFromHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");


        assertEquals(1, obj.size());
        obj.remove("key");
        assertEquals(Collections.emptyMap(), obj);
    }

    @Test
    public void testContainedKeysAreContainedInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");

        assertTrue(obj.containsKey("key"));
    }

    @Test
    public void testContainedValuesAreContainedInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");

        assertTrue(obj.containsValue("value"));
    }

    @Test
    public void testPutAllWorksInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");

        Map<String, String> otherMap = new HashMap<>();
        otherMap.put("otherKey1", "otherVal1");
        otherMap.put("otherKey2", "otherVal2");
        otherMap.put("otherKey3", "otherVal3");

        obj.putAll(otherMap);

        assertEquals(5, obj.size());
        assertEquals(obj.getOrDefault("key", null), "value");
        assertEquals(obj.getOrDefault("key2", null), "value2");
        assertEquals(obj.getOrDefault("otherKey1", null), "otherVal1");
        assertEquals(obj.getOrDefault("otherKey2", null), "otherVal2");
        assertEquals(obj.getOrDefault("otherKey3", null), "otherVal3");
    }

    @Test
    public void testClearWorksInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");

        assertEquals(2, obj.size());

        obj.clear();

        assertEquals(0, obj.size());
    }

    @Test
    public void testKeySetWorksInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");

        Set<String> keySet = obj.keySet();

        assertNotNull(keySet);
        assertEquals(2, keySet.size());
        assertTrue(keySet.stream().allMatch(k -> k.equals("key") || k.equals("key2")));
    }

    @Test
    public void testValuesWorksInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");

        Collection<String> values = obj.values();

        assertNotNull(values);
        assertEquals(2, values.size());
        assertTrue(values.stream().allMatch(v -> v.equals("value") || v.equals("value2")));
    }

    @Test
    public void testEntrySetWorksInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");

        Set<Map.Entry<String, String>> entrySet = obj.entrySet();

        assertNotNull(entrySet);
        assertEquals(2, entrySet.size());


        assertTrue(entrySet.stream()
                .allMatch(kvp -> (kvp.getKey().equals("key") && kvp.getValue().equals("value"))
                                || (kvp.getKey().equals("key2") && kvp.getValue().equals("value2"))));
    }
}
