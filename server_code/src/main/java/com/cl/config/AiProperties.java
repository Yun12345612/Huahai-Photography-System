package com.cl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ai")
public class AiProperties {
    private boolean enabled = false;
    private boolean searchEnabled = true;
    private String provider = "";
    private String baseUrl = "";
    private String apiKey = "";
    private String model = "";
    private int timeoutMs = 20000;
    private int maxOutputTokens = 800;
    private double temperature = 0.3;
    private int maxRequestsPerMinute = 10;
    private int maxHistoryMessages = 12;
    private int maxInputCharacters = 12000;

    public boolean isEnabled() { return enabled; }
    public boolean isSearchEnabled() { return searchEnabled; }
    public void setSearchEnabled(boolean searchEnabled) { this.searchEnabled = searchEnabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; }
    public int getMaxOutputTokens() { return maxOutputTokens; }
    public void setMaxOutputTokens(int maxOutputTokens) { this.maxOutputTokens = maxOutputTokens; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public int getMaxRequestsPerMinute() { return maxRequestsPerMinute; }
    public void setMaxRequestsPerMinute(int maxRequestsPerMinute) { this.maxRequestsPerMinute = maxRequestsPerMinute; }
    public int getMaxHistoryMessages() { return maxHistoryMessages; }
    public void setMaxHistoryMessages(int maxHistoryMessages) { this.maxHistoryMessages = maxHistoryMessages; }
    public int getMaxInputCharacters() { return maxInputCharacters; }
    public void setMaxInputCharacters(int maxInputCharacters) { this.maxInputCharacters = maxInputCharacters; }
}