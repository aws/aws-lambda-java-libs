package com.amazonaws.services.lambda.runtime.events;

import java.util.Map;

/**
 * Represents an API Gateway request using Lambda Proxy Integration.
 *
 */
public class LambdaProxyEvent {
    public static class RequestContext {
    	private String accountId;
    	
    	private String resourceId;
    	
    	private String stage;
    	
    	private String requestId;
    	
    	private Identity identity;
    	
    	private String resourcePath;
    	
    	private String httpMethod;
    	
    	private String apiId;

		/**
		 * Gets the account ID
		 */
		public String getAccountId() {
			return accountId;
		}

		/**
		 * Sets the account ID
		 * @param accountId The account ID to set
		 */
		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}

		/**
		 * Gets the resource ID
		 */
		public String getResourceId() {
			return resourceId;
		}

		/**
		 * Sets the resource ID
		 * @param resourceId The resource ID to set
		 */
		public void setResourceId(String resourceId) {
			this.resourceId = resourceId;
		}

		/**
		 * Gets the stage
		 */
		public String getStage() {
			return stage;
		}

		/**
		 * Sets the stage
		 * @param stage The stage to set
		 */
		public void setStage(String stage) {
			this.stage = stage;
		}

		/**
		 * Gets the request ID
		 */
		public String getRequestId() {
			return requestId;
		}

		/**
		 * Sets the request ID
		 * @param requestId The request ID to set
		 */
		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		/**
		 * Gets the identity
		 */
		public Identity getIdentity() {
			return identity;
		}

		/**
		 * Sets the identity
		 * @param identity The identity to set
		 */
		public void setIdentity(Identity identity) {
			this.identity = identity;
		}

		/**
		 * Gets the resource path
		 */
		public String getResourcePath() {
			return resourcePath;
		}

		/**
		 * Sets the resource path
		 * @param resourcePath The resource path to set
		 */
		public void setResourcePath(String resourcePath) {
			this.resourcePath = resourcePath;
		}

		/**
		 * Gets the HTTP method
		 */
		public String getHttpMethod() {
			return httpMethod;
		}

		/**
		 * Sets the HTTP method
		 * @param httpMethod The HTTP method to set
		 */
		public void setHttpMethod(String httpMethod) {
			this.httpMethod = httpMethod;
		}

		/**
		 * Gets the API ID
		 */
		public String getApiId() {
			return apiId;
		}

		/**
		 * Sets the API ID
		 * @param apiId The API ID to set
		 */
		public void setApiId(String apiId) {
			this.apiId = apiId;
		}
    }
    
    public static class Identity {
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

		/**
		 * Gets the Cognito Identity Pool ID
		 */
		public String getCognitoIdentityPoolId() {
			return cognitoIdentityPoolId;
		}

		/**
		 * Sets the Cognito Identity Pool ID
		 * @param cognitoIdentityPoolId The Cognito Identity Pool to set
		 */
		public void setCognitoIdentityPoolId(String cognitoIdentityPoolId) {
			this.cognitoIdentityPoolId = cognitoIdentityPoolId;
		}

		/**
		 * Gets the account ID
		 */
		public String getAccountId() {
			return accountId;
		}

		/**
		 * Sets the account ID
		 * @param accountId The Account ID to set
		 */
		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}

		/**
		 * Gets the Cognito Identity ID
		 */
		public String getCognitoIdentityId() {
			return cognitoIdentityId;
		}

		/**
		 * Sets the Cognito Identity ID
		 * @param cognitoIdentityId The Cognito Identity ID to set
		 */
		public void setCognitoIdentityId(String cognitoIdentityId) {
			this.cognitoIdentityId = cognitoIdentityId;
		}

		/**
		 * Gets the caller
		 */
		public String getCaller() {
			return caller;
		}

		/**
		 * Sets the caller
		 * @param caller The caller to set
		 */
		public void setCaller(String caller) {
			this.caller = caller;
		}

		/**
		 * Gets the API key
		 */
		public String getApiKey() {
			return apiKey;
		}

		/**
		 * Sets the API key
		 * @param apiKey The API key to set
		 */
		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}

		/**
		 * Gets the source IP
		 */
		public String getSourceIp() {
			return sourceIp;
		}

		/**
		 * Sets the source IP
		 * @param sourceIp The source IP to set
		 */
		public void setSourceIp(String sourceIp) {
			this.sourceIp = sourceIp;
		}

		/**
		 * Gets the Cognito Authentication Type
		 */
		public String getCognitoAuthenticationType() {
			return cognitoAuthenticationType;
		}

		/**
		 * Sets the Cognito Authentication Type
		 * @param cognitoAuthenticationType The Cognito Authentication Type to set
		 */
		public void setCognitoAuthenticationType(String cognitoAuthenticationType) {
			this.cognitoAuthenticationType = cognitoAuthenticationType;
		}

		/**
		 * Gets the Cognito Authentication Provider
		 */
		public String getCognitoAuthenticationProvider() {
			return cognitoAuthenticationProvider;
		}

		/**
		 * Sets the Cognito Authentication Provider
		 * @param cognitoAuthenticationProvider The Cognito Authentication Provider to set
		 */
		public void setCognitoAuthenticationProvider(String cognitoAuthenticationProvider) {
			this.cognitoAuthenticationProvider = cognitoAuthenticationProvider;
		}

		/**
		 * Gets the user ARN
		 */
		public String getUserArn() {
			return userArn;
		}

		/**
		 * Sets the user ARN
		 * @param userArn The user ARN to set
		 */
		public void setUserArn(String userArn) {
			this.userArn = userArn;
		}

		/**
		 * Gets the user agent
		 */
		public String getUserAgent() {
			return userAgent;
		}

		/**
		 * Sets the user agent
		 * @param userAgent The user agent to set
		 */
		public void setUserAgent(String userAgent) {
			this.userAgent = userAgent;
		}

		/**
		 * Gets the user
		 */
		public String getUser() {
			return user;
		}

		/**
		 * Sets the user
		 * @param user The user to set
		 */
		public void setUser(String user) {
			this.user = user;
		}
    }
    
    private String resource;
    
    private String path;
    
    private String httpMethod;
    
    private Map<String,String> headers;
    
    private Map<String,String> queryStringParameters;
    
    private Map<String,String> pathParameters;
    
    private Map<String,String> stageVariables;
    
    private RequestContext requestContext;
    
    private String body;
    
    private boolean isBase64Encoded;

    /**
     * Gets the 'resource' field (e.g. "/{proxy+}")
     */
	public String getResource() {
		return resource;
	}

	/**
     * Sets the 'resource' field
     * @param resource A string containing the 'resource' field
     */
	public void setResource(String resource) {
		this.resource = resource;
	}

	/**
     * Gets the 'path' field (e.g. "/hello/world")
     */
	public String getPath() {
		return path;
	}

	/**
     * Sets the 'path' field
     * @param path A string containing the 'path' field
     */
	public void setPath(String path) {
		this.path = path;
	}

	/**
     * Gets the 'httpMethod' field (e.g. "POST")
     */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
     * Sets the 'httpMethod' field
     * @param httpMethod A string containing the 'httpMethod' field
     */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
     * Gets the request headers
     */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
     * Sets the request headers
     * @param headers Map containing all the request headers
     */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
     * Gets the query string parameters
     */
	public Map<String, String> getQueryStringParameters() {
		return queryStringParameters;
	}

	/**
     * Sets the query string parameters
     * @param queryStringParameters Map containing all the query string parameters
     */
	public void setQueryStringParameters(Map<String, String> queryStringParameters) {
		this.queryStringParameters = queryStringParameters;
	}

	/**
     * Gets the path parameters
     */
	public Map<String, String> getPathParameters() {
		return pathParameters;
	}

	/**
     * Sets the path parameters
     * @param pathParameters Map containing all the path parameters
     */
	public void setPathParameters(Map<String, String> pathParameters) {
		this.pathParameters = pathParameters;
	}

	/**
     * Gets the stage variables
     */
	public Map<String, String> getStageVariables() {
		return stageVariables;
	}

	/**
     * Sets the stage variables
     * @param stageVariables Map containing all the stage variables
     */
	public void setStageVariables(Map<String, String> stageVariables) {
		this.stageVariables = stageVariables;
	}

	/**
     * Gets the request context
     */
	public RequestContext getRequestContext() {
		return requestContext;
	}

	/**
     * Sets the request context
     * @param requestContext The request context
     */
	public void setRequestContext(RequestContext requestContext) {
		this.requestContext = requestContext;
	}

	/**
     * Gets the request body
     */
	public String getBody() {
		return body;
	}

	/**
     * Sets the request body
     * @param body A string containing the request body
     */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Tells whether the request body is encoded in Base64
	 */
	public boolean isBase64Encoded() {
		return isBase64Encoded;
	}

	/**
	 * Sets the Base64 encoding parameter
	 * @param isBase64Encoded Whether or not the body is Base64 encoded
	 */
	public void setBase64Encoded(boolean isBase64Encoded) {
		this.isBase64Encoded = isBase64Encoded;
	}
}
