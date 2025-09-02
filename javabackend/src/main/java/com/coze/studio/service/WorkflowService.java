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

import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.workflow.*;
import com.coze.studio.entity.Workflow;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 工作流服务接口
 * 
 * @author coze-dev
 */
public interface WorkflowService {

    /**
     * 创建工作流
     */
    WorkflowResponse createWorkflow(Long userId, CreateWorkflowRequest request);

    /**
     * 更新工作流
     */
    WorkflowResponse updateWorkflow(Long userId, Long workflowId, UpdateWorkflowRequest request);

    /**
     * 根据ID获取工作流
     */
    WorkflowResponse getWorkflowById(Long workflowId);

    /**
     * 根据ID获取工作流实体
     */
    Workflow getWorkflowEntityById(Long workflowId);

    /**
     * 删除工作流
     */
    void deleteWorkflow(Long userId, Long workflowId);

    /**
     * 分页查询工作流列表
     */
    PageResponse<WorkflowResponse> getWorkflows(WorkflowQueryRequest request, Pageable pageable);

    /**
     * 获取用户的工作流列表
     */
    PageResponse<WorkflowResponse> getUserWorkflows(Long userId, Pageable pageable);

    /**
     * 发布工作流
     */
    WorkflowResponse publishWorkflow(Long userId, Long workflowId, String version);

    /**
     * 取消发布工作流
     */
    WorkflowResponse unpublishWorkflow(Long userId, Long workflowId);

    /**
     * 复制工作流
     */
    WorkflowResponse cloneWorkflow(Long userId, Long workflowId, String newName);

    /**
     * 从模板创建工作流
     */
    WorkflowResponse createFromTemplate(Long userId, Long templateId, String name);

    /**
     * 保存为模板
     */
    WorkflowResponse saveAsTemplate(Long userId, Long workflowId, String templateName, String category);

    /**
     * 获取工作流模板列表
     */
    PageResponse<WorkflowResponse> getWorkflowTemplates(String category, Pageable pageable);

    /**
     * 验证工作流
     */
    WorkflowValidationResult validateWorkflow(Long workflowId);

    /**
     * 执行工作流
     */
    WorkflowExecutionResponse executeWorkflow(Long userId, Long workflowId, ExecuteWorkflowRequest request);

    /**
     * 停止工作流执行
     */
    void stopWorkflowExecution(Long userId, String executionId);

    /**
     * 暂停工作流执行
     */
    void pauseWorkflowExecution(Long userId, String executionId);

    /**
     * 恢复工作流执行
     */
    void resumeWorkflowExecution(Long userId, String executionId);

    /**
     * 获取工作流执行记录
     */
    WorkflowExecutionResponse getWorkflowExecution(String executionId);

    /**
     * 获取工作流执行历史
     */
    PageResponse<WorkflowExecutionResponse> getWorkflowExecutions(Long workflowId, Pageable pageable);

    /**
     * 获取用户的工作流执行历史
     */
    PageResponse<WorkflowExecutionResponse> getUserWorkflowExecutions(Long userId, Pageable pageable);

    /**
     * 获取工作流统计信息
     */
    WorkflowStatsResponse getWorkflowStats(Long workflowId);

    /**
     * 检查用户是否有工作流的访问权限
     */
    boolean hasAccessToWorkflow(Long userId, Long workflowId);

    /**
     * 检查用户是否是工作流的创建者
     */
    boolean isWorkflowCreator(Long userId, Long workflowId);

    /**
     * 统计用户的工作流数量
     */
    long countUserWorkflows(Long userId);

    /**
     * 获取热门工作流列表
     */
    PageResponse<WorkflowResponse> getPopularWorkflows(Pageable pageable);

    /**
     * 搜索工作流
     */
    PageResponse<WorkflowResponse> searchWorkflows(String keyword, Pageable pageable);

    /**
     * 导出工作流
     */
    Map<String, Object> exportWorkflow(Long workflowId);

    /**
     * 导入工作流
     */
    WorkflowResponse importWorkflow(Long userId, Map<String, Object> workflowData);

    /**
     * 获取工作流版本历史
     */
    List<WorkflowVersionResponse> getWorkflowVersions(Long workflowId);

    /**
     * 回滚到指定版本
     */
    WorkflowResponse rollbackToVersion(Long userId, Long workflowId, String version);
}
