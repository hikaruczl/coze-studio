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

import com.coze.studio.entity.WorkflowExecution;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 工作流执行响应 DTO
 * 
 * @author coze-dev
 */
@Data
public class WorkflowExecutionResponse {

    /**
     * 执行记录ID
     */
    private Long id;

    /**
     * 工作流ID
     */
    private Long workflowId;

    /**
     * 工作流名称
     */
    private String workflowName;

    /**
     * 执行唯一标识符
     */
    private String executionId;

    /**
     * 执行者ID
     */
    private Long executorId;

    /**
     * 执行者用户名
     */
    private String executorUsername;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 触发类型
     */
    private String triggerType;

    /**
     * 触发来源
     */
    private String triggerSource;

    /**
     * 输入参数
     */
    private Map<String, Object> inputData;

    /**
     * 输出结果
     */
    private Map<String, Object> outputData;

    /**
     * 执行上下文
     */
    private Map<String, Object> context;

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
    private Integer executedNodes;

    /**
     * 总节点数
     */
    private Integer totalNodes;

    /**
     * 当前执行的节点ID
     */
    private String currentNodeId;

    /**
     * 执行模式
     */
    private String executionMode;

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
     * 执行配置
     */
    private Map<String, Object> executionConfig;

    /**
     * 执行日志
     */
    private String executionLog;

    /**
     * 节点执行记录列表
     */
    private List<WorkflowNodeExecutionResponse> nodeExecutions;

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
     * 从 WorkflowExecution 实体转换为 WorkflowExecutionResponse
     */
    public static WorkflowExecutionResponse fromWorkflowExecution(WorkflowExecution execution) {
        WorkflowExecutionResponse response = new WorkflowExecutionResponse();
        response.setId(execution.getId());
        response.setWorkflowId(execution.getWorkflowId());
        response.setExecutionId(execution.getExecutionId());
        response.setExecutorId(execution.getExecutorId());
        response.setStatus(execution.getStatus());
        response.setTriggerType(execution.getTriggerType());
        response.setTriggerSource(execution.getTriggerSource());
        response.setErrorMessage(execution.getErrorMessage());
        response.setErrorStack(execution.getErrorStack());
        response.setStartTime(execution.getStartTime());
        response.setEndTime(execution.getEndTime());
        response.setDuration(execution.getDuration());
        response.setExecutedNodes(execution.getExecutedNodes());
        response.setTotalNodes(execution.getTotalNodes());
        response.setCurrentNodeId(execution.getCurrentNodeId());
        response.setExecutionMode(execution.getExecutionMode());
        response.setWorkflowVersion(execution.getWorkflowVersion());
        response.setConversationId(execution.getConversationId());
        response.setMessageId(execution.getMessageId());
        response.setAppId(execution.getAppId());
        response.setExecutionLog(execution.getExecutionLog());
        response.setCreatedAt(execution.getCreatedAt());
        response.setUpdatedAt(execution.getUpdatedAt());
        
        // TODO: 解析JSON字段
        // response.setInputData(parseJsonToMap(execution.getInputData()));
        // response.setOutputData(parseJsonToMap(execution.getOutputData()));
        // response.setContext(parseJsonToMap(execution.getContext()));
        // response.setExecutionConfig(parseJsonToMap(execution.getExecutionConfig()));
        // response.setProperties(parseJsonToMap(execution.getProperties()));
        
        return response;
    }

    /**
     * 从 WorkflowExecution 实体转换为 WorkflowExecutionResponse（包含额外信息）
     */
    public static WorkflowExecutionResponse fromWorkflowExecution(WorkflowExecution execution, 
                                                                 String workflowName, 
                                                                 String executorUsername) {
        WorkflowExecutionResponse response = fromWorkflowExecution(execution);
        response.setWorkflowName(workflowName);
        response.setExecutorUsername(executorUsername);
        return response;
    }
}
