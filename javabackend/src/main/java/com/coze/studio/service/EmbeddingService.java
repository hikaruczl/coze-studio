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
 * 嵌入服务接口
 * 
 * @author coze-dev
 */
public interface EmbeddingService {

    /**
     * 生成文本嵌入
     */
    EmbeddingResult generateEmbedding(String text, EmbeddingOptions options);

    /**
     * 批量生成文本嵌入
     */
    BatchEmbeddingResult generateEmbeddingsBatch(List<String> texts, EmbeddingOptions options);

    /**
     * 计算相似度
     */
    double calculateSimilarity(float[] embedding1, float[] embedding2, String metricType);

    /**
     * 批量计算相似度
     */
    List<Double> calculateSimilarityBatch(float[] queryEmbedding, List<float[]> embeddings, String metricType);

    /**
     * 获取支持的模型列表
     */
    List<EmbeddingModel> getSupportedModels();

    /**
     * 获取模型信息
     */
    EmbeddingModel getModelInfo(String modelName);

    /**
     * 验证模型可用性
     */
    ModelValidationResult validateModel(String modelName);

    /**
     * 嵌入选项
     */
    class EmbeddingOptions {
        private String model = "text-embedding-ada-002";
        private int dimension = 1536;
        private boolean normalize = true;
        private String encodingFormat = "float";
        private Map<String, Object> modelParams;
        private int maxRetries = 3;
        private long timeoutMs = 30000;

        // Getters and setters
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        
        public int getDimension() { return dimension; }
        public void setDimension(int dimension) { this.dimension = dimension; }
        
        public boolean isNormalize() { return normalize; }
        public void setNormalize(boolean normalize) { this.normalize = normalize; }
        
        public String getEncodingFormat() { return encodingFormat; }
        public void setEncodingFormat(String encodingFormat) { this.encodingFormat = encodingFormat; }
        
        public Map<String, Object> getModelParams() { return modelParams; }
        public void setModelParams(Map<String, Object> modelParams) { this.modelParams = modelParams; }
        
        public int getMaxRetries() { return maxRetries; }
        public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
        
        public long getTimeoutMs() { return timeoutMs; }
        public void setTimeoutMs(long timeoutMs) { this.timeoutMs = timeoutMs; }
    }

    /**
     * 嵌入结果
     */
    class EmbeddingResult {
        private boolean success;
        private float[] embedding;
        private String model;
        private int dimension;
        private String error;
        private long processingTime;
        private int tokenCount;
        private Map<String, Object> metadata;

        public EmbeddingResult(boolean success, float[] embedding, String model, String error, long processingTime) {
            this.success = success;
            this.embedding = embedding;
            this.model = model;
            this.error = error;
            this.processingTime = processingTime;
            this.dimension = embedding != null ? embedding.length : 0;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public float[] getEmbedding() { return embedding; }
        public String getModel() { return model; }
        public int getDimension() { return dimension; }
        public String getError() { return error; }
        public long getProcessingTime() { return processingTime; }
        public int getTokenCount() { return tokenCount; }
        public void setTokenCount(int tokenCount) { this.tokenCount = tokenCount; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 批量嵌入结果
     */
    class BatchEmbeddingResult {
        private boolean success;
        private List<float[]> embeddings;
        private String model;
        private int dimension;
        private String error;
        private long totalProcessingTime;
        private int totalTokenCount;
        private int successCount;
        private int failedCount;
        private List<String> failedTexts;
        private Map<String, Object> metadata;

        public BatchEmbeddingResult(boolean success, List<float[]> embeddings, String model, String error, long totalProcessingTime) {
            this.success = success;
            this.embeddings = embeddings;
            this.model = model;
            this.error = error;
            this.totalProcessingTime = totalProcessingTime;
            this.dimension = embeddings != null && !embeddings.isEmpty() ? embeddings.get(0).length : 0;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public List<float[]> getEmbeddings() { return embeddings; }
        public String getModel() { return model; }
        public int getDimension() { return dimension; }
        public String getError() { return error; }
        public long getTotalProcessingTime() { return totalProcessingTime; }
        public int getTotalTokenCount() { return totalTokenCount; }
        public void setTotalTokenCount(int totalTokenCount) { this.totalTokenCount = totalTokenCount; }
        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
        public int getFailedCount() { return failedCount; }
        public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
        public List<String> getFailedTexts() { return failedTexts; }
        public void setFailedTexts(List<String> failedTexts) { this.failedTexts = failedTexts; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 嵌入模型信息
     */
    class EmbeddingModel {
        private String name;
        private String provider;
        private int dimension;
        private int maxTokens;
        private String description;
        private boolean available;
        private double costPer1kTokens;
        private List<String> supportedLanguages;
        private Map<String, Object> capabilities;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        
        public int getDimension() { return dimension; }
        public void setDimension(int dimension) { this.dimension = dimension; }
        
        public int getMaxTokens() { return maxTokens; }
        public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
        
        public double getCostPer1kTokens() { return costPer1kTokens; }
        public void setCostPer1kTokens(double costPer1kTokens) { this.costPer1kTokens = costPer1kTokens; }
        
        public List<String> getSupportedLanguages() { return supportedLanguages; }
        public void setSupportedLanguages(List<String> supportedLanguages) { this.supportedLanguages = supportedLanguages; }
        
        public Map<String, Object> getCapabilities() { return capabilities; }
        public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
    }

    /**
     * 模型验证结果
     */
    class ModelValidationResult {
        private boolean valid;
        private String modelName;
        private String status;
        private String message;
        private long responseTime;
        private Map<String, Object> details;

        public ModelValidationResult(boolean valid, String modelName, String status, String message, long responseTime) {
            this.valid = valid;
            this.modelName = modelName;
            this.status = status;
            this.message = message;
            this.responseTime = responseTime;
        }

        // Getters and setters
        public boolean isValid() { return valid; }
        public String getModelName() { return modelName; }
        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public long getResponseTime() { return responseTime; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }

    /**
     * 相似度计算工具类
     */
    class SimilarityCalculator {
        
        /**
         * 余弦相似度
         */
        public static double cosineSimilarity(float[] a, float[] b) {
            if (a.length != b.length) {
                throw new IllegalArgumentException("向量维度不匹配");
            }
            
            double dotProduct = 0.0;
            double normA = 0.0;
            double normB = 0.0;
            
            for (int i = 0; i < a.length; i++) {
                dotProduct += a[i] * b[i];
                normA += a[i] * a[i];
                normB += b[i] * b[i];
            }
            
            if (normA == 0.0 || normB == 0.0) {
                return 0.0;
            }
            
            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        }

        /**
         * 欧几里得距离
         */
        public static double euclideanDistance(float[] a, float[] b) {
            if (a.length != b.length) {
                throw new IllegalArgumentException("向量维度不匹配");
            }
            
            double sum = 0.0;
            for (int i = 0; i < a.length; i++) {
                double diff = a[i] - b[i];
                sum += diff * diff;
            }
            
            return Math.sqrt(sum);
        }

        /**
         * 点积相似度
         */
        public static double dotProduct(float[] a, float[] b) {
            if (a.length != b.length) {
                throw new IllegalArgumentException("向量维度不匹配");
            }
            
            double product = 0.0;
            for (int i = 0; i < a.length; i++) {
                product += a[i] * b[i];
            }
            
            return product;
        }

        /**
         * 曼哈顿距离
         */
        public static double manhattanDistance(float[] a, float[] b) {
            if (a.length != b.length) {
                throw new IllegalArgumentException("向量维度不匹配");
            }
            
            double sum = 0.0;
            for (int i = 0; i < a.length; i++) {
                sum += Math.abs(a[i] - b[i]);
            }
            
            return sum;
        }

        /**
         * 向量归一化
         */
        public static float[] normalize(float[] vector) {
            double norm = 0.0;
            for (float v : vector) {
                norm += v * v;
            }
            norm = Math.sqrt(norm);
            
            if (norm == 0.0) {
                return vector.clone();
            }
            
            float[] normalized = new float[vector.length];
            for (int i = 0; i < vector.length; i++) {
                normalized[i] = (float) (vector[i] / norm);
            }
            
            return normalized;
        }
    }
}
