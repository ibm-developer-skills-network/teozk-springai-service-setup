package com.example.customersupportchatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiAIService {

    private final WebClient webClient;
    private final String apiKey;

    public GeminiAIService(@Value("${gemini.api.key}") String apiKey) {
        this.apiKey = apiKey;

        // Create a more robust WebClient
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Generates a response using the Gemini API
     *
     * @param query The user's query
     * @return The response from the model
     */
    public String generateResponse(String query) {
        try {
            // Add context for the chatbot
            String fullPrompt = "You are a helpful customer support chatbot. Provide a concise and friendly response to this customer query: " + query;

            // Create the request body with proper structure
            Map<String, Object> partObject = new HashMap<>();
            partObject.put("text", fullPrompt);

            Map<String, Object> contentObject = new HashMap<>();
            contentObject.put("parts", List.of(partObject));
            contentObject.put("role", "user");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(contentObject));

            // Debug: Print the request being sent
            System.out.println("Sending request to Gemini API:");
            System.out.println("URL: /v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey.substring(0, 5) + "...");
            System.out.println("Request body: " + requestBody);

            // Make the API request with more detailed error handling
            Map<String, Object> response = webClient.post()
                    .uri("/v1beta/models/gemini-1.5-flash-latest:generateContent?key={apiKey}", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // Debug: Print response structure
            System.out.println("Response received: " + response);

            // Extract the text from the response
            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> responseParts = (List<Map<String, Object>>) content.get("parts");
                    if (!responseParts.isEmpty()) {
                        return (String) responseParts.get(0).get("text");
                    }
                }
            }

            return "I'm sorry, I couldn't process your request.";

        } catch (WebClientResponseException e) {
            // Handle HTTP error responses specifically
            System.err.println("Error calling Gemini API: " + e.getStatusCode() + " " + e.getStatusText());
            System.err.println("Response body: " + e.getResponseBodyAsString());

            // Return a more informative error message
            if (e.getStatusCode().value() == 400) {
                return "I'm sorry, there was an error with the request format (400 Bad Request). Please ensure your API key is correct and try again.";
            } else if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                return "I'm sorry, there was an authentication error. Please check your API key.";
            } else if (e.getStatusCode().value() == 429) {
                return "I'm sorry, we've hit the rate limit for the Gemini API. Please try again in a minute.";
            } else {
                return "I'm sorry, there was an error calling the Gemini API: " + e.getStatusCode() + " " + e.getStatusText();
            }
        } catch (Exception e) {
            // General error handling
            System.err.println("Unexpected error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            return "I'm sorry, there was an unexpected error processing your request: " + e.getMessage();
        }
    }
}