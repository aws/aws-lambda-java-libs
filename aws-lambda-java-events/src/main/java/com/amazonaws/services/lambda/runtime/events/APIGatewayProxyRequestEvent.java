package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Class that represents an APIGatewayProxyRequestEvent
 */
public class APIGatewayProxyRequestEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = 4189228800688527467L;

    private String version;

    private String resource;

    private String path;

    private String httpMethod;

    private Map<String, String> headers;

    private Map<String, List<String>> multiValueHeaders;

    private Map<String, String> queryStringParameters;

    private Map<String, List<String>> multiValueQueryStringParameters;

    private Map<String, String> pathParameters;

    private Map<String, String> stageVariables;

    private ProxyRequestContext requestContext;

    private String body;

    private Boolean isBase64Encoded;

    /**
     * class that represents proxy request context
     */
    public static class ProxyRequestContext implements Serializable, Cloneable {

        private static final long serialVersionUID = 8783459961042799774L;

        private String accountId;

        private String stage;

        private String resourceId;

        private String requestId;

        private String operationName;

        private RequestIdentity identity;

        private String resourcePath;

        private String httpMethod;

        private String apiId;

        private String path;

        private Map<String, Object> authorizer;

        /**
         * default constructor
         */
        public ProxyRequestContext() {}

        /**
         * @return account id that owns Lambda function
         */
        public String getAccountId() {
            return accountId;
        }

        /**
         * @param accountId account id that owns Lambda function
         */
        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        /**
         * @param accountId account id that owns Lambda function
         * @return ProxyRequestContext object
         */
        public ProxyRequestContext withAccountId(String accountId) {
            this.setAccountId(accountId);
            return this;
        }

        public Map<String, Object> getAuthorizer() {
            return authorizer;
        }

        public void setAuthorizer(final Map<String, Object> authorizer) {
            this.authorizer = authorizer;
        }

        /**
         * @return  API Gateway stage name
         */
        public String getStage() {
            return stage;
        }

        /**
         * @param stage API Gateway stage name
         */
        public void setStage(String stage) {
            this.stage = stage;
        }

        /**
         * @param stage API Gateway stage name
         * @return ProxyRequestContext object
         */
        public ProxyRequestContext withStage(String stage) {
            this.setStage(stage);
            return this;
        }

        /**
         * @return resource id
         */
        public String getResourceId() {
            return resourceId;
        }

        /**
         * @param resourceId resource id
         */
        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        /**
         * @param resourceId resource id
         * @return ProxyRequestContext object
         */
        public ProxyRequestContext withResourceId(String resourceId) {
            this.setResourceId(resourceId);
            return this;
        }

        /**
         * @return unique request id
         */
        public String getRequestId() {
            return requestId;
        }

        /**
         * @param requestId unique request id
         */
        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        /**
         * @param requestId unique request id
         * @return ProxyRequestContext object
         */
        public ProxyRequestContext withRequestId(String requestId) {
            this.setRequestId(requestId);
            return this;
        }

        /**
         * @return The identity information for the request caller
         */
        public RequestIdentity getIdentity() {
            return identity;
        }

        /**
         * @param identity The identity information for the request caller
         */
        public void setIdentity(RequestIdentity identity) {
            this.identity = identity;
        }

        /**
         * @param identity The identity information for the request caller
         * @return ProxyRequestContext object
         */
        public ProxyRequestContext withIdentity(RequestIdentity identity) {
            this.setIdentity(identity);
            return this;
        }

        /**
         * @return The resource path defined in API Gateway
         */
        public String getResourcePath() {
            return resourcePath;
        }

        /**
         * @param resourcePath The resource path defined in API Gateway
         */
        public void setResourcePath(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        /**
         * @param resourcePath The resource path defined in API Gateway
         * @return ProxyRequestContext object
         */
        public ProxyRequestContext withResourcePath(String resourcePath) {
            this.setResourcePath(resourcePath);
            return this;
        }

        /**
         * @return The HTTP method used
         */
        public String getHttpMethod() {
            return httpMethod;
        }

        /**
         * @param httpMethod the HTTP method used
         */
        public void setHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
        }

        /**
         * @param httpMethod the HTTP method used
         * @return ProxyRequestContext object
         */
        public ProxyRequestContext withHttpMethod(String httpMethod) {
            this.setHttpMethod(httpMethod);
            return this;
        }

        /**
         * @return The API Gateway rest API Id.
         */
        public String getApiId() {
            return apiId;
        }

        /**
         * @param apiId The API Gateway rest API Id.
         */
        public void setApiId(String apiId) {
            this.apiId = apiId;
        }

        /**
         * @param apiId The API Gateway rest API Id
         * @return ProxyRequestContext object
         */
        public ProxyRequestContext withApiId(String apiId) {
            this.setApiId(apiId);
            return this;
        }

        /**
         * @return The API Gateway path (Does not include base url)
         */
        public String getPath() {
            return this.path;
        }

        /**
         * @param path The API Gateway path (Does not include base url)
         */
        public void setPath(String path) {
            this.path = path;
        }

        /**
         * @param path The API Gateway path (Does not include base url)
         * @return ProxyRequestContext object
         */
        public ProxyRequestContext withPath(String path) {
            this.setPath(path);
            return this;
        }

        /**
         * @return The name of the operation being performed
         * */
        public String getOperationName() {
            return operationName;
        }

        /**
         * @param operationName The name of the operation being performed
         * */
        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }

        public ProxyRequestContext withOperationName(String operationName) {
            this.setOperationName(operationName);
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
            if (getAccountId() != null)
                sb.append("accountId: ").append(getAccountId()).append(",");
            if (getResourceId() != null)
                sb.append("resourceId: ").append(getResourceId()).append(",");
            if (getStage() != null)
                sb.append("stage: ").append(getStage()).append(",");
            if (getRequestId() != null)
                sb.append("requestId: ").append(getRequestId()).append(",");
            if (getIdentity() != null)
                sb.append("identity: ").append(getIdentity().toString()).append(",");
            if (getResourcePath() != null)
                sb.append("resourcePath: ").append(getResourcePath()).append(",");
            if (getHttpMethod() != null)
                sb.append("httpMethod: ").append(getHttpMethod()).append(",");
            if (getApiId() != null)
                sb.append("apiId: ").append(getApiId()).append(",");
            if (getPath() != null)
                sb.append("path: ").append(getPath()).append(",");
            if (getAuthorizer() != null)
                sb.append("authorizer: ").append(getAuthorizer().toString());
            if (getOperationName() != null)
                sb.append("operationName: ").append(getOperationName().toString());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof ProxyRequestContext == false)
                return false;
            ProxyRequestContext other = (ProxyRequestContext) obj;
            if (other.getAccountId() == null ^ this.getAccountId() == null)
                return false;
            if (other.getAccountId() != null && other.getAccountId().equals(this.getAccountId()) == false)
                return false;
            if (other.getResourceId() == null ^ this.getResourceId() == null)
                return false;
            if (other.getResourceId() != null && other.getResourceId().equals(this.getResourceId()) == false)
                return false;
            if (other.getStage() == null ^ this.getStage() == null)
                return false;
            if (other.getStage() != null && other.getStage().equals(this.getStage()) == false)
                return false;
            if (other.getRequestId() == null ^ this.getRequestId() == null)
                return false;
            if (other.getRequestId() != null && other.getRequestId().equals(this.getRequestId()) == false)
                return false;
            if (other.getIdentity() == null ^ this.getIdentity() == null)
                return false;
            if (other.getIdentity() != null && other.getIdentity().equals(this.getIdentity()) == false)
                return false;
            if (other.getResourcePath() == null ^ this.getResourcePath() == null)
                return false;
            if (other.getResourcePath() != null && other.getResourcePath().equals(this.getResourcePath()) == false)
                return false;
            if (other.getHttpMethod() == null ^ this.getHttpMethod() == null)
                return false;
            if (other.getHttpMethod() != null && other.getHttpMethod().equals(this.getHttpMethod()) == false)
                return false;
            if (other.getApiId() == null ^ this.getApiId() == null)
                return false;
            if (other.getApiId() != null && other.getApiId().equals(this.getApiId()) == false)
                return false;
            if (other.getPath() == null ^ this.getPath() == null)
                return false;
            if (other.getPath() != null && other.getPath().equals(this.getPath()) == false)
                return false;
            if (other.getAuthorizer() == null ^ this.getAuthorizer() == null)
                return false;
            if (other.getAuthorizer() != null && !other.getAuthorizer().equals(this.getAuthorizer()))
                return false;
            if (other.getOperationName() == null ^ this.getOperationName() == null)
                return false;
            if (other.getOperationName() != null && !other.getOperationName().equals(this.getOperationName()))
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getAccountId() == null) ? 0 : getAccountId().hashCode());
            hashCode = prime * hashCode + ((getResourceId() == null) ? 0 : getResourceId().hashCode());
            hashCode = prime * hashCode + ((getStage() == null) ? 0 : getStage().hashCode());
            hashCode = prime * hashCode + ((getRequestId() == null) ? 0 : getRequestId().hashCode());
            hashCode = prime * hashCode + ((getIdentity() == null) ? 0 : getIdentity().hashCode());
            hashCode = prime * hashCode + ((getResourcePath() == null) ? 0 : getResourcePath().hashCode());
            hashCode = prime * hashCode + ((getHttpMethod() == null) ? 0 : getHttpMethod().hashCode());
            hashCode = prime * hashCode + ((getApiId() == null) ? 0 : getApiId().hashCode());
            hashCode = prime * hashCode + ((getPath() == null) ? 0 : getPath().hashCode());
            hashCode = prime * hashCode + ((getAuthorizer() == null) ? 0 : getAuthorizer().hashCode());
            hashCode = prime * hashCode + ((getOperationName() == null) ? 0: getOperationName().hashCode());
            return hashCode;
        }

        @Override
        public ProxyRequestContext clone() {
            try {
                return (ProxyRequestContext) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    public static class RequestIdentity implements Serializable, Cloneable {

        private static final long serialVersionUID = -5283829736983640346L;

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

        /**
         * default constructor
         */
        public RequestIdentity() {}

        /**
         * @return The Cognito identity pool id.
         */
        public String getCognitoIdentityPoolId() {
            return cognitoIdentityPoolId;
        }

        /**
         * @param cognitoIdentityPoolId The Cognito identity pool id.
         */
        public void setCognitoIdentityPoolId(String cognitoIdentityPoolId) {
            this.cognitoIdentityPoolId = cognitoIdentityPoolId;
        }

        /**
         * @param cognitoIdentityPoolId The Cognito Identity pool id
         * @return RequestIdentity object
         */
        public RequestIdentity withCognitoIdentityPoolId(String cognitoIdentityPoolId) {
            this.setCognitoIdentityPoolId(cognitoIdentityPoolId);
            return this;
        }

        /**
         * @return The account id that owns the executing Lambda function
         */
        public String getAccountId() {
            return accountId;
        }

        /**
         * @param accountId The account id that owns the executing Lambda function
         */
        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        /**
         * @param accountId The account id that owns the executing Lambda function
         * @return RequestIdentity object
         */
        public RequestIdentity withAccountId(String accountId) {
            this.setAccountId(accountId);
            return this;
        }

        /**
         * @return The cognito identity id.
         */
        public String getCognitoIdentityId() {
            return cognitoIdentityId;
        }

        /**
         * @param cognitoIdentityId The cognito identity id.
         */
        public void setCognitoIdentityId(String cognitoIdentityId) {
            this.cognitoIdentityId = cognitoIdentityId;
        }

        /**
         * @param cognitoIdentityId The cognito identity id
         * @return RequestIdentity object
         */
        public RequestIdentity withCognitoIdentityId(String cognitoIdentityId) {
            this.setCognitoIdentityId(cognitoIdentityId);
            return this;
        }

        /**
         * @return the caller
         */
        public String getCaller() {
            return caller;
        }

        /**
         * @param caller the caller
         */
        public void setCaller(String caller) {
            this.caller = caller;
        }

        /**
         * @param caller the caller
         * @return RequestIdentity object
         */
        public RequestIdentity withCaller(String caller) {
            this.setCaller(caller);
            return this;
        }

        /**
         * @return the api key
         */
        public String getApiKey() {
            return apiKey;
        }

        /**
         * @param apiKey the api key
         */
        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        /**
         * @param apiKey the api key
         * @return RequestIdentity object
         */
        public RequestIdentity withApiKey(String apiKey) {
            this.setApiKey(apiKey);
            return this;
        }

        /**
         * @return source ip address
         */
        public String getSourceIp() {
            return sourceIp;
        }

        /**
         * @param sourceIp source ip address
         */
        public void setSourceIp(String sourceIp) {
            this.sourceIp = sourceIp;
        }

        /**
         * @param sourceIp source ip address
         * @return RequestIdentity object
         */
        public RequestIdentity withSourceIp(String sourceIp) {
            this.setSourceIp(sourceIp);
            return this;
        }

        /**
         * @return The Cognito authentication type used for authentication
         */
        public String getCognitoAuthenticationType() {
            return cognitoAuthenticationType;
        }

        /**
         * @param cognitoAuthenticationType The Cognito authentication type used for authentication
         */
        public void setCognitoAuthenticationType(String cognitoAuthenticationType) {
            this.cognitoAuthenticationType = cognitoAuthenticationType;
        }

        /**
         * @param cognitoAuthenticationType The Cognito authentication type used for authentication
         * @return
         */
        public RequestIdentity withCognitoAuthenticationType(String cognitoAuthenticationType) {
            this.setCognitoAuthenticationType(cognitoAuthenticationType);
            return this;
        }

        /**
         * @return The Cognito authentication provider
         */
        public String getCognitoAuthenticationProvider() {
            return cognitoAuthenticationProvider;
        }

        /**
         * @param cognitoAuthenticationProvider The Cognito authentication provider
         */
        public void setCognitoAuthenticationProvider(String cognitoAuthenticationProvider) {
            this.cognitoAuthenticationProvider = cognitoAuthenticationProvider;
        }

        /**
         * @param cognitoAuthenticationProvider The Cognito authentication provider
         * @return RequestIdentity object
         */
        public RequestIdentity withCognitoAuthenticationProvider(String cognitoAuthenticationProvider) {
            this.setCognitoAuthenticationProvider(cognitoAuthenticationProvider);
            return this;
        }

        /**
         * @return the user arn
         */
        public String getUserArn() {
            return userArn;
        }

        /**
         * @param userArn user arn
         */
        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        /**
         * @param userArn user arn
         * @return RequestIdentity object
         */
        public RequestIdentity withUserArn(String userArn) {
            this.setUserArn(userArn);
            return this;
        }

        /**
         * @return user agent
         */
        public String getUserAgent() {
            return userAgent;
        }

        /**
         * @param userAgent user agent
         */
        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        /**
         * @param userAgent user agent
         * @return RequestIdentityType
         */
        public RequestIdentity withUserAgent(String userAgent) {
            this.setUserAgent(userAgent);
            return this;
        }

        /**
         * @return user
         */
        public String getUser() {
            return user;
        }

        /**
         * @param user user
         */
        public void setUser(String user) {
            this.user = user;
        }

        /**
         * @param user user
         * @return RequestIdentity
         */
        public RequestIdentity withUser(String user) {
            this.setUser(user);
            return this;
        }

        /**
         * @return access key
         */
        public String getAccessKey() {
            return this.accessKey;
        }

        /**
         * @param accessKey Cognito access key
         */
        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        /**
         * @param accessKey Cognito access key
         * @return RequestIdentity
         */
        public RequestIdentity withAccessKey(String accessKey) {
            this.setAccessKey(accessKey);
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
            if (getCognitoIdentityPoolId() != null)
                sb.append("cognitoIdentityPoolId: ").append(getCognitoIdentityPoolId()).append(",");
            if (getAccountId() != null)
                sb.append("accountId: ").append(getAccountId()).append(",");
            if (getCognitoIdentityId() != null)
                sb.append("cognitoIdentityId: ").append(getCognitoIdentityId()).append(",");
            if (getCaller() != null)
                sb.append("caller: ").append(getCaller()).append(",");
            if (getApiKey() != null)
                sb.append("apiKey: ").append(getApiKey()).append(",");
            if (getSourceIp() != null)
                sb.append("sourceIp: ").append(getSourceIp()).append(",");
            if (getCognitoAuthenticationType() != null)
                sb.append("eventTriggerConfigId: ").append(getCognitoAuthenticationType()).append(",");
            if (getCognitoAuthenticationProvider() != null)
                sb.append("cognitoAuthenticationProvider: ").append(getCognitoAuthenticationProvider()).append(",");
            if (getUserArn() != null)
                sb.append("userArn: ").append(getUserArn()).append(",");
            if (getUserAgent() != null)
                sb.append("userAgent: ").append(getUserAgent()).append(",");
            if (getUser() != null)
                sb.append("user: ").append(getUser()).append(",");
            if (getAccessKey() != null)
                sb.append("accessKey: ").append(getAccessKey());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof RequestIdentity == false)
                return false;
            RequestIdentity other = (RequestIdentity) obj;
            if (other.getCognitoIdentityPoolId() == null ^ this.getCognitoIdentityPoolId() == null)
                return false;
            if (other.getCognitoIdentityPoolId() != null && other.getCognitoIdentityPoolId().equals(this.getCognitoIdentityPoolId()) == false)
                return false;
            if (other.getAccountId() == null ^ this.getAccountId() == null)
                return false;
            if (other.getAccountId() != null && other.getAccountId().equals(this.getAccountId()) == false)
                return false;
            if (other.getCognitoIdentityId() == null ^ this.getCognitoIdentityId() == null)
                return false;
            if (other.getCognitoIdentityId() != null && other.getCognitoIdentityId().equals(this.getCognitoIdentityId()) == false)
                return false;
            if (other.getCaller() == null ^ this.getCaller() == null)
                return false;
            if (other.getCaller() != null && other.getCaller().equals(this.getCaller()) == false)
                return false;
            if (other.getApiKey() == null ^ this.getApiKey() == null)
                return false;
            if (other.getApiKey() != null && other.getApiKey().equals(this.getApiKey()) == false)
                return false;
            if (other.getSourceIp() == null ^ this.getSourceIp() == null)
                return false;
            if (other.getSourceIp() != null && other.getSourceIp().equals(this.getSourceIp()) == false)
                return false;
            if (other.getCognitoAuthenticationType() == null ^ this.getCognitoAuthenticationType() == null)
                return false;
            if (other.getCognitoAuthenticationType() != null && other.getCognitoAuthenticationType().equals(this.getCognitoAuthenticationType()) == false)
                return false;
            if (other.getCognitoAuthenticationProvider() == null ^ this.getCognitoAuthenticationProvider() == null)
                return false;
            if (other.getCognitoAuthenticationProvider() != null && other.getCognitoAuthenticationProvider().equals(this.getCognitoAuthenticationProvider()) == false)
                return false;
            if (other.getUserArn() == null ^ this.getUserArn() == null)
                return false;
            if (other.getUserArn() != null && other.getUserArn().equals(this.getUserArn()) == false)
                return false;
            if (other.getUserAgent() == null ^ this.getUserAgent() == null)
                return false;
            if (other.getUserAgent() != null && other.getUserAgent().equals(this.getUserAgent()) == false)
                return false;
            if (other.getUser() == null ^ this.getUser() == null)
                return false;
            if (other.getUser() != null && other.getUser().equals(this.getUser()) == false)
                return false;
            if (other.getAccessKey() == null ^ this.getAccessKey() == null)
                return false;
            if (other.getAccessKey() != null && other.getAccessKey().equals(this.getAccessKey()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getCognitoIdentityPoolId() == null) ? 0 : getCognitoIdentityPoolId().hashCode());
            hashCode = prime * hashCode + ((getAccountId() == null) ? 0 : getAccountId().hashCode());
            hashCode = prime * hashCode + ((getCognitoIdentityId() == null) ? 0 : getCognitoIdentityId().hashCode());
            hashCode = prime * hashCode + ((getCognitoIdentityId() == null) ? 0 : getCognitoIdentityId().hashCode());
            hashCode = prime * hashCode + ((getCaller() == null) ? 0 : getCaller().hashCode());
            hashCode = prime * hashCode + ((getApiKey() == null) ? 0 : getApiKey().hashCode());
            hashCode = prime * hashCode + ((getSourceIp() == null) ? 0 : getSourceIp().hashCode());
            hashCode = prime * hashCode + ((getCognitoAuthenticationType() == null) ? 0 : getCognitoAuthenticationType().hashCode());
            hashCode = prime * hashCode + ((getCognitoAuthenticationProvider() == null) ? 0 : getCognitoAuthenticationProvider().hashCode());
            hashCode = prime * hashCode + ((getUserArn() == null) ? 0 : getUserArn().hashCode());
            hashCode = prime * hashCode + ((getUserAgent() == null) ? 0 : getUserAgent().hashCode());
            hashCode = prime * hashCode + ((getUser() == null) ? 0 : getUser().hashCode());
            hashCode = prime * hashCode + ((getAccessKey() == null) ? 0 : getAccessKey().hashCode());
            return hashCode;
        }

        @Override
        public RequestIdentity clone() {
            try {
                return (RequestIdentity) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * default constructor
     */
    public APIGatewayProxyRequestEvent() {}

    /**
     * @return The payload format version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version The payload format version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @param version The payload format version
     * @return
     */
    public APIGatewayProxyRequestEvent withVersion(String version) {
        this.setVersion(version);
        return this;
    }

    /**
     * @return The resource path defined in API Gateway
     */
    public String getResource() {
        return resource;
    }

    /**
     * @param resource The resource path defined in API Gateway
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * @param resource The resource path defined in API Gateway
     * @return
     */
    public APIGatewayProxyRequestEvent withResource(String resource) {
        this.setResource(resource);
        return this;
    }

    /**
     * @return The url path for the caller
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path The url path for the caller
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @param path The url path for the caller
     * @return APIGatewayProxyRequestEvent object
     */
    public APIGatewayProxyRequestEvent withPath(String path) {
        this.setPath(path);
        return this;
    }

    /**
     * @return The HTTP method used
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * @param httpMethod The HTTP method used
     */
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * @param httpMethod The HTTP method used
     * @return APIGatewayProxyRequestEvent
     */
    public APIGatewayProxyRequestEvent withHttpMethod(String httpMethod) {
        this.setHttpMethod(httpMethod);
        return this;
    }

    /**
     * @return The headers sent with the request
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers The headers sent with the request
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @param headers The headers sent with the request
     * @return APIGatewayProxyRequestEvent object
     */
    public APIGatewayProxyRequestEvent withHeaders(Map<String, String> headers) {
        this.setHeaders(headers);
        return this;
    }

    /**
     * @return The multi value headers sent with the request
     */
    public Map<String, List<String>> getMultiValueHeaders() {
        return multiValueHeaders;
    }

    /**
     * @param multiValueHeaders The multi value headers sent with the request
     */
    public void setMultiValueHeaders(Map<String, List<String>> multiValueHeaders) {
        this.multiValueHeaders = multiValueHeaders;
    }

    /**
     * @param multiValueHeaders The multi value headers sent with the request
     * @return APIGatewayProxyRequestEvent object
     */
    public APIGatewayProxyRequestEvent withMultiValueHeaders(Map<String, List<String>> multiValueHeaders) {
        this.setMultiValueHeaders(multiValueHeaders);
        return this;
    }

    /**
     * @return The query string parameters that were part of the request
     */
    public Map<String, String> getQueryStringParameters() {
        return queryStringParameters;
    }

    /**
     * @param queryStringParameters The query string parameters that were part of the request
     */
    public void setQueryStringParameters(Map<String, String> queryStringParameters) {
        this.queryStringParameters = queryStringParameters;
    }

    /**
     * @param queryStringParameters The query string parameters that were part of the request
     * @return APIGatewayProxyRequestEvent
     */
    public APIGatewayProxyRequestEvent withQueryStringParameters(Map<String, String> queryStringParameters) {
        this.setQueryStringParameters(queryStringParameters);
        return this;
    }

    /**
     * @deprecated Because of typo in method's name, use {@link #withQueryStringParameters} instead.
     */
    @Deprecated
    public APIGatewayProxyRequestEvent withQueryStringParamters(Map<String, String> queryStringParameters) {
        return withQueryStringParameters(queryStringParameters);
    }

    /**
     * @return The multi value query string parameters that were part of the request
     */
    public Map<String, List<String>> getMultiValueQueryStringParameters() {
        return multiValueQueryStringParameters;
    }

    /**
     * @param multiValueQueryStringParameters The multi value query string parameters that were part of the request
     */
    public void setMultiValueQueryStringParameters(Map<String, List<String>> multiValueQueryStringParameters) {
        this.multiValueQueryStringParameters = multiValueQueryStringParameters;
    }

    /**
     * @param multiValueQueryStringParameters The multi value query string parameters that were part of the request
     * @return APIGatewayProxyRequestEvent
     */
    public APIGatewayProxyRequestEvent withMultiValueQueryStringParameters(Map<String, List<String>> multiValueQueryStringParameters) {
        this.setMultiValueQueryStringParameters(multiValueQueryStringParameters);
        return this;
    }

    /**
     * @return The path parameters that were part of the request
     */
    public Map<String, String> getPathParameters() {
        return pathParameters;
    }

    /**
     * @param pathParameters The path parameters that were part of the request
     */
    public void setPathParameters(Map<String, String> pathParameters) {
        this.pathParameters = pathParameters;
    }

    /**
     * @param pathParameters The path parameters that were part of the request
     * @return APIGatewayProxyRequestEvent object
     */
    public APIGatewayProxyRequestEvent withPathParameters(Map<String, String> pathParameters) {
        this.setPathParameters(pathParameters);
        return this;
    }

    /**
     * @deprecated Because of typo in method's name, use {@link #withPathParameters} instead.
     */
    @Deprecated
    public APIGatewayProxyRequestEvent withPathParamters(Map<String, String> pathParameters) {
        return withPathParameters(pathParameters);
    }

    /**
     * @return The stage variables defined for the stage in API Gateway
     */
    public Map<String, String> getStageVariables() {
        return stageVariables;
    }

    /**
     * @param stageVariables The stage variables defined for the stage in API Gateway
     */
    public void setStageVariables(Map<String, String> stageVariables) {
        this.stageVariables = stageVariables;
    }

    /**
     * @param stageVariables The stage variables defined for the stage in API Gateway
     * @return APIGatewayProxyRequestEvent
     */
    public APIGatewayProxyRequestEvent withStageVariables(Map<String, String> stageVariables) {
        this.setStageVariables(stageVariables);
        return this;
    }

    /**
     * @return The request context for the request
     */
    public ProxyRequestContext getRequestContext() {
        return requestContext;
    }

    /**
     * @param requestContext The request context for the request
     */
    public void setRequestContext(ProxyRequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * @param requestContext The request context for the request
     * @return APIGatewayProxyRequestEvent object
     */
    public APIGatewayProxyRequestEvent withRequestContext(ProxyRequestContext requestContext) {
        this.setRequestContext(requestContext);
        return this;
    }

    /**
     * @return The HTTP request body.
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body The HTTP request body.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @param body The HTTP request body
     * @return APIGatewayProxyRequestEvent
     */
    public APIGatewayProxyRequestEvent withBody(String body) {
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
    public APIGatewayProxyRequestEvent withIsBase64Encoded(Boolean isBase64Encoded) {
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
        if (getVersion() != null)
            sb.append("version: ").append(getVersion()).append(",");
        if (getResource() != null)
            sb.append("resource: ").append(getResource()).append(",");
        if (getPath() != null)
            sb.append("path: ").append(getPath()).append(",");
        if (getHttpMethod() != null)
            sb.append("httpMethod: ").append(getHttpMethod()).append(",");
        if (getHeaders() != null)
            sb.append("headers: ").append(getHeaders().toString()).append(",");
        if (getMultiValueHeaders() != null)
            sb.append("multiValueHeaders: ").append(getMultiValueHeaders().toString()).append(",");
        if (getQueryStringParameters() != null)
            sb.append("queryStringParameters: ").append(getQueryStringParameters().toString()).append(",");
        if (getMultiValueQueryStringParameters() != null)
            sb.append("multiValueQueryStringParameters: ").append(getMultiValueQueryStringParameters().toString()).append(",");
        if (getPathParameters() != null)
            sb.append("pathParameters: ").append(getPathParameters().toString()).append(",");
        if (getStageVariables() != null)
            sb.append("stageVariables: ").append(getStageVariables().toString()).append(",");
        if (getRequestContext() != null)
            sb.append("requestContext: ").append(getRequestContext().toString()).append(",");
        if (getBody() != null)
            sb.append("body: ").append(getBody()).append(",");
        if (getIsBase64Encoded() != null)
            sb.append("isBase64Encoded: ").append(getIsBase64Encoded());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof APIGatewayProxyRequestEvent == false)
            return false;
        APIGatewayProxyRequestEvent other = (APIGatewayProxyRequestEvent) obj;
        if (other.getVersion() == null ^ this.getVersion() == null)
            return false;
        if (other.getVersion() != null && other.getVersion().equals(this.getVersion()) == false)
            return false;
        if (other.getResource() == null ^ this.getResource() == null)
            return false;
        if (other.getResource() != null && other.getResource().equals(this.getResource()) == false)
            return false;
        if (other.getPath() == null ^ this.getPath() == null)
            return false;
        if (other.getPath() != null && other.getPath().equals(this.getPath()) == false)
            return false;
        if (other.getHttpMethod() == null ^ this.getHttpMethod() == null)
            return false;
        if (other.getHttpMethod() != null && other.getHttpMethod().equals(this.getHttpMethod()) == false)
            return false;
        if (other.getHeaders() == null ^ this.getHeaders() == null)
            return false;
        if (other.getHeaders() != null && other.getHeaders().equals(this.getHeaders()) == false)
            return false;
        if (other.getMultiValueHeaders() == null ^ this.getMultiValueHeaders() == null)
            return false;
        if (other.getMultiValueHeaders() != null && other.getMultiValueHeaders().equals(this.getMultiValueHeaders()) == false)
            return false;
        if (other.getQueryStringParameters() == null ^ this.getQueryStringParameters() == null)
            return false;
        if (other.getQueryStringParameters() != null && other.getQueryStringParameters().equals(this.getQueryStringParameters()) == false)
            return false;
        if (other.getMultiValueQueryStringParameters() == null ^ this.getMultiValueQueryStringParameters() == null)
            return false;
        if (other.getMultiValueQueryStringParameters() != null && other.getMultiValueQueryStringParameters().equals(this.getMultiValueQueryStringParameters()) == false)
            return false;
        if (other.getPathParameters() == null ^ this.getPathParameters() == null)
            return false;
        if (other.getPathParameters() != null && other.getPathParameters().equals(this.getPathParameters()) == false)
            return false;
        if (other.getStageVariables() == null ^ this.getStageVariables() == null)
            return false;
        if (other.getStageVariables() != null && other.getStageVariables().equals(this.getStageVariables()) == false)
            return false;
        if (other.getRequestContext() == null ^ this.getRequestContext() == null)
            return false;
        if (other.getRequestContext() != null && other.getRequestContext().equals(this.getRequestContext()) == false)
            return false;
        if (other.getBody() == null ^ this.getBody() == null)
            return false;
        if (other.getBody() != null && other.getBody().equals(this.getBody()) == false)
            return false;
        if (other.getIsBase64Encoded() == null ^ this.getIsBase64Encoded() == null)
            return false;
        if (other.getIsBase64Encoded() != null && other.getIsBase64Encoded().equals(this.getIsBase64Encoded()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getVersion() == null) ? 0 : getVersion().hashCode());
        hashCode = prime * hashCode + ((getResource() == null) ? 0 : getResource().hashCode());
        hashCode = prime * hashCode + ((getPath() == null) ? 0 : getPath().hashCode());
        hashCode = prime * hashCode + ((getHttpMethod() == null) ? 0 : getHttpMethod().hashCode());
        hashCode = prime * hashCode + ((getHeaders() == null) ? 0 : getHeaders().hashCode());
        hashCode = prime * hashCode + ((getMultiValueHeaders() == null) ? 0 : getMultiValueHeaders().hashCode());
        hashCode = prime * hashCode + ((getQueryStringParameters() == null) ? 0 : getQueryStringParameters().hashCode());
        hashCode = prime * hashCode + ((getMultiValueQueryStringParameters() == null) ? 0 : getMultiValueQueryStringParameters().hashCode());
        hashCode = prime * hashCode + ((getPathParameters() == null) ? 0 : getPathParameters().hashCode());
        hashCode = prime * hashCode + ((getStageVariables() == null) ? 0 : getStageVariables().hashCode());
        hashCode = prime * hashCode + ((getRequestContext() == null) ? 0 : getRequestContext().hashCode());
        hashCode = prime * hashCode + ((getBody() == null) ? 0 : getBody().hashCode());
        hashCode = prime * hashCode + ((getIsBase64Encoded() == null) ? 0 : getIsBase64Encoded().hashCode());
        return hashCode;
    }

    @Override
    public APIGatewayProxyRequestEvent clone() {
        try {
            return (APIGatewayProxyRequestEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}
