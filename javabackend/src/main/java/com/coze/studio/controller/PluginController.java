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
import com.coze.studio.service.PluginExecutionService;
import com.coze.studio.service.PluginManagementService;
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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * 插件管理API控制器
 * 
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/plugins")
@RequiredArgsConstructor
@Validated
@Tag(name = "插件管理", description = "插件的注册、管理、安装和执行接口")
public class PluginController {

    private final PluginManagementService pluginManagementService;
    private final PluginExecutionService pluginExecutionService;

    @Operation(summary = "注册插件", description = "注册新的插件")
    @PostMapping
    public ResponseEntity<ApiResponse<PluginManagementService.PluginResponse>> registerPlugin(
            @Valid @RequestBody RegisterPluginRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("注册插件: name={}, userId={}", request.getName(), userDetails.getUsername());
        
        PluginManagementService.RegisterPluginRequest registerRequest = 
                new PluginManagementService.RegisterPluginRequest();
        registerRequest.setName(request.getName());
        registerRequest.setDescription(request.getDescription());
        registerRequest.setVersion(request.getVersion());
        registerRequest.setPluginType(request.getPluginType());
        registerRequest.setCategory(request.getCategory());
        registerRequest.setTags(request.getTags());
        registerRequest.setIconUrl(request.getIconUrl());
        registerRequest.setOpenapi(request.getOpenapi());
        registerRequest.setAiPlugin(request.getAiPlugin());
        registerRequest.setServiceUrl(request.getServiceUrl());
        registerRequest.setServiceToken(request.getServiceToken());
        registerRequest.setPluginConfig(request.getPluginConfig());
        registerRequest.setDependencies(request.getDependencies());
        registerRequest.setPermissions(request.getPermissions());
        registerRequest.setDocumentationUrl(request.getDocumentationUrl());
        registerRequest.setSupportUrl(request.getSupportUrl());
        registerRequest.setLicense(request.getLicense());
        registerRequest.setPrivate(request.isPrivate());
        
        PluginManagementService.PluginResponse plugin = pluginManagementService.registerPlugin(
                getUserId(userDetails), registerRequest);
        
        return ResponseEntity.ok(ApiResponse.success(plugin));
    }

    @Operation(summary = "获取插件列表", description = "分页获取插件列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PluginManagementService.PluginResponse>>> getPlugins(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("获取插件列表: category={}, status={}, keyword={}", category, status, keyword);
        
        PluginManagementService.PluginQueryRequest queryRequest = 
                new PluginManagementService.PluginQueryRequest();
        queryRequest.setCategory(category);
        queryRequest.setStatus(status);
        queryRequest.setKeyword(keyword);
        
        PageResponse<PluginManagementService.PluginResponse> plugins = 
                pluginManagementService.getPlugins(queryRequest, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(plugins));
    }

    @Operation(summary = "搜索插件", description = "根据关键词搜索插件")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<PluginManagementService.PluginResponse>>> searchPlugins(
            @RequestParam @NotBlank String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("搜索插件: keyword={}", keyword);
        
        PageResponse<PluginManagementService.PluginResponse> plugins = 
                pluginManagementService.searchPlugins(keyword, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(plugins));
    }

    @Operation(summary = "获取热门插件", description = "获取热门插件列表")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<PageResponse<PluginManagementService.PluginResponse>>> getPopularPlugins(
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("获取热门插件");
        
        PageResponse<PluginManagementService.PluginResponse> plugins = 
                pluginManagementService.getPopularPlugins(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(plugins));
    }

    @Operation(summary = "获取插件详情", description = "根据ID获取插件详细信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginResponse>> getPlugin(
            @Parameter(description = "插件ID") @PathVariable Long id) {
        
        log.info("获取插件详情: id={}", id);
        
        PluginManagementService.PluginResponse plugin = pluginManagementService.getPluginById(id);
        
        return ResponseEntity.ok(ApiResponse.success(plugin));
    }

    @Operation(summary = "更新插件", description = "更新插件信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginResponse>> updatePlugin(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Valid @RequestBody UpdatePluginRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("更新插件: id={}, userId={}", id, userDetails.getUsername());
        
        PluginManagementService.UpdatePluginRequest updateRequest = 
                new PluginManagementService.UpdatePluginRequest();
        updateRequest.setName(request.getName());
        updateRequest.setDescription(request.getDescription());
        updateRequest.setVersion(request.getVersion());
        updateRequest.setCategory(request.getCategory());
        updateRequest.setTags(request.getTags());
        updateRequest.setIconUrl(request.getIconUrl());
        updateRequest.setOpenapi(request.getOpenapi());
        updateRequest.setServiceUrl(request.getServiceUrl());
        updateRequest.setServiceToken(request.getServiceToken());
        updateRequest.setPluginConfig(request.getPluginConfig());
        updateRequest.setDependencies(request.getDependencies());
        updateRequest.setPermissions(request.getPermissions());
        updateRequest.setDocumentationUrl(request.getDocumentationUrl());
        updateRequest.setSupportUrl(request.getSupportUrl());
        updateRequest.setLicense(request.getLicense());
        updateRequest.setPrivate(request.isPrivate());
        
        PluginManagementService.PluginResponse plugin = pluginManagementService.updatePlugin(
                getUserId(userDetails), id, updateRequest);
        
        return ResponseEntity.ok(ApiResponse.success(plugin));
    }

    @Operation(summary = "删除插件", description = "删除指定的插件")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePlugin(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("删除插件: id={}, userId={}", id, userDetails.getUsername());
        
        pluginManagementService.deletePlugin(getUserId(userDetails), id);
        
        return ResponseEntity.ok(ApiResponse.success("插件删除成功"));
    }

    @Operation(summary = "发布插件", description = "发布插件到市场")
    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginResponse>> publishPlugin(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("发布插件: id={}, userId={}", id, userDetails.getUsername());
        
        PluginManagementService.PluginResponse plugin = pluginManagementService.publishPlugin(
                getUserId(userDetails), id);
        
        return ResponseEntity.ok(ApiResponse.success(plugin));
    }

    @Operation(summary = "取消发布插件", description = "取消发布插件")
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginResponse>> unpublishPlugin(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("取消发布插件: id={}, userId={}", id, userDetails.getUsername());
        
        PluginManagementService.PluginResponse plugin = pluginManagementService.unpublishPlugin(
                getUserId(userDetails), id);
        
        return ResponseEntity.ok(ApiResponse.success(plugin));
    }

    @Operation(summary = "安装插件", description = "安装插件到用户环境")
    @PostMapping("/{id}/install")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginInstallationResponse>> installPlugin(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Valid @RequestBody InstallPluginRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("安装插件: id={}, userId={}", id, userDetails.getUsername());
        
        PluginManagementService.InstallPluginRequest installRequest = 
                new PluginManagementService.InstallPluginRequest();
        installRequest.setVersion(request.getVersion());
        installRequest.setInstallationConfig(request.getInstallationConfig());
        
        PluginManagementService.PluginInstallationResponse installation = 
                pluginManagementService.installPlugin(getUserId(userDetails), id, installRequest);
        
        return ResponseEntity.ok(ApiResponse.success(installation));
    }

    @Operation(summary = "卸载插件", description = "卸载已安装的插件")
    @PostMapping("/{id}/uninstall")
    public ResponseEntity<ApiResponse<String>> uninstallPlugin(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("卸载插件: id={}, userId={}, reason={}", id, userDetails.getUsername(), reason);
        
        pluginManagementService.uninstallPlugin(getUserId(userDetails), id, reason);
        
        return ResponseEntity.ok(ApiResponse.success("插件卸载成功"));
    }

    @Operation(summary = "启用/禁用插件", description = "切换插件的启用状态")
    @PostMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<String>> togglePluginStatus(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @RequestParam boolean enabled,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("切换插件状态: id={}, enabled={}, userId={}", id, enabled, userDetails.getUsername());
        
        pluginManagementService.togglePluginStatus(getUserId(userDetails), id, enabled);
        
        return ResponseEntity.ok(ApiResponse.success(enabled ? "插件已启用" : "插件已禁用"));
    }

    @Operation(summary = "执行插件", description = "执行插件功能")
    @PostMapping("/{id}/execute")
    public ResponseEntity<ApiResponse<PluginExecutionService.PluginExecutionResult>> executePlugin(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Valid @RequestBody ExecutePluginRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("执行插件: id={}, operation={}, userId={}", id, request.getOperation(), userDetails.getUsername());
        
        PluginExecutionService.PluginExecutionRequest executionRequest = 
                new PluginExecutionService.PluginExecutionRequest();
        executionRequest.setUserId(getUserId(userDetails));
        executionRequest.setPluginId(id);
        executionRequest.setOperation(request.getOperation());
        executionRequest.setApiEndpoint(request.getApiEndpoint());
        executionRequest.setHttpMethod(request.getHttpMethod());
        executionRequest.setParameters(request.getParameters());
        executionRequest.setHeaders(request.getHeaders());
        executionRequest.setRequestId(request.getRequestId());
        executionRequest.setSessionId(request.getSessionId());
        executionRequest.setWorkflowId(request.getWorkflowId());
        executionRequest.setNodeId(request.getNodeId());
        executionRequest.setTimeoutSeconds(request.getTimeoutSeconds());
        executionRequest.setAsync(request.isAsync());
        
        PluginExecutionService.PluginExecutionResult result = 
                pluginExecutionService.executePlugin(executionRequest);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "调试插件API", description = "调试插件的API调用")
    @PostMapping("/{id}/debug-api")
    public ResponseEntity<ApiResponse<PluginExecutionService.PluginExecutionResult>> debugPluginApi(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Valid @RequestBody DebugPluginApiRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("调试插件API: id={}, apiId={}, userId={}", id, request.getApiId(), userDetails.getUsername());

        PluginExecutionService.PluginExecutionRequest executionRequest =
                new PluginExecutionService.PluginExecutionRequest();
        executionRequest.setUserId(getUserId(userDetails));
        executionRequest.setPluginId(id);
        executionRequest.setOperation("debug");
        executionRequest.setApiEndpoint(request.getApiEndpoint());
        executionRequest.setHttpMethod(request.getHttpMethod());
        executionRequest.setParameters(request.getParameters());
        executionRequest.setHeaders(request.getHeaders());
        executionRequest.setRequestId(request.getRequestId());
        executionRequest.setTimeoutSeconds(request.getTimeoutSeconds());
        executionRequest.setAsync(false);

        PluginExecutionService.PluginExecutionResult result =
                pluginExecutionService.debugPluginApi(executionRequest);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "测试插件", description = "测试插件功能")
    @PostMapping("/{id}/test")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginTestResult>> testPlugin(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> testData,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("测试插件: id={}, userId={}", id, userDetails.getUsername());

        PluginManagementService.PluginTestResult result = pluginManagementService.testPlugin(
                getUserId(userDetails), id, testData);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "同步插件到市场", description = "将插件同步到插件市场")
    @PostMapping("/{id}/sync-to-market")
    public ResponseEntity<ApiResponse<String>> syncPluginToMarket(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("同步插件到市场: id={}, userId={}", id, userDetails.getUsername());

        pluginManagementService.syncPluginToMarket(getUserId(userDetails), id);

        return ResponseEntity.ok(ApiResponse.success("插件同步到市场成功"));
    }

    @Operation(summary = "从市场下载插件", description = "从插件市场下载插件")
    @PostMapping("/download-from-market")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginResponse>> downloadPluginFromMarket(
            @Valid @RequestBody DownloadPluginFromMarketRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("从市场下载插件: marketId={}, userId={}", request.getMarketPluginId(), userDetails.getUsername());

        PluginManagementService.PluginResponse plugin = pluginManagementService.downloadPluginFromMarket(
                getUserId(userDetails), request);

        return ResponseEntity.ok(ApiResponse.success(plugin));
    }

    @Operation(summary = "创建新版本", description = "为插件创建新版本")
    @PostMapping("/{id}/versions")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginVersionResponse>> createPluginVersion(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Valid @RequestBody CreatePluginVersionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("创建插件版本: pluginId={}, version={}, userId={}", id, request.getVersion(), userDetails.getUsername());

        PluginManagementService.CreatePluginVersionRequest createRequest =
                new PluginManagementService.CreatePluginVersionRequest();
        createRequest.setVersion(request.getVersion());
        createRequest.setTitle(request.getTitle());
        createRequest.setDescription(request.getDescription());
        createRequest.setReleaseNotes(request.getReleaseNotes());
        createRequest.setMinCompatibleVersion(request.getMinCompatibleVersion());
        createRequest.setIsStable(request.getIsStable());
        createRequest.setVersionFiles(request.getVersionFiles());
        createRequest.setDependencies(request.getDependencies());
        createRequest.setConfigTemplate(request.getConfigTemplate());

        PluginManagementService.PluginVersionResponse version = pluginManagementService.createPluginVersion(
                getUserId(userDetails), id, createRequest);

        return ResponseEntity.ok(ApiResponse.success(version));
    }

    @Operation(summary = "发布版本", description = "发布指定的插件版本")
    @PostMapping("/{id}/versions/{versionId}/publish")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginVersionResponse>> publishPluginVersion(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Parameter(description = "版本ID") @PathVariable Long versionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("发布插件版本: pluginId={}, versionId={}, userId={}", id, versionId, userDetails.getUsername());

        PluginManagementService.PluginVersionResponse version = pluginManagementService.publishPluginVersion(
                getUserId(userDetails), id, versionId);

        return ResponseEntity.ok(ApiResponse.success(version));
    }

    @Operation(summary = "获取版本列表", description = "获取插件的版本列表")
    @GetMapping("/{id}/versions")
    public ResponseEntity<ApiResponse<PageResponse<PluginManagementService.PluginVersionResponse>>> getPluginVersions(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("获取插件版本列表: pluginId={}, status={}", id, status);

        PluginManagementService.PluginVersionQueryRequest queryRequest =
                new PluginManagementService.PluginVersionQueryRequest();
        queryRequest.setStatus(status);

        PageResponse<PluginManagementService.PluginVersionResponse> versions =
                pluginManagementService.getPluginVersions(id, queryRequest, pageable);

        return ResponseEntity.ok(ApiResponse.success(versions));
    }

    @Operation(summary = "获取版本详情", description = "获取指定版本的详细信息")
    @GetMapping("/{id}/versions/{versionId}")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginVersionResponse>> getPluginVersion(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Parameter(description = "版本ID") @PathVariable Long versionId) {

        log.info("获取插件版本详情: pluginId={}, versionId={}", id, versionId);

        PluginManagementService.PluginVersionResponse version = pluginManagementService.getPluginVersion(id, versionId);

        return ResponseEntity.ok(ApiResponse.success(version));
    }

    @Operation(summary = "更新版本", description = "更新插件版本信息")
    @PutMapping("/{id}/versions/{versionId}")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginVersionResponse>> updatePluginVersion(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Parameter(description = "版本ID") @PathVariable Long versionId,
            @Valid @RequestBody UpdatePluginVersionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("更新插件版本: pluginId={}, versionId={}, userId={}", id, versionId, userDetails.getUsername());

        PluginManagementService.UpdatePluginVersionRequest updateRequest =
                new PluginManagementService.UpdatePluginVersionRequest();
        updateRequest.setTitle(request.getTitle());
        updateRequest.setDescription(request.getDescription());
        updateRequest.setReleaseNotes(request.getReleaseNotes());
        updateRequest.setMinCompatibleVersion(request.getMinCompatibleVersion());
        updateRequest.setIsStable(request.getIsStable());
        updateRequest.setVersionFiles(request.getVersionFiles());
        updateRequest.setDependencies(request.getDependencies());
        updateRequest.setConfigTemplate(request.getConfigTemplate());

        PluginManagementService.PluginVersionResponse version = pluginManagementService.updatePluginVersion(
                getUserId(userDetails), id, versionId, updateRequest);

        return ResponseEntity.ok(ApiResponse.success(version));
    }

    @Operation(summary = "删除版本", description = "删除指定的插件版本")
    @DeleteMapping("/{id}/versions/{versionId}")
    public ResponseEntity<ApiResponse<String>> deletePluginVersion(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Parameter(description = "版本ID") @PathVariable Long versionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("删除插件版本: pluginId={}, versionId={}, userId={}", id, versionId, userDetails.getUsername());

        pluginManagementService.deletePluginVersion(getUserId(userDetails), id, versionId);

        return ResponseEntity.ok(ApiResponse.success("插件版本删除成功"));
    }

    @Operation(summary = "设置最新版本", description = "将指定版本设置为最新版本")
    @PostMapping("/{id}/versions/{versionId}/set-latest")
    public ResponseEntity<ApiResponse<String>> setPluginLatestVersion(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Parameter(description = "版本ID") @PathVariable Long versionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("设置插件最新版本: pluginId={}, versionId={}, userId={}", id, versionId, userDetails.getUsername());

        pluginManagementService.setPluginLatestVersion(getUserId(userDetails), id, versionId);

        return ResponseEntity.ok(ApiResponse.success("设置最新版本成功"));
    }

    @Operation(summary = "获取插件市场列表", description = "从插件市场获取插件列表")
    @GetMapping("/market-list")
    public ResponseEntity<ApiResponse<PageResponse<PluginManagementService.PluginResponse>>> getPluginMarketList(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("获取插件市场列表: category={}, keyword={}, sortBy={}", category, keyword, sortBy);

        PluginManagementService.MarketPluginQueryRequest queryRequest =
                new PluginManagementService.MarketPluginQueryRequest();
        queryRequest.setCategory(category);
        queryRequest.setKeyword(keyword);
        queryRequest.setSortBy(sortBy);

        PageResponse<PluginManagementService.PluginResponse> plugins =
                pluginManagementService.getPluginMarketList(queryRequest, pageable);

        return ResponseEntity.ok(ApiResponse.success(plugins));
    }

    @Operation(summary = "评价插件", description = "对插件进行评价")
    @PostMapping("/{id}/rate")
    public ResponseEntity<ApiResponse<String>> ratePlugin(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @Valid @RequestBody RatePluginRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("评价插件: id={}, rating={}, userId={}", id, request.getRating(), userDetails.getUsername());
        
        pluginManagementService.ratePlugin(getUserId(userDetails), id, request.getRating(), request.getComment());
        
        return ResponseEntity.ok(ApiResponse.success("插件评价成功"));
    }

    @Operation(summary = "获取插件评价", description = "获取插件的评价列表")
    @GetMapping("/{id}/ratings")
    public ResponseEntity<ApiResponse<PageResponse<PluginManagementService.PluginRatingResponse>>> getPluginRatings(
            @Parameter(description = "插件ID") @PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("获取插件评价: id={}", id);
        
        PageResponse<PluginManagementService.PluginRatingResponse> ratings = 
                pluginManagementService.getPluginRatings(id, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(ratings));
    }

    @Operation(summary = "获取插件统计", description = "获取插件的统计信息")
    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginStatsResponse>> getPluginStats(
            @Parameter(description = "插件ID") @PathVariable Long id) {
        
        log.info("获取插件统计: id={}", id);
        
        PluginManagementService.PluginStatsResponse stats = pluginManagementService.getPluginStats(id);
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "获取已安装插件", description = "获取用户已安装的插件列表")
    @GetMapping("/installed")
    public ResponseEntity<ApiResponse<PageResponse<PluginManagementService.PluginInstallationResponse>>> getInstalledPlugins(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取已安装插件: userId={}", userDetails.getUsername());
        
        PageResponse<PluginManagementService.PluginInstallationResponse> installations = 
                pluginManagementService.getInstalledPlugins(getUserId(userDetails), pageable);
        
        return ResponseEntity.ok(ApiResponse.success(installations));
    }

    @Operation(summary = "获取用户插件", description = "获取用户创建的插件列表")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<PluginManagementService.PluginResponse>>> getUserPlugins(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取用户插件: userId={}", userDetails.getUsername());
        
        PageResponse<PluginManagementService.PluginResponse> plugins = 
                pluginManagementService.getUserPlugins(getUserId(userDetails), pageable);
        
        return ResponseEntity.ok(ApiResponse.success(plugins));
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

    // 请求DTO类定义
    public static class RegisterPluginRequest {
        @NotBlank(message = "插件名称不能为空")
        private String name;
        private String description;
        private String version;
        private String pluginType;
        private String category;
        private java.util.List<String> tags;
        private String iconUrl;
        private String openapi;
        private String aiPlugin;
        private String serviceUrl;
        private String serviceToken;
        private Map<String, Object> pluginConfig;
        private java.util.List<String> dependencies;
        private java.util.List<String> permissions;
        private String documentationUrl;
        private String supportUrl;
        private String license;
        private boolean isPrivate;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getPluginType() { return pluginType; }
        public void setPluginType(String pluginType) { this.pluginType = pluginType; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public java.util.List<String> getTags() { return tags; }
        public void setTags(java.util.List<String> tags) { this.tags = tags; }
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        public String getOpenapi() { return openapi; }
        public void setOpenapi(String openapi) { this.openapi = openapi; }
        public String getAiPlugin() { return aiPlugin; }
        public void setAiPlugin(String aiPlugin) { this.aiPlugin = aiPlugin; }
        public String getServiceUrl() { return serviceUrl; }
        public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }
        public String getServiceToken() { return serviceToken; }
        public void setServiceToken(String serviceToken) { this.serviceToken = serviceToken; }
        public Map<String, Object> getPluginConfig() { return pluginConfig; }
        public void setPluginConfig(Map<String, Object> pluginConfig) { this.pluginConfig = pluginConfig; }
        public java.util.List<String> getDependencies() { return dependencies; }
        public void setDependencies(java.util.List<String> dependencies) { this.dependencies = dependencies; }
        public java.util.List<String> getPermissions() { return permissions; }
        public void setPermissions(java.util.List<String> permissions) { this.permissions = permissions; }
        public String getDocumentationUrl() { return documentationUrl; }
        public void setDocumentationUrl(String documentationUrl) { this.documentationUrl = documentationUrl; }
        public String getSupportUrl() { return supportUrl; }
        public void setSupportUrl(String supportUrl) { this.supportUrl = supportUrl; }
        public String getLicense() { return license; }
        public void setLicense(String license) { this.license = license; }
        public boolean isPrivate() { return isPrivate; }
        public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    }

    public static class UpdatePluginRequest {
        private String name;
        private String description;
        private String version;
        private String category;
        private java.util.List<String> tags;
        private String iconUrl;
        private String openapi;
        private String serviceUrl;
        private String serviceToken;
        private Map<String, Object> pluginConfig;
        private java.util.List<String> dependencies;
        private java.util.List<String> permissions;
        private String documentationUrl;
        private String supportUrl;
        private String license;
        private boolean isPrivate;

        // Getters and setters (省略具体实现)
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public java.util.List<String> getTags() { return tags; }
        public void setTags(java.util.List<String> tags) { this.tags = tags; }
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        public String getOpenapi() { return openapi; }
        public void setOpenapi(String openapi) { this.openapi = openapi; }
        public String getServiceUrl() { return serviceUrl; }
        public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }
        public String getServiceToken() { return serviceToken; }
        public void setServiceToken(String serviceToken) { this.serviceToken = serviceToken; }
        public Map<String, Object> getPluginConfig() { return pluginConfig; }
        public void setPluginConfig(Map<String, Object> pluginConfig) { this.pluginConfig = pluginConfig; }
        public java.util.List<String> getDependencies() { return dependencies; }
        public void setDependencies(java.util.List<String> dependencies) { this.dependencies = dependencies; }
        public java.util.List<String> getPermissions() { return permissions; }
        public void setPermissions(java.util.List<String> permissions) { this.permissions = permissions; }
        public String getDocumentationUrl() { return documentationUrl; }
        public void setDocumentationUrl(String documentationUrl) { this.documentationUrl = documentationUrl; }
        public String getSupportUrl() { return supportUrl; }
        public void setSupportUrl(String supportUrl) { this.supportUrl = supportUrl; }
        public String getLicense() { return license; }
        public void setLicense(String license) { this.license = license; }
        public boolean isPrivate() { return isPrivate; }
        public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    }

    public static class InstallPluginRequest {
        private String version;
        private Map<String, Object> installationConfig;

        // Getters and setters
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public Map<String, Object> getInstallationConfig() { return installationConfig; }
        public void setInstallationConfig(Map<String, Object> installationConfig) { this.installationConfig = installationConfig; }
    }

    public static class ExecutePluginRequest {
        private String operation;
        private String apiEndpoint;
        private String httpMethod;
        private Map<String, Object> parameters;
        private Map<String, String> headers;
        private String requestId;
        private String sessionId;
        private Long workflowId;
        private String nodeId;
        private int timeoutSeconds = 30;
        private boolean async = false;

        // Getters and setters
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public String getApiEndpoint() { return apiEndpoint; }
        public void setApiEndpoint(String apiEndpoint) { this.apiEndpoint = apiEndpoint; }
        public String getHttpMethod() { return httpMethod; }
        public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public Map<String, String> getHeaders() { return headers; }
        public void setHeaders(Map<String, String> headers) { this.headers = headers; }
        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public int getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
        public boolean isAsync() { return async; }
        public void setAsync(boolean async) { this.async = async; }
    }

    public static class RatePluginRequest {
        @NotNull(message = "评分不能为空")
        @Min(value = 1, message = "评分不能小于1")
        @Max(value = 5, message = "评分不能大于5")
        private Integer rating;

        private String comment;

        // Getters and setters
        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }

    public static class DebugPluginApiRequest {
        @NotNull(message = "API ID不能为空")
        private Long apiId;

        private String apiEndpoint;
        private String httpMethod = "POST";
        private Map<String, Object> parameters;
        private Map<String, String> headers;
        private String requestId;
        private int timeoutSeconds = 30;

        // Getters and setters
        public Long getApiId() { return apiId; }
        public void setApiId(Long apiId) { this.apiId = apiId; }
        public String getApiEndpoint() { return apiEndpoint; }
        public void setApiEndpoint(String apiEndpoint) { this.apiEndpoint = apiEndpoint; }
        public String getHttpMethod() { return httpMethod; }
        public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public Map<String, String> getHeaders() { return headers; }
        public void setHeaders(Map<String, String> headers) { this.headers = headers; }
        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        public int getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    }

    public static class DownloadPluginFromMarketRequest {
        @NotBlank(message = "市场插件ID不能为空")
        private String marketPluginId;

        private String version;
        private Map<String, Object> installationConfig;

        // Getters and setters
        public String getMarketPluginId() { return marketPluginId; }
        public void setMarketPluginId(String marketPluginId) { this.marketPluginId = marketPluginId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public Map<String, Object> getInstallationConfig() { return installationConfig; }
        public void setInstallationConfig(Map<String, Object> installationConfig) { this.installationConfig = installationConfig; }
    }

    public static class CreatePluginVersionRequest {
        @NotBlank(message = "版本号不能为空")
        private String version;

        @NotBlank(message = "版本标题不能为空")
        private String title;

        private String description;
        private String releaseNotes;
        private String minCompatibleVersion;
        private Boolean isStable = true;
        private String versionFiles;
        private String dependencies;
        private String configTemplate;

        // Getters and setters
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getReleaseNotes() { return releaseNotes; }
        public void setReleaseNotes(String releaseNotes) { this.releaseNotes = releaseNotes; }
        public String getMinCompatibleVersion() { return minCompatibleVersion; }
        public void setMinCompatibleVersion(String minCompatibleVersion) { this.minCompatibleVersion = minCompatibleVersion; }
        public Boolean getIsStable() { return isStable; }
        public void setIsStable(Boolean isStable) { this.isStable = isStable; }
        public String getVersionFiles() { return versionFiles; }
        public void setVersionFiles(String versionFiles) { this.versionFiles = versionFiles; }
        public String getDependencies() { return dependencies; }
        public void setDependencies(String dependencies) { this.dependencies = dependencies; }
        public String getConfigTemplate() { return configTemplate; }
        public void setConfigTemplate(String configTemplate) { this.configTemplate = configTemplate; }
    }

    public static class UpdatePluginVersionRequest {
        private String title;
        private String description;
        private String releaseNotes;
        private String minCompatibleVersion;
        private Boolean isStable;
        private String versionFiles;
        private String dependencies;
        private String configTemplate;

        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getReleaseNotes() { return releaseNotes; }
        public void setReleaseNotes(String releaseNotes) { this.releaseNotes = releaseNotes; }
        public String getMinCompatibleVersion() { return minCompatibleVersion; }
        public void setMinCompatibleVersion(String minCompatibleVersion) { this.minCompatibleVersion = minCompatibleVersion; }
        public Boolean getIsStable() { return isStable; }
        public void setIsStable(Boolean isStable) { this.isStable = isStable; }
        public String getVersionFiles() { return versionFiles; }
        public void setVersionFiles(String versionFiles) { this.versionFiles = versionFiles; }
        public String getDependencies() { return dependencies; }
        public void setDependencies(String dependencies) { this.dependencies = dependencies; }
        public String getConfigTemplate() { return configTemplate; }
        public void setConfigTemplate(String configTemplate) { this.configTemplate = configTemplate; }
    }
}
