package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class IoTCustomAuthorizerResponse implements Serializable {

	private static final long serialVersionUID = -5207837140558617153L;

	/**
	 * A Boolean value that indicates whether the request is authenticated.
	 */
	private Boolean isAuthenticated;

	/**
	 * An alphanumeric string that acts as an identifier for the token sent by the
	 * custom authorization request. The value must be an alphanumeric string with
	 * at least one, and no more than 128, characters and match this regular
	 * expression (regex) pattern: ([a-zA-Z0-9]){1,128}.
	 */
	private String principalId;

	/**
	 * A list of JSON-formatted AWS IoT Core policy documents. For more information
	 * about creating AWS IoT Core policies, see <a href=
	 * "https://docs.aws.amazon.com/iot/latest/developerguide/iot-policies.html">
	 * AWS IoT Core policies</a>. The maximum number of policy documents is 10
	 * policy documents. Each policy document can contain a maximum of 2,048
	 * characters.
	 */
	private List<PolicyDocument> policyDocuments;

	/**
	 * An integer that specifies the maximum duration (in seconds) of the connection
	 * to the AWS IoT Core gateway. The minimum value is 300 seconds, and the
	 * maximum value is 86,400 seconds.
	 */
	private Integer disconnectAfterInSeconds;

	/**
	 * An integer that specifies the interval between policy refreshes. When this
	 * interval passes, AWS IoT Core invokes the Lambda function to allow for policy
	 * refreshes. The minimum value is 300 seconds, and the maximum value is 86,400
	 * seconds.
	 */
	private Integer refreshAfterInSeconds;

	@Data
	@Builder(setterPrefix = "with")
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PolicyDocument implements Serializable {

		private static final long serialVersionUID = 7910390256205634020L;

		@JsonProperty("Version")
		private String version;
		@JsonProperty("Statement")
		private List<Statement> statement;
	}

	@Data
	@Builder(setterPrefix = "with")
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Statement implements Serializable {

		private static final long serialVersionUID = 5337749742653523366L;

		@JsonProperty("Action")
		private List<String> action;
		@JsonProperty("Effect")
		private String effect;
		@JsonProperty("Resource")
		private List<String> resource;
		@JsonProperty("Condition")
		private Map<String, Map<String, Object>> condition;
	}
}
