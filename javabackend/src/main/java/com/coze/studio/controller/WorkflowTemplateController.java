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
import com.coze.studio.dto.workflow.WorkflowResponse;
import com.coze.studio.security.UserPrincipal;
import com.coze.studio.service.WorkflowTemplateService;
import com.coze.studio.service.WorkflowTemplateService.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工作流模板控制器
 * 提供工作流模板管理相关接口
 * 
 * @author coze-dev
 */
@Tag(name = "工作流模板", description = "工作流模板管理相关接口")
@RestController
@RequestMapping("/api/workflows/templates")
@RequiredArgsConstructor
public class WorkflowTemplateController {

    private final WorkflowTemplateService workflowTemplateService;

    /**
     * 创建工作流模板
     */
    @Operation(summary = "创建模板", description = "从现有工作流创建模板")
    @PostMapping
    public ResponseEntity<ApiResponse<WorkflowTemplateResponse>> createTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateTemplateRequest request) {
        WorkflowTemplateResponse response = workflowTemplateService.createTemplate(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 更新工作流模板
     */
    @Operation(summary = "更新模板", description = "更新模板基本信息")
    @PutMapping("/{templateId}")
    public ResponseEntity<ApiResponse<WorkflowTemplateResponse>> updateTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long templateId,
            @Valid @RequestBody UpdateTemplateRequest request) {
        WorkflowTemplateResponse response = workflowTemplateService.updateTemplate(userPrincipal.getId(), templateId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 删除工作流模板
     */
    @Operation(summary = "删除模板", description = "删除指定的工作流模板")
    @DeleteMapping("/{templateId}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long templateId) {
        workflowTemplateService.deleteTemplate(userPrincipal.getId(), templateId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 获取模板详情
     */
    @Operation(summary = "获取模板详情", description = "获取指定模板的详细信息")
    @GetMapping("/{templateId}")
    public ResponseEntity<ApiResponse<WorkflowTemplateResponse>> getTemplate(
            @PathVariable Long templateId) {
        WorkflowTemplateResponse response = workflowTemplateService.getTemplateById(templateId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 查询模板列表
     */
    @Operation(summary = "查询模板列表", description = "分页查询公开的模板列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<WorkflowTemplateResponse>>> getTemplates(
            @Parameter(description = "分类") @RequestParam(required = false) String category,
            @Parameter(description = "标签") @RequestParam(required = false) List<String> tags,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "创建者ID") @RequestParam(required = false) Long creatorId,
            @Parameter(description = "排序字段") @RequestParam(required = false) String sortBy,
            @Parameter(description = "排序方向") @RequestParam(required = false) String sortDirection,
            @PageableDefault(size = 20) Pageable pageable) {
        
        TemplateQueryRequest queryRequest = new TemplateQueryRequest();
        queryRequest.setCategory(category);
        queryRequest.setTags(tags);
        queryRequest.setStatus(status);
        queryRequest.setCreatorId(creatorId);
        queryRequest.setSortBy(sortBy);
        queryRequest.setSortDirection(sortDirection);
        
        PageResponse<WorkflowTemplateResponse> response = workflowTemplateService.getTemplates(queryRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 获取用户的模板列表
     */
    @Operation(summary = "获取用户模板", description = "获取当前用户创建的模板列表")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowTemplateResponse>>> getUserTemplates(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<WorkflowTemplateResponse> response = workflowTemplateService.getUserTemplates(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 获取热门模板
     */
    @Operation(summary = "获取热门模板", description = "获取使用次数最多的热门模板")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowTemplateResponse>>> getPopularTemplates(
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<WorkflowTemplateResponse> response = workflowTemplateService.getPopularTemplates(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 搜索模板
     */
    @Operation(summary = "搜索模板", description = "根据关键词搜索模板")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowTemplateResponse>>> searchTemplates(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<WorkflowTemplateResponse> response = workflowTemplateService.searchTemplates(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 从模板创建工作流
     */
    @Operation(summary = "从模板创建工作流", description = "使用模板创建新的工作流")
    @PostMapping("/{templateId}/create-workflow")
    public ResponseEntity<ApiResponse<WorkflowResponse>> createWorkflowFromTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long templateId,
            @Parameter(description = "工作流名称") @RequestParam String workflowName) {
        WorkflowResponse response = workflowTemplateService.createWorkflowFromTemplate(
                userPrincipal.getId(), templateId, workflowName);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 发布模板
     */
    @Operation(summary = "发布模板", description = "将模板发布到公开市场")
    @PostMapping("/{templateId}/publish")
    public ResponseEntity<ApiResponse<WorkflowTemplateResponse>> publishTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long templateId) {
        WorkflowTemplateResponse response = workflowTemplateService.publishTemplate(userPrincipal.getId(), templateId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 取消发布模板
     */
    @Operation(summary = "取消发布模板", description = "将模板从公开市场撤回")
    @PostMapping("/{templateId}/unpublish")
    public ResponseEntity<ApiResponse<WorkflowTemplateResponse>> unpublishTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long templateId) {
        WorkflowTemplateResponse response = workflowTemplateService.unpublishTemplate(userPrincipal.getId(), templateId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 收藏模板
     */
    @Operation(summary = "收藏模板", description = "将模板添加到收藏夹")
    @PostMapping("/{templateId}/favorite")
    public ResponseEntity<ApiResponse<Void>> favoriteTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long templateId) {
        workflowTemplateService.favoriteTemplate(userPrincipal.getId(), templateId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 取消收藏模板
     */
    @Operation(summary = "取消收藏模板", description = "从收藏夹移除模板")
    @DeleteMapping("/{templateId}/favorite")
    public ResponseEntity<ApiResponse<Void>> unfavoriteTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long templateId) {
        workflowTemplateService.unfavoriteTemplate(userPrincipal.getId(), templateId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 获取收藏的模板
     */
    @Operation(summary = "获取收藏模板", description = "获取用户收藏的模板列表")
    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowTemplateResponse>>> getFavoriteTemplates(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<WorkflowTemplateResponse> response = workflowTemplateService.getFavoriteTemplates(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 评价模板
     */
    @Operation(summary = "评价模板", description = "对模板进行评分和评论")
    @PostMapping("/{templateId}/rate")
    public ResponseEntity<ApiResponse<Void>> rateTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long templateId,
            @Parameter(description = "评分(1-5)") @RequestParam int rating,
            @Parameter(description = "评论内容") @RequestParam(required = false) String comment) {
        workflowTemplateService.rateTemplate(userPrincipal.getId(), templateId, rating, comment);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 获取模板评价
     */
    @Operation(summary = "获取模板评价", description = "获取模板的评价列表")
    @GetMapping("/{templateId}/ratings")
    public ResponseEntity<ApiResponse<PageResponse<TemplateRatingResponse>>> getTemplateRatings(
            @PathVariable Long templateId,
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<TemplateRatingResponse> response = workflowTemplateService.getTemplateRatings(templateId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 导出模板
     */
    @Operation(summary = "导出模板", description = "导出模板定义为JSON格式")
    @GetMapping("/{templateId}/export")
    public ResponseEntity<ApiResponse<Map<String, Object>>> exportTemplate(
            @PathVariable Long templateId) {
        Map<String, Object> response = workflowTemplateService.exportTemplate(templateId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 导入模板
     */
    @Operation(summary = "导入模板", description = "从JSON定义导入模板")
    @PostMapping("/import")
    public ResponseEntity<ApiResponse<WorkflowTemplateResponse>> importTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Map<String, Object> templateData) {
        WorkflowTemplateResponse response = workflowTemplateService.importTemplate(userPrincipal.getId(), templateData);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 获取模板统计信息
     */
    @Operation(summary = "获取模板统计", description = "获取模板的使用统计信息")
    @GetMapping("/{templateId}/stats")
    public ResponseEntity<ApiResponse<TemplateStatsResponse>> getTemplateStats(
            @PathVariable Long templateId) {
        TemplateStatsResponse response = workflowTemplateService.getTemplateStats(templateId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
