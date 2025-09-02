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

/**
 * 向量存储配置实体类
 * 管理不同的向量数据库配置
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: vector_stores
// 索引信息已移至数据库DDL脚本
public class VectorStore extends BaseEntity {

    /**
     * 向量存储名称
     */
        private String name;

    /**
     * 向量存储类型：MILVUS, PINECONE, WEAVIATE, CHROMA, QDRANT, ELASTICSEARCH
     */
        private String storeType;

    /**
     * 连接配置（JSON格式）
     */
        private String connectionConfig;

    /**
     * 索引配置（JSON格式）
     */
        private String indexConfig;

    /**
     * 嵌入模型配置（JSON格式）
     */
        private String embeddingConfig;

    /**
     * 默认嵌入维度
     */
        private Integer defaultDimension;

    /**
     * 默认相似度度量：COSINE, EUCLIDEAN, DOT_PRODUCT
     */
        private String defaultMetric = "COSINE";

    /**
     * 是否启用
     */
        private Boolean enabled = true;

    /**
     * 是否为默认存储
     */
        private Boolean isDefault = false;

    /**
     * 描述
     */
        private String description;

    /**
     * 最大向量数量
     */
        private Long maxVectors;

    /**
     * 当前向量数量
     */
        private Long currentVectors = 0L;

    /**
     * 存储状态：ACTIVE-活跃, INACTIVE-非活跃, ERROR-错误
     */
        private String status = "ACTIVE";

    /**
     * 状态消息
     */
        private String statusMessage;

    /**
     * 最后健康检查时间
     */
        private java.time.LocalDateTime lastHealthCheck;

    /**
     * 健康检查结果
     */
        private String healthStatus = "UNKNOWN";

    /**
     * 平均查询延迟（毫秒）
     */
        private Double avgQueryLatency;

    /**
     * 总查询次数
     */
        private Long totalQueries = 0L;

    /**
     * 成功查询次数
     */
        private Long successfulQueries = 0L;

    /**
     * 创建者ID
     */
        private Long creatorId;

    /**
     * 访问权限配置（JSON格式）
     */
        private String accessConfig;

    /**
     * 备份配置（JSON格式）
     */
        private String backupConfig;

    /**
     * 监控配置（JSON格式）
     */
        private String monitoringConfig;


    // Lombok生成的getter/setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getConnectionConfig() {
        return connectionConfig;
    }

    public void setConnectionConfig(String connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public String getIndexConfig() {
        return indexConfig;
    }

    public void setIndexConfig(String indexConfig) {
        this.indexConfig = indexConfig;
    }

    public String getEmbeddingConfig() {
        return embeddingConfig;
    }

    public void setEmbeddingConfig(String embeddingConfig) {
        this.embeddingConfig = embeddingConfig;
    }

    public Integer getDefaultDimension() {
        return defaultDimension;
    }

    public void setDefaultDimension(Integer defaultDimension) {
        this.defaultDimension = defaultDimension;
    }

    public String getDefaultMetric() {
        return defaultMetric;
    }

    public void setDefaultMetric(String defaultMetric) {
        this.defaultMetric = defaultMetric;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMaxVectors() {
        return maxVectors;
    }

    public void setMaxVectors(Long maxVectors) {
        this.maxVectors = maxVectors;
    }

    public Long getCurrentVectors() {
        return currentVectors;
    }

    public void setCurrentVectors(Long currentVectors) {
        this.currentVectors = currentVectors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Double getAvgQueryLatency() {
        return avgQueryLatency;
    }

    public void setAvgQueryLatency(Double avgQueryLatency) {
        this.avgQueryLatency = avgQueryLatency;
    }

    public Long getTotalQueries() {
        return totalQueries;
    }

    public void setTotalQueries(Long totalQueries) {
        this.totalQueries = totalQueries;
    }

    public Long getSuccessfulQueries() {
        return successfulQueries;
    }

    public void setSuccessfulQueries(Long successfulQueries) {
        this.successfulQueries = successfulQueries;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getAccessConfig() {
        return accessConfig;
    }

    public void setAccessConfig(String accessConfig) {
        this.accessConfig = accessConfig;
    }

    public String getBackupConfig() {
        return backupConfig;
    }

    public void setBackupConfig(String backupConfig) {
        this.backupConfig = backupConfig;
    }

    public String getMonitoringConfig() {
        return monitoringConfig;
    }

    public void setMonitoringConfig(String monitoringConfig) {
        this.monitoringConfig = monitoringConfig;
    }
}
