package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to authorize an IoT core request
 *
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class IoTCustomAuthorizerEvent implements Serializable {

	private static final long serialVersionUID = -6593017563444769208L;

	/**
	 * The token if it's authenticated
	 */
	private String token;

	/**
	 * Indicates whether the device gateway has validated the signature.
	 */
	private Boolean signatureVerified;

	/**
	 * Indicates which protocols to expect for the request.
	 */
	private EnumSet<Protocol> protocols;

	private ProtocolData protocolData;

	private ConnectionMetadata connectionMetadata;
	
	public enum Protocol {
		@JsonProperty("tls") TLS, 
		@JsonProperty("http") HTTP,
		@JsonProperty("mqtt") MQTT
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder(setterPrefix = "with")
	public static class ProtocolData {
		private Tls tls;
		private Http http;
		private Mqtt mqtt;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder(setterPrefix = "with")
	public static class Tls {
		/**
		 * The server name indication (SNI) host_name string.
		 */
		private String serverName;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder(setterPrefix = "with")
	public static class Http {
		private Map<String, String> headers;
		private String queryString;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder(setterPrefix = "with")
	public static class Mqtt {
		private String username;
		/**
		 * A base64-encoded string
		 */
		private String password;
		/**
		 * Included in the event only when the device sends the value.
		 */
		private String clientId;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder(setterPrefix = "with")
	public static class ConnectionMetadata {
		/**
		 * The connection ID. You can use this for logging.
		 */
		private UUID id;
	}
}
