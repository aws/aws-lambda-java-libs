package io.github.maxday;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.annotation.JsonProperty;

public class Car {
    
    private String color;
    
    private String model;
    
    public Car() {
        super();
    }
    
    @JsonProperty("color_hex")
    public String getColor() {
        return color;
    }

    @JsonProperty("color_hex")
    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Car [color=" + color + ", model=" + model + "]";
    }
}
