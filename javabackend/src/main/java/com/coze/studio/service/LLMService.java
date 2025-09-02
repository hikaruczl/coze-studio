/*
 * Copyright 2025 coze-dev Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coze.studio.service;

import java.util.List;
import java.util.Map;

/**
 * LLM服务接口
 * 
 * @author coze-dev
 */
public interface LLMService {

    /**
     * 调用LLM生成文本
     */
    LLMResponse generateText(LLMRequest request);

    /**
     * 流式调用LLM生成文本
     */
    void generateTextStream(LLMRequest request, LLMStreamCallback callback);

    /**
     * LLM请求参数
     */
    class LLMRequest {
        private String model;
        private List<LLMMessage> messages;
        private Map<String, Object> parameters;
        private int maxTokens;
        private double temperature;
        private double topP;
        private String systemPrompt;

        // Constructors, getters and setters
        public LLMRequest() {}

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        
        public List<LLMMessage> getMessages() { return messages; }
        public void setMessages(List<LLMMessage> messages) { this.messages = messages; }
        
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        
        public int getMaxTokens() { return maxTokens; }
        public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
        
        public double getTemperature() { return temperature; }
        public void setTemperature(double temperature) { this.temperature = temperature; }
        
        public double getTopP() { return topP; }
        public void setTopP(double topP) { this.topP = topP; }
        
        public String getSystemPrompt() { return systemPrompt; }
        public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
    }

    /**
     * LLM消息
     */
    class LLMMessage {
        private String role;
        private String content;

        public LLMMessage() {}
        
        public LLMMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    /**
     * LLM响应结果
     */
    class LLMResponse {
        private boolean success;
        private String content;
        private String error;
        private Map<String, Object> usage;
        private long responseTime;

        public LLMResponse(boolean success, String content, String error, 
                          Map<String, Object> usage, long responseTime) {
            this.success = success;
            this.content = content;
            this.error = error;
            this.usage = usage;
            this.responseTime = responseTime;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getContent() { return content; }
        public String getError() { return error; }
        public Map<String, Object> getUsage() { return usage; }
        public long getResponseTime() { return responseTime; }
    }

    /**
     * LLM流式回调接口
     */
    interface LLMStreamCallback {
        void onStart();
        void onContent(String content);
        void onComplete(LLMResponse response);
        void onError(Exception error);
    }
}
