package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class StreamHandlerApp implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try{
            //test to make sure all events are available
            Class.forName("com.amazonaws.services.lambda.runtime.events.CloudFrontEvent");
        }catch(Exception e){
            e.printStackTrace();
            outputStream.write("error".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            return;
        }

        outputStream.write("success".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }
}
