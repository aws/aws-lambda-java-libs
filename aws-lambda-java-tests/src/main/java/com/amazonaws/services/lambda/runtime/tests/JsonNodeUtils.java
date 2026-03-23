package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.JsonNode;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

/**
 * Utility methods for working with shaded Jackson {@link JsonNode} trees.
 *
 * <p>
 * Package-private — not part of the public API.
 * </p>
 */
class JsonNodeUtils {

    private static final Pattern ISO_DATE_REGEX = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T.+");

    private JsonNodeUtils() {
    }

    /**
     * Recursively removes all fields whose value is {@code null} from the
     * tree. This mirrors the serializer's {@code Include.NON_NULL} behaviour
     * so that explicit nulls in the fixture don't cause false-positive diffs.
     */
    static JsonNode stripNulls(JsonNode node) {
        if (node.isObject()) {
            ObjectNode obj = (ObjectNode) node;
            Iterator<String> fieldNames = obj.fieldNames();
            while (fieldNames.hasNext()) {
                String field = fieldNames.next();
                if (obj.get(field).isNull()) {
                    fieldNames.remove();
                } else {
                    stripNulls(obj.get(field));
                }
            }
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                stripNulls(element);
            }
        }
        return node;
    }

    /**
     * Recursively walks both trees and collects human-readable diff lines.
     */
    static void diffNodes(String path, JsonNode expected, JsonNode actual, List<String> diffs) {
        if (expected.equals(actual))
            return;

        // Compares two datetime strings by parsed instant, because DateTimeModule
        // normalizes the format on serialization (e.g. "+0000" → "Z", "Z" → ".000Z")
        if (areSameDateTime(expected.textValue(), actual.textValue())) {
            return;
        }

        if (expected.isObject() && actual.isObject()) {
            TreeSet<String> allKeys = new TreeSet<>();
            expected.fieldNames().forEachRemaining(allKeys::add);
            actual.fieldNames().forEachRemaining(allKeys::add);
            for (String key : allKeys) {
                diffChild(path + "." + key, expected.get(key), actual.get(key), diffs);
            }
        } else if (expected.isArray() && actual.isArray()) {
            for (int i = 0; i < Math.max(expected.size(), actual.size()); i++) {
                diffChild(path + "[" + i + "]", expected.get(i), actual.get(i), diffs);
            }
        } else {
            diffs.add("CHANGED " + path + " : " + summarize(expected) + " -> " + summarize(actual));
        }
    }

    /**
     * Compares two strings by parsed instant when both look like ISO-8601 dates,
     * because DateTimeModule normalizes format on serialization
     * (e.g. "+0000" → "Z", "Z" → ".000Z").
     */
    private static boolean areSameDateTime(String expected, String actual) {
        if (expected == null || actual == null
                || !ISO_DATE_REGEX.matcher(expected).matches()
                || !ISO_DATE_REGEX.matcher(actual).matches()) {

            return DateTime.parse(expected).equals(DateTime.parse(actual));
        }
        return false;

    }

    private static void diffChild(String path, JsonNode expected, JsonNode actual, List<String> diffs) {
        if (expected == null)
            diffs.add("ADDED   " + path + " = " + summarize(actual));
        else if (actual == null)
            diffs.add("MISSING " + path + " (was " + summarize(expected) + ")");
        else
            diffNodes(path, expected, actual, diffs);
    }

    private static String summarize(JsonNode node) {
        if (node == null) {
            return "<absent>";
        }
        String text = node.toString();
        return text.length() > 80 ? text.substring(0, 77) + "..." : text;
    }
}
