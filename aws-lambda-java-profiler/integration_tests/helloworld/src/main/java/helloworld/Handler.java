package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        long start = System.currentTimeMillis();
        List<Integer> result = slowRecursiveFunction(0, 5);
        long end = System.currentTimeMillis();
        long duration = end - start;

        System.out.println("Function execution time: " + duration + " ms");
        System.out.println("Result size: " + result.size());
        System.out.println("First few elements: " + result.subList(0, Math.min(10, result.size())));

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("ok");

    }

    private static List<Integer> slowRecursiveFunction(int n, int depth) {
        List<Integer> result = new ArrayList<>();
        if (depth == 0) {
            return result;
        }
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 100) {
            // nothing to do here
        }
        result.add(n);
        result.addAll(slowRecursiveFunction(n + 2, depth - 1));
        return result;
    }
}
