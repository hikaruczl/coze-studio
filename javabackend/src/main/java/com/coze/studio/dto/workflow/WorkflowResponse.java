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

package com.coze.studio.dto.workflow;

import com.coze.studio.entity.Workflow;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 工作流响应 DTO
 * 
 * @author coze-dev
 */
@Data
public class WorkflowResponse {

    /**
     * 工作流ID
     */
    private Long id;

    /**
     * 工作流名称
     */
    private String name;

    /**
     * 工作流描述
     */
    private String description;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者用户名
     */
    private String creatorUsername;

    /**
     * 工作流状态
     */
    private String status;

    /**
     * 工作流类型
     */
    private String type;

    /**
     * 工作流版本
     */
    private String version;

    /**
     * 工作流图标URL
     */
    private String iconUrl;

    /**
     * 工作流标签
     */
    private List<String> tags;

    /**
     * 是否为模板
     */
    private Boolean isTemplate;

    /**
     * 模板分类
     */
    private String templateCategory;

    /**
     * 输入参数定义
     */
    private Map<String, Object> inputSchema;

    /**
     * 输出参数定义
     */
    private Map<String, Object> outputSchema;

    /**
     * 工作流配置
     */
    private Map<String, Object> config;

    /**
     * 画布配置
     */
    private Map<String, Object> canvasConfig;

    /**
     * 工作流节点列表
     */
    private List<WorkflowNodeResponse> nodes;

    /**
     * 工作流连接列表
     */
    private List<WorkflowConnectionResponse> connections;

    /**
     * 执行次数
     */
    private Long executionCount;

    /**
     * 成功执行次数
     */
    private Long successCount;

    /**
     * 失败执行次数
     */
    private Long failureCount;

    /**
     * 平均执行时间（毫秒）
     */
    private Double averageExecutionTime;

    /**
     * 最后执行时间
     */
    private LocalDateTime lastExecutedAt;

    /**
     * 最后发布时间
     */
    private LocalDateTime lastPublishedAt;

    /**
     * 发布者ID
     */
    private Long publisherId;

    /**
     * 发布者用户名
     */
    private String publisherUsername;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 从 Workflow 实体转换为 WorkflowResponse
     */
    public static WorkflowResponse fromWorkflow(Workflow workflow) {
        WorkflowResponse response = new WorkflowResponse();
        response.setId(workflow.getId());
        response.setName(workflow.getName());
        response.setDescription(workflow.getDescription());
        response.setCreatorId(workflow.getCreatorId());
        response.setStatus(workflow.getStatus());
        response.setType(workflow.getType());
        response.setVersion(workflow.getVersion());
        response.setIconUrl(workflow.getIconUrl());
        response.setIsTemplate(workflow.isIsTemplate());
        response.setTemplateCategory(workflow.getTemplateCategory());
        response.setLastExecutedAt(workflow.getLastExecutedAt());
        response.setLastPublishedAt(workflow.getLastPublishedAt());
        response.setPublisherId(workflow.getPublisherId());
        response.setCreatedAt(workflow.getCreatedAt());
        response.setUpdatedAt(workflow.getUpdatedAt());
        
        // TODO: 解析JSON字段
        // response.setTags(parseTagsFromJson(workflow.getTags()));
        // response.setInputSchema(parseJsonToMap(workflow.getInputSchema()));
        // response.setOutputSchema(parseJsonToMap(workflow.getOutputSchema()));
        // response.setConfig(parseJsonToMap(workflow.getConfig()));
        // response.setCanvasConfig(parseJsonToMap(workflow.getCanvasConfig()));
        
        return response;
    }

    /**
     * 从 Workflow 实体转换为 WorkflowResponse（包含额外信息）
     */
    public static WorkflowResponse fromWorkflow(Workflow workflow, 
                                              String creatorUsername, 
                                              String publisherUsername) {
        WorkflowResponse response = fromWorkflow(workflow);
        response.setCreatorUsername(creatorUsername);
        response.setPublisherUsername(publisherUsername);
        return response;
    }


    // Lombok生成的getter/setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getIsTemplate() {
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

    public Map<String, Object> getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(Map<String, Object> inputSchema) {
        this.inputSchema = inputSchema;
    }

    public Map<String, Object> getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Map<String, Object> outputSchema) {
        this.outputSchema = outputSchema;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, Object> getCanvasConfig() {
        return canvasConfig;
    }

    public void setCanvasConfig(Map<String, Object> canvasConfig) {
        this.canvasConfig = canvasConfig;
    }

    public List<WorkflowNodeResponse> getNodes() {
        return nodes;
    }

    public void setNodes(List<WorkflowNodeResponse> nodes) {
        this.nodes = nodes;
    }

    public List<WorkflowConnectionResponse> getConnections() {
        return connections;
    }

    public void setConnections(List<WorkflowConnectionResponse> connections) {
        this.connections = connections;
    }

    public Long getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Long executionCount) {
        this.executionCount = executionCount;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Long failureCount) {
        this.failureCount = failureCount;
    }

    public Double getAverageExecutionTime() {
        return averageExecutionTime;
    }

    public void setAverageExecutionTime(Double averageExecutionTime) {
        this.averageExecutionTime = averageExecutionTime;
    }

    public LocalDateTime getLastExecutedAt() {
        return lastExecutedAt;
    }

    public void setLastExecutedAt(LocalDateTime lastExecutedAt) {
        this.lastExecutedAt = lastExecutedAt;
    }

    public LocalDateTime getLastPublishedAt() {
        return lastPublishedAt;
    }

    public void setLastPublishedAt(LocalDateTime lastPublishedAt) {
        this.lastPublishedAt = lastPublishedAt;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherUsername() {
        return publisherUsername;
    }

    public void setPublisherUsername(String publisherUsername) {
        this.publisherUsername = publisherUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
