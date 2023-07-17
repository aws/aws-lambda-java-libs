package test.lambda.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class RequestHandlerImpl implements RequestHandler<String, String> {
    @Override
    public String handleRequest(String event, Context context) {
        return "success";
    }
}
