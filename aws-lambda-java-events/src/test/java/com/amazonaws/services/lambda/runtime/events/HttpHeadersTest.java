package com.amazonaws.services.lambda.runtime.events;

import com.amazonaws.services.lambda.runtime.events.models.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpHeadersTest {

    @Test
    public void testHttpHeadersCanBeInit() {
        HttpHeaders<String> obj = new HttpHeaders<>();
    }

    @Test
    public void testValueCanBeAddedToHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        assertThat(obj).hasSize(1);
        assertThat(obj).containsEntry("key", "value");
    }

    @Test
    public void testValuesCanBeAddedToHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");
        obj.put("key3", "value3");

        assertThat(obj).hasSize(3);
        assertThat(obj).containsEntry("key", "value");
        assertThat(obj).containsEntry("key2", "value2");
        assertThat(obj).containsEntry("key3", "value3");
    }

    @Test
    public void testOverridingHttpHeadersKeysIsCaseInsensitive() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("KEY", "value2");
        obj.put("kEy", "value3");

        assertThat(obj).hasSize(1);
        assertThat(obj).containsEntry("key", "value3");
    }

    @Test
    public void testValuesCanBeRemovedFromHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");

        assertThat(obj).hasSize(1);
        obj.remove("key");
        assertThat(obj).isEmpty();
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

        assertThat(obj).hasSize(5);
        assertThat(obj).containsEntry("key", "value");
        assertThat(obj).containsEntry("key2", "value2");
        assertThat(obj).containsEntry("otherKey1", "otherVal1");
        assertThat(obj).containsEntry("otherKey2", "otherVal2");
        assertThat(obj).containsEntry("otherKey3", "otherVal3");
    }

    @Test
    public void testClearWorksInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");

        assertThat(obj).hasSize(2);

        obj.clear();

        assertThat(obj).hasSize(0);
    }

    @Test
    public void testKeySetWorksInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");

        Set<String> keySet = obj.keySet();

        assertNotNull(keySet);
        assertThat(keySet).hasSize(2);
        assertThat(keySet).contains("key", "key2");
    }

    @Test
    public void testValuesWorksInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");

        Collection<String> values = obj.values();

        assertNotNull(values);
        assertThat(values).hasSize(2);
        assertThat(values).contains("value", "value2");
    }

    @Test
    public void testEntrySetWorksInHttpHeaders() {
        HttpHeaders<String> obj = new HttpHeaders<>();
        obj.put("key", "value");
        obj.put("key2", "value2");

        Set<Map.Entry<String, String>> entrySet = obj.entrySet();

        assertNotNull(entrySet);
        assertThat(entrySet).hasSize(2);
        assertThat(entrySet).contains(new AbstractMap.SimpleEntry<>("key", "value"));
        assertThat(entrySet).contains(new AbstractMap.SimpleEntry<>("key2", "value2"));
    }
}
