package com.example.customersupportchatbot.model;

public class EnhancedChatResponse {
    private String response;
    private String category;

    // Default constructor
    public EnhancedChatResponse() {
    }

    // Constructor with all parameters
    public EnhancedChatResponse(String response, String category) {
        this.response = response;
        this.category = category;
    }

    // Getters and setters
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}