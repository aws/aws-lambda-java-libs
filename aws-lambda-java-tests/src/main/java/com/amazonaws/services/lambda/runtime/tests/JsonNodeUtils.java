package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.JsonNode;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * Utility methods for working with shaded Jackson {@link JsonNode} trees.
 *
 * <p>
 * Package-private — not part of the public API.
 * </p>
 */
class JsonNodeUtils {

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
