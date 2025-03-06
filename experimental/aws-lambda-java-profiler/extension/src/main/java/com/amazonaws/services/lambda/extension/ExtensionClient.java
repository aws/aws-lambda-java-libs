// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.services.lambda.extension;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * Utility class that takes care of registration of extension, fetching the next event, initializing
 * and exiting with error
 */
public class ExtensionClient {
	private static final String EXTENSION_NAME = "profiler-extension";
	private static final String BASEURL = String
			.format("http://%s/2020-01-01/extension", System.getenv("AWS_LAMBDA_RUNTIME_API"));
	private static final String BODY = "{" +
			"            \"events\": [" +
			"                \"INVOKE\"," +
			"                \"SHUTDOWN\"" +
			"            ]" +
			"        }";
	private static final String LAMBDA_EXTENSION_IDENTIFIER = "Lambda-Extension-Identifier";
	private static final HttpClient client = HttpClient.newBuilder().build();

	public static String registerExtension() {
		final String registerUrl = String.format("%s/register", BASEURL);
		HttpRequest request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(BODY))
				.header("Content-Type", "application/json")
				.header("Lambda-Extension-Name", EXTENSION_NAME)
				.uri(URI.create(registerUrl))
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			// Get extension ID from the response headers
			Optional<String> lambdaExtensionHeader = response.headers().firstValue("lambda-extension-identifier");
			if (lambdaExtensionHeader.isPresent()) {
                return lambdaExtensionHeader.get();
            }
		}
		catch (Exception e) {
			Logger.error("could not register the extension");
			e.printStackTrace();
		}
		throw new RuntimeException("Error while registering extension");
	}

	public static String getNext(final String extensionId) {
		try {
			final String nextEventUrl = String.format("%s/event/next", BASEURL);
			HttpRequest request = HttpRequest.newBuilder()
					.GET()
					.header(LAMBDA_EXTENSION_IDENTIFIER, extensionId)
					.uri(URI.create(nextEventUrl))
					.build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
                return response.body();
            } else {
                Logger.error("invalid status code returned while processing event = " + response.statusCode());
            }
		}
		catch (Exception e) {
			Logger.error("could not get /next event");
			e.printStackTrace();
		}

		return null;
	}
}
