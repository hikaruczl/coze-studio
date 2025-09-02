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
 * 文档处理任务实体类
 * 管理文档的异步处理任务
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: document_processing_tasks
// 索引信息已移至数据库DDL脚本
public class DocumentProcessingTask extends BaseEntity {

    /**
     * 关联文档
     */
            private Document document;

    /**
     * 任务类型：PARSE-解析, SPLIT-切片, VECTORIZE-向量化, INDEX-索引, EXTRACT-提取
     */
        private String taskType;

    /**
     * 任务状态：PENDING-待处理, RUNNING-运行中, COMPLETED-已完成, FAILED-失败, CANCELLED-已取消
     */
        private String status = "PENDING";

    /**
     * 任务优先级：1-最高, 5-最低
     */
        private Integer priority = 3;

    /**
     * 任务参数（JSON格式）
     */
        private String taskParams;

    /**
     * 任务结果（JSON格式）
     */
        private String taskResult;

    /**
     * 错误信息
     */
        private String errorMessage;

    /**
     * 错误堆栈
     */
        private String errorStack;

    /**
     * 开始时间
     */
        private java.time.LocalDateTime startedAt;

    /**
     * 完成时间
     */
        private java.time.LocalDateTime completedAt;

    /**
     * 处理耗时（毫秒）
     */
        private Long processingTime;

    /**
     * 进度百分比（0-100）
     */
        private Integer progress = 0;

    /**
     * 进度描述
     */
        private String progressMessage;

    /**
     * 重试次数
     */
        private Integer retryCount = 0;

    /**
     * 最大重试次数
     */
        private Integer maxRetries = 3;

    /**
     * 下次重试时间
     */
        private java.time.LocalDateTime nextRetryAt;

    /**
     * 执行节点/服务器
     */
        private String executorNode;

    /**
     * 任务ID（用于外部系统追踪）
     */
        private String externalTaskId;

    /**
     * 父任务ID（用于任务依赖）
     */
        private Long parentTaskId;

    /**
     * 任务依赖（JSON格式）
     */
        private String dependencies;

    /**
     * 任务配置（JSON格式）
     */
        private String taskConfig;

    /**
     * 资源使用情况（JSON格式）
     */
        private String resourceUsage;

    /**
     * 任务标签（JSON格式）
     */
        private String tags;

    /**
     * 是否可取消
     */
        private Boolean cancellable = true;

    /**
     * 超时时间（秒）
     */
        private Integer timeoutSeconds;

    /**
     * 预计完成时间
     */
        private java.time.LocalDateTime estimatedCompletion;


    // Lombok生成的getter/setter方法
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(String taskParams) {
        this.taskParams = taskParams;
    }

    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorStack() {
        return errorStack;
    }

    public void setErrorStack(String errorStack) {
        this.errorStack = errorStack;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getProgressMessage() {
        return progressMessage;
    }

    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getExecutorNode() {
        return executorNode;
    }

    public void setExecutorNode(String executorNode) {
        this.executorNode = executorNode;
    }

    public String getExternalTaskId() {
        return externalTaskId;
    }

    public void setExternalTaskId(String externalTaskId) {
        this.externalTaskId = externalTaskId;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public String getTaskConfig() {
        return taskConfig;
    }

    public void setTaskConfig(String taskConfig) {
        this.taskConfig = taskConfig;
    }

    public String getResourceUsage() {
        return resourceUsage;
    }

    public void setResourceUsage(String resourceUsage) {
        this.resourceUsage = resourceUsage;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean isCancellable() {
        return cancellable;
    }

    public void setCancellable(Boolean cancellable) {
        this.cancellable = cancellable;
    }

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}
