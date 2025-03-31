// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.services.lambda.extension;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import one.profiler.AsyncProfiler;

public class PreMain {    

    private static final String DEFAULT_AWS_LAMBDA_PROFILER_START_COMMAND = "start,event=wall,interval=1us";
    private static final String DEFAULT_AWS_LAMBDA_PROFILER_STOP_COMMAND = "stop,file=%s,include=*AWSLambda.main,include=start_thread";
    private static final String PROFILER_START_COMMAND = System.getenv().getOrDefault("AWS_LAMBDA_PROFILER_START_COMMAND", DEFAULT_AWS_LAMBDA_PROFILER_START_COMMAND);
    private static final String PROFILER_STOP_COMMAND = System.getenv().getOrDefault("AWS_LAMBDA_PROFILER_STOP_COMMAND", DEFAULT_AWS_LAMBDA_PROFILER_STOP_COMMAND);
    private static final String INTERNAL_COMMUNICATION_PORT = System.getenv().getOrDefault("AWS_LAMBDA_PROFILER_COMMUNICATION_PORT", "1234");

    public static void premain(String agentArgs, Instrumentation inst) {
        Logger.debug("premain is starting");
        if(!createFileIfNotExist("/tmp/aws-lambda-java-profiler")) {
            Logger.debug("starting the profiler for coldstart");
            startProfiler();
            registerShutdownHook();
            try {
                Integer port = Integer.parseInt(INTERNAL_COMMUNICATION_PORT);
                Logger.debug("using profile communication port = " + port);
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                server.createContext("/profiler/start", new StartProfiler());
                server.createContext("/profiler/stop", new StopProfiler());
                server.setExecutor(null); // Use the default executor
                server.start();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean createFileIfNotExist(String filePath) {
        File file = new File(filePath);   
        try {
            return file.createNewFile();
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    public static class StopProfiler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Logger.debug("hit /profiler/stop");
            final String fileName = exchange.getRequestHeaders().getFirst(ExtensionMain.HEADER_NAME);
            stopProfiler(fileName);
            String response = "ok";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    public static class StartProfiler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Logger.debug("hit /profiler/start");
            startProfiler();
            String response = "ok";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }


    public static void stopProfiler(String fileNameSuffix) {
        try {
            final String fileName = String.format("/tmp/profiling-data-%s.html", fileNameSuffix);
            Logger.debug("stopping the profiler with filename  = " + fileName + " with command = " + PROFILER_STOP_COMMAND);
            AsyncProfiler.getInstance().execute(String.format(PROFILER_STOP_COMMAND, fileName));
        } catch(Exception e) {
            Logger.error("could not stop the profiler");
            e.printStackTrace();
        }
    }

    public static void startProfiler() {
        try {
            Logger.debug("staring the profiler with command = " + PROFILER_START_COMMAND);
            AsyncProfiler.getInstance().execute(PROFILER_START_COMMAND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerShutdownHook() {
        Logger.debug("registering shutdown hook");
        Thread shutdownHook = new Thread(new ShutdownHook(PROFILER_STOP_COMMAND));
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

}