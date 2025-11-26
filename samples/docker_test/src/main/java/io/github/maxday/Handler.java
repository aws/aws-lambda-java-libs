package io.github.maxday;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import io.github.maxday.LambdaResponse;

public class Handler implements RequestHandler<SQSEvent, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(SQSEvent event, Context context) {

        if (event.getRecords().toArray().length == 0){
            return new LambdaResponse("no records at all");
        }


        System.out.println(String.format("getting the event data: %s", event));

        return new LambdaResponse(event.getRecords().toString());
    }
}

