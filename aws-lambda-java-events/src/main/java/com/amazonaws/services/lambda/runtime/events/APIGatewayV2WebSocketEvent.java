package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Tim Gustafson <tjg@amazon.com>
 */
public class APIGatewayV2WebSocketEvent implements Serializable, Cloneable {

  private static final long serialVersionUID = 5695319264103347099L;

  public static class RequestIdentity implements Serializable, Cloneable {

    private static final long serialVersionUID = -3276649362684921217L;

    private String cognitoIdentityPoolId;
    private String accountId;
    private String cognitoIdentityId;
    private String caller;
    private String apiKey;
    private String sourceIp;
    private String cognitoAuthenticationType;
    private String cognitoAuthenticationProvider;
    private String userArn;
    private String userAgent;
    private String user;
    private String accessKey;

    public String getCognitoIdentityPoolId() {
      return cognitoIdentityPoolId;
    }

    public void setCognitoIdentityPoolId(String cognitoIdentityPoolId) {
      this.cognitoIdentityPoolId = cognitoIdentityPoolId;
    }

    public String getAccountId() {
      return accountId;
    }

    public void setAccountId(String accountId) {
      this.accountId = accountId;
    }

    public String getCognitoIdentityId() {
      return cognitoIdentityId;
    }

    public void setCognitoIdentityId(String cognitoIdentityId) {
      this.cognitoIdentityId = cognitoIdentityId;
    }

    public String getCaller() {
      return caller;
    }

    public void setCaller(String caller) {
      this.caller = caller;
    }

    public String getApiKey() {
      return apiKey;
    }

    public void setApiKey(String apiKey) {
      this.apiKey = apiKey;
    }

    public String getSourceIp() {
      return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
      this.sourceIp = sourceIp;
    }

    public String getCognitoAuthenticationType() {
      return cognitoAuthenticationType;
    }

    public void setCognitoAuthenticationType(String cognitoAuthenticationType) {
      this.cognitoAuthenticationType = cognitoAuthenticationType;
    }

    public String getCognitoAuthenticationProvider() {
      return cognitoAuthenticationProvider;
    }

    public void setCognitoAuthenticationProvider(String cognitoAuthenticationProvider) {
      this.cognitoAuthenticationProvider = cognitoAuthenticationProvider;
    }

    public String getUserArn() {
      return userArn;
    }

    public void setUserArn(String userArn) {
      this.userArn = userArn;
    }

    public String getUserAgent() {
      return userAgent;
    }

    public void setUserAgent(String userAgent) {
      this.userAgent = userAgent;
    }

    public String getUser() {
      return user;
    }

    public void setUser(String user) {
      this.user = user;
    }

    public String getAccessKey() {
      return accessKey;
    }

    public void setAccessKey(String accessKey) {
      this.accessKey = accessKey;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 29 * hash + (this.cognitoIdentityPoolId != null ? this.cognitoIdentityPoolId.hashCode() : 0);
      hash = 29 * hash + (this.accountId != null ? this.accountId.hashCode() : 0);
      hash = 29 * hash + (this.cognitoIdentityId != null ? this.cognitoIdentityId.hashCode() : 0);
      hash = 29 * hash + (this.caller != null ? this.caller.hashCode() : 0);
      hash = 29 * hash + (this.apiKey != null ? this.apiKey.hashCode() : 0);
      hash = 29 * hash + (this.sourceIp != null ? this.sourceIp.hashCode() : 0);
      hash = 29 * hash + (this.cognitoAuthenticationType != null ? this.cognitoAuthenticationType.hashCode() : 0);
      hash = 29 * hash + (this.cognitoAuthenticationProvider != null ? this.cognitoAuthenticationProvider.hashCode() : 0);
      hash = 29 * hash + (this.userArn != null ? this.userArn.hashCode() : 0);
      hash = 29 * hash + (this.userAgent != null ? this.userAgent.hashCode() : 0);
      hash = 29 * hash + (this.user != null ? this.user.hashCode() : 0);
      hash = 29 * hash + (this.accessKey != null ? this.accessKey.hashCode() : 0);
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
      if ((this.cognitoIdentityPoolId == null) ? (other.cognitoIdentityPoolId != null) : !this.cognitoIdentityPoolId.equals(other.cognitoIdentityPoolId)) {
        return false;
      }
      if ((this.accountId == null) ? (other.accountId != null) : !this.accountId.equals(other.accountId)) {
        return false;
      }
      if ((this.cognitoIdentityId == null) ? (other.cognitoIdentityId != null) : !this.cognitoIdentityId.equals(other.cognitoIdentityId)) {
        return false;
      }
      if ((this.caller == null) ? (other.caller != null) : !this.caller.equals(other.caller)) {
        return false;
      }
      if ((this.apiKey == null) ? (other.apiKey != null) : !this.apiKey.equals(other.apiKey)) {
        return false;
      }
      if ((this.sourceIp == null) ? (other.sourceIp != null) : !this.sourceIp.equals(other.sourceIp)) {
        return false;
      }
      if ((this.cognitoAuthenticationType == null) ? (other.cognitoAuthenticationType != null) : !this.cognitoAuthenticationType.equals(other.cognitoAuthenticationType)) {
        return false;
      }
      if ((this.cognitoAuthenticationProvider == null) ? (other.cognitoAuthenticationProvider != null) : !this.cognitoAuthenticationProvider.equals(other.cognitoAuthenticationProvider)) {
        return false;
      }
      if ((this.userArn == null) ? (other.userArn != null) : !this.userArn.equals(other.userArn)) {
        return false;
      }
      if ((this.userAgent == null) ? (other.userAgent != null) : !this.userAgent.equals(other.userAgent)) {
        return false;
      }
      if ((this.user == null) ? (other.user != null) : !this.user.equals(other.user)) {
        return false;
      }
      if ((this.accessKey == null) ? (other.accessKey != null) : !this.accessKey.equals(other.accessKey)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "{cognitoIdentityPoolId=" + cognitoIdentityPoolId
        + ", accountId=" + accountId
        + ", cognitoIdentityId=" + cognitoIdentityId
        + ", caller=" + caller
        + ", apiKey=" + apiKey
        + ", sourceIp=" + sourceIp
        + ", cognitoAuthenticationType=" + cognitoAuthenticationType
        + ", cognitoAuthenticationProvider=" + cognitoAuthenticationProvider
        + ", userArn=" + userArn
        + ", userAgent=" + userAgent
        + ", user=" + user
        + ", accessKey=" + accessKey
        + "}";
    }
  }

  public static class RequestContext implements Serializable, Cloneable {

    private static final long serialVersionUID = -6641935365992304860L;

    private String accountId;
    private String resourceId;
    private String stage;
    private String requestId;
    private RequestIdentity identity;
    private String ResourcePath;
    private Map<String, Object> authorizer;
    private String httpMethod;
    private String apiId;
    private long connectedAt;
    private String connectionId;
    private String domainName;
    private String error;
    private String eventType;
    private String extendedRequestId;
    private String integrationLatency;
    private String messageDirection;
    private String messageId;
    private String requestTime;
    private long requestTimeEpoch;
    private String routeKey;
    private String status;

    public String getAccountId() {
      return accountId;
    }

    public void setAccountId(String accountId) {
      this.accountId = accountId;
    }

    public String getResourceId() {
      return resourceId;
    }

    public void setResourceId(String resourceId) {
      this.resourceId = resourceId;
    }

    public String getStage() {
      return stage;
    }

    public void setStage(String stage) {
      this.stage = stage;
    }

    public String getRequestId() {
      return requestId;
    }

    public void setRequestId(String requestId) {
      this.requestId = requestId;
    }

    public RequestIdentity getIdentity() {
      return identity;
    }

    public void setIdentity(RequestIdentity identity) {
      this.identity = identity;
    }

    public String getResourcePath() {
      return ResourcePath;
    }

    public void setResourcePath(String ResourcePath) {
      this.ResourcePath = ResourcePath;
    }

    public Map<String, Object> getAuthorizer() {
      return authorizer;
    }

    public void setAuthorizer(Map<String, Object> authorizer) {
      this.authorizer = authorizer;
    }

    public String getHttpMethod() {
      return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
      this.httpMethod = httpMethod;
    }

    public String getApiId() {
      return apiId;
    }

    public void setApiId(String apiId) {
      this.apiId = apiId;
    }

    public long getConnectedAt() {
      return connectedAt;
    }

    public void setConnectedAt(long connectedAt) {
      this.connectedAt = connectedAt;
    }

    public String getConnectionId() {
      return connectionId;
    }

    public void setConnectionId(String connectionId) {
      this.connectionId = connectionId;
    }

    public String getDomainName() {
      return domainName;
    }

    public void setDomainName(String domainName) {
      this.domainName = domainName;
    }

    public String getError() {
      return error;
    }

    public void setError(String error) {
      this.error = error;
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

    public String getIntegrationLatency() {
      return integrationLatency;
    }

    public void setIntegrationLatency(String integrationLatency) {
      this.integrationLatency = integrationLatency;
    }

    public String getMessageDirection() {
      return messageDirection;
    }

    public void setMessageDirection(String messageDirection) {
      this.messageDirection = messageDirection;
    }

    public String getMessageId() {
      return messageId;
    }

    public void setMessageId(String messageId) {
      this.messageId = messageId;
    }

    public String getRequestTime() {
      return requestTime;
    }

    public void setRequestTime(String requestTime) {
      this.requestTime = requestTime;
    }

    public long getRequestTimeEpoch() {
      return requestTimeEpoch;
    }

    public void setRequestTimeEpoch(long requestTimeEpoch) {
      this.requestTimeEpoch = requestTimeEpoch;
    }

    public String getRouteKey() {
      return routeKey;
    }

    public void setRouteKey(String routeKey) {
      this.routeKey = routeKey;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 59 * hash + (this.accountId != null ? this.accountId.hashCode() : 0);
      hash = 59 * hash + (this.resourceId != null ? this.resourceId.hashCode() : 0);
      hash = 59 * hash + (this.stage != null ? this.stage.hashCode() : 0);
      hash = 59 * hash + (this.requestId != null ? this.requestId.hashCode() : 0);
      hash = 59 * hash + (this.identity != null ? this.identity.hashCode() : 0);
      hash = 59 * hash + (this.ResourcePath != null ? this.ResourcePath.hashCode() : 0);
      hash = 59 * hash + (this.authorizer != null ? this.authorizer.hashCode() : 0);
      hash = 59 * hash + (this.httpMethod != null ? this.httpMethod.hashCode() : 0);
      hash = 59 * hash + (this.apiId != null ? this.apiId.hashCode() : 0);
      hash = 59 * hash + (int) (this.connectedAt ^ (this.connectedAt >>> 32));
      hash = 59 * hash + (this.connectionId != null ? this.connectionId.hashCode() : 0);
      hash = 59 * hash + (this.domainName != null ? this.domainName.hashCode() : 0);
      hash = 59 * hash + (this.error != null ? this.error.hashCode() : 0);
      hash = 59 * hash + (this.eventType != null ? this.eventType.hashCode() : 0);
      hash = 59 * hash + (this.extendedRequestId != null ? this.extendedRequestId.hashCode() : 0);
      hash = 59 * hash + (this.integrationLatency != null ? this.integrationLatency.hashCode() : 0);
      hash = 59 * hash + (this.messageDirection != null ? this.messageDirection.hashCode() : 0);
      hash = 59 * hash + (this.messageId != null ? this.messageId.hashCode() : 0);
      hash = 59 * hash + (this.requestTime != null ? this.requestTime.hashCode() : 0);
      hash = 59 * hash + (int) (this.requestTimeEpoch ^ (this.requestTimeEpoch >>> 32));
      hash = 59 * hash + (this.routeKey != null ? this.routeKey.hashCode() : 0);
      hash = 59 * hash + (this.status != null ? this.status.hashCode() : 0);
      return hash;
    }

    @Override
    public String toString() {
      return "{accountId=" + accountId
        + ", resourceId=" + resourceId
        + ", stage=" + stage
        + ", requestId=" + requestId
        + ", identity=" + identity
        + ", ResourcePath=" + ResourcePath
        + ", authorizer=" + authorizer
        + ", httpMethod=" + httpMethod
        + ", apiId=" + apiId
        + ", connectedAt=" + connectedAt
        + ", connectionId=" + connectionId
        + ", domainName=" + domainName
        + ", error=" + error
        + ", eventType=" + eventType
        + ", extendedRequestId=" + extendedRequestId
        + ", integrationLatency=" + integrationLatency
        + ", messageDirection=" + messageDirection
        + ", messageId=" + messageId
        + ", requestTime=" + requestTime
        + ", requestTimeEpoch=" + requestTimeEpoch
        + ", routeKey=" + routeKey
        + ", status=" + status
        + "}";
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
      if ((this.accountId == null) ? (other.accountId != null) : !this.accountId.equals(other.accountId)) {
        return false;
      }
      if ((this.resourceId == null) ? (other.resourceId != null) : !this.resourceId.equals(other.resourceId)) {
        return false;
      }
      if ((this.stage == null) ? (other.stage != null) : !this.stage.equals(other.stage)) {
        return false;
      }
      if ((this.requestId == null) ? (other.requestId != null) : !this.requestId.equals(other.requestId)) {
        return false;
      }
      if ((this.ResourcePath == null) ? (other.ResourcePath != null) : !this.ResourcePath.equals(other.ResourcePath)) {
        return false;
      }
      if ((this.authorizer == null) ? (other.authorizer != null) : !this.authorizer.equals(other.authorizer)) {
        return false;
      }
      if ((this.httpMethod == null) ? (other.httpMethod != null) : !this.httpMethod.equals(other.httpMethod)) {
        return false;
      }
      if ((this.apiId == null) ? (other.apiId != null) : !this.apiId.equals(other.apiId)) {
        return false;
      }
      if ((this.connectionId == null) ? (other.connectionId != null) : !this.connectionId.equals(other.connectionId)) {
        return false;
      }
      if ((this.domainName == null) ? (other.domainName != null) : !this.domainName.equals(other.domainName)) {
        return false;
      }
      if ((this.error == null) ? (other.error != null) : !this.error.equals(other.error)) {
        return false;
      }
      if ((this.eventType == null) ? (other.eventType != null) : !this.eventType.equals(other.eventType)) {
        return false;
      }
      if ((this.extendedRequestId == null) ? (other.extendedRequestId != null) : !this.extendedRequestId.equals(other.extendedRequestId)) {
        return false;
      }
      if ((this.integrationLatency == null) ? (other.integrationLatency != null) : !this.integrationLatency.equals(other.integrationLatency)) {
        return false;
      }
      if ((this.messageDirection == null) ? (other.messageDirection != null) : !this.messageDirection.equals(other.messageDirection)) {
        return false;
      }
      if ((this.messageId == null) ? (other.messageId != null) : !this.messageId.equals(other.messageId)) {
        return false;
      }
      if ((this.requestTime == null) ? (other.requestTime != null) : !this.requestTime.equals(other.requestTime)) {
        return false;
      }
      if ((this.routeKey == null) ? (other.routeKey != null) : !this.routeKey.equals(other.routeKey)) {
        return false;
      }
      if ((this.status == null) ? (other.status != null) : !this.status.equals(other.status)) {
        return false;
      }
      if (this.identity != other.identity && (this.identity == null || !this.identity.equals(other.identity))) {
        return false;
      }
      return true;
    }

  }

  private String resource;
  private String path;
  private String httpMethod;
  private Map<String, String> headers;
  private Map<String, List<String>> multiValueHeaders;
  private Map<String, String> queryStringParameters;
  private Map<String, List<String>> multiValueQueryStringParameters;
  private Map<String, String> pathParameters;
  private Map<String, String> stageVariables;
  private RequestContext requestContext;
  private String body;
  private boolean isBase64Encoded = false;

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, List<String>> getMultiValueHeaders() {
    return multiValueHeaders;
  }

  public void setMultiValueHeaders(Map<String, List<String>> multiValueHeaders) {
    this.multiValueHeaders = multiValueHeaders;
  }

  public Map<String, String> getQueryStringParameters() {
    return queryStringParameters;
  }

  public void setQueryStringParameters(Map<String, String> queryStringParameters) {
    this.queryStringParameters = queryStringParameters;
  }

  public Map<String, List<String>> getMultiValueQueryStringParameters() {
    return multiValueQueryStringParameters;
  }

  public void setMultiValueQueryStringParameters(Map<String, List<String>> multiValueQueryStringParameters) {
    this.multiValueQueryStringParameters = multiValueQueryStringParameters;
  }

  public Map<String, String> getPathParameters() {
    return pathParameters;
  }

  public void setPathParameters(Map<String, String> pathParameters) {
    this.pathParameters = pathParameters;
  }

  public Map<String, String> getStageVariables() {
    return stageVariables;
  }

  public void setStageVariables(Map<String, String> stageVariables) {
    this.stageVariables = stageVariables;
  }

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    APIGatewayV2WebSocketEvent that = (APIGatewayV2WebSocketEvent) o;

    if (isBase64Encoded != that.isBase64Encoded) return false;
    if (resource != null ? !resource.equals(that.resource) : that.resource != null) return false;
    if (path != null ? !path.equals(that.path) : that.path != null) return false;
    if (httpMethod != null ? !httpMethod.equals(that.httpMethod) : that.httpMethod != null) return false;
    if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
    if (multiValueHeaders != null ? !multiValueHeaders.equals(that.multiValueHeaders) : that.multiValueHeaders != null)
      return false;
    if (queryStringParameters != null ? !queryStringParameters.equals(that.queryStringParameters) : that.queryStringParameters != null)
      return false;
    if (multiValueQueryStringParameters != null ? !multiValueQueryStringParameters.equals(that.multiValueQueryStringParameters) : that.multiValueQueryStringParameters != null)
      return false;
    if (pathParameters != null ? !pathParameters.equals(that.pathParameters) : that.pathParameters != null)
      return false;
    if (stageVariables != null ? !stageVariables.equals(that.stageVariables) : that.stageVariables != null)
      return false;
    if (requestContext != null ? !requestContext.equals(that.requestContext) : that.requestContext != null)
      return false;
    return body != null ? body.equals(that.body) : that.body == null;
  }

  @Override
  public int hashCode() {
    int result = resource != null ? resource.hashCode() : 0;
    result = 31 * result + (path != null ? path.hashCode() : 0);
    result = 31 * result + (httpMethod != null ? httpMethod.hashCode() : 0);
    result = 31 * result + (headers != null ? headers.hashCode() : 0);
    result = 31 * result + (multiValueHeaders != null ? multiValueHeaders.hashCode() : 0);
    result = 31 * result + (queryStringParameters != null ? queryStringParameters.hashCode() : 0);
    result = 31 * result + (multiValueQueryStringParameters != null ? multiValueQueryStringParameters.hashCode() : 0);
    result = 31 * result + (pathParameters != null ? pathParameters.hashCode() : 0);
    result = 31 * result + (stageVariables != null ? stageVariables.hashCode() : 0);
    result = 31 * result + (requestContext != null ? requestContext.hashCode() : 0);
    result = 31 * result + (body != null ? body.hashCode() : 0);
    result = 31 * result + (isBase64Encoded ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("APIGatewayV2WebSocketEvent{");
    sb.append("resource='").append(resource).append('\'');
    sb.append(", path='").append(path).append('\'');
    sb.append(", httpMethod='").append(httpMethod).append('\'');
    sb.append(", headers=").append(headers);
    sb.append(", multiValueHeaders=").append(multiValueHeaders);
    sb.append(", queryStringParameters=").append(queryStringParameters);
    sb.append(", multiValueQueryStringParameters=").append(multiValueQueryStringParameters);
    sb.append(", pathParameters=").append(pathParameters);
    sb.append(", stageVariables=").append(stageVariables);
    sb.append(", requestContext=").append(requestContext);
    sb.append(", body='").append(body).append('\'');
    sb.append(", isBase64Encoded=").append(isBase64Encoded);
    sb.append('}');
    return sb.toString();
  }
}
