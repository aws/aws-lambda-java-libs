package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Tim Gustafson <tjg@amazon.com>
 */
public class APIGatewayV2ProxyResponseEvent implements Serializable, Cloneable {

  private int statusCode;
  private String body;
  private boolean isBase64Encoded = false;

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public boolean isIsBase64Encoded() {
    return isBase64Encoded;
  }

  public void setIsBase64Encoded(boolean isBase64Encoded) {
    this.isBase64Encoded = isBase64Encoded;
  }

  @Override
  public int hashCode() {
    int hash = 3;

    hash = 29 * hash + this.statusCode;
    hash = 29 * hash + (this.isBase64Encoded ? 1 : 0);
    hash = 29 * hash + Objects.hashCode(this.body);

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

    final APIGatewayV2ProxyResponseEvent other = (APIGatewayV2ProxyResponseEvent) obj;

    if (this.statusCode != other.statusCode) {
      return false;
    }

    if (this.isBase64Encoded != other.isBase64Encoded) {
      return false;
    }

    if (!Objects.equals(this.body, other.body)) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");

    sb.append("statusCode: ").append(statusCode).append(",");

    if (body != null) {
      sb.append("body: ").append(body).append(",");
    }

    sb.append("isBase64Encoded: ").append(isBase64Encoded).append(",");

    sb.append("}");

    return sb.toString();
  }

}
