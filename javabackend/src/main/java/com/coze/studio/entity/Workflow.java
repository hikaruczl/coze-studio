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
 * 工作流实体类
 * 表示一个可执行的工作流
 *
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: workflows
public class Workflow extends BaseEntity {

    /**
     * 工作流名称
     */
    // 字段: name
    private String name;

    /**
     * 工作流描述
     */
    // 字段: description
    private String description;

    /**
     * 创建者ID
     */
    // 字段: creator_id
    private Long creatorId;

    /**
     * 工作流状态
     */
    private String status;

    /**
     * 工作流类型
     */
    private String type;

    /**
     * 图标URL
     */
    private String iconUrl;

    /**
     * 是否为模板
     */
    private Boolean isTemplate = false;

    /**
     * 模板分类
     */
    private String templateCategory;

    /**
     * 标签
     */
    private String tags;

    /**
     * 输入模式
     */
    private String inputSchema;

    /**
     * 输出模式
     */
    private String outputSchema;

    /**
     * 配置信息
     */
    private String config;

    /**
     * 画布配置
     */
    private String canvasConfig;

    /**
     * 最后发布时间
     */
    private java.time.LocalDateTime lastPublishedAt;


	public java.time.LocalDateTime getLastPublishedAt() {
		return lastPublishedAt;
	}

	public void setLastPublishedAt(java.time.LocalDateTime lastPublishedAt) {
		this.lastPublishedAt = lastPublishedAt;
	}

    /**
     * 发布者ID
     */
    private Long publisherId;

    /**
     * 空间ID
     */
    // 字段: space_id
    private Long spaceId;

    /**
     * 关联的应用ID
     */
    // 字段: app_id
    private Long appId;

    /**
     * 最新发布版本
     */
    // 字段: latest_published_version
    private String latestPublishedVersion;

    /**
     * 工作流画布定义
     * 存储工作流的 JSON 定义，包含节点和连接信息
     */
    // 字段: canvas (TEXT类型)
    private String canvas;

    /**
     * 工作流版本
     */
    // 字段: version
    private String version;

    /**
     * 是否已发布
     */
    // 字段: published
    private Boolean published = false;

    /**
     * 是否启用
     */
    // 字段: enabled
    private Boolean enabled = true;

    /**
     * 执行次数
     */
    // 字段: execution_count
    private Long executionCount = 0L;

    /**
     * 最后执行时间
     */
    // 字段: last_executed_at
    private java.time.LocalDateTime lastExecutedAt;

	public java.time.LocalDateTime getLastExecutedAt() {
		return lastExecutedAt;
	}

	public void setLastExecutedAt(java.time.LocalDateTime lastExecutedAt) {
		this.lastExecutedAt = lastExecutedAt;
	}


    /**
     * 获取分类（兼容方法）
     */
    public String getCategory() {
        return templateCategory;
    }

    /**
     * 设置分类（兼容方法）
     */
    public void setCategory(String category) {
        this.templateCategory = category;
    }


    // Lombok生成的getter/setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean isIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public String getTemplateCategory() {
        return templateCategory;
    }

    public void setTemplateCategory(String templateCategory) {
        this.templateCategory = templateCategory;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(String inputSchema) {
        this.inputSchema = inputSchema;
    }

    public String getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(String outputSchema) {
        this.outputSchema = outputSchema;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getCanvasConfig() {
        return canvasConfig;
    }

    public void setCanvasConfig(String canvasConfig) {
        this.canvasConfig = canvasConfig;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getLatestPublishedVersion() {
        return latestPublishedVersion;
    }

    public void setLatestPublishedVersion(String latestPublishedVersion) {
        this.latestPublishedVersion = latestPublishedVersion;
    }

    public String getCanvas() {
        return canvas;
    }

    public void setCanvas(String canvas) {
        this.canvas = canvas;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Long executionCount) {
        this.executionCount = executionCount;
    }
}
