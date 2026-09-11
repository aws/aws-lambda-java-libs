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

    @SuppressWarnings("unused")
    public PojoOutput pojoOutputHandler(String event) {
        return new PojoOutput();
    }

    @SuppressWarnings("unused")
    public static class PojoOutput {
        private final String internalField = "field-based-value";

        public String getBeanProperty() {
            return "property-based-value";
        }
    }
}
