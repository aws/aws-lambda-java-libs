package io.github.maxday;

public class LambdaResponse {
    private String message;

    public LambdaResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}