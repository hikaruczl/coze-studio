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
 * 工作流执行实体类
 * 表示工作流的一次执行记录
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: workflow_executions
// 索引信息已移至数据库DDL脚本
public class WorkflowExecution extends BaseEntity {

    /**
     * 工作流ID
     */
        private Long workflowId;

    /**
     * 执行唯一标识符
     */
        private String executionId;

    /**
     * 执行者ID
     */
        private Long executorId;

    /**
     * 执行状态
     */
        private String status = "PENDING";

    /**
     * 触发类型
     */
        private String triggerType = "MANUAL";

    /**
     * 触发来源
     */
        private String triggerSource;

    /**
     * 输入参数（JSON格式）
     */
            private String inputData;

    /**
     * 输出结果（JSON格式）
     */
            private String outputData;

    /**
     * 执行上下文（JSON格式）
     */
            private String context;

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
        private LocalDateTime startTime;

    /**
     * 结束时间
     */
        private LocalDateTime endTime;

    /**
     * 执行持续时间（毫秒）
     */
        private Long duration;

    /**
     * 已执行的节点数
     */
        private Integer executedNodes = 0;

    /**
     * 总节点数
     */
        private Integer totalNodes = 0;

    /**
     * 当前执行的节点ID
     */
        private String currentNodeId;

    /**
     * 执行模式
     */
        private String executionMode = "SYNC";

    /**
     * 工作流版本
     */
        private String workflowVersion;

    /**
     * 关联的对话ID
     */
        private Long conversationId;

    /**
     * 关联的消息ID
     */
        private Long messageId;

    /**
     * 关联的应用ID
     */
        private Long appId;

    /**
     * 执行配置（JSON格式）
     */
            private String executionConfig;

    /**
     * 执行日志
     */
            private String executionLog;

    /**
     * 扩展属性（JSON格式）
     */
            private String properties;


    // Lombok生成的getter/setter方法
    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerSource() {
        return triggerSource;
    }

    public void setTriggerSource(String triggerSource) {
        this.triggerSource = triggerSource;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getOutputData() {
        return outputData;
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getExecutedNodes() {
        return executedNodes;
    }

    public void setExecutedNodes(Integer executedNodes) {
        this.executedNodes = executedNodes;
    }

    public Integer getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(Integer totalNodes) {
        this.totalNodes = totalNodes;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public String getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(String executionMode) {
        this.executionMode = executionMode;
    }

    public String getWorkflowVersion() {
        return workflowVersion;
    }

    public void setWorkflowVersion(String workflowVersion) {
        this.workflowVersion = workflowVersion;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getExecutionConfig() {
        return executionConfig;
    }

    public void setExecutionConfig(String executionConfig) {
        this.executionConfig = executionConfig;
    }

    public String getExecutionLog() {
        return executionLog;
    }

    public void setExecutionLog(String executionLog) {
        this.executionLog = executionLog;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
