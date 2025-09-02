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

import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.entity.ModelConfiguration;
import com.coze.studio.entity.ModelProvider;
import com.coze.studio.entity.ModelUsageRecord;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 模型管理服务接口
 * 提供AI模型提供商、模型配置和使用记录的管理功能
 * 
 * @author coze-dev
 */
public interface ModelManagementService {

    // ==================== 提供商管理 ====================

    /**
     * 创建模型提供商
     */
    ModelProvider createProvider(CreateProviderRequest request);

    /**
     * 更新模型提供商
     */
    ModelProvider updateProvider(Long id, UpdateProviderRequest request);

    /**
     * 删除模型提供商
     */
    boolean deleteProvider(Long id);

    /**
     * 获取模型提供商
     */
    Optional<ModelProvider> getProvider(Long id);

    /**
     * 根据代码获取提供商
     */
    Optional<ModelProvider> getProviderByCode(String code);

    /**
     * 获取提供商列表
     */
    PageResponse<ModelProvider> getProviders(GetProvidersRequest request, Pageable pageable);

    /**
     * 搜索提供商
     */
    PageResponse<ModelProvider> searchProviders(SearchProvidersRequest request, Pageable pageable);

    /**
     * 获取活跃的提供商
     */
    List<ModelProvider> getActiveProviders();

    /**
     * 获取健康的提供商
     */
    List<ModelProvider> getHealthyProviders();

    /**
     * 设置默认提供商
     */
    ModelProvider setDefaultProvider(Long id);

    /**
     * 测试提供商连接
     */
    ProviderTestResult testProvider(Long id);

    /**
     * 健康检查
     */
    ProviderHealthResult checkProviderHealth(Long id);

    /**
     * 批量健康检查
     */
    List<ProviderHealthResult> batchHealthCheck();

    // ==================== 模型配置管理 ====================

    /**
     * 创建模型配置
     */
    ModelConfiguration createModel(CreateModelRequest request);

    /**
     * 更新模型配置
     */
    ModelConfiguration updateModel(Long id, UpdateModelRequest request);

    /**
     * 删除模型配置
     */
    boolean deleteModel(Long id);

    /**
     * 获取模型配置
     */
    Optional<ModelConfiguration> getModel(Long id);

    /**
     * 根据代码获取模型
     */
    Optional<ModelConfiguration> getModelByCode(String code);

    /**
     * 获取模型列表
     */
    PageResponse<ModelConfiguration> getModels(GetModelsRequest request, Pageable pageable);

    /**
     * 搜索模型
     */
    PageResponse<ModelConfiguration> searchModels(SearchModelsRequest request, Pageable pageable);

    /**
     * 获取活跃的模型
     */
    List<ModelConfiguration> getActiveModels();

    /**
     * 根据类型获取模型
     */
    List<ModelConfiguration> getModelsByType(String type);

    /**
     * 获取推荐模型
     */
    List<ModelConfiguration> getRecommendedModels(String type, int limit);

    /**
     * 设置默认模型
     */
    ModelConfiguration setDefaultModel(Long id, String type);

    /**
     * 获取模型选择建议
     */
    ModelSelectionResult selectModel(ModelSelectionRequest request);

    // ==================== 使用记录管理 ====================

    /**
     * 记录模型使用
     */
    ModelUsageRecord recordUsage(RecordUsageRequest request);

    /**
     * 更新使用记录
     */
    ModelUsageRecord updateUsageRecord(Long id, UpdateUsageRecordRequest request);

    /**
     * 获取使用记录
     */
    Optional<ModelUsageRecord> getUsageRecord(Long id);

    /**
     * 根据请求ID获取使用记录
     */
    Optional<ModelUsageRecord> getUsageRecordByRequestId(String requestId);

    /**
     * 获取使用记录列表
     */
    PageResponse<ModelUsageRecord> getUsageRecords(GetUsageRecordsRequest request, Pageable pageable);

    /**
     * 获取用户使用记录
     */
    PageResponse<ModelUsageRecord> getUserUsageRecords(Long userId, Pageable pageable);

    /**
     * 获取模型使用记录
     */
    PageResponse<ModelUsageRecord> getModelUsageRecords(Long modelId, Pageable pageable);

    // ==================== 统计和分析 ====================

    /**
     * 获取提供商统计
     */
    ProviderStatistics getProviderStatistics();

    /**
     * 获取模型统计
     */
    ModelStatistics getModelStatistics();

    /**
     * 获取使用统计
     */
    UsageStatistics getUsageStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取用户使用统计
     */
    UserUsageStatistics getUserUsageStatistics(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取模型使用统计
     */
    ModelUsageStatistics getModelUsageStatistics(Long modelId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取使用趋势
     */
    List<UsageTrendData> getUsageTrend(String period, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取成本分析
     */
    CostAnalysis getCostAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取性能分析
     */
    PerformanceAnalysis getPerformanceAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    // ==================== 批量操作 ====================

    /**
     * 批量更新提供商状态
     */
    BatchOperationResult batchUpdateProviderStatus(List<Long> providerIds, String status);

    /**
     * 批量更新模型状态
     */
    BatchOperationResult batchUpdateModelStatus(List<Long> modelIds, String status);

    /**
     * 批量启用/禁用提供商
     */
    BatchOperationResult batchToggleProviders(List<Long> providerIds, boolean enabled);

    /**
     * 批量启用/禁用模型
     */
    BatchOperationResult batchToggleModels(List<Long> modelIds, boolean enabled);

    /**
     * 同步提供商模型
     */
    SyncResult syncProviderModels(Long providerId);

    /**
     * 清理过期使用记录
     */
    CleanupResult cleanupUsageRecords(LocalDateTime threshold);

    // ==================== 请求和响应类 ====================

    /**
     * 创建提供商请求
     */
    class CreateProviderRequest {
        private String code;
        private String name;
        private String displayName;
        private String description;
        private String type;
        private String baseUrl;
        private String apiVersion;
        private String authType;
        private String apiKey;
        private String organizationId;
        private String projectId;
        private String region;
        private Map<String, Object> authConfig;
        private Map<String, Object> requestConfig;
        private Integer priority;
        private Integer timeoutMs;
        private Integer maxConcurrentRequests;
        private Map<String, Object> rateLimitConfig;
        private List<String> supportedFeatures;

        // Getters and setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getApiVersion() { return apiVersion; }
        public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }
        public String getAuthType() { return authType; }
        public void setAuthType(String authType) { this.authType = authType; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getOrganizationId() { return organizationId; }
        public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
        public String getProjectId() { return projectId; }
        public void setProjectId(String projectId) { this.projectId = projectId; }
        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }
        public Map<String, Object> getAuthConfig() { return authConfig; }
        public void setAuthConfig(Map<String, Object> authConfig) { this.authConfig = authConfig; }
        public Map<String, Object> getRequestConfig() { return requestConfig; }
        public void setRequestConfig(Map<String, Object> requestConfig) { this.requestConfig = requestConfig; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
        public Integer getTimeoutMs() { return timeoutMs; }
        public void setTimeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; }
        public Integer getMaxConcurrentRequests() { return maxConcurrentRequests; }
        public void setMaxConcurrentRequests(Integer maxConcurrentRequests) { this.maxConcurrentRequests = maxConcurrentRequests; }
        public Map<String, Object> getRateLimitConfig() { return rateLimitConfig; }
        public void setRateLimitConfig(Map<String, Object> rateLimitConfig) { this.rateLimitConfig = rateLimitConfig; }
        public List<String> getSupportedFeatures() { return supportedFeatures; }
        public void setSupportedFeatures(List<String> supportedFeatures) { this.supportedFeatures = supportedFeatures; }
    }

    /**
     * 更新提供商请求
     */
    class UpdateProviderRequest {
        private String name;
        private String displayName;
        private String description;
        private String baseUrl;
        private String apiVersion;
        private String authType;
        private String apiKey;
        private String organizationId;
        private String projectId;
        private String region;
        private Map<String, Object> authConfig;
        private Map<String, Object> requestConfig;
        private String status;
        private Boolean enabled;
        private Integer priority;
        private Integer timeoutMs;
        private Integer maxConcurrentRequests;
        private Map<String, Object> rateLimitConfig;
        private List<String> supportedFeatures;

        // Getters and setters (省略具体实现)
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getApiVersion() { return apiVersion; }
        public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }
        public String getAuthType() { return authType; }
        public void setAuthType(String authType) { this.authType = authType; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getOrganizationId() { return organizationId; }
        public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
        public String getProjectId() { return projectId; }
        public void setProjectId(String projectId) { this.projectId = projectId; }
        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }
        public Map<String, Object> getAuthConfig() { return authConfig; }
        public void setAuthConfig(Map<String, Object> authConfig) { this.authConfig = authConfig; }
        public Map<String, Object> getRequestConfig() { return requestConfig; }
        public void setRequestConfig(Map<String, Object> requestConfig) { this.requestConfig = requestConfig; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
        public Integer getTimeoutMs() { return timeoutMs; }
        public void setTimeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; }
        public Integer getMaxConcurrentRequests() { return maxConcurrentRequests; }
        public void setMaxConcurrentRequests(Integer maxConcurrentRequests) { this.maxConcurrentRequests = maxConcurrentRequests; }
        public Map<String, Object> getRateLimitConfig() { return rateLimitConfig; }
        public void setRateLimitConfig(Map<String, Object> rateLimitConfig) { this.rateLimitConfig = rateLimitConfig; }
        public List<String> getSupportedFeatures() { return supportedFeatures; }
        public void setSupportedFeatures(List<String> supportedFeatures) { this.supportedFeatures = supportedFeatures; }
    }

    /**
     * 获取提供商列表请求
     */
    class GetProvidersRequest {
        private String type;
        private String status;
        private Boolean enabled;

        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }

    /**
     * 搜索提供商请求
     */
    class SearchProvidersRequest {
        private String keyword;
        private String type;
        private String status;
        private Boolean enabled;

        // Getters and setters
        public String getKeyword() { return keyword; }
        public void setKeyword(String keyword) { this.keyword = keyword; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }

    // 其他请求和响应类（省略具体实现，但包含必要的字段和方法）
    class CreateModelRequest {
        private String code;
        private String name;
        private String displayName;
        private String description;
        private Long providerId;
        private String type;
        private String version;
        private String providerModelId;
        private Integer maxInputTokens;
        private Integer maxOutputTokens;
        private Integer maxTotalTokens;
        private List<String> supportedLanguages;
        private List<String> supportedFeatures;
        private Map<String, Object> defaultParameters;
        private BigDecimal inputPricePer1k;
        private BigDecimal outputPricePer1k;
        private String currency;
        private Integer rateLimitRpm;
        private Integer rateLimitTpm;
        private Integer concurrencyLimit;
        private List<String> tags;
        private List<String> useCases;

        // Getters and setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getProviderId() { return providerId; }
        public void setProviderId(Long providerId) { this.providerId = providerId; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getProviderModelId() { return providerModelId; }
        public void setProviderModelId(String providerModelId) { this.providerModelId = providerModelId; }
        public Integer getMaxInputTokens() { return maxInputTokens; }
        public void setMaxInputTokens(Integer maxInputTokens) { this.maxInputTokens = maxInputTokens; }
        public Integer getMaxOutputTokens() { return maxOutputTokens; }
        public void setMaxOutputTokens(Integer maxOutputTokens) { this.maxOutputTokens = maxOutputTokens; }
        public Integer getMaxTotalTokens() { return maxTotalTokens; }
        public void setMaxTotalTokens(Integer maxTotalTokens) { this.maxTotalTokens = maxTotalTokens; }
        public List<String> getSupportedLanguages() { return supportedLanguages; }
        public void setSupportedLanguages(List<String> supportedLanguages) { this.supportedLanguages = supportedLanguages; }
        public List<String> getSupportedFeatures() { return supportedFeatures; }
        public void setSupportedFeatures(List<String> supportedFeatures) { this.supportedFeatures = supportedFeatures; }
        public Map<String, Object> getDefaultParameters() { return defaultParameters; }
        public void setDefaultParameters(Map<String, Object> defaultParameters) { this.defaultParameters = defaultParameters; }
        public BigDecimal getInputPricePer1k() { return inputPricePer1k; }
        public void setInputPricePer1k(BigDecimal inputPricePer1k) { this.inputPricePer1k = inputPricePer1k; }
        public BigDecimal getOutputPricePer1k() { return outputPricePer1k; }
        public void setOutputPricePer1k(BigDecimal outputPricePer1k) { this.outputPricePer1k = outputPricePer1k; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public Integer getRateLimitRpm() { return rateLimitRpm; }
        public void setRateLimitRpm(Integer rateLimitRpm) { this.rateLimitRpm = rateLimitRpm; }
        public Integer getRateLimitTpm() { return rateLimitTpm; }
        public void setRateLimitTpm(Integer rateLimitTpm) { this.rateLimitTpm = rateLimitTpm; }
        public Integer getConcurrencyLimit() { return concurrencyLimit; }
        public void setConcurrencyLimit(Integer concurrencyLimit) { this.concurrencyLimit = concurrencyLimit; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public List<String> getUseCases() { return useCases; }
        public void setUseCases(List<String> useCases) { this.useCases = useCases; }
    }

    // 其他请求类和响应类的定义（省略具体实现）
    class UpdateModelRequest { /* 类似CreateModelRequest但字段可选 */ }
    class GetModelsRequest { /* 包含过滤条件 */ }
    class SearchModelsRequest { /* 包含搜索条件 */ }
    class ModelSelectionRequest { /* 模型选择条件 */ }
    class RecordUsageRequest { /* 使用记录请求 */ }
    class UpdateUsageRecordRequest { /* 更新使用记录请求 */ }
    class GetUsageRecordsRequest { /* 获取使用记录请求 */ }

    // 响应类
    class ProviderTestResult {
        private boolean success;
        private String message;
        private long responseTimeMs;
        private Map<String, Object> details;

        public ProviderTestResult(boolean success, String message, long responseTimeMs, Map<String, Object> details) {
            this.success = success;
            this.message = message;
            this.responseTimeMs = responseTimeMs;
            this.details = details;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public long getResponseTimeMs() { return responseTimeMs; }
        public Map<String, Object> getDetails() { return details; }
    }

    class ProviderHealthResult {
        private Long providerId;
        private String providerCode;
        private String healthStatus;
        private String message;
        private LocalDateTime checkTime;
        private long responseTimeMs;

        public ProviderHealthResult(Long providerId, String providerCode, String healthStatus, String message, LocalDateTime checkTime, long responseTimeMs) {
            this.providerId = providerId;
            this.providerCode = providerCode;
            this.healthStatus = healthStatus;
            this.message = message;
            this.checkTime = checkTime;
            this.responseTimeMs = responseTimeMs;
        }

        // Getters
        public Long getProviderId() { return providerId; }
        public String getProviderCode() { return providerCode; }
        public String getHealthStatus() { return healthStatus; }
        public String getMessage() { return message; }
        public LocalDateTime getCheckTime() { return checkTime; }
        public long getResponseTimeMs() { return responseTimeMs; }
    }

    class ModelSelectionResult {
        private ModelConfiguration selectedModel;
        private String reason;
        private List<ModelConfiguration> alternatives;
        private Map<String, Object> selectionCriteria;

        public ModelSelectionResult(ModelConfiguration selectedModel, String reason, List<ModelConfiguration> alternatives, Map<String, Object> selectionCriteria) {
            this.selectedModel = selectedModel;
            this.reason = reason;
            this.alternatives = alternatives;
            this.selectionCriteria = selectionCriteria;
        }

        // Getters
        public ModelConfiguration getSelectedModel() { return selectedModel; }
        public String getReason() { return reason; }
        public List<ModelConfiguration> getAlternatives() { return alternatives; }
        public Map<String, Object> getSelectionCriteria() { return selectionCriteria; }
    }

    // 统计类
    class ProviderStatistics {
        private long totalProviders;
        private long activeProviders;
        private long healthyProviders;
        private Map<String, Long> providersByType;
        private Map<String, Long> providersByStatus;
        private Map<String, Long> providersByHealth;

        // Getters and setters
        public long getTotalProviders() { return totalProviders; }
        public void setTotalProviders(long totalProviders) { this.totalProviders = totalProviders; }
        public long getActiveProviders() { return activeProviders; }
        public void setActiveProviders(long activeProviders) { this.activeProviders = activeProviders; }
        public long getHealthyProviders() { return healthyProviders; }
        public void setHealthyProviders(long healthyProviders) { this.healthyProviders = healthyProviders; }
        public Map<String, Long> getProvidersByType() { return providersByType; }
        public void setProvidersByType(Map<String, Long> providersByType) { this.providersByType = providersByType; }
        public Map<String, Long> getProvidersByStatus() { return providersByStatus; }
        public void setProvidersByStatus(Map<String, Long> providersByStatus) { this.providersByStatus = providersByStatus; }
        public Map<String, Long> getProvidersByHealth() { return providersByHealth; }
        public void setProvidersByHealth(Map<String, Long> providersByHealth) { this.providersByHealth = providersByHealth; }
    }

    class ModelStatistics {
        private long totalModels;
        private long activeModels;
        private Map<String, Long> modelsByType;
        private Map<String, Long> modelsByStatus;
        private Map<String, Long> modelsByProvider;
        private BigDecimal averagePerformanceScore;
        private BigDecimal averageQualityScore;

        // Getters and setters
        public long getTotalModels() { return totalModels; }
        public void setTotalModels(long totalModels) { this.totalModels = totalModels; }
        public long getActiveModels() { return activeModels; }
        public void setActiveModels(long activeModels) { this.activeModels = activeModels; }
        public Map<String, Long> getModelsByType() { return modelsByType; }
        public void setModelsByType(Map<String, Long> modelsByType) { this.modelsByType = modelsByType; }
        public Map<String, Long> getModelsByStatus() { return modelsByStatus; }
        public void setModelsByStatus(Map<String, Long> modelsByStatus) { this.modelsByStatus = modelsByStatus; }
        public Map<String, Long> getModelsByProvider() { return modelsByProvider; }
        public void setModelsByProvider(Map<String, Long> modelsByProvider) { this.modelsByProvider = modelsByProvider; }
        public BigDecimal getAveragePerformanceScore() { return averagePerformanceScore; }
        public void setAveragePerformanceScore(BigDecimal averagePerformanceScore) { this.averagePerformanceScore = averagePerformanceScore; }
        public BigDecimal getAverageQualityScore() { return averageQualityScore; }
        public void setAverageQualityScore(BigDecimal averageQualityScore) { this.averageQualityScore = averageQualityScore; }
    }

    class UsageStatistics {
        private long totalRequests;
        private long successfulRequests;
        private long failedRequests;
        private long totalTokens;
        private BigDecimal totalCost;
        private double successRate;
        private double averageResponseTime;
        private Map<String, Long> requestsByType;
        private Map<String, Long> requestsByStatus;

        // Getters and setters
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        public long getSuccessfulRequests() { return successfulRequests; }
        public void setSuccessfulRequests(long successfulRequests) { this.successfulRequests = successfulRequests; }
        public long getFailedRequests() { return failedRequests; }
        public void setFailedRequests(long failedRequests) { this.failedRequests = failedRequests; }
        public long getTotalTokens() { return totalTokens; }
        public void setTotalTokens(long totalTokens) { this.totalTokens = totalTokens; }
        public BigDecimal getTotalCost() { return totalCost; }
        public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public double getAverageResponseTime() { return averageResponseTime; }
        public void setAverageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
        public Map<String, Long> getRequestsByType() { return requestsByType; }
        public void setRequestsByType(Map<String, Long> requestsByType) { this.requestsByType = requestsByType; }
        public Map<String, Long> getRequestsByStatus() { return requestsByStatus; }
        public void setRequestsByStatus(Map<String, Long> requestsByStatus) { this.requestsByStatus = requestsByStatus; }
    }

    // 其他统计和结果类
    class UserUsageStatistics {
        private Long userId;
        private long totalRequests;
        private long totalTokens;
        private BigDecimal totalCost;
        private double averageResponseTime;
        private Map<String, Long> requestsByModel;
        private Map<String, Long> requestsByType;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        public long getTotalTokens() { return totalTokens; }
        public void setTotalTokens(long totalTokens) { this.totalTokens = totalTokens; }
        public BigDecimal getTotalCost() { return totalCost; }
        public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
        public double getAverageResponseTime() { return averageResponseTime; }
        public void setAverageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
        public Map<String, Long> getRequestsByModel() { return requestsByModel; }
        public void setRequestsByModel(Map<String, Long> requestsByModel) { this.requestsByModel = requestsByModel; }
        public Map<String, Long> getRequestsByType() { return requestsByType; }
        public void setRequestsByType(Map<String, Long> requestsByType) { this.requestsByType = requestsByType; }
    }

    class ModelUsageStatistics {
        private Long modelId;
        private String modelCode;
        private long totalRequests;
        private long successfulRequests;
        private long totalTokens;
        private BigDecimal totalCost;
        private double successRate;
        private double averageResponseTime;
        private long minResponseTime;
        private long maxResponseTime;

        // Getters and setters
        public Long getModelId() { return modelId; }
        public void setModelId(Long modelId) { this.modelId = modelId; }
        public String getModelCode() { return modelCode; }
        public void setModelCode(String modelCode) { this.modelCode = modelCode; }
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        public long getSuccessfulRequests() { return successfulRequests; }
        public void setSuccessfulRequests(long successfulRequests) { this.successfulRequests = successfulRequests; }
        public long getTotalTokens() { return totalTokens; }
        public void setTotalTokens(long totalTokens) { this.totalTokens = totalTokens; }
        public BigDecimal getTotalCost() { return totalCost; }
        public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public double getAverageResponseTime() { return averageResponseTime; }
        public void setAverageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
        public long getMinResponseTime() { return minResponseTime; }
        public void setMinResponseTime(long minResponseTime) { this.minResponseTime = minResponseTime; }
        public long getMaxResponseTime() { return maxResponseTime; }
        public void setMaxResponseTime(long maxResponseTime) { this.maxResponseTime = maxResponseTime; }
    }

    class UsageTrendData {
        private String period;
        private long requestCount;
        private long tokenCount;
        private BigDecimal cost;
        private double averageResponseTime;

        public UsageTrendData(String period, long requestCount, long tokenCount, BigDecimal cost, double averageResponseTime) {
            this.period = period;
            this.requestCount = requestCount;
            this.tokenCount = tokenCount;
            this.cost = cost;
            this.averageResponseTime = averageResponseTime;
        }

        // Getters
        public String getPeriod() { return period; }
        public long getRequestCount() { return requestCount; }
        public long getTokenCount() { return tokenCount; }
        public BigDecimal getCost() { return cost; }
        public double getAverageResponseTime() { return averageResponseTime; }
    }

    class CostAnalysis {
        private BigDecimal totalCost;
        private BigDecimal averageCostPerRequest;
        private BigDecimal averageCostPerToken;
        private Map<String, BigDecimal> costByModel;
        private Map<String, BigDecimal> costByProvider;
        private Map<String, BigDecimal> costByType;
        private List<CostTrendData> costTrend;

        // Getters and setters
        public BigDecimal getTotalCost() { return totalCost; }
        public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
        public BigDecimal getAverageCostPerRequest() { return averageCostPerRequest; }
        public void setAverageCostPerRequest(BigDecimal averageCostPerRequest) { this.averageCostPerRequest = averageCostPerRequest; }
        public BigDecimal getAverageCostPerToken() { return averageCostPerToken; }
        public void setAverageCostPerToken(BigDecimal averageCostPerToken) { this.averageCostPerToken = averageCostPerToken; }
        public Map<String, BigDecimal> getCostByModel() { return costByModel; }
        public void setCostByModel(Map<String, BigDecimal> costByModel) { this.costByModel = costByModel; }
        public Map<String, BigDecimal> getCostByProvider() { return costByProvider; }
        public void setCostByProvider(Map<String, BigDecimal> costByProvider) { this.costByProvider = costByProvider; }
        public Map<String, BigDecimal> getCostByType() { return costByType; }
        public void setCostByType(Map<String, BigDecimal> costByType) { this.costByType = costByType; }
        public List<CostTrendData> getCostTrend() { return costTrend; }
        public void setCostTrend(List<CostTrendData> costTrend) { this.costTrend = costTrend; }
    }

    class CostTrendData {
        private String period;
        private BigDecimal cost;

        public CostTrendData(String period, BigDecimal cost) {
            this.period = period;
            this.cost = cost;
        }

        // Getters
        public String getPeriod() { return period; }
        public BigDecimal getCost() { return cost; }
    }

    class PerformanceAnalysis {
        private double averageResponseTime;
        private long minResponseTime;
        private long maxResponseTime;
        private double p95ResponseTime;
        private double p99ResponseTime;
        private Map<String, Double> responseTimeByModel;
        private Map<String, Double> responseTimeByProvider;
        private List<PerformanceTrendData> performanceTrend;

        // Getters and setters
        public double getAverageResponseTime() { return averageResponseTime; }
        public void setAverageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
        public long getMinResponseTime() { return minResponseTime; }
        public void setMinResponseTime(long minResponseTime) { this.minResponseTime = minResponseTime; }
        public long getMaxResponseTime() { return maxResponseTime; }
        public void setMaxResponseTime(long maxResponseTime) { this.maxResponseTime = maxResponseTime; }
        public double getP95ResponseTime() { return p95ResponseTime; }
        public void setP95ResponseTime(double p95ResponseTime) { this.p95ResponseTime = p95ResponseTime; }
        public double getP99ResponseTime() { return p99ResponseTime; }
        public void setP99ResponseTime(double p99ResponseTime) { this.p99ResponseTime = p99ResponseTime; }
        public Map<String, Double> getResponseTimeByModel() { return responseTimeByModel; }
        public void setResponseTimeByModel(Map<String, Double> responseTimeByModel) { this.responseTimeByModel = responseTimeByModel; }
        public Map<String, Double> getResponseTimeByProvider() { return responseTimeByProvider; }
        public void setResponseTimeByProvider(Map<String, Double> responseTimeByProvider) { this.responseTimeByProvider = responseTimeByProvider; }
        public List<PerformanceTrendData> getPerformanceTrend() { return performanceTrend; }
        public void setPerformanceTrend(List<PerformanceTrendData> performanceTrend) { this.performanceTrend = performanceTrend; }
    }

    class PerformanceTrendData {
        private String period;
        private double averageResponseTime;

        public PerformanceTrendData(String period, double averageResponseTime) {
            this.period = period;
            this.averageResponseTime = averageResponseTime;
        }

        // Getters
        public String getPeriod() { return period; }
        public double getAverageResponseTime() { return averageResponseTime; }
    }

    class BatchOperationResult {
        private boolean success;
        private int successCount;
        private int failedCount;
        private List<String> errors;

        public BatchOperationResult(boolean success, int successCount, int failedCount, List<String> errors) {
            this.success = success;
            this.successCount = successCount;
            this.failedCount = failedCount;
            this.errors = errors;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public int getSuccessCount() { return successCount; }
        public int getFailedCount() { return failedCount; }
        public List<String> getErrors() { return errors; }
    }

    class SyncResult {
        private boolean success;
        private int syncedModels;
        private int newModels;
        private int updatedModels;
        private int removedModels;
        private List<String> errors;

        public SyncResult(boolean success, int syncedModels, int newModels, int updatedModels, int removedModels, List<String> errors) {
            this.success = success;
            this.syncedModels = syncedModels;
            this.newModels = newModels;
            this.updatedModels = updatedModels;
            this.removedModels = removedModels;
            this.errors = errors;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public int getSyncedModels() { return syncedModels; }
        public int getNewModels() { return newModels; }
        public int getUpdatedModels() { return updatedModels; }
        public int getRemovedModels() { return removedModels; }
        public List<String> getErrors() { return errors; }
    }

    class CleanupResult {
        private boolean success;
        private long deletedRecords;
        private String message;

        public CleanupResult(boolean success, long deletedRecords, String message) {
            this.success = success;
            this.deletedRecords = deletedRecords;
            this.message = message;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public long getDeletedRecords() { return deletedRecords; }
        public String getMessage() { return message; }
    }

    // ==================== 模型配置模板管理 ====================

    /**
     * 创建模型配置
     */
    ModelConfigResponse createModelConfig(Long userId, CreateModelConfigRequest request);

    /**
     * 更新模型配置
     */
    ModelConfigResponse updateModelConfig(Long userId, Long configId, UpdateModelConfigRequest request);

    /**
     * 删除模型配置
     */
    void deleteModelConfig(Long userId, Long configId);

    /**
     * 获取模型配置详情
     */
    ModelConfigResponse getModelConfig(Long configId);

    /**
     * 获取模型配置列表
     */
    PageResponse<ModelConfigResponse> getModelConfigs(ModelConfigQueryRequest queryRequest, Pageable pageable);

    /**
     * 获取模板配置列表
     */
    PageResponse<ModelConfigResponse> getTemplateConfigs(String category, Pageable pageable);

    /**
     * 从模板创建配置
     */
    ModelConfigResponse createFromTemplate(Long userId, Long templateId, CreateFromTemplateRequest request);

    /**
     * 启用/禁用模型配置
     */
    ModelConfigResponse toggleModelConfigStatus(Long userId, Long configId, boolean enabled);

    /**
     * 复制模型配置
     */
    ModelConfigResponse duplicateModelConfig(Long userId, Long configId, String newName);

    /**
     * 导入模型配置模板
     */
    List<ModelConfigResponse> importModelConfigTemplates(List<ModelConfigTemplate> templates);

    /**
     * 导出模型配置
     */
    ModelConfigTemplate exportModelConfig(Long configId);

    /**
     * 验证模型配置
     */
    ValidationResult validateModelConfig(CreateModelConfigRequest request);

    // ==================== 模型配置相关DTO类 ====================

    /**
     * 创建模型配置请求
     */
    class CreateModelConfigRequest {
        private Long modelId;
        private String name;
        private String iconUri;
        private String iconUrl;
        private String description;
        private String defaultParameters;
        private String meta;
        private String connectionConfig;
        private Boolean isTemplate;
        private String templateCategory;

        // Getters and setters
        public Long getModelId() { return modelId; }
        public void setModelId(Long modelId) { this.modelId = modelId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getIconUri() { return iconUri; }
        public void setIconUri(String iconUri) { this.iconUri = iconUri; }
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDefaultParameters() { return defaultParameters; }
        public void setDefaultParameters(String defaultParameters) { this.defaultParameters = defaultParameters; }
        public String getMeta() { return meta; }
        public void setMeta(String meta) { this.meta = meta; }
        public String getConnectionConfig() { return connectionConfig; }
        public void setConnectionConfig(String connectionConfig) { this.connectionConfig = connectionConfig; }
        public Boolean getIsTemplate() { return isTemplate; }
        public void setIsTemplate(Boolean isTemplate) { this.isTemplate = isTemplate; }
        public String getTemplateCategory() { return templateCategory; }
        public void setTemplateCategory(String templateCategory) { this.templateCategory = templateCategory; }
    }

    /**
     * 更新模型配置请求
     */
    class UpdateModelConfigRequest {
        private String name;
        private String iconUri;
        private String iconUrl;
        private String description;
        private String defaultParameters;
        private String meta;
        private String connectionConfig;
        private String templateCategory;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getIconUri() { return iconUri; }
        public void setIconUri(String iconUri) { this.iconUri = iconUri; }
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDefaultParameters() { return defaultParameters; }
        public void setDefaultParameters(String defaultParameters) { this.defaultParameters = defaultParameters; }
        public String getMeta() { return meta; }
        public void setMeta(String meta) { this.meta = meta; }
        public String getConnectionConfig() { return connectionConfig; }
        public void setConnectionConfig(String connectionConfig) { this.connectionConfig = connectionConfig; }
        public String getTemplateCategory() { return templateCategory; }
        public void setTemplateCategory(String templateCategory) { this.templateCategory = templateCategory; }
    }

    /**
     * 模型配置查询请求
     */
    class ModelConfigQueryRequest {
        private String name;
        private String templateCategory;
        private Boolean isTemplate;
        private Integer status;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getTemplateCategory() { return templateCategory; }
        public void setTemplateCategory(String templateCategory) { this.templateCategory = templateCategory; }
        public Boolean getIsTemplate() { return isTemplate; }
        public void setIsTemplate(Boolean isTemplate) { this.isTemplate = isTemplate; }
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
    }

    /**
     * 从模板创建配置请求
     */
    class CreateFromTemplateRequest {
        private String name;
        private Long modelId;
        private String connectionConfig;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getModelId() { return modelId; }
        public void setModelId(Long modelId) { this.modelId = modelId; }
        public String getConnectionConfig() { return connectionConfig; }
        public void setConnectionConfig(String connectionConfig) { this.connectionConfig = connectionConfig; }
    }

    /**
     * 模型配置模板
     */
    class ModelConfigTemplate {
        private String name;
        private String templateCategory;
        private String iconUri;
        private String iconUrl;
        private String description;
        private String defaultParameters;
        private String meta;
        private String connectionConfigTemplate;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getTemplateCategory() { return templateCategory; }
        public void setTemplateCategory(String templateCategory) { this.templateCategory = templateCategory; }
        public String getIconUri() { return iconUri; }
        public void setIconUri(String iconUri) { this.iconUri = iconUri; }
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDefaultParameters() { return defaultParameters; }
        public void setDefaultParameters(String defaultParameters) { this.defaultParameters = defaultParameters; }
        public String getMeta() { return meta; }
        public void setMeta(String meta) { this.meta = meta; }
        public String getConnectionConfigTemplate() { return connectionConfigTemplate; }
        public void setConnectionConfigTemplate(String connectionConfigTemplate) { this.connectionConfigTemplate = connectionConfigTemplate; }
    }

    /**
     * 验证结果
     */
    class ValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;

        public ValidationResult() {
            this.errors = new java.util.ArrayList<>();
            this.warnings = new java.util.ArrayList<>();
        }

        public ValidationResult(boolean valid) {
            this();
            this.valid = valid;
        }

        // Getters and setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }

        public void addError(String error) {
            this.errors.add(error);
        }

        public void addWarning(String warning) {
            this.warnings.add(warning);
        }
    }

    /**
     * 模型配置响应
     */
    class ModelConfigResponse {
        private Long id;
        private Long modelId;
        private String name;
        private String iconUri;
        private String iconUrl;
        private String description;
        private String defaultParameters;
        private String meta;
        private String connectionConfig;
        private Integer status;
        private Boolean isTemplate;
        private String templateCategory;
        private Long creatorId;
        private Long updaterId;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getModelId() { return modelId; }
        public void setModelId(Long modelId) { this.modelId = modelId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getIconUri() { return iconUri; }
        public void setIconUri(String iconUri) { this.iconUri = iconUri; }
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDefaultParameters() { return defaultParameters; }
        public void setDefaultParameters(String defaultParameters) { this.defaultParameters = defaultParameters; }
        public String getMeta() { return meta; }
        public void setMeta(String meta) { this.meta = meta; }
        public String getConnectionConfig() { return connectionConfig; }
        public void setConnectionConfig(String connectionConfig) { this.connectionConfig = connectionConfig; }
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        public Boolean getIsTemplate() { return isTemplate; }
        public void setIsTemplate(Boolean isTemplate) { this.isTemplate = isTemplate; }
        public String getTemplateCategory() { return templateCategory; }
        public void setTemplateCategory(String templateCategory) { this.templateCategory = templateCategory; }
        public Long getCreatorId() { return creatorId; }
        public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }
        public Long getUpdaterId() { return updaterId; }
        public void setUpdaterId(Long updaterId) { this.updaterId = updaterId; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
        public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }
}

