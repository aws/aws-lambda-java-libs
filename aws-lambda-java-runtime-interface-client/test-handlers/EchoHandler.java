import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;
import java.util.HashMap;

public class EchoHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {
        context.getLogger().log("Processing event: " + event);
        
        Map<String, Object> response = new HashMap<>(event);
        response.put("timestamp", System.currentTimeMillis());
        response.put("requestId", context.getAwsRequestId());
        response.put("functionName", context.getFunctionName());
        response.put("remainingTimeInMillis", context.getRemainingTimeInMillis());
        
        return response;
    }
}