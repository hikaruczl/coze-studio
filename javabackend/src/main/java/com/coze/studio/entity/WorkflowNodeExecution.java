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
 * 工作流节点执行实体类
 * 表示工作流中单个节点的执行记录
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: workflow_node_executions
// 索引信息已移至数据库DDL脚本
public class WorkflowNodeExecution extends BaseEntity {

    /**
     * 工作流执行ID
     */
        private Long executionId;

    /**
     * 节点ID
     */
        private String nodeId;

    /**
     * 节点名称
     */
        private String nodeName;

    /**
     * 节点类型
     */
        private String nodeType;

    /**
     * 执行状态
     */
        private String status = "PENDING";

    /**
     * 输入数据（JSON格式）
     */
            private String inputData;

    /**
     * 输出数据（JSON格式）
     */
            private String outputData;

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
     * 重试次数
     */
        private Integer retryCount = 0;

    /**
     * 最大重试次数
     */
        private Integer maxRetries = 0;

    /**
     * 执行序号
     */
        private Integer executionOrder;

    /**
     * 节点配置快照（JSON格式）
     */
            private String nodeConfig;

    /**
     * 执行日志
     */
            private String executionLog;

    /**
     * 性能指标（JSON格式）
     */
            private String metrics;

    /**
     * 扩展属性（JSON格式）
     */
            private String properties;


    // Lombok生成的getter/setter方法
    public Long getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Long executionId) {
        this.executionId = executionId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Integer executionOrder) {
        this.executionOrder = executionOrder;
    }

    public String getNodeConfig() {
        return nodeConfig;
    }

    public void setNodeConfig(String nodeConfig) {
        this.nodeConfig = nodeConfig;
    }

    public String getExecutionLog() {
        return executionLog;
    }

    public void setExecutionLog(String executionLog) {
        this.executionLog = executionLog;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
