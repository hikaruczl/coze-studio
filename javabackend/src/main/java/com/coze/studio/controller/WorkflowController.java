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

package com.coze.studio.controller;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.workflow.*;
import com.coze.studio.security.UserPrincipal;
import com.coze.studio.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工作流管理控制器
 * 处理工作流相关的 CRUD 操作
 * 
 * @author coze-dev
 */
@Tag(name = "工作流管理", description = "工作流管理相关接口")
@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    /**
     * 创建工作流
     */
    @Operation(summary = "创建工作流", description = "创建一个新的工作流")
    @PostMapping
    public ResponseEntity<ApiResponse<WorkflowResponse>> createWorkflow(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateWorkflowRequest request) {
        WorkflowResponse workflow = workflowService.createWorkflow(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("工作流创建成功", workflow));
    }

    /**
     * 更新工作流
     */
    @Operation(summary = "更新工作流", description = "更新工作流的基本信息和配置")
    @PutMapping("/{workflowId}")
    public ResponseEntity<ApiResponse<WorkflowResponse>> updateWorkflow(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long workflowId,
            @Valid @RequestBody UpdateWorkflowRequest request) {
        WorkflowResponse workflow = workflowService.updateWorkflow(userPrincipal.getId(), workflowId, request);
        return ResponseEntity.ok(ApiResponse.success("工作流更新成功", workflow));
    }

    /**
     * 获取工作流详情
     */
    @Operation(summary = "获取工作流详情", description = "根据ID获取工作流的详细信息")
    @GetMapping("/{workflowId}")
    public ResponseEntity<ApiResponse<WorkflowResponse>> getWorkflowById(@PathVariable Long workflowId) {
        WorkflowResponse workflow = workflowService.getWorkflowById(workflowId);
        return ResponseEntity.ok(ApiResponse.success(workflow));
    }

    /**
     * 删除工作流
     */
    @Operation(summary = "删除工作流", description = "删除指定的工作流（软删除）")
    @DeleteMapping("/{workflowId}")
    public ResponseEntity<ApiResponse<String>> deleteWorkflow(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long workflowId) {
        workflowService.deleteWorkflow(userPrincipal.getId(), workflowId);
        return ResponseEntity.ok(ApiResponse.success("工作流删除成功"));
    }

    /**
     * 分页查询工作流列表
     */
    @Operation(summary = "查询工作流列表", description = "分页查询工作流列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<WorkflowResponse>>> getWorkflows(
            WorkflowQueryRequest queryRequest,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<WorkflowResponse> workflows = workflowService.getWorkflows(queryRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(workflows));
    }

    /**
     * 获取当前用户的工作流列表
     */
    @Operation(summary = "获取我的工作流列表", description = "获取当前用户创建的工作流列表")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowResponse>>> getMyWorkflows(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<WorkflowResponse> workflows = workflowService.getUserWorkflows(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(workflows));
    }

    /**
     * 发布工作流
     */
    @Operation(summary = "发布工作流", description = "发布工作流到公开市场")
    @PostMapping("/{workflowId}/publish")
    public ResponseEntity<ApiResponse<WorkflowResponse>> publishWorkflow(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long workflowId,
            @RequestParam String version) {
        WorkflowResponse workflow = workflowService.publishWorkflow(userPrincipal.getId(), workflowId, version);
        return ResponseEntity.ok(ApiResponse.success("工作流发布成功", workflow));
    }

    /**
     * 取消发布工作流
     */
    @Operation(summary = "取消发布工作流", description = "将工作流从公开市场下线")
    @PostMapping("/{workflowId}/unpublish")
    public ResponseEntity<ApiResponse<WorkflowResponse>> unpublishWorkflow(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long workflowId) {
        WorkflowResponse workflow = workflowService.unpublishWorkflow(userPrincipal.getId(), workflowId);
        return ResponseEntity.ok(ApiResponse.success("工作流取消发布成功", workflow));
    }

    /**
     * 复制工作流
     */
    @Operation(summary = "复制工作流", description = "复制一个现有的工作流")
    @PostMapping("/{workflowId}/clone")
    public ResponseEntity<ApiResponse<WorkflowResponse>> cloneWorkflow(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long workflowId,
            @RequestParam String newName) {
        WorkflowResponse workflow = workflowService.cloneWorkflow(userPrincipal.getId(), workflowId, newName);
        return ResponseEntity.ok(ApiResponse.success("工作流复制成功", workflow));
    }

    /**
     * 验证工作流
     */
    @Operation(summary = "验证工作流", description = "验证工作流的配置是否正确")
    @PostMapping("/{workflowId}/validate")
    public ResponseEntity<ApiResponse<WorkflowValidationResult>> validateWorkflow(@PathVariable Long workflowId) {
        WorkflowValidationResult result = workflowService.validateWorkflow(workflowId);
        return ResponseEntity.ok(ApiResponse.success("工作流验证完成", result));
    }

    /**
     * 执行工作流
     */
    @Operation(summary = "执行工作流", description = "执行指定的工作流")
    @PostMapping("/{workflowId}/execute")
    public ResponseEntity<ApiResponse<WorkflowExecutionResponse>> executeWorkflow(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long workflowId,
            @Valid @RequestBody ExecuteWorkflowRequest request) {
        WorkflowExecutionResponse execution = workflowService.executeWorkflow(userPrincipal.getId(), workflowId, request);
        return ResponseEntity.ok(ApiResponse.success("工作流执行成功", execution));
    }

    /**
     * 停止工作流执行
     */
    @Operation(summary = "停止工作流执行", description = "停止正在执行的工作流")
    @PostMapping("/executions/{executionId}/stop")
    public ResponseEntity<ApiResponse<String>> stopWorkflowExecution(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String executionId) {
        workflowService.stopWorkflowExecution(userPrincipal.getId(), executionId);
        return ResponseEntity.ok(ApiResponse.success("工作流执行已停止"));
    }

    /**
     * 暂停工作流执行
     */
    @Operation(summary = "暂停工作流执行", description = "暂停正在执行的工作流")
    @PostMapping("/executions/{executionId}/pause")
    public ResponseEntity<ApiResponse<String>> pauseWorkflowExecution(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String executionId) {
        workflowService.pauseWorkflowExecution(userPrincipal.getId(), executionId);
        return ResponseEntity.ok(ApiResponse.success("工作流执行已暂停"));
    }

    /**
     * 恢复工作流执行
     */
    @Operation(summary = "恢复工作流执行", description = "恢复暂停的工作流执行")
    @PostMapping("/executions/{executionId}/resume")
    public ResponseEntity<ApiResponse<String>> resumeWorkflowExecution(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String executionId) {
        workflowService.resumeWorkflowExecution(userPrincipal.getId(), executionId);
        return ResponseEntity.ok(ApiResponse.success("工作流执行已恢复"));
    }

    /**
     * 获取工作流执行记录
     */
    @Operation(summary = "获取工作流执行记录", description = "获取指定的工作流执行记录")
    @GetMapping("/executions/{executionId}")
    public ResponseEntity<ApiResponse<WorkflowExecutionResponse>> getWorkflowExecution(@PathVariable String executionId) {
        WorkflowExecutionResponse execution = workflowService.getWorkflowExecution(executionId);
        return ResponseEntity.ok(ApiResponse.success(execution));
    }

    /**
     * 获取工作流执行历史
     */
    @Operation(summary = "获取工作流执行历史", description = "获取指定工作流的执行历史")
    @GetMapping("/{workflowId}/executions")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowExecutionResponse>>> getWorkflowExecutions(
            @PathVariable Long workflowId,
            @PageableDefault(size = 20, sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<WorkflowExecutionResponse> executions = workflowService.getWorkflowExecutions(workflowId, pageable);
        return ResponseEntity.ok(ApiResponse.success(executions));
    }

    /**
     * 获取用户的工作流执行历史
     */
    @Operation(summary = "获取我的工作流执行历史", description = "获取当前用户的工作流执行历史")
    @GetMapping("/executions/my")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowExecutionResponse>>> getMyWorkflowExecutions(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20, sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<WorkflowExecutionResponse> executions = workflowService.getUserWorkflowExecutions(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(executions));
    }

    /**
     * 获取工作流统计信息
     */
    @Operation(summary = "获取工作流统计", description = "获取指定工作流的统计信息")
    @GetMapping("/{workflowId}/stats")
    public ResponseEntity<ApiResponse<WorkflowStatsResponse>> getWorkflowStats(@PathVariable Long workflowId) {
        WorkflowStatsResponse stats = workflowService.getWorkflowStats(workflowId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    /**
     * 统计工作流数量
     */
    @Operation(summary = "统计工作流数量", description = "统计当前用户的工作流数量")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countMyWorkflows(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long count = workflowService.countUserWorkflows(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * 获取工作流模板列表
     */
    @Operation(summary = "获取工作流模板", description = "获取工作流模板列表（迁移：避免与 WorkflowTemplateController 冲突）")
    @GetMapping("/templates/list") // 与 /api/workflows/templates 区分，避免冲突
    public ResponseEntity<ApiResponse<PageResponse<WorkflowResponse>>> getWorkflowTemplates(
            @RequestParam(required = false) String category,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<WorkflowResponse> templates = workflowService.getWorkflowTemplates(category, pageable);
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    /**
     * 从模板创建工作流
     */
    @Operation(summary = "从模板创建工作流", description = "基于模板创建新的工作流")
    @PostMapping("/templates/{templateId}/create")
    public ResponseEntity<ApiResponse<WorkflowResponse>> createFromTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long templateId,
            @RequestParam String name) {
        WorkflowResponse workflow = workflowService.createFromTemplate(userPrincipal.getId(), templateId, name);
        return ResponseEntity.ok(ApiResponse.success("从模板创建工作流成功", workflow));
    }

    /**
     * 保存为模板
     */
    @Operation(summary = "保存为模板", description = "将工作流保存为模板")
    @PostMapping("/{workflowId}/save-as-template")
    public ResponseEntity<ApiResponse<WorkflowResponse>> saveAsTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long workflowId,
            @RequestParam String templateName,
            @RequestParam String category) {
        WorkflowResponse template = workflowService.saveAsTemplate(userPrincipal.getId(), workflowId, templateName, category);
        return ResponseEntity.ok(ApiResponse.success("保存为模板成功", template));
    }

    /**
     * 搜索工作流
     */
    @Operation(summary = "搜索工作流", description = "根据关键词搜索工作流")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowResponse>>> searchWorkflows(
            @RequestParam String keyword,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<WorkflowResponse> workflows = workflowService.searchWorkflows(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(workflows));
    }

    /**
     * 导出工作流
     */
    @Operation(summary = "导出工作流", description = "导出工作流的配置和数据")
    @GetMapping("/{workflowId}/export")
    public ResponseEntity<ApiResponse<Map<String, Object>>> exportWorkflow(@PathVariable Long workflowId) {
        Map<String, Object> exportData = workflowService.exportWorkflow(workflowId);
        return ResponseEntity.ok(ApiResponse.success(exportData));
    }

    /**
     * 导入工作流
     */
    @Operation(summary = "导入工作流", description = "导入工作流配置")
    @PostMapping("/import")
    public ResponseEntity<ApiResponse<WorkflowResponse>> importWorkflow(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Map<String, Object> workflowData) {
        WorkflowResponse workflow = workflowService.importWorkflow(userPrincipal.getId(), workflowData);
        return ResponseEntity.ok(ApiResponse.success("工作流导入成功", workflow));
    }

    /**
     * 获取工作流版本历史
     */
    @Operation(summary = "获取工作流版本历史", description = "获取工作流的版本历史记录")
    @GetMapping("/{workflowId}/versions")
    public ResponseEntity<ApiResponse<List<WorkflowVersionResponse>>> getWorkflowVersions(@PathVariable Long workflowId) {
        List<WorkflowVersionResponse> versions = workflowService.getWorkflowVersions(workflowId);
        return ResponseEntity.ok(ApiResponse.success(versions));
    }

    /**
     * 回滚到指定版本
     */
    @Operation(summary = "回滚工作流版本", description = "将工作流回滚到指定版本")
    @PostMapping("/{workflowId}/rollback")
    public ResponseEntity<ApiResponse<WorkflowResponse>> rollbackToVersion(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long workflowId,
            @RequestParam String version) {
        WorkflowResponse workflow = workflowService.rollbackToVersion(userPrincipal.getId(), workflowId, version);
        return ResponseEntity.ok(ApiResponse.success("工作流版本回滚成功", workflow));
    }
}
