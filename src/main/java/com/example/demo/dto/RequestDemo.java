package com.example.demo.dto;

public class RequestDemo {
    String image;
    String label;

    public RequestDemo(String image, String label) {
        this.image = image;
        this.label = label;
    }

    public RequestDemo() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
