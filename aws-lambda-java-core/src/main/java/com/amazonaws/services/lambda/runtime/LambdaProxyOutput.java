package com.amazonaws.services.lambda.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps the output expected by the Lambda Proxy Integration.
 */
public class LambdaProxyOutput {
	private int statusCode = 200;
	
	private Map<String,String> headers = new HashMap<String,String>();
	
	private String body = "";
	
	/**
	 * Creates an output with status code 200 (OK),
	 * no headers and an empty body
	 */
	public LambdaProxyOutput() {
		
	}
	
	/**
	 * Creates an output with the provided status
	 * code, no headers and an empty body
	 * @param statusCode The response status code
	 */
	public LambdaProxyOutput(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * Creates an output with the provided parameters
	 * @param statusCode The response status code
	 * @param headers The headers
	 * @param body The response body
	 */
	public LambdaProxyOutput(int statusCode, Map<String,String> headers, String body) {
		this.statusCode = statusCode;
		this.headers = headers;
		this.body = body;
	}

	/**
	 * Gets the response status code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the status code of this response
	 * @param statusCode The status code to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Gets the headers of this response
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Sets the headers of this response
	 * @param headers The headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	/**
	 * Appends a header to this response
	 * @param header The header name
	 * @param value The header value
	 */
	public void addHeader(String header, String value) {
		this.headers.put(header, value);
	}

	/**
	 * Gets the response body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Sets the response body
	 * @param body The response body
	 */
	public void setBody(String body) {
		this.body = body;
	}
}
