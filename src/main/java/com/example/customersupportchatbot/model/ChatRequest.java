package com.example.customersupportchatbot.model;

public class ChatRequest {
    private String query;

    // Default constructor
    public ChatRequest() {
    }

    // Constructor with parameters
    public ChatRequest(String query) {
        this.query = query;
    }

    // Getter and setter
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}