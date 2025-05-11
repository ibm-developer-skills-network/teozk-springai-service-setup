package com.example.customersupportchatbot.model;

import java.util.List;

public class GeminiRequest {
    private List<Content> contents;

    // Default constructor
    public GeminiRequest() {
    }

    // Constructor with parameters
    public GeminiRequest(List<Content> contents) {
        this.contents = contents;
    }

    // Getter and setter
    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public static class Content {
        private List<Part> parts;

        // Default constructor
        public Content() {
        }

        // Constructor with parameters
        public Content(List<Part> parts) {
            this.parts = parts;
        }

        // Getter and setter
        public List<Part> getParts() {
            return parts;
        }

        public void setParts(List<Part> parts) {
            this.parts = parts;
        }
    }

    public static class Part {
        private String text;

        // Default constructor
        public Part() {
        }

        // Constructor with text parameter
        public Part(String text) {
            this.text = text;
        }

        // Getter and setter
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}