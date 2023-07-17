package test.lambda.handlers;

import com.amazonaws.services.lambda.runtime.Context;

@SuppressWarnings("unused")
public class POJOHanlderImpl {
    @SuppressWarnings("unused")
    public String noParamsHandler() {
        return "success";
    }

    @SuppressWarnings("unused")
    public String oneParamHandler_event(String event) {
        return "success";
    }

    @SuppressWarnings("unused")
    public String oneParamHandler_context(Context context) {
        return "success";
    }

    @SuppressWarnings("unused")
    public String twoParamsHandler(String event, Context context) {
        return "success";
    }
}
