package com.amazonaws.services.lambda.runtime.events;

import com.amazonaws.services.lambda.runtime.events.models.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(result).isInstanceOf(HttpHeaders.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testWhenFromObjIsHashmapThenNewHttpHeadersReturned() {
        HashMap<String, String> from = createHashMap("key", "value");
        Map<String, String> result = HttpHeaders.mergeOrReplace(from);

        assertNotNull(result);
        assertThat(result).isInstanceOf(HttpHeaders.class);
        assertThat(result).containsAllEntriesOf(from);
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
        assertThat(result).isInstanceOf(HttpHeaders.class);
        assertThat(result).containsAllEntriesOf(from);
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
        assertThat(result).isInstanceOf(HttpHeaders.class);
        assertThat(result).containsAllEntriesOf(from);
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
        assertThat(result).isInstanceOf(HttpHeaders.class);
        assertThat(result).hasSize(1);
        Map.Entry<String, String> lastEntry = from.lastEntry();
        assertThat(result).containsEntry(lastEntry.getKey(), lastEntry.getValue());
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
        assertThat(result).isInstanceOf(HttpHeaders.class);
        assertThat(result).hasSize(1);

        List<Map.Entry<String, String>> entries = new ArrayList<>(from.entrySet());;
        assertThat(result).containsAnyOf(entries.get(0), entries.get(1), entries.get(2));
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
