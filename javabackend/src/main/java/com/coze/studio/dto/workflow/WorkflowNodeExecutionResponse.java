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

import com.coze.studio.entity.WorkflowNodeExecution;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工作流节点执行响应 DTO
 * 
 * @author coze-dev
 */
@Data
public class WorkflowNodeExecutionResponse {

    /**
     * 节点执行记录ID
     */
    private Long id;

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
    private String status;

    /**
     * 输入数据
     */
    private Map<String, Object> inputData;

    /**
     * 输出数据
     */
    private Map<String, Object> outputData;

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
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    private Integer maxRetries;

    /**
     * 执行序号
     */
    private Integer executionOrder;

    /**
     * 节点配置快照
     */
    private Map<String, Object> nodeConfig;

    /**
     * 执行日志
     */
    private String executionLog;

    /**
     * 性能指标
     */
    private Map<String, Object> metrics;

    /**
     * 扩展属性
     */
    private Map<String, Object> properties;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 从 WorkflowNodeExecution 实体转换为 WorkflowNodeExecutionResponse
     */
    public static WorkflowNodeExecutionResponse fromWorkflowNodeExecution(WorkflowNodeExecution nodeExecution) {
        WorkflowNodeExecutionResponse response = new WorkflowNodeExecutionResponse();
        response.setId(nodeExecution.getId());
        response.setExecutionId(nodeExecution.getExecutionId());
        response.setNodeId(nodeExecution.getNodeId());
        response.setNodeName(nodeExecution.getNodeName());
        response.setNodeType(nodeExecution.getNodeType());
        response.setStatus(nodeExecution.getStatus());
        response.setErrorMessage(nodeExecution.getErrorMessage());
        response.setErrorStack(nodeExecution.getErrorStack());
        response.setStartTime(nodeExecution.getStartTime());
        response.setEndTime(nodeExecution.getEndTime());
        response.setDuration(nodeExecution.getDuration());
        response.setRetryCount(nodeExecution.getRetryCount());
        response.setMaxRetries(nodeExecution.getMaxRetries());
        response.setExecutionOrder(nodeExecution.getExecutionOrder());
        response.setExecutionLog(nodeExecution.getExecutionLog());
        response.setCreatedAt(nodeExecution.getCreatedAt());
        response.setUpdatedAt(nodeExecution.getUpdatedAt());
        
        // TODO: 解析JSON字段
        // response.setInputData(parseJsonToMap(nodeExecution.getInputData()));
        // response.setOutputData(parseJsonToMap(nodeExecution.getOutputData()));
        // response.setNodeConfig(parseJsonToMap(nodeExecution.getNodeConfig()));
        // response.setMetrics(parseJsonToMap(nodeExecution.getMetrics()));
        // response.setProperties(parseJsonToMap(nodeExecution.getProperties()));
        
        return response;
    }
}
