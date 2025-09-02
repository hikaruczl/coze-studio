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

package com.coze.studio.entity;

// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * AI模型提供商实体类
 * 用于管理不同的AI模型提供商配置和认证信息
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: model_providers
// 索引信息已移至数据库DDL脚本
public class ModelProvider extends BaseEntity {

    /**
     * 提供商代码（唯一标识）
     */
        private String code;

    /**
     * 提供商名称
     */
        private String name;

    /**
     * 提供商显示名称
     */
        private String displayName;

    /**
     * 提供商描述
     */
        private String description;

    /**
     * 提供商类型
     * OPENAI - OpenAI
     * ANTHROPIC - Anthropic
     * GOOGLE - Google
     * AZURE - Azure OpenAI
     * HUGGINGFACE - Hugging Face
     * COHERE - Cohere
     * CUSTOM - 自定义
     */
        private String type;

    /**
     * API基础URL
     */
        private String baseUrl;

    /**
     * API版本
     */
        private String apiVersion;

    /**
     * 认证类型
     * API_KEY - API密钥
     * OAUTH2 - OAuth2.0
     * BEARER - Bearer Token
     * BASIC - Basic Auth
     * CUSTOM - 自定义
     */
        private String authType = "API_KEY";

    /**
     * API密钥
     */
        private String apiKey;

    /**
     * 组织ID（如OpenAI的organization）
     */
        private String organizationId;

    /**
     * 项目ID（如Google的project_id）
     */
        private String projectId;

    /**
     * 区域/地域（如Azure的region）
     */
        private String region;

    /**
     * 额外的认证配置（JSON格式）
     */
        private String authConfig;

    /**
     * 请求配置（JSON格式）
     */
        private String requestConfig;

    /**
     * 提供商状态
     * ACTIVE - 活跃
     * INACTIVE - 非活跃
     * MAINTENANCE - 维护中
     * DEPRECATED - 已弃用
     */
        private String status = "ACTIVE";

    /**
     * 是否启用
     */
        private Boolean enabled = true;

    /**
     * 是否为默认提供商
     */
        private Boolean isDefault = false;

    /**
     * 优先级（数字越小优先级越高）
     */
        private Integer priority = 100;

    /**
     * 速率限制配置（JSON格式）
     */
        private String rateLimitConfig;

    /**
     * 重试配置（JSON格式）
     */
        private String retryConfig;

    /**
     * 超时配置（毫秒）
     */
        private Integer timeoutMs = 30000;

    /**
     * 连接超时（毫秒）
     */
        private Integer connectTimeoutMs = 10000;

    /**
     * 读取超时（毫秒）
     */
        private Integer readTimeoutMs = 30000;

    /**
     * 最大并发请求数
     */
        private Integer maxConcurrentRequests = 10;

    /**
     * 健康检查URL
     */
        private String healthCheckUrl;

    /**
     * 健康检查间隔（秒）
     */
        private Integer healthCheckInterval = 300;

    /**
     * 最后健康检查时间
     */
        private LocalDateTime lastHealthCheck;

    /**
     * 健康状态
     */
        private String healthStatus = "UNKNOWN";

    /**
     * 支持的功能列表（JSON格式）
     */
        private String supportedFeatures;

    /**
     * 提供商图标URL
     */
        private String iconUrl;

    /**
     * 文档URL
     */
        private String documentationUrl;

    /**
     * 提供商官网
     */
        private String websiteUrl;

    /**
     * 联系邮箱
     */
        private String contactEmail;

    /**
     * 支持邮箱
     */
        private String supportEmail;

    /**
     * 提供商元数据（JSON格式）
     */
        private String metadata;

    /**
     * 提供商类型枚举
     */
    public enum Type {
        OPENAI("OPENAI", "OpenAI"),
        ANTHROPIC("ANTHROPIC", "Anthropic"),
        GOOGLE("GOOGLE", "Google"),
        AZURE("AZURE", "Azure OpenAI"),
        HUGGINGFACE("HUGGINGFACE", "Hugging Face"),
        COHERE("COHERE", "Cohere"),
        CUSTOM("CUSTOM", "自定义");

        private final String code;
        private final String description;

        Type(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 认证类型枚举
     */
    public enum AuthType {
        API_KEY("API_KEY", "API密钥"),
        OAUTH2("OAUTH2", "OAuth2.0"),
        BEARER("BEARER", "Bearer Token"),
        BASIC("BASIC", "Basic Auth"),
        CUSTOM("CUSTOM", "自定义");

        private final String code;
        private final String description;

        AuthType(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 提供商状态枚举
     */
    public enum Status {
        ACTIVE("ACTIVE", "活跃"),
        INACTIVE("INACTIVE", "非活跃"),
        MAINTENANCE("MAINTENANCE", "维护中"),
        DEPRECATED("DEPRECATED", "已弃用");

        private final String code;
        private final String description;

        Status(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 健康状态枚举
     */
    public enum HealthStatus {
        HEALTHY("HEALTHY", "健康"),
        UNHEALTHY("UNHEALTHY", "不健康"),
        UNKNOWN("UNKNOWN", "未知");

        private final String code;
        private final String description;

        HealthStatus(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 检查是否为活跃状态
     */
    public boolean isActive() {
        return Status.ACTIVE.getCode().equals(this.status) && this.enabled;
    }

    /**
     * 检查是否健康
     */
    public boolean isHealthy() {
        return HealthStatus.HEALTHY.getCode().equals(this.healthStatus);
    }

    /**
     * 更新健康状态
     */
    public void updateHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
        this.lastHealthCheck = LocalDateTime.now();
    }

    /**
     * 检查是否需要健康检查
     */
    public boolean needsHealthCheck() {
        if (lastHealthCheck == null) {
            return true;
        }
        return lastHealthCheck.plusSeconds(healthCheckInterval).isBefore(LocalDateTime.now());
    }


    // Lombok生成的getter/setter方法
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAuthConfig() {
        return authConfig;
    }

    public void setAuthConfig(String authConfig) {
        this.authConfig = authConfig;
    }

    public String getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(String requestConfig) {
        this.requestConfig = requestConfig;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getRateLimitConfig() {
        return rateLimitConfig;
    }

    public void setRateLimitConfig(String rateLimitConfig) {
        this.rateLimitConfig = rateLimitConfig;
    }

    public String getRetryConfig() {
        return retryConfig;
    }

    public void setRetryConfig(String retryConfig) {
        this.retryConfig = retryConfig;
    }

    public Integer getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(Integer timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public Integer getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(Integer connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public Integer getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(Integer readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public Integer getMaxConcurrentRequests() {
        return maxConcurrentRequests;
    }

    public void setMaxConcurrentRequests(Integer maxConcurrentRequests) {
        this.maxConcurrentRequests = maxConcurrentRequests;
    }

    public String getHealthCheckUrl() {
        return healthCheckUrl;
    }

    public void setHealthCheckUrl(String healthCheckUrl) {
        this.healthCheckUrl = healthCheckUrl;
    }

    public Integer getHealthCheckInterval() {
        return healthCheckInterval;
    }

    public void setHealthCheckInterval(Integer healthCheckInterval) {
        this.healthCheckInterval = healthCheckInterval;
    }

    public LocalDateTime getLastHealthCheck() {
        return lastHealthCheck;
    }

    public void setLastHealthCheck(LocalDateTime lastHealthCheck) {
        this.lastHealthCheck = lastHealthCheck;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getSupportedFeatures() {
        return supportedFeatures;
    }

    public void setSupportedFeatures(String supportedFeatures) {
        this.supportedFeatures = supportedFeatures;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
