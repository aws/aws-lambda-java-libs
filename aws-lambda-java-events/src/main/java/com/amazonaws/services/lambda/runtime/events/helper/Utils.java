package com.amazonaws.services.lambda.runtime.events.helper;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * This class exposes some utility methods to work with request values such as headers
 * and query string parameters.
 */
public class Utils {

    static final String HEADER_KEY_VALUE_SEPARATOR = "=";
    static final String HEADER_VALUE_SEPARATOR = ";";
    static final String HEADER_QUALIFIER_SEPARATOR = ",";
    static final String ENCODING_VALUE_KEY = "charset";


    //-------------------------------------------------------------
    // Methods
    //-------------------------------------------------------------

    /**
     * Given a map of key/values query string parameters from API Gateway, creates a query string as it would have
     * been in the original url.
     * @param parameters A Map&lt;String, String&gt; of query string parameters
     * @param encode Whether the key and values should be URL encoded
     * @param encodeCharset Charset to use for encoding the query string
     * @return The generated query string for the URI
     */
    public static String generateQueryString(Map<String, List<String>> parameters, boolean encode, String encodeCharset)
            throws Exception {

        if (parameters == null || parameters.size() == 0) {
            return null;
        }

        StringBuilder queryStringBuilder = new StringBuilder();

        try {
            for (String key : parameters.keySet()) {
                for (String val : parameters.get(key)) {
                    queryStringBuilder.append("&");
                    if (encode) {
                        queryStringBuilder.append(URLEncoder.encode(key, encodeCharset));
                    } else {
                        queryStringBuilder.append(key);
                    }
                    queryStringBuilder.append("=");
                    if (val != null) {
                        if (encode) {
                            queryStringBuilder.append(URLEncoder.encode(val, encodeCharset));
                        } else {
                            queryStringBuilder.append(val);
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Invalid charset passed for query string encoding", e);
        }
        String queryString = null;

        queryString = queryStringBuilder.toString();
        queryString = queryString.substring(1); // remove the first & - faster to do it here than adding logic in the Lambda
        return queryString;
    }

    /**
     * Parses a header value using the default value separator "," and qualifier separator ";".
     * @param headerValue The value to be parsed
     * @return A list of SimpleMapEntry objects with all of the possible values for the header.
     */
    public static List<HeaderValue> parseHeaderValue(String headerValue) {
        return parseHeaderValue(headerValue, HEADER_VALUE_SEPARATOR, HEADER_QUALIFIER_SEPARATOR);
    }

    /**
     * Generic method to parse an HTTP header value and split it into a list of key/values for all its components.
     * When the property in the header does not specify a key the key field in the output pair is null and only the value
     * is populated. For example, The header <code>Accept: application/json; application/xml</code> will contain two
     * key value pairs with key null and the value set to application/json and application/xml respectively.
     *
     * @param headerValue The string value for the HTTP header
     * @param valueSeparator The separator to be used for parsing header values
     * @return A list of SimpleMapEntry objects with all of the possible values for the header.
     */
    public static List<HeaderValue> parseHeaderValue(String headerValue, String valueSeparator, String qualifierSeparator) {
        // Accept: text/html, application/xhtml+xml, application/xml;q=0.9, */*;q=0.8
        // Accept-Language: fr-CH, fr;q=0.9, en;q=0.8, de;q=0.7, *;q=0.5
        // Cookie: name=value; name2=value2; name3=value3
        // X-Custom-Header: YQ==

        List<HeaderValue> values = new ArrayList<>();
        if (headerValue == null) {
            return values;
        }

        for (String v : headerValue.split(valueSeparator)) {
            String curValue = v;
            float curPreference = 1.0f;
            HeaderValue newValue = new HeaderValue();
            newValue.setRawValue(v);

            for (String q : curValue.split(qualifierSeparator)) {

                String[] kv = q.split(HEADER_KEY_VALUE_SEPARATOR, 2);
                String key = null;
                String val = null;
                // no separator, set the value only
                if (kv.length == 1) {
                    val = q.trim();
                }
                // we have a separator
                if (kv.length == 2) {
                    // if the length of the value is 0 we assume that we are looking at a
                    // base64 encoded value with padding, so we just set the value. This is because
                    // we assume that empty values in a key/value pair will contain at least a white space
                    if (kv[1].length() == 0) {
                        val = q.trim();
                    }
                    // this was a base64 string with an additional = for padding, set the value only
                    if ("=".equals(kv[1].trim())) {
                        val = q.trim();
                    } else { // it's a proper key/value set both
                        key = kv[0].trim();
                        val = ("".equals(kv[1].trim()) ? null : kv[1].trim());
                    }
                }

                if (newValue.getValue() == null) {
                    newValue.setKey(key);
                    newValue.setValue(val);
                } else {
                    // special case for quality q=
                    if ("q".equals(key)) {
                        curPreference = Float.parseFloat(val);
                    } else {
                        newValue.addAttribute(key, val);
                    }
                }
            }
            newValue.setPriority(curPreference);
            values.add(newValue);
        }

        // sort list by preference
        values.sort((HeaderValue first, HeaderValue second) -> {
            if ((first.getPriority() - second.getPriority()) < .001f) {
                return 0;
            }
            if (first.getPriority() < second.getPriority()) {
                return 1;
            }
            return -1;
        });
        return values;
    }

    public static String parseCharacterEncoding(String contentTypeHeader) {
        // we only look at content-type because content-encoding should only be used for
        // "binary" requests such as gzip/deflate.
        if (contentTypeHeader == null) {
            return null;
        }

        String[] contentTypeValues = contentTypeHeader.split(HEADER_VALUE_SEPARATOR);
        if (contentTypeValues.length <= 1) {
            return null;
        }

        for (String contentTypeValue : contentTypeValues) {
            if (contentTypeValue.trim().startsWith(ENCODING_VALUE_KEY)) {
                String[] encodingValues = contentTypeValue.split(HEADER_KEY_VALUE_SEPARATOR);
                if (encodingValues.length <= 1) {
                    return null;
                }
                return encodingValues[1];
            }
        }
        return null;
    }

    public static String getFirstQueryParamValue(Map<String, List<String>> queryString, String key, boolean isCaseSensitive) {
        if (queryString != null) {
            if (isCaseSensitive) {
                return getFirst(queryString, key);
            }

            for (String k : queryString.keySet()) {
                if (k.toLowerCase(Locale.getDefault()).equals(key.toLowerCase(Locale.getDefault()))) {
                    return getFirst(queryString, k);
                }
            }
        }

        return null;
    }

    public  static String[] getQueryParamValues(Map<String, List<String>> qs, String key, boolean isCaseSensitive) {
        if (qs != null) {
            if (isCaseSensitive) {
                return qs.get(key).toArray(new String[0]);
            }

            for (String k : qs.keySet()) {
                if (k.toLowerCase(Locale.getDefault()).equals(key.toLowerCase(Locale.getDefault()))) {
                    return qs.get(k).toArray(new String[0]);
                }
            }
        }

        return new String[0];
    }

    public static String appendCharacterEncoding(String currentContentType, String newEncoding) {
        if (currentContentType == null || "".equals(currentContentType.trim())) {
            return null;
        }

        if (currentContentType.contains(HEADER_VALUE_SEPARATOR)) {
            String[] contentTypeValues = currentContentType.split(HEADER_VALUE_SEPARATOR);
            StringBuilder contentType = new StringBuilder(contentTypeValues[0]);

            for (int i = 1; i < contentTypeValues.length; i++) {
                String contentTypeValue = contentTypeValues[i];
                String contentTypeString = HEADER_VALUE_SEPARATOR + " " + contentTypeValue;
                if (contentTypeValue.trim().startsWith(ENCODING_VALUE_KEY)) {
                    contentTypeString = HEADER_VALUE_SEPARATOR + " " + ENCODING_VALUE_KEY + HEADER_KEY_VALUE_SEPARATOR + newEncoding;
                }
                contentType.append(contentTypeString);
            }

            return contentType.toString();
        } else {
            return currentContentType + HEADER_VALUE_SEPARATOR + " " + ENCODING_VALUE_KEY + HEADER_KEY_VALUE_SEPARATOR + newEncoding;
        }
    }

    private static String getFirst(Map<String, List<String>> map, String key) {
        List<String> values = map.get(key);
        if (values == null || values.size() == 0) {
            return null;
        }
        return values.get(0);
    }

    /**
     * Class that represents a header value.
     */
    public static class HeaderValue {
        private String key;
        private String value;
        private String rawValue;
        private float priority;
        private Map<String, String> attributes;

        public HeaderValue() {
            attributes = new HashMap<>();
        }


        public String getKey() {
            return key;
        }


        public void setKey(String key) {
            this.key = key;
        }


        public String getValue() {
            return value;
        }


        public void setValue(String value) {
            this.value = value;
        }


        public String getRawValue() {
            return rawValue;
        }


        public void setRawValue(String rawValue) {
            this.rawValue = rawValue;
        }


        public float getPriority() {
            return priority;
        }


        public void setPriority(float priority) {
            this.priority = priority;
        }


        public Map<String, String> getAttributes() {
            return attributes;
        }


        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        public void addAttribute(String key, String value) {
            attributes.put(key, value);
        }

        public String getAttribute(String key) {
            return attributes.get(key);
        }
    }
}
