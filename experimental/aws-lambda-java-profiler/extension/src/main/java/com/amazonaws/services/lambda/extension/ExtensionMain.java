// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.services.lambda.extension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.UUID;

public class ExtensionMain {

    private static final HttpClient client = HttpClient.newBuilder().build();
    private static String previousFileSuffix = null;
    private static boolean coldstart = true;
    private static final String REQUEST_ID = "requestId";
    private static final String EVENT_TYPE = "eventType";
    private static final String INTERNAL_COMMUNICATION_PORT = System.getenv().getOrDefault("AWS_LAMBDA_PROFILER_COMMUNICATION_PORT", "1234");
    public static final String HEADER_NAME = "X-FileName";

    private static S3Manager s3Manager;

    public static void main(String[] args) {
        final String extension = ExtensionClient.registerExtension();
        Logger.debug("Extension registration complete, extensionID: " + extension);
        s3Manager = new S3Manager();
        while (true) {
            try {
                String response = ExtensionClient.getNext(extension);
                if (response != null && !response.isEmpty()) {
                    final String eventType = extractInfo(EVENT_TYPE, response);
                    Logger.debug("eventType = " + eventType);
                    if (eventType != null) {
                        switch (eventType) {
                            case "INVOKE":
                                handleInvoke(response);
                                break;
                            case "SHUTDOWN":
                                handleShutDown();
                                break;
                            default:
                                Logger.error("invalid event type received " + eventType);
                        }
                    }
                }
            } catch (Exception e) {
                Logger.error("error while processing extension -" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void handleShutDown() {
        Logger.debug("handling SHUTDOWN event, flushing the last profile");
        try {
            // no need to stop the profiler as it has been stopped by the shutdown hook
            s3Manager.upload(previousFileSuffix, true);
        } catch (Exception e) {
            Logger.error("could not upload the file");
            throw e;
        }
        System.exit(0);
    }

    public static void handleInvoke(String payload) {
        final String requestId = extractInfo(REQUEST_ID, payload);
        final String randomSuffix = UUID.randomUUID().toString().substring(0,5);
        Logger.debug("handling INVOKE event, requestID = " + requestId);
        if (!coldstart) {
            try {
                stopProfiler(previousFileSuffix);
                s3Manager.upload(previousFileSuffix, false);
                startProfiler();
            } catch (Exception e) {
                Logger.error("could not start the profiler");
                throw e;
            }
        }
        coldstart = false;
        previousFileSuffix = extractInfo(REQUEST_ID, payload) + "-" + randomSuffix;
    }

    private static String extractInfo(String info, String jsonString) {
        String prefix = "\"" + info + "\":\"";
        String suffix = "\"";
        
        int startIndex = jsonString.indexOf(prefix);
        if (startIndex == -1) {
            return null; // requestId not found
        }
        
        startIndex += prefix.length();
        int endIndex = jsonString.indexOf(suffix, startIndex);
        
        if (endIndex == -1) {
            return null; // Malformed JSON
        }
        
        return jsonString.substring(startIndex, endIndex);
    }

    private static void startProfiler() {
        try {
            String url = String.format("http://localhost:%s/profiler/start", INTERNAL_COMMUNICATION_PORT);
            HttpRequest request = HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(url))
                            .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Logger.debug("profiler successfully started");
            }
        } catch(Exception e) {
            Logger.error("could not start the profiler");
            e.printStackTrace();
        }
    }

    private static void stopProfiler(String fileNameSuffix) {
        try {
            String url = String.format("http://localhost:%s/profiler/stop", INTERNAL_COMMUNICATION_PORT);
            HttpRequest request = HttpRequest.newBuilder()
                            .GET()
                            .setHeader(HEADER_NAME, fileNameSuffix)
                            .uri(URI.create(url))
                            .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Logger.debug("profiler successfully stopped");
            }
        } catch(Exception e) {
            Logger.error("could not stop the profiler");
            e.printStackTrace();
        }
    }
}
