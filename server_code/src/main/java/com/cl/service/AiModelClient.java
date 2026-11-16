package com.cl.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cl.config.AiProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import java.util.*;

@Component
public class AiModelClient {
    private final AiProperties properties;
    private final RestTemplate restTemplate;

    public AiModelClient(AiProperties properties) {
        this.properties = properties;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getTimeoutMs());
        factory.setReadTimeout(properties.getTimeoutMs());
        this.restTemplate = new RestTemplate(factory);
    }

    public Reply complete(List<Map<String, String>> messages) {
        if (!properties.isEnabled() || blank(properties.getApiKey()) || blank(properties.getBaseUrl()) || blank(properties.getModel())) return null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("model", properties.getModel());
        body.put("messages", messages);
        body.put("temperature", properties.getTemperature());
        body.put("max_tokens", properties.getMaxOutputTokens());
        ResponseEntity<String> response = restTemplate.exchange(properties.getBaseUrl(), HttpMethod.POST, new HttpEntity<Map<String, Object>>(body, headers), String.class);
        JSONObject root = JSON.parseObject(response.getBody());
        JSONArray choices = root == null ? null : root.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) throw new IllegalStateException("模型返回内容为空");
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        String text = message == null ? null : message.getString("content");
        if (blank(text)) throw new IllegalStateException("模型回复为空");
        JSONObject usage = root.getJSONObject("usage");
        return new Reply(text.trim(), properties.getModel(), usage == null ? null : usage.getInteger("prompt_tokens"), usage == null ? null : usage.getInteger("completion_tokens"), root.getString("id"));
    }

    public SearchResults search(String query) {
        if (!properties.isEnabled() || blank(properties.getApiKey()) || blank(properties.getBaseUrl())) return null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("model", "web-search-pro");
        List<Map<String, String>> messages = new ArrayList<Map<String, String>>();
        Map<String, String> prompt = new LinkedHashMap<String, String>();
        prompt.put("role", "user");
        prompt.put("content", query);
        messages.add(prompt);
        body.put("messages", messages);
        ResponseEntity<String> response = restTemplate.exchange(properties.getBaseUrl(), HttpMethod.POST, new HttpEntity<Map<String, Object>>(body, headers), String.class);
        JSONObject root = JSON.parseObject(response.getBody());
        JSONArray choices = root == null ? null : root.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) return null;
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        JSONArray toolCalls = message == null ? null : message.getJSONArray("tool_calls");
        if (toolCalls == null) return null;
        List<String> sources = new ArrayList<String>();
        StringBuilder context = new StringBuilder();
        for (int i = 0; i < toolCalls.size(); i++) {
            JSONObject call = toolCalls.getJSONObject(i);
            JSONArray results = call.getJSONArray("search_result");
            if (results == null) continue;
            for (int j = 0; j < results.size() && sources.size() < 6; j++) {
                JSONObject item = results.getJSONObject(j);
                String title = item.getString("title");
                String link = item.getString("link");
                String content = item.getString("content");
                if (blank(content)) continue;
                context.append("[来源 ").append(sources.size() + 1).append("] ")
                        .append(title == null ? "网页" : title).append("\n")
                        .append(content.length() > 1200 ? content.substring(0, 1200) : content).append("\n\n");
                if (!blank(link)) sources.add((title == null ? "网页" : title) + "：" + link);
            }
        }
        return context.length() == 0 ? null : new SearchResults(context.toString(), sources);
    }

    public static class SearchResults {
        private final String context;
        private final List<String> sources;
        public SearchResults(String context, List<String> sources) { this.context = context; this.sources = sources; }
        public String getContext() { return context; }
        public List<String> getSources() { return sources; }
    }
    private boolean blank(String value) { return value == null || value.trim().isEmpty(); }

    public static class Reply {
        private final String text;
        private final String model;
        private final Integer promptTokens;
        private final Integer completionTokens;
        private final String requestId;
        public Reply(String text, String model, Integer promptTokens, Integer completionTokens, String requestId) { this.text=text; this.model=model; this.promptTokens=promptTokens; this.completionTokens=completionTokens; this.requestId=requestId; }
        public String getText(){return text;}
        public String getModel(){return model;}
        public Integer getPromptTokens(){return promptTokens;}
        public Integer getCompletionTokens(){return completionTokens;}
        public String getRequestId(){return requestId;}
    }
}