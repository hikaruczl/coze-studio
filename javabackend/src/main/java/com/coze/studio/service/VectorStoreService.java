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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 向量存储服务接口
 * 
 * @author coze-dev
 */
public interface VectorStoreService {

    /**
     * 存储向量
     */
    VectorStoreResult storeVector(String vectorId, float[] vector, Map<String, Object> metadata, Long storeId);

    /**
     * 批量存储向量
     */
    BatchVectorStoreResult storeVectorsBatch(List<VectorData> vectors, Long storeId);

    /**
     * 相似度搜索
     */
    VectorSearchResult similaritySearch(float[] queryVector, int topK, Long storeId, VectorSearchOptions options);

    /**
     * 混合搜索（向量+关键词）
     */
    VectorSearchResult hybridSearch(String queryText, float[] queryVector, int topK, Long storeId, HybridSearchOptions options);

    /**
     * 删除向量
     */
    boolean deleteVector(String vectorId, Long storeId);

    /**
     * 批量删除向量
     */
    BatchDeleteResult deleteVectorsBatch(List<String> vectorIds, Long storeId);

    /**
     * 更新向量
     */
    boolean updateVector(String vectorId, float[] vector, Map<String, Object> metadata, Long storeId);

    /**
     * 获取向量
     */
    VectorData getVector(String vectorId, Long storeId);

    /**
     * 检查向量是否存在
     */
    boolean vectorExists(String vectorId, Long storeId);

    /**
     * 获取向量存储统计信息
     */
    VectorStoreStats getVectorStoreStats(Long storeId);

    /**
     * 健康检查
     */
    HealthCheckResult healthCheck(Long storeId);

    /**
     * 创建索引
     */
    boolean createIndex(Long storeId, IndexConfig indexConfig);

    /**
     * 删除索引
     */
    boolean deleteIndex(Long storeId, String indexName);

    /**
     * 向量数据
     */
    class VectorData {
        private String vectorId;
        private float[] vector;
        private Map<String, Object> metadata;

        public VectorData() {}

        public VectorData(String vectorId, float[] vector, Map<String, Object> metadata) {
            this.vectorId = vectorId;
            this.vector = vector;
            this.metadata = metadata;
        }

        // Getters and setters
        public String getVectorId() { return vectorId; }
        public void setVectorId(String vectorId) { this.vectorId = vectorId; }
        
        public float[] getVector() { return vector; }
        public void setVector(float[] vector) { this.vector = vector; }
        
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 向量存储结果
     */
    class VectorStoreResult {
        private boolean success;
        private String vectorId;
        private String error;
        private long storeTime;

        public VectorStoreResult(boolean success, String vectorId, String error, long storeTime) {
            this.success = success;
            this.vectorId = vectorId;
            this.error = error;
            this.storeTime = storeTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public String getVectorId() { return vectorId; }
        public String getError() { return error; }
        public long getStoreTime() { return storeTime; }
    }

    /**
     * 批量向量存储结果
     */
    class BatchVectorStoreResult {
        private boolean success;
        private int successCount;
        private int failedCount;
        private List<String> failedVectorIds;
        private String error;
        private long totalTime;

        public BatchVectorStoreResult(boolean success, int successCount, int failedCount, long totalTime) {
            this.success = success;
            this.successCount = successCount;
            this.failedCount = failedCount;
            this.totalTime = totalTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public int getSuccessCount() { return successCount; }
        public int getFailedCount() { return failedCount; }
        public List<String> getFailedVectorIds() { return failedVectorIds; }
        public void setFailedVectorIds(List<String> failedVectorIds) { this.failedVectorIds = failedVectorIds; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public long getTotalTime() { return totalTime; }
    }

    /**
     * 向量搜索结果
     */
    class VectorSearchResult {
        private boolean success;
        private List<SearchMatch> matches;
        private String error;
        private long searchTime;
        private Map<String, Object> metadata;

        public VectorSearchResult(boolean success, List<SearchMatch> matches, String error, long searchTime) {
            this.success = success;
            this.matches = matches;
            this.error = error;
            this.searchTime = searchTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public List<SearchMatch> getMatches() { return matches; }
        public String getError() { return error; }
        public long getSearchTime() { return searchTime; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 搜索匹配结果
     */
    class SearchMatch {
        private String vectorId;
        private float score;
        private Map<String, Object> metadata;
        private float[] vector;

        public SearchMatch(String vectorId, float score, Map<String, Object> metadata) {
            this.vectorId = vectorId;
            this.score = score;
            this.metadata = metadata;
        }

        // Getters and setters
        public String getVectorId() { return vectorId; }
        public float getScore() { return score; }
        public Map<String, Object> getMetadata() { return metadata; }
        public float[] getVector() { return vector; }
        public void setVector(float[] vector) { this.vector = vector; }
    }

    /**
     * 向量搜索选项
     */
    class VectorSearchOptions {
        private Map<String, Object> filter;
        private boolean includeVector = false;
        private boolean includeMetadata = true;
        private String metricType = "COSINE";
        private Double scoreThreshold;
        private List<String> selectFields;

        // Getters and setters
        public Map<String, Object> getFilter() { return filter; }
        public void setFilter(Map<String, Object> filter) { this.filter = filter; }
        
        public boolean isIncludeVector() { return includeVector; }
        public void setIncludeVector(boolean includeVector) { this.includeVector = includeVector; }
        
        public boolean isIncludeMetadata() { return includeMetadata; }
        public void setIncludeMetadata(boolean includeMetadata) { this.includeMetadata = includeMetadata; }
        
        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
        
        public Double getScoreThreshold() { return scoreThreshold; }
        public void setScoreThreshold(Double scoreThreshold) { this.scoreThreshold = scoreThreshold; }
        
        public List<String> getSelectFields() { return selectFields; }
        public void setSelectFields(List<String> selectFields) { this.selectFields = selectFields; }
    }

    /**
     * 混合搜索选项
     */
    class HybridSearchOptions extends VectorSearchOptions {
        private double vectorWeight = 0.7;
        private double textWeight = 0.3;
        private String textSearchType = "BM25";
        private Map<String, Object> textSearchParams;

        // Getters and setters
        public double getVectorWeight() { return vectorWeight; }
        public void setVectorWeight(double vectorWeight) { this.vectorWeight = vectorWeight; }
        
        public double getTextWeight() { return textWeight; }
        public void setTextWeight(double textWeight) { this.textWeight = textWeight; }
        
        public String getTextSearchType() { return textSearchType; }
        public void setTextSearchType(String textSearchType) { this.textSearchType = textSearchType; }
        
        public Map<String, Object> getTextSearchParams() { return textSearchParams; }
        public void setTextSearchParams(Map<String, Object> textSearchParams) { this.textSearchParams = textSearchParams; }
    }

    /**
     * 批量删除结果
     */
    class BatchDeleteResult {
        private boolean success;
        private int deletedCount;
        private int failedCount;
        private List<String> failedVectorIds;
        private String error;

        public BatchDeleteResult(boolean success, int deletedCount, int failedCount) {
            this.success = success;
            this.deletedCount = deletedCount;
            this.failedCount = failedCount;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public int getDeletedCount() { return deletedCount; }
        public int getFailedCount() { return failedCount; }
        public List<String> getFailedVectorIds() { return failedVectorIds; }
        public void setFailedVectorIds(List<String> failedVectorIds) { this.failedVectorIds = failedVectorIds; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    /**
     * 向量存储统计信息
     */
    class VectorStoreStats {
        private long totalVectors;
        private long indexSize;
        private double avgQueryLatency;
        private long totalQueries;
        private double successRate;
        private Map<String, Object> additionalStats;

        public <V, K> VectorStoreStats(int i, int i1, int i2, HashMap<K,V> kvHashMap) {
        }

        // Getters and setters
        public long getTotalVectors() { return totalVectors; }
        public void setTotalVectors(long totalVectors) { this.totalVectors = totalVectors; }
        
        public long getIndexSize() { return indexSize; }
        public void setIndexSize(long indexSize) { this.indexSize = indexSize; }
        
        public double getAvgQueryLatency() { return avgQueryLatency; }
        public void setAvgQueryLatency(double avgQueryLatency) { this.avgQueryLatency = avgQueryLatency; }
        
        public long getTotalQueries() { return totalQueries; }
        public void setTotalQueries(long totalQueries) { this.totalQueries = totalQueries; }
        
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        
        public Map<String, Object> getAdditionalStats() { return additionalStats; }
        public void setAdditionalStats(Map<String, Object> additionalStats) { this.additionalStats = additionalStats; }
    }

    /**
     * 健康检查结果
     */
    class HealthCheckResult {
        private boolean healthy;
        private String status;
        private long responseTime;
        private String message;
        private Map<String, Object> details;

        public HealthCheckResult(boolean healthy, String status, long responseTime, String message) {
            this.healthy = healthy;
            this.status = status;
            this.responseTime = responseTime;
            this.message = message;
        }

        // Getters and setters
        public boolean isHealthy() { return healthy; }
        public String getStatus() { return status; }
        public long getResponseTime() { return responseTime; }
        public String getMessage() { return message; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }

    /**
     * 索引配置
     */
    class IndexConfig {
        private String indexName;
        private String indexType = "HNSW";
        private Map<String, Object> parameters;
        private String metricType = "COSINE";

        // Getters and setters
        public String getIndexName() { return indexName; }
        public void setIndexName(String indexName) { this.indexName = indexName; }
        
        public String getIndexType() { return indexType; }
        public void setIndexType(String indexType) { this.indexType = indexType; }
        
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        
        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
    }
}
