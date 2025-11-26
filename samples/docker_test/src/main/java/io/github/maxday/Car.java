package io.github.maxday;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Car {
    
    @JsonProperty("color_hex")
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
