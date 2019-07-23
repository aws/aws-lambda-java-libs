package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Tim Gustafson <tjg@amazon.com>
 */
public class APIGatewayV2ProxyRequestEvent implements Serializable, Cloneable {

  public class RequestIdentity implements Serializable, Cloneable {

    private String sourceIp;
    private String userAgent;

    public String getSourceIp() {
      return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
      this.sourceIp = sourceIp;
    }

    public String getUserAgent() {
      return userAgent;
    }

    public void setUserAgent(String userAgent) {
      this.userAgent = userAgent;
    }

    @Override
    public int hashCode() {
      int hash = 7;

      hash = 59 * hash + Objects.hashCode(this.sourceIp);
      hash = 59 * hash + Objects.hashCode(this.userAgent);

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

      final RequestIdentity other = (RequestIdentity) obj;

      if (!Objects.equals(this.sourceIp, other.sourceIp)) {
        return false;
      }

      if (!Objects.equals(this.userAgent, other.userAgent)) {
        return false;
      }

      return true;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");

      if (getSourceIp() != null) {
        sb.append("sourceIp: ").append(getSourceIp()).append(",");
      }
      if (getUserAgent() != null) {
        sb.append("userAgent: ").append(getUserAgent()).append(",");
      }

      sb.append("}");

      return sb.toString();
    }

  }

  public class RequestContext implements Serializable, Cloneable {

    private String routeKey;
    private String messageId;
    private String eventType;
    private String extendedRequestId;
    private String requestTime;
    private String messageDirection;
    private String stage;
    private long connectedAt;
    private long requestTimeEpoch;
    private RequestIdentity identity;
    private String requestId;
    private String domainName;
    private String connectionId;
    private String apiId;

    public String getRouteKey() {
      return routeKey;
    }

    public void setRouteKey(String routeKey) {
      this.routeKey = routeKey;
    }

    public String getMessageId() {
      return messageId;
    }

    public void setMessageId(String messageId) {
      this.messageId = messageId;
    }

    public String getEventType() {
      return eventType;
    }

    public void setEventType(String eventType) {
      this.eventType = eventType;
    }

    public String getExtendedRequestId() {
      return extendedRequestId;
    }

    public void setExtendedRequestId(String extendedRequestId) {
      this.extendedRequestId = extendedRequestId;
    }

    public String getRequestTime() {
      return requestTime;
    }

    public void setRequestTime(String requestTime) {
      this.requestTime = requestTime;
    }

    public String getMessageDirection() {
      return messageDirection;
    }

    public void setMessageDirection(String messageDirection) {
      this.messageDirection = messageDirection;
    }

    public String getStage() {
      return stage;
    }

    public void setStage(String stage) {
      this.stage = stage;
    }

    public long getConnectedAt() {
      return connectedAt;
    }

    public void setConnectedAt(long connectedAt) {
      this.connectedAt = connectedAt;
    }

    public long getRequestTimeEpoch() {
      return requestTimeEpoch;
    }

    public void setRequestTimeEpoch(long requestTimeEpoch) {
      this.requestTimeEpoch = requestTimeEpoch;
    }

    public RequestIdentity getIdentity() {
      return identity;
    }

    public void setIdentity(RequestIdentity identity) {
      this.identity = identity;
    }

    public String getRequestId() {
      return requestId;
    }

    public void setRequestId(String requestId) {
      this.requestId = requestId;
    }

    public String getDomainName() {
      return domainName;
    }

    public void setDomainName(String domainName) {
      this.domainName = domainName;
    }

    public String getConnectionId() {
      return connectionId;
    }

    public void setConnectionId(String connectionId) {
      this.connectionId = connectionId;
    }

    public String getApiId() {
      return apiId;
    }

    public void setApiId(String apiId) {
      this.apiId = apiId;
    }

    @Override
    public int hashCode() {
      int hash = 7;

      hash = 41 * hash + Objects.hashCode(this.routeKey);
      hash = 41 * hash + Objects.hashCode(this.messageId);
      hash = 41 * hash + Objects.hashCode(this.eventType);
      hash = 41 * hash + Objects.hashCode(this.extendedRequestId);
      hash = 41 * hash + Objects.hashCode(this.requestTime);
      hash = 41 * hash + Objects.hashCode(this.messageDirection);
      hash = 41 * hash + Objects.hashCode(this.stage);
      hash = 41 * hash + (int) (this.connectedAt ^ (this.connectedAt >>> 32));
      hash = 41 * hash + (int) (this.requestTimeEpoch ^ (this.requestTimeEpoch >>> 32));
      hash = 41 * hash + Objects.hashCode(this.identity);
      hash = 41 * hash + Objects.hashCode(this.requestId);
      hash = 41 * hash + Objects.hashCode(this.domainName);
      hash = 41 * hash + Objects.hashCode(this.connectionId);
      hash = 41 * hash + Objects.hashCode(this.apiId);

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

      final RequestContext other = (RequestContext) obj;

      if (this.connectedAt != other.connectedAt) {
        return false;
      }

      if (this.requestTimeEpoch != other.requestTimeEpoch) {
        return false;
      }

      if (!Objects.equals(this.routeKey, other.routeKey)) {
        return false;
      }

      if (!Objects.equals(this.messageId, other.messageId)) {
        return false;
      }

      if (!Objects.equals(this.eventType, other.eventType)) {
        return false;
      }

      if (!Objects.equals(this.extendedRequestId, other.extendedRequestId)) {
        return false;
      }

      if (!Objects.equals(this.requestTime, other.requestTime)) {
        return false;
      }

      if (!Objects.equals(this.messageDirection, other.messageDirection)) {
        return false;
      }

      if (!Objects.equals(this.stage, other.stage)) {
        return false;
      }

      if (!Objects.equals(this.requestId, other.requestId)) {
        return false;
      }

      if (!Objects.equals(this.domainName, other.domainName)) {
        return false;
      }

      if (!Objects.equals(this.connectionId, other.connectionId)) {
        return false;
      }

      if (!Objects.equals(this.apiId, other.apiId)) {
        return false;
      }

      if (!Objects.equals(this.identity, other.identity)) {
        return false;
      }

      return true;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");

      if (routeKey != null) {
        sb.append("routeKey: ").append(routeKey).append(",");
      }

      if (messageId != null) {
        sb.append("messageId: ").append(messageId).append(",");
      }

      if (eventType != null) {
        sb.append("eventType: ").append(eventType).append(",");
      }

      if (extendedRequestId != null) {
        sb.append("extendedRequestId: ").append(extendedRequestId).append(",");
      }

      if (requestTime != null) {
        sb.append("requestTime: ").append(requestTime).append(",");
      }

      if (messageDirection != null) {
        sb.append("messageDirection: ").append(messageDirection).append(",");
      }

      if (stage != null) {
        sb.append("stage: ").append(stage).append(",");
      }

      if (connectedAt != 0) {
        sb.append("connectedAt: ").append(connectedAt).append(",");
      }

      if (messageId != null) {
        sb.append("messageId: ").append(messageId).append(",");
      }

      if (identity != null) {
        sb.append("identity: ").append(identity).append(",");
      }

      if (requestTimeEpoch != 0) {
        sb.append("requestTimeEpoch: ").append(requestTimeEpoch).append(",");
      }

      if (requestId != null) {
        sb.append("requestId: ").append(requestId).append(",");
      }

      if (domainName != null) {
        sb.append("domainName: ").append(domainName).append(",");
      }

      if (connectionId != null) {
        sb.append("connectionId: ").append(connectionId).append(",");
      }

      if (apiId != null) {
        sb.append("apiId: ").append(apiId).append(",");
      }

      sb.append("}");

      return sb.toString();
    }
  }

  private RequestContext requestContext;
  private String body;
  private boolean isBase64Encoded = false;

  public RequestContext getRequestContext() {
    return requestContext;
  }

  public void setRequestContext(RequestContext requestContext) {
    this.requestContext = requestContext;
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
    int hash = 7;

    hash = 43 * hash + Objects.hashCode(this.requestContext);
    hash = 43 * hash + Objects.hashCode(this.body);
    hash = 43 * hash + (this.isBase64Encoded ? 1 : 0);

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

    final APIGatewayV2ProxyRequestEvent other = (APIGatewayV2ProxyRequestEvent) obj;

    if (this.isBase64Encoded != other.isBase64Encoded) {
      return false;
    }

    if (!Objects.equals(this.body, other.body)) {
      return false;
    }

    if (!Objects.equals(this.requestContext, other.requestContext)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");

    if (requestContext != null) {
      sb.append("requestContext: ").append(requestContext).append(",");
    }

    if (body != null) {
      sb.append("body: ").append(body).append(",");
    }

    sb.append("isBase64Encoded: ").append(isBase64Encoded).append(",");

    sb.append("}");

    return sb.toString();
  }

}
