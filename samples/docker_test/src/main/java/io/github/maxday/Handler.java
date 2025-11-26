package io.github.maxday;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import io.github.maxday.LambdaResponse;
import io.github.maxday.Car;

public class Handler implements RequestHandler<Car, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(Car event, Context context) {
        System.out.println(String.format("getting the event data: %s", event));

        return new LambdaResponse(event.toString());
    }
}

