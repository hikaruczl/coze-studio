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

package com.coze.studio.service;

import com.coze.studio.dto.workflow.ExecuteWorkflowRequest;
import com.coze.studio.dto.workflow.WorkflowExecutionResponse;
import com.coze.studio.entity.Workflow;
import com.coze.studio.entity.WorkflowExecution;

import java.util.Map;

/**
 * 工作流执行引擎服务接口
 * 
 * @author coze-dev
 */
public interface WorkflowExecutionService {

    /**
     * 执行工作流
     */
    WorkflowExecutionResponse executeWorkflow(Workflow workflow, Long executorId, ExecuteWorkflowRequest request);

    /**
     * 异步执行工作流
     */
    String executeWorkflowAsync(Workflow workflow, Long executorId, ExecuteWorkflowRequest request);

    /**
     * 停止工作流执行
     */
    void stopExecution(String executionId);

    /**
     * 暂停工作流执行
     */
    void pauseExecution(String executionId);

    /**
     * 恢复工作流执行
     */
    void resumeExecution(String executionId);

    /**
     * 重试工作流执行
     */
    WorkflowExecutionResponse retryExecution(String executionId);

    /**
     * 获取执行状态
     */
    String getExecutionStatus(String executionId);

    /**
     * 获取执行结果
     */
    Map<String, Object> getExecutionResult(String executionId);

    /**
     * 获取执行日志
     */
    String getExecutionLog(String executionId);

    /**
     * 清理过期的执行记录
     */
    void cleanupExpiredExecutions(int daysOld);

    /**
     * 工作流执行回调接口
     */
    interface WorkflowExecutionCallback {
        void onStart(WorkflowExecution execution);
        void onNodeStart(String nodeId, Map<String, Object> inputData);
        void onNodeComplete(String nodeId, Map<String, Object> outputData);
        void onNodeError(String nodeId, String errorMessage, Exception exception);
        void onComplete(WorkflowExecution execution, Map<String, Object> outputData);
        void onError(WorkflowExecution execution, String errorMessage, Exception exception);
        void onPause(WorkflowExecution execution);
        void onResume(WorkflowExecution execution);
        void onStop(WorkflowExecution execution);
    }
}
