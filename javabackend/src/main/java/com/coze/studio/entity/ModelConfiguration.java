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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI模型配置实体类
 * 用于管理具体的AI模型配置信息和参数
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: model_configurations
// 索引信息已移至数据库DDL脚本
public class ModelConfiguration extends BaseEntity {

    /**
     * 模型代码（唯一标识）
     */
        private String code;

    /**
     * 模型名称
     */
        private String name;

    /**
     * 模型显示名称
     */
        private String displayName;

    /**
     * 模型描述
     */
        private String description;

    /**
     * 提供商ID
     */
        private Long providerId;

    /**
     * 模型类型
     * TEXT_GENERATION - 文本生成
     * CHAT_COMPLETION - 对话完成
     * TEXT_EMBEDDING - 文本嵌入
     * IMAGE_GENERATION - 图像生成
     * IMAGE_ANALYSIS - 图像分析
     * AUDIO_TRANSCRIPTION - 音频转录
     * AUDIO_GENERATION - 音频生成
     * CODE_GENERATION - 代码生成
     * FUNCTION_CALLING - 函数调用
     */
        private String type;

    /**
     * 模型版本
     */
        private String version;

    /**
     * 模型在提供商处的标识符
     */
        private String providerModelId;

    /**
     * 模型状态
     * ACTIVE - 活跃
     * INACTIVE - 非活跃
     * BETA - 测试版
     * DEPRECATED - 已弃用
     * MAINTENANCE - 维护中
     */
        private String status = "ACTIVE";

    /**
     * 是否启用
     */
        private Boolean enabled = true;

    /**
     * 是否为默认模型
     */
        private Boolean isDefault = false;

    /**
     * 优先级（数字越小优先级越高）
     */
        private Integer priority = 100;

    /**
     * 最大输入token数
     */
        private Integer maxInputTokens;

    /**
     * 最大输出token数
     */
        private Integer maxOutputTokens;

    /**
     * 最大总token数
     */
        private Integer maxTotalTokens;

    /**
     * 支持的语言列表（JSON格式）
     */
        private String supportedLanguages;

    /**
     * 支持的功能列表（JSON格式）
     */
        private String supportedFeatures;

    /**
     * 默认参数配置（JSON格式）
     */
        private String defaultParameters;

    /**
     * 参数约束配置（JSON格式）
     */
        private String parameterConstraints;

    /**
     * 输入价格（每1K tokens）
     */
        private BigDecimal inputPricePer1k;

    /**
     * 输出价格（每1K tokens）
     */
        private BigDecimal outputPricePer1k;

    /**
     * 货币单位
     */
        private String currency = "USD";

    /**
     * 速率限制（每分钟请求数）
     */
        private Integer rateLimitRpm;

    /**
     * 速率限制（每分钟token数）
     */
        private Integer rateLimitTpm;

    /**
     * 并发限制
     */
        private Integer concurrencyLimit;

    /**
     * 模型性能评分
     */
        private BigDecimal performanceScore;

    /**
     * 质量评分
     */
        private BigDecimal qualityScore;

    /**
     * 延迟评分（毫秒）
     */
        private Integer latencyScore;

    /**
     * 可用性评分（百分比）
     */
        private BigDecimal availabilityScore;

    /**
     * 模型标签（JSON格式）
     */
        private String tags;

    /**
     * 使用场景（JSON格式）
     */
        private String useCases;

    /**
     * 模型限制说明
     */
        private String limitations;

    /**
     * 最后更新时间
     */
        private LocalDateTime lastUpdated;

    /**
     * 发布时间
     */
        private LocalDateTime releaseDate;

    /**
     * 弃用时间
     */
        private LocalDateTime deprecatedDate;

    /**
     * 模型文档URL
     */
        private String documentationUrl;

    /**
     * 模型示例URL
     */
        private String exampleUrl;

    /**
     * 模型元数据（JSON格式）
     */
        private String metadata;

    /**
     * 模型类型枚举
     */
    public enum Type {
        TEXT_GENERATION("TEXT_GENERATION", "文本生成"),
        CHAT_COMPLETION("CHAT_COMPLETION", "对话完成"),
        TEXT_EMBEDDING("TEXT_EMBEDDING", "文本嵌入"),
        IMAGE_GENERATION("IMAGE_GENERATION", "图像生成"),
        IMAGE_ANALYSIS("IMAGE_ANALYSIS", "图像分析"),
        AUDIO_TRANSCRIPTION("AUDIO_TRANSCRIPTION", "音频转录"),
        AUDIO_GENERATION("AUDIO_GENERATION", "音频生成"),
        CODE_GENERATION("CODE_GENERATION", "代码生成"),
        FUNCTION_CALLING("FUNCTION_CALLING", "函数调用");

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
     * 模型状态枚举
     */
    public enum Status {
        ACTIVE("ACTIVE", "活跃"),
        INACTIVE("INACTIVE", "非活跃"),
        BETA("BETA", "测试版"),
        DEPRECATED("DEPRECATED", "已弃用"),
        MAINTENANCE("MAINTENANCE", "维护中");

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
     * 检查是否为活跃状态
     */
    public boolean isActive() {
        return Status.ACTIVE.getCode().equals(this.status) && this.enabled;
    }

    /**
     * 检查是否已弃用
     */
    public boolean isDeprecated() {
        return Status.DEPRECATED.getCode().equals(this.status);
    }

    /**
     * 检查是否为测试版
     */
    public boolean isBeta() {
        return Status.BETA.getCode().equals(this.status);
    }

    /**
     * 计算综合评分
     */
    public BigDecimal calculateOverallScore() {
        if (performanceScore == null || qualityScore == null || availabilityScore == null) {
            return BigDecimal.ZERO;
        }
        
        // 权重：性能30%，质量40%，可用性30%
        BigDecimal score = performanceScore.multiply(new BigDecimal("0.3"))
                .add(qualityScore.multiply(new BigDecimal("0.4")))
                .add(availabilityScore.divide(new BigDecimal("100")).multiply(new BigDecimal("0.3")));
        
        return score.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 检查是否支持指定功能
     */
    public boolean supportsFeature(String feature) {
        if (supportedFeatures == null) {
            return false;
        }
        return supportedFeatures.contains(feature);
    }

    /**
     * 检查token限制
     */
    public boolean isWithinTokenLimit(int inputTokens, int outputTokens) {
        if (maxInputTokens != null && inputTokens > maxInputTokens) {
            return false;
        }
        if (maxOutputTokens != null && outputTokens > maxOutputTokens) {
            return false;
        }
        if (maxTotalTokens != null && (inputTokens + outputTokens) > maxTotalTokens) {
            return false;
        }
        return true;
    }

    /**
     * 计算成本
     */
    public BigDecimal calculateCost(int inputTokens, int outputTokens) {
        BigDecimal cost = BigDecimal.ZERO;
        
        if (inputPricePer1k != null && inputTokens > 0) {
            cost = cost.add(inputPricePer1k.multiply(new BigDecimal(inputTokens)).divide(new BigDecimal("1000")));
        }
        
        if (outputPricePer1k != null && outputTokens > 0) {
            cost = cost.add(outputPricePer1k.multiply(new BigDecimal(outputTokens)).divide(new BigDecimal("1000")));
        }
        
        return cost.setScale(6, BigDecimal.ROUND_HALF_UP);
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

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProviderModelId() {
        return providerModelId;
    }

    public void setProviderModelId(String providerModelId) {
        this.providerModelId = providerModelId;
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

    public Integer getMaxInputTokens() {
        return maxInputTokens;
    }

    public void setMaxInputTokens(Integer maxInputTokens) {
        this.maxInputTokens = maxInputTokens;
    }

    public Integer getMaxOutputTokens() {
        return maxOutputTokens;
    }

    public void setMaxOutputTokens(Integer maxOutputTokens) {
        this.maxOutputTokens = maxOutputTokens;
    }

    public Integer getMaxTotalTokens() {
        return maxTotalTokens;
    }

    public void setMaxTotalTokens(Integer maxTotalTokens) {
        this.maxTotalTokens = maxTotalTokens;
    }

    public String getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(String supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public String getSupportedFeatures() {
        return supportedFeatures;
    }

    public void setSupportedFeatures(String supportedFeatures) {
        this.supportedFeatures = supportedFeatures;
    }

    public String getDefaultParameters() {
        return defaultParameters;
    }

    public void setDefaultParameters(String defaultParameters) {
        this.defaultParameters = defaultParameters;
    }

    public String getParameterConstraints() {
        return parameterConstraints;
    }

    public void setParameterConstraints(String parameterConstraints) {
        this.parameterConstraints = parameterConstraints;
    }

    public BigDecimal getInputPricePer1k() {
        return inputPricePer1k;
    }

    public void setInputPricePer1k(BigDecimal inputPricePer1k) {
        this.inputPricePer1k = inputPricePer1k;
    }

    public BigDecimal getOutputPricePer1k() {
        return outputPricePer1k;
    }

    public void setOutputPricePer1k(BigDecimal outputPricePer1k) {
        this.outputPricePer1k = outputPricePer1k;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getRateLimitRpm() {
        return rateLimitRpm;
    }

    public void setRateLimitRpm(Integer rateLimitRpm) {
        this.rateLimitRpm = rateLimitRpm;
    }

    public Integer getRateLimitTpm() {
        return rateLimitTpm;
    }

    public void setRateLimitTpm(Integer rateLimitTpm) {
        this.rateLimitTpm = rateLimitTpm;
    }

    public Integer getConcurrencyLimit() {
        return concurrencyLimit;
    }

    public void setConcurrencyLimit(Integer concurrencyLimit) {
        this.concurrencyLimit = concurrencyLimit;
    }

    public BigDecimal getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(BigDecimal performanceScore) {
        this.performanceScore = performanceScore;
    }

    public BigDecimal getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(BigDecimal qualityScore) {
        this.qualityScore = qualityScore;
    }

    public Integer getLatencyScore() {
        return latencyScore;
    }

    public void setLatencyScore(Integer latencyScore) {
        this.latencyScore = latencyScore;
    }

    public BigDecimal getAvailabilityScore() {
        return availabilityScore;
    }

    public void setAvailabilityScore(BigDecimal availabilityScore) {
        this.availabilityScore = availabilityScore;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUseCases() {
        return useCases;
    }

    public void setUseCases(String useCases) {
        this.useCases = useCases;
    }

    public String getLimitations() {
        return limitations;
    }

    public void setLimitations(String limitations) {
        this.limitations = limitations;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getDeprecatedDate() {
        return deprecatedDate;
    }

    public void setDeprecatedDate(LocalDateTime deprecatedDate) {
        this.deprecatedDate = deprecatedDate;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getExampleUrl() {
        return exampleUrl;
    }

    public void setExampleUrl(String exampleUrl) {
        this.exampleUrl = exampleUrl;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
