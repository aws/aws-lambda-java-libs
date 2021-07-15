package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class RequestHandlerApp implements RequestHandler<Map<String,String>, String>{
    @Override
    public String handleRequest(Map<String,String> event, Context context)
    {
        try{
            //test to make sure all events are available
            Class.forName("com.amazonaws.services.lambda.runtime.events.CloudFrontEvent");
        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }

        return "success";
    }
}
