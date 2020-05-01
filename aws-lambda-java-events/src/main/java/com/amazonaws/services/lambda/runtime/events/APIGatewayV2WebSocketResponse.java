package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Tim Gustafson <tjg@amazon.com>
 */
public class APIGatewayV2WebSocketResponse implements Serializable, Cloneable {

  private static final long serialVersionUID = -5155789062248356200L;

  private boolean isBase64Encoded = false;
  private int statusCode;
  private Map<String, String> headers;
  private Map<String, String[]> multiValueHeaders;
  private String body;

  public boolean isIsBase64Encoded() {
    return isBase64Encoded;
  }

  public void setIsBase64Encoded(boolean isBase64Encoded) {
    this.isBase64Encoded = isBase64Encoded;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, String[]> getMultiValueHeaders() {
    return multiValueHeaders;
  }

  public void setMultiValueHeaders(Map<String, String[]> multiValueHeaders) {
    this.multiValueHeaders = multiValueHeaders;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 71 * hash + (this.isBase64Encoded ? 1 : 0);
    hash = 71 * hash + this.statusCode;
    hash = 71 * hash + (this.headers != null ? this.headers.hashCode() : 0);
    hash = 71 * hash + (this.multiValueHeaders != null ? this.multiValueHeaders.hashCode() : 0);
    hash = 71 * hash + (this.body != null ? this.body.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final APIGatewayV2WebSocketResponse other = (APIGatewayV2WebSocketResponse) obj;
    if (this.isBase64Encoded != other.isBase64Encoded) {
      return false;
    }
    if (this.statusCode != other.statusCode) {
      return false;
    }
    if ((this.body == null) ? (other.body != null) : !this.body.equals(other.body)) {
      return false;
    }
    if (this.headers != other.headers && (this.headers == null || !this.headers.equals(other.headers))) {
      return false;
    }
    if (this.multiValueHeaders != other.multiValueHeaders && (this.multiValueHeaders == null || !this.multiValueHeaders.equals(other.multiValueHeaders))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "{isBase64Encoded=" + isBase64Encoded
      + ", statusCode=" + statusCode
      + ", headers=" + headers
      + ", multiValueHeaders=" + multiValueHeaders
      + ", body=" + body
      + "}";
  }

}
