package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Class that represents an APIGatewayProxyResponseEvent object
 */
public class APIGatewayProxyResponseEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = 2263167344670024172L;
    
    private Integer statusCode;

    private Map<String, String> headers;

    private Map<String, List<String>> multiValueHeaders;
    
    private String body;

    private Boolean isBase64Encoded;

    /**
     * default constructor
     */
    public APIGatewayProxyResponseEvent() {}

    /**
     * @return The HTTP status code for the request
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode The HTTP status code for the request
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @param statusCode The HTTP status code for the request
     * @return APIGatewayProxyResponseEvent object
     */
    public APIGatewayProxyResponseEvent withStatusCode(Integer statusCode) {
        this.setStatusCode(statusCode);
        return this;
    }

    /**
     * @return The Http headers return in the response
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers The Http headers return in the response
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @param headers The Http headers return in the response
     * @return APIGatewayProxyResponseEvent
     */
    public APIGatewayProxyResponseEvent withHeaders(Map<String, String> headers) {
        this.setHeaders(headers);
        return this;
    }

    /**
     * @return the Http multi value headers to return in the response
     */
    public Map<String, List<String>> getMultiValueHeaders() {
        return multiValueHeaders;
    }

    /**
     * @param multiValueHeaders the Http multi value headers to return in the response
     */
    public void setMultiValueHeaders(Map<String, List<String>> multiValueHeaders) {
        this.multiValueHeaders = multiValueHeaders;
    }

    /**
     *
     * @param multiValueHeaders the Http multi value headers to return in the response
     * @return APIGatewayProxyResponseEvent
     */
    public APIGatewayProxyResponseEvent withMultiValueHeaders(Map<String, List<String>> multiValueHeaders) {
        this.setMultiValueHeaders(multiValueHeaders);
        return this;
    }

    /**
     * @return The response body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body The response body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @param body The response body
     * @return APIGatewayProxyResponseEvent object
     */
    public APIGatewayProxyResponseEvent withBody(String body) {
        this.setBody(body);
        return this;
    }

    /**
     * @return whether the body String is base64 encoded.
     */
    public Boolean getIsBase64Encoded() {
        return this.isBase64Encoded;
    }

    /**
     * @param isBase64Encoded Whether the body String is base64 encoded
     */
    public void setIsBase64Encoded(Boolean isBase64Encoded) {
        this.isBase64Encoded = isBase64Encoded;
    }

    /**
     * @param isBase64Encoded Whether the body String is base64 encoded
     * @return APIGatewayProxyRequestEvent
     */
    public APIGatewayProxyResponseEvent withIsBase64Encoded(Boolean isBase64Encoded) {
        this.setIsBase64Encoded(isBase64Encoded);
        return this;
    }

    /**
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getStatusCode() != null)
            sb.append("statusCode: ").append(getStatusCode()).append(",");
        if (getHeaders() != null)
            sb.append("headers: ").append(getHeaders().toString()).append(",");
        if (getMultiValueHeaders() != null)
            sb.append("multiValueHeaders: ").append(getMultiValueHeaders().toString()).append(",");
        if (getBody() != null)
            sb.append("body: ").append(getBody());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof APIGatewayProxyResponseEvent == false)
            return false;
        APIGatewayProxyResponseEvent other = (APIGatewayProxyResponseEvent) obj;
        if (other.getStatusCode() == null ^ this.getStatusCode() == null)
            return false;
        if (other.getStatusCode() != null && other.getStatusCode().equals(this.getStatusCode()) == false)
            return false;
        if (other.getHeaders() == null ^ this.getHeaders() == null)
            return false;
        if (other.getHeaders() != null && other.getHeaders().equals(this.getHeaders()) == false)
            return false;
        if (other.getMultiValueHeaders() == null ^ this.getMultiValueHeaders() == null)
            return false;
        if (other.getMultiValueHeaders() != null && other.getMultiValueHeaders().equals(this.getMultiValueHeaders()) == false)
            return false;
        if (other.getBody() == null ^ this.getBody() == null)
            return false;
        if (other.getBody() != null && other.getBody().equals(this.getBody()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getStatusCode() == null) ? 0 : getStatusCode().hashCode());
        hashCode = prime * hashCode + ((getHeaders() == null) ? 0 : getHeaders().hashCode());
        hashCode = prime * hashCode + ((getMultiValueHeaders() == null) ? 0 : getMultiValueHeaders().hashCode());
        hashCode = prime * hashCode + ((getBody() == null) ? 0 : getBody().hashCode());
        return hashCode;
    }

    @Override
    public APIGatewayProxyResponseEvent clone() {
        try {
            return (APIGatewayProxyResponseEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

}
