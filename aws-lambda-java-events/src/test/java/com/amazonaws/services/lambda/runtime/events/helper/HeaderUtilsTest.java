package com.amazonaws.services.lambda.runtime.events.helper;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HeaderUtilsTest {

    private Map<String, List<String>> queryString = new HashMap<>();
    private List<String> list1 = Arrays.asList("value1", "value2");
    private List<String> list2 = Arrays.asList("value3", "value4");
    public static String ENCODED_CHARSET = "UTF-8";
    static final String HEADER_VALUE_SEPARATOR = ";";
    static final String HEADER_QUALIFIER_SEPARATOR = ",";
    static final String ENCODING_VALUE_KEY = "charset";

    @Test
    public void getFirstQueryParamValue_caseInsensitive_returnsValue() {
        queryString.put("key1", list1);
        queryString.put("key2", list2);
        assertEquals("value1", HeaderUtils.getFirstQueryParamValue(queryString, "key1"));
        assertEquals("value1", HeaderUtils.getFirstQueryParamValue(queryString, "KEY1"));
    }

    @Test
    public void getFirstQueryParamValue_caseSensitive_returnsValue() {
        queryString.put("KEY1", list1);
        queryString.put("key2", list2);
        assertEquals("value1", HeaderUtils.getFirstCaseSensitiveQueryParamValue(queryString, "KEY1"));
        assertNotEquals("value3", HeaderUtils.getFirstCaseSensitiveQueryParamValue(queryString, "KEY2"));
    }

    @Test
    public void queryString_generateQueryString_validQuery() {
        queryString.put("key1", list1);
        queryString.put("key2", list2);
        String parsedString = null;
        try {
            parsedString = HeaderUtils.generateQueryString(queryString, true, ENCODED_CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertTrue(parsedString.contains("key1=value1"));
        assertTrue(parsedString.contains("key2=value3"));
        assertTrue(parsedString.contains("&") && parsedString.indexOf("&") > 0 && parsedString.indexOf("&") < parsedString.length());
    }

    @Test
    void queryString_generateQueryString_nullParameterIsEmpty() {
        list2 = Arrays.asList(null, "value4");
        queryString.put("key2", list2);
        String parsedString = null;
        try {
            parsedString = HeaderUtils.generateQueryString(queryString, true, ENCODED_CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertTrue(parsedString.startsWith("key2=&"));
    }

    @Test
    void queryStringWithEncodedParams_generateQueryString_validQuery() {
        list2 = Arrays.asList("{\"name\":\"faisal\"}");
        queryString.put("key2", list2);
        queryString.put("key1", list1);
        String parsedString = null;
        try {
            parsedString = HeaderUtils.generateQueryString(queryString, true, ENCODED_CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertTrue(parsedString.contains("key1=value1"));
        assertTrue(parsedString.contains("key2=%7B%22name%22%3A%22faisal%22%7D"));
        assertTrue(parsedString.contains("&") && parsedString.indexOf("&") > 0 && parsedString.indexOf("&") < parsedString.length());
    }

    @Test
    void headers_parseHeaderValue_multiValue() {
        String headerValue = "application/xml; charset=utf-8";
        List<HeaderUtils.HeaderValue> values = HeaderUtils.parseHeaderValue(headerValue);

        assertEquals(2, values.size());
        assertEquals("application/xml", values.get(0).getValue());
        assertNull(values.get(0).getKey());

        assertEquals(ENCODING_VALUE_KEY, values.get(1).getKey());
        assertEquals("utf-8", values.get(1).getValue());
    }

    @Test
    void headers_parseHeaderValue_validMultipleCookie() {
        String headerValue = "yummy_cookie=choco; tasty_cookie=strawberry";
        List<HeaderUtils.HeaderValue> values = HeaderUtils.parseHeaderValue(headerValue, HEADER_VALUE_SEPARATOR, HEADER_QUALIFIER_SEPARATOR);

        assertEquals(2, values.size());
        assertEquals("yummy_cookie", values.get(0).getKey());
        assertEquals("choco", values.get(0).getValue());
        assertEquals("tasty_cookie", values.get(1).getKey());
        assertEquals("strawberry", values.get(1).getValue());
    }

    @Test
    void headers_parseHeaderValue_complexAccept() {
        String headerValue = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        List<HeaderUtils.HeaderValue> values = HeaderUtils.parseHeaderValue(headerValue, HEADER_QUALIFIER_SEPARATOR, HEADER_VALUE_SEPARATOR);

        assertEquals(4, values.size());
    }

    @Test
    void headers_parseHeaderValue_encodedContentWithEquals() {
        String headerValue = Base64.getUrlEncoder().encodeToString("a".getBytes());
        List<HeaderUtils.HeaderValue> values = HeaderUtils.parseHeaderValue(headerValue);

        assertTrue(values.size() > 0);
        assertEquals("YQ==", values.get(0).getValue());
    }

    @Test
    void headers_parseHeaderValue_base64EncodedCookieValue() {
        String value = Base64.getUrlEncoder().encodeToString("a".getBytes());
        String cookieValue = "jwt=" + value + "; secondValue=second";

        List<HeaderUtils.HeaderValue> values = HeaderUtils.parseHeaderValue(cookieValue);

        assertEquals(2, values.size());
        assertEquals("jwt", values.get(0).getKey());
        assertEquals(value, values.get(0).getValue());
    }

    @Test
    void headers_parseHeaderValue_cookieWithSeparatorInValue() {
        String cookieValue = "jwt==test; secondValue=second";

        List<HeaderUtils.HeaderValue> values = HeaderUtils.parseHeaderValue(cookieValue);

        assertEquals(2, values.size());
        assertEquals("jwt", values.get(0).getKey());
        assertEquals("=test", values.get(0).getValue());
    }

    @Test
    void headers_parseHeaderValue_headerWithPaddingButNotBase64Encoded() {
        List<HeaderUtils.HeaderValue> result = HeaderUtils.parseHeaderValue("hello=");
        assertTrue(result.size() > 0);
        assertEquals("hello", result.get(0).getKey());
        assertNull(result.get(0).getValue());
    }

    @Test
    void contentType_parseCharacterEncoding_validEncodingValue() {
        String contentTypeHeader = "application/xml; charset=utf-8";
        String encodingValue = HeaderUtils.parseCharacterEncoding(contentTypeHeader);

        assertEquals("utf-8", encodingValue);
    }

    @Test
    void contentType_appendCharacterEncoding_validEncodingValue() {
        String currentContentType = "application/xml";
        String newEncoding = StandardCharsets.UTF_8.name();
        String contentType = HeaderUtils.appendCharacterEncoding(currentContentType, newEncoding);

        assertEquals("application/xml; charset=UTF-8", contentType);
    }
}
