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
import com.coze.studio.entity.PromptCategory;
import com.coze.studio.entity.PromptTemplate;
import com.coze.studio.service.PromptManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 提示词管理API控制器
 * 
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/prompts")
@RequiredArgsConstructor
@Validated
@Tag(name = "提示词管理", description = "提示词模板的创建、管理、版本控制和渲染接口")
public class PromptController {

    private final PromptManagementService promptManagementService;

    // ==================== 模板管理 ====================

    @Operation(summary = "创建提示词模板", description = "创建新的提示词模板")
    @PostMapping("/templates")
    public ResponseEntity<ApiResponse<PromptTemplate>> createTemplate(
            @Valid @RequestBody CreateTemplateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("创建提示词模板: name={}, userId={}", request.getName(), userDetails.getUsername());
        
        PromptManagementService.CreateTemplateRequest serviceRequest = 
                new PromptManagementService.CreateTemplateRequest();
        serviceRequest.setName(request.getName());
        serviceRequest.setDisplayName(request.getDisplayName());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setContent(request.getContent());
        serviceRequest.setType(request.getType());
        serviceRequest.setCategory(request.getCategory());
        serviceRequest.setTags(request.getTags());
        serviceRequest.setLanguage(request.getLanguage());
        serviceRequest.setParameters(request.getParameters());
        serviceRequest.setVariables(request.getVariables());
        serviceRequest.setConfig(request.getConfig());
        serviceRequest.setMetadata(request.getMetadata());
        serviceRequest.setIsPublic(request.getIsPublic());
        serviceRequest.setAuthor(request.getAuthor());
        serviceRequest.setLicense(request.getLicense());
        
        PromptTemplate template = promptManagementService.createTemplate(serviceRequest);
        
        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @Operation(summary = "更新提示词模板", description = "更新现有的提示词模板")
    @PutMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<PromptTemplate>> updateTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody UpdateTemplateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("更新提示词模板: id={}, userId={}", id, userDetails.getUsername());
        
        PromptManagementService.UpdateTemplateRequest serviceRequest = 
                new PromptManagementService.UpdateTemplateRequest();
        serviceRequest.setDisplayName(request.getDisplayName());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setContent(request.getContent());
        serviceRequest.setCategory(request.getCategory());
        serviceRequest.setTags(request.getTags());
        serviceRequest.setParameters(request.getParameters());
        serviceRequest.setVariables(request.getVariables());
        serviceRequest.setConfig(request.getConfig());
        serviceRequest.setMetadata(request.getMetadata());
        serviceRequest.setIsPublic(request.getIsPublic());
        serviceRequest.setAuthor(request.getAuthor());
        serviceRequest.setLicense(request.getLicense());
        
        PromptTemplate template = promptManagementService.updateTemplate(id, serviceRequest);
        
        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @Operation(summary = "删除提示词模板", description = "删除指定的提示词模板")
    @DeleteMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("删除提示词模板: id={}, userId={}", id, userDetails.getUsername());
        
        boolean deleted = promptManagementService.deleteTemplate(id, getUserId(userDetails));
        
        return ResponseEntity.ok(ApiResponse.success(deleted));
    }

    @Operation(summary = "获取提示词模板", description = "根据ID获取提示词模板详情")
    @GetMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<PromptTemplate>> getTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取提示词模板: id={}, userId={}", id, userDetails.getUsername());
        
        Optional<PromptTemplate> template = promptManagementService.getTemplate(id, getUserId(userDetails));
        
        return ResponseEntity.ok(ApiResponse.success(template.orElse(null)));
    }

    @Operation(summary = "根据名称获取模板", description = "根据模板名称获取模板")
    @GetMapping("/templates/by-name/{name}")
    public ResponseEntity<ApiResponse<PromptTemplate>> getTemplateByName(
            @Parameter(description = "模板名称") @PathVariable @NotBlank String name,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("根据名称获取模板: name={}, userId={}", name, userDetails.getUsername());
        
        Optional<PromptTemplate> template = promptManagementService.getTemplateByName(name, getUserId(userDetails));
        
        return ResponseEntity.ok(ApiResponse.success(template.orElse(null)));
    }

    @Operation(summary = "获取模板列表", description = "获取提示词模板列表")
    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<PageResponse<PromptTemplate>>> getTemplates(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean isPublic,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取模板列表: type={}, category={}, userId={}", type, category, userDetails.getUsername());
        
        PromptManagementService.GetTemplatesRequest request = 
                new PromptManagementService.GetTemplatesRequest();
        request.setType(type);
        request.setCategory(category);
        request.setStatus(status);
        request.setLanguage(language);
        request.setIsPublic(isPublic);
        request.setUserId(getUserId(userDetails));
        
        PageResponse<PromptTemplate> templates = promptManagementService.getTemplates(request, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @Operation(summary = "搜索模板", description = "根据关键词搜索提示词模板")
    @GetMapping("/templates/search")
    public ResponseEntity<ApiResponse<PageResponse<PromptTemplate>>> searchTemplates(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean isPublic,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("搜索模板: keyword={}, userId={}", keyword, userDetails.getUsername());
        
        PromptManagementService.SearchTemplatesRequest request = 
                new PromptManagementService.SearchTemplatesRequest();
        request.setKeyword(keyword);
        request.setType(type);
        request.setCategory(category);
        request.setStatus(status);
        request.setLanguage(language);
        request.setIsPublic(isPublic);
        request.setUserId(getUserId(userDetails));
        
        PageResponse<PromptTemplate> templates = promptManagementService.searchTemplates(request, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @Operation(summary = "获取用户模板", description = "获取当前用户的模板列表")
    @GetMapping("/templates/my")
    public ResponseEntity<ApiResponse<PageResponse<PromptTemplate>>> getUserTemplates(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取用户模板: userId={}", userDetails.getUsername());
        
        PageResponse<PromptTemplate> templates = promptManagementService.getUserTemplates(getUserId(userDetails), pageable);
        
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @Operation(summary = "获取公开模板", description = "获取所有公开的模板")
    @GetMapping("/templates/public")
    public ResponseEntity<ApiResponse<PageResponse<PromptTemplate>>> getPublicTemplates(
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("获取公开模板");
        
        PageResponse<PromptTemplate> templates = promptManagementService.getPublicTemplates(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @Operation(summary = "获取热门模板", description = "获取热门的模板")
    @GetMapping("/templates/popular")
    public ResponseEntity<ApiResponse<PageResponse<PromptTemplate>>> getPopularTemplates(
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("获取热门模板");
        
        PageResponse<PromptTemplate> templates = promptManagementService.getPopularTemplates(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @Operation(summary = "获取最新模板", description = "获取最新发布的模板")
    @GetMapping("/templates/latest")
    public ResponseEntity<ApiResponse<PageResponse<PromptTemplate>>> getLatestTemplates(
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("获取最新模板");
        
        PageResponse<PromptTemplate> templates = promptManagementService.getLatestTemplates(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @Operation(summary = "获取推荐模板", description = "获取推荐的模板")
    @GetMapping("/templates/recommended")
    public ResponseEntity<ApiResponse<PageResponse<PromptTemplate>>> getRecommendedTemplates(
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("获取推荐模板");
        
        PageResponse<PromptTemplate> templates = promptManagementService.getRecommendedTemplates(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    // ==================== 版本管理 ====================

    @Operation(summary = "创建新版本", description = "为模板创建新版本")
    @PostMapping("/templates/{id}/versions")
    public ResponseEntity<ApiResponse<PromptTemplate>> createNewVersion(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody CreateVersionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("创建新版本: templateId={}, versionType={}, userId={}", 
                id, request.getVersionType(), userDetails.getUsername());
        
        PromptManagementService.CreateVersionRequest serviceRequest = 
                new PromptManagementService.CreateVersionRequest();
        serviceRequest.setVersionType(request.getVersionType());
        serviceRequest.setContent(request.getContent());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setParameters(request.getParameters());
        serviceRequest.setVariables(request.getVariables());
        serviceRequest.setConfig(request.getConfig());
        
        PromptTemplate newVersion = promptManagementService.createNewVersion(id, serviceRequest);
        
        return ResponseEntity.ok(ApiResponse.success(newVersion));
    }

    @Operation(summary = "获取模板版本列表", description = "获取模板的所有版本")
    @GetMapping("/templates/{rootId}/versions")
    public ResponseEntity<ApiResponse<List<PromptTemplate>>> getTemplateVersions(
            @Parameter(description = "根模板ID") @PathVariable @NotNull Long rootId) {
        
        log.info("获取模板版本列表: rootId={}", rootId);
        
        List<PromptTemplate> versions = promptManagementService.getTemplateVersions(rootId);
        
        return ResponseEntity.ok(ApiResponse.success(versions));
    }

    @Operation(summary = "获取最新版本", description = "获取模板的最新版本")
    @GetMapping("/templates/{rootId}/versions/latest")
    public ResponseEntity<ApiResponse<PromptTemplate>> getLatestVersion(
            @Parameter(description = "根模板ID") @PathVariable @NotNull Long rootId) {
        
        log.info("获取最新版本: rootId={}", rootId);
        
        Optional<PromptTemplate> latestVersion = promptManagementService.getLatestVersion(rootId);
        
        return ResponseEntity.ok(ApiResponse.success(latestVersion.orElse(null)));
    }

    @Operation(summary = "比较版本", description = "比较两个版本的差异")
    @GetMapping("/templates/versions/compare")
    public ResponseEntity<ApiResponse<PromptManagementService.VersionComparisonResult>> compareVersions(
            @RequestParam @NotNull Long version1Id,
            @RequestParam @NotNull Long version2Id) {
        
        log.info("比较版本: version1Id={}, version2Id={}", version1Id, version2Id);
        
        PromptManagementService.VersionComparisonResult result = 
                promptManagementService.compareVersions(version1Id, version2Id);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // ==================== 模板操作 ====================

    @Operation(summary = "发布模板", description = "发布模板使其变为活跃状态")
    @PostMapping("/templates/{id}/publish")
    public ResponseEntity<ApiResponse<PromptTemplate>> publishTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("发布模板: id={}, userId={}", id, userDetails.getUsername());

        PromptTemplate template = promptManagementService.publishTemplate(id, getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @Operation(summary = "弃用模板", description = "将模板标记为已弃用")
    @PostMapping("/templates/{id}/deprecate")
    public ResponseEntity<ApiResponse<PromptTemplate>> deprecateTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("弃用模板: id={}, userId={}", id, userDetails.getUsername());

        PromptTemplate template = promptManagementService.deprecateTemplate(id, getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @Operation(summary = "归档模板", description = "将模板归档")
    @PostMapping("/templates/{id}/archive")
    public ResponseEntity<ApiResponse<PromptTemplate>> archiveTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("归档模板: id={}, userId={}", id, userDetails.getUsername());

        PromptTemplate template = promptManagementService.archiveTemplate(id, getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @Operation(summary = "克隆模板", description = "克隆现有模板创建新模板")
    @PostMapping("/templates/{id}/clone")
    public ResponseEntity<ApiResponse<PromptTemplate>> cloneTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody CloneTemplateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("克隆模板: id={}, newName={}, userId={}", id, request.getName(), userDetails.getUsername());

        PromptManagementService.CloneTemplateRequest serviceRequest =
                new PromptManagementService.CloneTemplateRequest();
        serviceRequest.setName(request.getName());
        serviceRequest.setDisplayName(request.getDisplayName());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setIsPublic(request.getIsPublic());

        PromptTemplate clonedTemplate = promptManagementService.cloneTemplate(id, serviceRequest);

        return ResponseEntity.ok(ApiResponse.success(clonedTemplate));
    }

    @Operation(summary = "收藏模板", description = "收藏指定的模板")
    @PostMapping("/templates/{id}/favorite")
    public ResponseEntity<ApiResponse<Boolean>> favoriteTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("收藏模板: id={}, userId={}", id, userDetails.getUsername());

        boolean result = promptManagementService.favoriteTemplate(id, getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "取消收藏模板", description = "取消收藏指定的模板")
    @DeleteMapping("/templates/{id}/favorite")
    public ResponseEntity<ApiResponse<Boolean>> unfavoriteTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("取消收藏模板: id={}, userId={}", id, userDetails.getUsername());

        boolean result = promptManagementService.unfavoriteTemplate(id, getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "评价模板", description = "为模板评分")
    @PostMapping("/templates/{id}/rate")
    public ResponseEntity<ApiResponse<Boolean>> rateTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody RateTemplateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("评价模板: id={}, rating={}, userId={}", id, request.getRating(), userDetails.getUsername());

        boolean result = promptManagementService.rateTemplate(id, getUserId(userDetails), request.getRating());

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "下载模板", description = "下载模板文件")
    @GetMapping("/templates/{id}/download")
    public ResponseEntity<ApiResponse<PromptManagementService.TemplateDownloadResult>> downloadTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("下载模板: id={}, userId={}", id, userDetails.getUsername());

        PromptManagementService.TemplateDownloadResult result =
                promptManagementService.downloadTemplate(id, getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // ==================== 模板渲染 ====================

    @Operation(summary = "渲染模板", description = "使用变量渲染模板内容")
    @PostMapping("/templates/{id}/render")
    public ResponseEntity<ApiResponse<String>> renderTemplate(
            @Parameter(description = "模板ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody RenderTemplateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("渲染模板: id={}, userId={}", id, userDetails.getUsername());

        String result = promptManagementService.renderTemplate(id, request.getVariables(), getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(result)); // 泛型为 String，保持不变
    }

    @Operation(summary = "根据名称渲染模板", description = "根据模板名称渲染模板")
    @PostMapping("/templates/render-by-name")
    public ResponseEntity<ApiResponse<String>> renderTemplateByName(
            @Valid @RequestBody RenderTemplateByNameRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("根据名称渲染模板: name={}, userId={}", request.getTemplateName(), userDetails.getUsername());

        String result = promptManagementService.renderTemplateByName(
                request.getTemplateName(), request.getVariables(), getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "预览模板", description = "预览模板渲染结果")
    @PostMapping("/templates/preview")
    public ResponseEntity<ApiResponse<PromptManagementService.TemplatePreviewResult>> previewTemplate(
            @Valid @RequestBody PreviewTemplateRequest request) {

        log.info("预览模板");

        PromptManagementService.PreviewTemplateRequest serviceRequest =
                new PromptManagementService.PreviewTemplateRequest();
        serviceRequest.setContent(request.getContent());
        serviceRequest.setVariables(request.getVariables());

        PromptManagementService.TemplatePreviewResult result =
                promptManagementService.previewTemplate(serviceRequest);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "验证模板", description = "验证模板语法")
    @PostMapping("/templates/validate")
    public ResponseEntity<ApiResponse<PromptManagementService.TemplateValidationResult>> validateTemplate(
            @Valid @RequestBody ValidateTemplateRequest request) {

        log.info("验证模板");

        PromptManagementService.TemplateValidationResult result =
                promptManagementService.validateTemplate(request.getContent());

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "提取模板变量", description = "提取模板中的变量")
    @PostMapping("/templates/extract-variables")
    public ResponseEntity<ApiResponse<List<String>>> extractTemplateVariables(
            @Valid @RequestBody ExtractVariablesRequest request) {

        log.info("提取模板变量");

        List<String> variables = promptManagementService.extractTemplateVariables(request.getContent());

        return ResponseEntity.ok(ApiResponse.success(variables));
    }

    // ==================== 分类管理 ====================

    @Operation(summary = "创建分类", description = "创建新的提示词分类")
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<PromptCategory>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {

        log.info("创建分类: code={}, name={}", request.getCode(), request.getName());

        PromptManagementService.CreateCategoryRequest serviceRequest =
                new PromptManagementService.CreateCategoryRequest();
        serviceRequest.setCode(request.getCode());
        serviceRequest.setName(request.getName());
        serviceRequest.setDisplayName(request.getDisplayName());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setParentId(request.getParentId());
        serviceRequest.setIcon(request.getIcon());
        serviceRequest.setColor(request.getColor());
        serviceRequest.setSortOrder(request.getSortOrder());

        PromptCategory category = promptManagementService.createCategory(serviceRequest);

        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @Operation(summary = "更新分类", description = "更新现有的分类")
    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<PromptCategory>> updateCategory(
            @Parameter(description = "分类ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {

        log.info("更新分类: id={}", id);

        PromptManagementService.UpdateCategoryRequest serviceRequest =
                new PromptManagementService.UpdateCategoryRequest();
        serviceRequest.setName(request.getName());
        serviceRequest.setDisplayName(request.getDisplayName());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setIcon(request.getIcon());
        serviceRequest.setColor(request.getColor());
        serviceRequest.setSortOrder(request.getSortOrder());
        serviceRequest.setEnabled(request.getEnabled());

        PromptCategory category = promptManagementService.updateCategory(id, serviceRequest);

        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @Operation(summary = "删除分类", description = "删除指定的分类")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteCategory(
            @Parameter(description = "分类ID") @PathVariable @NotNull Long id) {

        log.info("删除分类: id={}", id);

        boolean deleted = promptManagementService.deleteCategory(id);

        return ResponseEntity.ok(ApiResponse.success(deleted));
    }

    @Operation(summary = "获取分类", description = "根据ID获取分类详情")
    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<PromptCategory>> getCategory(
            @Parameter(description = "分类ID") @PathVariable @NotNull Long id) {

        log.info("获取分类: id={}", id);

        Optional<PromptCategory> category = promptManagementService.getCategory(id);

        return ResponseEntity.ok(ApiResponse.success(category.orElse(null)));
    }

    @Operation(summary = "根据代码获取分类", description = "根据分类代码获取分类")
    @GetMapping("/categories/by-code/{code}")
    public ResponseEntity<ApiResponse<PromptCategory>> getCategoryByCode(
            @Parameter(description = "分类代码") @PathVariable @NotBlank String code) {

        log.info("根据代码获取分类: code={}", code);

        Optional<PromptCategory> category = promptManagementService.getCategoryByCode(code);

        return ResponseEntity.ok(ApiResponse.success(category.orElse(null)));
    }

    @Operation(summary = "获取所有分类", description = "获取所有启用的分类")
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<PromptCategory>>> getAllCategories() {

        log.info("获取所有分类");

        List<PromptCategory> categories = promptManagementService.getAllCategories();

        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @Operation(summary = "获取分类树", description = "获取分类的树形结构")
    @GetMapping("/categories/tree")
    public ResponseEntity<ApiResponse<List<PromptManagementService.CategoryTreeNode>>> getCategoryTree() {

        log.info("获取分类树");

        List<PromptManagementService.CategoryTreeNode> tree = promptManagementService.getCategoryTree();

        return ResponseEntity.ok(ApiResponse.success(tree));
    }

    @Operation(summary = "获取根分类", description = "获取所有根分类")
    @GetMapping("/categories/roots")
    public ResponseEntity<ApiResponse<List<PromptCategory>>> getRootCategories() {

        log.info("获取根分类");

        List<PromptCategory> categories = promptManagementService.getRootCategories();

        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @Operation(summary = "获取子分类", description = "获取指定分类的子分类")
    @GetMapping("/categories/{parentId}/children")
    public ResponseEntity<ApiResponse<List<PromptCategory>>> getChildCategories(
            @Parameter(description = "父分类ID") @PathVariable @NotNull Long parentId) {

        log.info("获取子分类: parentId={}", parentId);

        List<PromptCategory> categories = promptManagementService.getChildCategories(parentId);

        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    // ==================== 统计和分析 ====================

    @Operation(summary = "获取模板统计", description = "获取模板的统计信息")
    @GetMapping("/statistics/templates")
    public ResponseEntity<ApiResponse<PromptManagementService.TemplateStatistics>> getTemplateStatistics(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("获取模板统计: userId={}", userDetails.getUsername());

        PromptManagementService.TemplateStatistics statistics =
                promptManagementService.getTemplateStatistics(getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    @Operation(summary = "获取用户模板统计", description = "获取用户的模板统计信息")
    @GetMapping("/statistics/user-templates")
    public ResponseEntity<ApiResponse<PromptManagementService.UserTemplateStatistics>> getUserTemplateStatistics(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("获取用户模板统计: userId={}", userDetails.getUsername());

        PromptManagementService.UserTemplateStatistics statistics =
                promptManagementService.getUserTemplateStatistics(getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    @Operation(summary = "获取分类统计", description = "获取分类的统计信息")
    @GetMapping("/statistics/categories")
    public ResponseEntity<ApiResponse<PromptManagementService.CategoryStatistics>> getCategoryStatistics() {

        log.info("获取分类统计");

        PromptManagementService.CategoryStatistics statistics =
                promptManagementService.getCategoryStatistics();

        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    @Operation(summary = "获取使用趋势", description = "获取模板使用趋势数据")
    @GetMapping("/statistics/usage-trend")
    public ResponseEntity<ApiResponse<List<PromptManagementService.UsageTrendData>>> getUsageTrend(
            @RequestParam(defaultValue = "daily") String period,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("获取使用趋势: period={}, userId={}", period, userDetails.getUsername());

        List<PromptManagementService.UsageTrendData> trendData =
                promptManagementService.getUsageTrend(getUserId(userDetails), period);

        return ResponseEntity.ok(ApiResponse.success(trendData));
    }

    // ==================== 批量操作 ====================

    @Operation(summary = "批量更新模板状态", description = "批量更新多个模板的状态")
    @PostMapping("/templates/batch/update-status")
    public ResponseEntity<ApiResponse<PromptManagementService.BatchOperationResult>> batchUpdateStatus(
            @Valid @RequestBody BatchUpdateStatusRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("批量更新模板状态: templateIds={}, status={}, userId={}",
                request.getTemplateIds(), request.getStatus(), userDetails.getUsername());

        PromptManagementService.BatchOperationResult result =
                promptManagementService.batchUpdateStatus(request.getTemplateIds(), request.getStatus(), getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "批量删除模板", description = "批量删除多个模板")
    @PostMapping("/templates/batch/delete")
    public ResponseEntity<ApiResponse<PromptManagementService.BatchOperationResult>> batchDeleteTemplates(
            @Valid @RequestBody BatchDeleteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("批量删除模板: templateIds={}, userId={}",
                request.getTemplateIds(), userDetails.getUsername());

        PromptManagementService.BatchOperationResult result =
                promptManagementService.batchDeleteTemplates(request.getTemplateIds(), getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "导出模板", description = "导出指定的模板")
    @PostMapping("/templates/export")
    public ResponseEntity<ApiResponse<PromptManagementService.TemplateExportResult>> exportTemplates(
            @Valid @RequestBody ExportTemplatesRequest request) {

        log.info("导出模板: templateIds={}, format={}", request.getTemplateIds(), request.getFormat());

        PromptManagementService.ExportTemplatesRequest serviceRequest =
                new PromptManagementService.ExportTemplatesRequest();
        serviceRequest.setTemplateIds(request.getTemplateIds());
        serviceRequest.setFormat(request.getFormat());
        serviceRequest.setIncludeMetadata(request.isIncludeMetadata());

        PromptManagementService.TemplateExportResult result =
                promptManagementService.exportTemplates(serviceRequest);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "导入模板", description = "导入模板数据")
    @PostMapping("/templates/import")
    public ResponseEntity<ApiResponse<PromptManagementService.TemplateImportResult>> importTemplates(
            @Valid @RequestBody ImportTemplatesRequest request) {

        log.info("导入模板: format={}", request.getFormat());

        PromptManagementService.ImportTemplatesRequest serviceRequest =
                new PromptManagementService.ImportTemplatesRequest();
        serviceRequest.setData(request.getData());
        serviceRequest.setFormat(request.getFormat());
        serviceRequest.setOverwriteExisting(request.isOverwriteExisting());
        serviceRequest.setValidateBeforeImport(request.isValidateBeforeImport());

        PromptManagementService.TemplateImportResult result =
                promptManagementService.importTemplates(serviceRequest);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 从UserDetails中提取用户ID
     */
    private Long getUserId(UserDetails userDetails) {
        try {
            return Long.parseLong(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return 1L; // 临时返回默认值
        }
    }

    // ==================== 请求DTO类 ====================

    /**
     * 创建模板请求
     */
    public static class CreateTemplateRequest {
        @NotBlank(message = "模板名称不能为空")
        private String name;
        private String displayName;
        private String description;
        @NotBlank(message = "模板内容不能为空")
        private String content;
        @NotBlank(message = "模板类型不能为空")
        private String type;
        private String category;
        private List<String> tags;
        private String language;
        private Map<String, Object> parameters;
        private Map<String, Object> variables;
        private Map<String, Object> config;
        private Map<String, Object> metadata;
        private Boolean isPublic;
        private String author;
        private String license;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public Boolean getIsPublic() { return isPublic; }
        public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getLicense() { return license; }
        public void setLicense(String license) { this.license = license; }
    }

    /**
     * 更新模板请求
     */
    public static class UpdateTemplateRequest {
        private String displayName;
        private String description;
        private String content;
        private String category;
        private List<String> tags;
        private Map<String, Object> parameters;
        private Map<String, Object> variables;
        private Map<String, Object> config;
        private Map<String, Object> metadata;
        private Boolean isPublic;
        private String author;
        private String license;

        // Getters and setters
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public Boolean getIsPublic() { return isPublic; }
        public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getLicense() { return license; }
        public void setLicense(String license) { this.license = license; }
    }

    /**
     * 创建版本请求
     */
    public static class CreateVersionRequest {
        @NotBlank(message = "版本类型不能为空")
        private String versionType; // major, minor, patch
        @NotBlank(message = "内容不能为空")
        private String content;
        private String description;
        private Map<String, Object> parameters;
        private Map<String, Object> variables;
        private Map<String, Object> config;

        // Getters and setters
        public String getVersionType() { return versionType; }
        public void setVersionType(String versionType) { this.versionType = versionType; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }

    /**
     * 克隆模板请求
     */
    public static class CloneTemplateRequest {
        @NotBlank(message = "模板名称不能为空")
        private String name;
        private String displayName;
        private String description;
        private Boolean isPublic;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Boolean getIsPublic() { return isPublic; }
        public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    }

    /**
     * 评价模板请求
     */
    public static class RateTemplateRequest {
        @NotNull(message = "评分不能为空")
        private Double rating;

        // Getters and setters
        public Double getRating() { return rating; }
        public void setRating(Double rating) { this.rating = rating; }
    }

    /**
     * 渲染模板请求
     */
    public static class RenderTemplateRequest {
        private Map<String, Object> variables;

        // Getters and setters
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
    }

    /**
     * 根据名称渲染模板请求
     */
    public static class RenderTemplateByNameRequest {
        @NotBlank(message = "模板名称不能为空")
        private String templateName;
        private Map<String, Object> variables;

        // Getters and setters
        public String getTemplateName() { return templateName; }
        public void setTemplateName(String templateName) { this.templateName = templateName; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
    }

    /**
     * 预览模板请求
     */
    public static class PreviewTemplateRequest {
        @NotBlank(message = "模板内容不能为空")
        private String content;
        private Map<String, Object> variables;

        // Getters and setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
    }

    /**
     * 验证模板请求
     */
    public static class ValidateTemplateRequest {
        @NotBlank(message = "模板内容不能为空")
        private String content;

        // Getters and setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    /**
     * 提取变量请求
     */
    public static class ExtractVariablesRequest {
        @NotBlank(message = "模板内容不能为空")
        private String content;

        // Getters and setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    /**
     * 创建分类请求
     */
    public static class CreateCategoryRequest {
        @NotBlank(message = "分类代码不能为空")
        private String code;
        @NotBlank(message = "分类名称不能为空")
        private String name;
        private String displayName;
        private String description;
        private Long parentId;
        private String icon;
        private String color;
        private Integer sortOrder;

        // Getters and setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    }

    /**
     * 更新分类请求
     */
    public static class UpdateCategoryRequest {
        private String name;
        private String displayName;
        private String description;
        private String icon;
        private String color;
        private Integer sortOrder;
        private Boolean enabled;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }

    /**
     * 批量更新状态请求
     */
    public static class BatchUpdateStatusRequest {
        @NotNull(message = "模板ID列表不能为空")
        private List<Long> templateIds;
        @NotBlank(message = "状态不能为空")
        private String status;

        // Getters and setters
        public List<Long> getTemplateIds() { return templateIds; }
        public void setTemplateIds(List<Long> templateIds) { this.templateIds = templateIds; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * 批量删除请求
     */
    public static class BatchDeleteRequest {
        @NotNull(message = "模板ID列表不能为空")
        private List<Long> templateIds;

        // Getters and setters
        public List<Long> getTemplateIds() { return templateIds; }
        public void setTemplateIds(List<Long> templateIds) { this.templateIds = templateIds; }
    }

    /**
     * 导出模板请求
     */
    public static class ExportTemplatesRequest {
        @NotNull(message = "模板ID列表不能为空")
        private List<Long> templateIds;
        private String format = "JSON";
        private boolean includeMetadata = true;

        // Getters and setters
        public List<Long> getTemplateIds() { return templateIds; }
        public void setTemplateIds(List<Long> templateIds) { this.templateIds = templateIds; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public boolean isIncludeMetadata() { return includeMetadata; }
        public void setIncludeMetadata(boolean includeMetadata) { this.includeMetadata = includeMetadata; }
    }

    /**
     * 导入模板请求
     */
    public static class ImportTemplatesRequest {
        @NotBlank(message = "导入数据不能为空")
        private String data;
        private String format = "JSON";
        private boolean overwriteExisting = false;
        private boolean validateBeforeImport = true;

        // Getters and setters
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public boolean isOverwriteExisting() { return overwriteExisting; }
        public void setOverwriteExisting(boolean overwriteExisting) { this.overwriteExisting = overwriteExisting; }
        public boolean isValidateBeforeImport() { return validateBeforeImport; }
        public void setValidateBeforeImport(boolean validateBeforeImport) { this.validateBeforeImport = validateBeforeImport; }
    }
}
