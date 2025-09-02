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
 * 模型使用记录实体类
 * 用于记录AI模型的使用情况和统计信息
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: model_usage_records
// 索引信息已移至数据库DDL脚本
public class ModelUsageRecord extends BaseEntity {

    /**
     * 模型配置ID
     */
        private Long modelId;

    /**
     * 用户ID
     */
        private Long userId;

    /**
     * 会话ID
     */
        private String sessionId;

    /**
     * 请求ID（用于追踪）
     */
        private String requestId;

    /**
     * 应用ID
     */
        private Long applicationId;

    /**
     * 工作流ID
     */
        private Long workflowId;

    /**
     * 对话ID
     */
        private Long conversationId;

    /**
     * 使用类型
     * CHAT - 对话
     * COMPLETION - 文本完成
     * EMBEDDING - 文本嵌入
     * IMAGE_GENERATION - 图像生成
     * IMAGE_ANALYSIS - 图像分析
     * AUDIO_TRANSCRIPTION - 音频转录
     * FUNCTION_CALL - 函数调用
     * WORKFLOW - 工作流
     */
        private String usageType;

    /**
     * 请求状态
     * SUCCESS - 成功
     * FAILED - 失败
     * TIMEOUT - 超时
     * RATE_LIMITED - 速率限制
     * QUOTA_EXCEEDED - 配额超限
     */
        private String status;

    /**
     * 输入token数
     */
        private Integer inputTokens = 0;

    /**
     * 输出token数
     */
        private Integer outputTokens = 0;

    /**
     * 总token数
     */
        private Integer totalTokens = 0;

    /**
     * 请求开始时间
     */
        private LocalDateTime requestStartTime;

    /**
     * 请求结束时间
     */
        private LocalDateTime requestEndTime;

    /**
     * 响应时间（毫秒）
     */
        private Long responseTimeMs;

    /**
     * 成本
     */
        private BigDecimal cost;

    /**
     * 货币单位
     */
        private String currency = "USD";

    /**
     * 错误代码
     */
        private String errorCode;

    /**
     * 错误消息
     */
        private String errorMessage;

    /**
     * 请求参数（JSON格式）
     */
        private String requestParameters;

    /**
     * 响应元数据（JSON格式）
     */
        private String responseMetadata;

    /**
     * 使用日期（用于统计）
     */
        private LocalDateTime usageDate;

    /**
     * 客户端IP
     */
        private String clientIp;

    /**
     * 用户代理
     */
        private String userAgent;

    /**
     * 地理位置信息（JSON格式）
     */
        private String geoInfo;

    /**
     * 使用类型枚举
     */
    public enum UsageType {
        CHAT("CHAT", "对话"),
        COMPLETION("COMPLETION", "文本完成"),
        EMBEDDING("EMBEDDING", "文本嵌入"),
        IMAGE_GENERATION("IMAGE_GENERATION", "图像生成"),
        IMAGE_ANALYSIS("IMAGE_ANALYSIS", "图像分析"),
        AUDIO_TRANSCRIPTION("AUDIO_TRANSCRIPTION", "音频转录"),
        FUNCTION_CALL("FUNCTION_CALL", "函数调用"),
        WORKFLOW("WORKFLOW", "工作流");

        private final String code;
        private final String description;

        UsageType(String code, String description) {
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
     * 请求状态枚举
     */
    public enum Status {
        SUCCESS("SUCCESS", "成功"),
        FAILED("FAILED", "失败"),
        TIMEOUT("TIMEOUT", "超时"),
        RATE_LIMITED("RATE_LIMITED", "速率限制"),
        QUOTA_EXCEEDED("QUOTA_EXCEEDED", "配额超限");

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
     * 检查是否成功
     */
    public boolean isSuccess() {
        return Status.SUCCESS.getCode().equals(this.status);
    }

    /**
     * 检查是否失败
     */
    public boolean isFailed() {
        return Status.FAILED.getCode().equals(this.status);
    }

    /**
     * 计算响应时间
     */
    public void calculateResponseTime() {
        if (requestStartTime != null && requestEndTime != null) {
            this.responseTimeMs = java.time.Duration.between(requestStartTime, requestEndTime).toMillis();
        }
    }

    /**
     * 设置成功状态
     */
    public void setSuccess(int inputTokens, int outputTokens, BigDecimal cost) {
        this.status = Status.SUCCESS.getCode();
        this.inputTokens = inputTokens;
        this.outputTokens = outputTokens;
        this.totalTokens = inputTokens + outputTokens;
        this.cost = cost;
        this.requestEndTime = LocalDateTime.now();
        calculateResponseTime();
    }

    /**
     * 设置失败状态
     */
    public void setFailed(String errorCode, String errorMessage) {
        this.status = Status.FAILED.getCode();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.requestEndTime = LocalDateTime.now();
        calculateResponseTime();
    }

    /**
     * 设置超时状态
     */
    public void setTimeout() {
        this.status = Status.TIMEOUT.getCode();
        this.errorCode = "TIMEOUT";
        this.errorMessage = "请求超时";
        this.requestEndTime = LocalDateTime.now();
        calculateResponseTime();
    }

    /**
     * 设置速率限制状态
     */
    public void setRateLimited() {
        this.status = Status.RATE_LIMITED.getCode();
        this.errorCode = "RATE_LIMITED";
        this.errorMessage = "请求速率超限";
        this.requestEndTime = LocalDateTime.now();
        calculateResponseTime();
    }

    /**
     * 设置配额超限状态
     */
    public void setQuotaExceeded() {
        this.status = Status.QUOTA_EXCEEDED.getCode();
        this.errorCode = "QUOTA_EXCEEDED";
        this.errorMessage = "配额已用完";
        this.requestEndTime = LocalDateTime.now();
        calculateResponseTime();
    }

    /**
     * 获取成本效率（token/成本）
     */
    public BigDecimal getCostEfficiency() {
        if (cost == null || cost.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(totalTokens).divide(cost, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取处理速度（token/秒）
     */
    public BigDecimal getProcessingSpeed() {
        if (responseTimeMs == null || responseTimeMs == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal seconds = new BigDecimal(responseTimeMs).divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);
        return new BigDecimal(totalTokens).divide(seconds, 2, BigDecimal.ROUND_HALF_UP);
    }


    // Lombok生成的getter/setter方法
    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getInputTokens() {
        return inputTokens;
    }

    public void setInputTokens(Integer inputTokens) {
        this.inputTokens = inputTokens;
    }

    public Integer getOutputTokens() {
        return outputTokens;
    }

    public void setOutputTokens(Integer outputTokens) {
        this.outputTokens = outputTokens;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    public LocalDateTime getRequestStartTime() {
        return requestStartTime;
    }

    public void setRequestStartTime(LocalDateTime requestStartTime) {
        this.requestStartTime = requestStartTime;
    }

    public LocalDateTime getRequestEndTime() {
        return requestEndTime;
    }

    public void setRequestEndTime(LocalDateTime requestEndTime) {
        this.requestEndTime = requestEndTime;
    }

    public Long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(Long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getResponseMetadata() {
        return responseMetadata;
    }

    public void setResponseMetadata(String responseMetadata) {
        this.responseMetadata = responseMetadata;
    }

    public LocalDateTime getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDateTime usageDate) {
        this.usageDate = usageDate;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getGeoInfo() {
        return geoInfo;
    }

    public void setGeoInfo(String geoInfo) {
        this.geoInfo = geoInfo;
    }
}
