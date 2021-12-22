package com.amazonaws.services.lambda.runtime.events;

import com.amazonaws.services.lambda.runtime.events.models.HttpHeaders;

import java.util.Map;

final class HttpHeadersUtil {
    private HttpHeadersUtil() {}

    public static <T> HttpHeaders<T> mergeOrReplace(Map<String, T> from) {
        if (from == null) return null;
        if (from instanceof HttpHeaders) return (HttpHeaders<T>) from;

        HttpHeaders<T> out = new HttpHeaders<>();
        if (from.isEmpty()) return out;

        out.putAll(from);
        return out;
    }
}
