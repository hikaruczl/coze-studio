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
import com.coze.studio.security.UserPrincipal;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 插件配置管理控制器
 * 提供插件配置的统一管理功能
 *
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/plugin-configs")
@RequiredArgsConstructor
@Validated
@Tag(name = "插件配置管理", description = "插件配置的创建、更新、验证和管理接口")
public class PluginConfigController {

    private final PluginManagementService pluginManagementService;

    @Operation(summary = "获取插件配置", description = "获取指定插件的配置信息")
    @GetMapping("/{pluginId}")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginConfigResponse>> getPluginConfig(
            @Parameter(description = "插件ID") @PathVariable Long pluginId) {

        log.info("获取插件配置: pluginId={}", pluginId);

        PluginManagementService.PluginConfigResponse config = pluginManagementService.getPluginConfig(pluginId);

        return ResponseEntity.ok(ApiResponse.success(config));
    }

    @Operation(summary = "更新插件配置", description = "更新指定插件的配置信息")
    @PutMapping("/{pluginId}")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginConfigResponse>> updatePluginConfig(
            @Parameter(description = "插件ID") @PathVariable Long pluginId,
            @Valid @RequestBody UpdatePluginConfigRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("更新插件配置: pluginId={}, userId={}", pluginId, userDetails.getUsername());

        PluginManagementService.UpdatePluginConfigRequest updateRequest =
                new PluginManagementService.UpdatePluginConfigRequest();
        updateRequest.setConfigData(request.getConfigData());
        updateRequest.setEnvironment(request.getEnvironment());
        updateRequest.setVersion(request.getVersion());

        PluginManagementService.PluginConfigResponse config = pluginManagementService.updatePluginConfig(
                getUserId(userDetails), pluginId, updateRequest);

        return ResponseEntity.ok(ApiResponse.success(config));
    }

    @Operation(summary = "验证插件配置", description = "验证插件配置的有效性")
    @PostMapping("/{pluginId}/validate")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginConfigValidationResult>> validatePluginConfig(
            @Parameter(description = "插件ID") @PathVariable Long pluginId,
            @RequestBody(required = false) Map<String, Object> configData) {

        log.info("验证插件配置: pluginId={}", pluginId);

        PluginManagementService.PluginConfigValidationResult result =
                pluginManagementService.validatePluginConfig(pluginId, configData);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "测试插件配置", description = "使用配置数据测试插件功能")
    @PostMapping("/{pluginId}/test")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginConfigTestResult>> testPluginConfig(
            @Parameter(description = "插件ID") @PathVariable Long pluginId,
            @RequestBody(required = false) Map<String, Object> configData,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("测试插件配置: pluginId={}, userId={}", pluginId, userDetails.getUsername());

        PluginManagementService.PluginConfigTestResult result =
                pluginManagementService.testPluginConfig(getUserId(userDetails), pluginId, configData);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "获取配置历史", description = "获取插件配置的历史版本")
    @GetMapping("/{pluginId}/history")
    public ResponseEntity<ApiResponse<PageResponse<PluginManagementService.PluginConfigHistoryResponse>>> getPluginConfigHistory(
            @Parameter(description = "插件ID") @PathVariable Long pluginId,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("获取插件配置历史: pluginId={}", pluginId);

        PageResponse<PluginManagementService.PluginConfigHistoryResponse> history =
                pluginManagementService.getPluginConfigHistory(pluginId, pageable);

        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @Operation(summary = "恢复配置版本", description = "恢复到指定的配置版本")
    @PostMapping("/{pluginId}/restore/{versionId}")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginConfigResponse>> restorePluginConfigVersion(
            @Parameter(description = "插件ID") @PathVariable Long pluginId,
            @Parameter(description = "版本ID") @PathVariable Long versionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("恢复插件配置版本: pluginId={}, versionId={}, userId={}", pluginId, versionId, userDetails.getUsername());

        PluginManagementService.PluginConfigResponse config = pluginManagementService.restorePluginConfigVersion(
                getUserId(userDetails), pluginId, versionId);

        return ResponseEntity.ok(ApiResponse.success(config));
    }

    @Operation(summary = "获取配置模板", description = "获取插件的配置模板")
    @GetMapping("/templates/{pluginType}")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginConfigTemplate>> getPluginConfigTemplate(
            @Parameter(description = "插件类型") @PathVariable String pluginType) {

        log.info("获取插件配置模板: pluginType={}", pluginType);

        PluginManagementService.PluginConfigTemplate template =
                pluginManagementService.getPluginConfigTemplate(pluginType);

        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @Operation(summary = "批量更新配置", description = "批量更新多个插件的配置")
    @PostMapping("/batch-update")
    public ResponseEntity<ApiResponse<List<PluginManagementService.PluginConfigResponse>>> batchUpdatePluginConfigs(
            @Valid @RequestBody BatchUpdatePluginConfigRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("批量更新插件配置: userId={}, count={}", userDetails.getUsername(),
                request.getConfigs() != null ? request.getConfigs().size() : 0);

        List<PluginManagementService.PluginConfigResponse> results =
                pluginManagementService.batchUpdatePluginConfigs(getUserId(userDetails), request.getConfigs());

        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @Operation(summary = "导出配置", description = "导出插件配置数据")
    @GetMapping("/{pluginId}/export")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginConfigExportData>> exportPluginConfig(
            @Parameter(description = "插件ID") @PathVariable Long pluginId,
            @RequestParam(defaultValue = "json") String format) {

        log.info("导出插件配置: pluginId={}, format={}", pluginId, format);

        PluginManagementService.PluginConfigExportData exportData =
                pluginManagementService.exportPluginConfig(pluginId, format);

        return ResponseEntity.ok(ApiResponse.success(exportData));
    }

    @Operation(summary = "导入配置", description = "导入插件配置数据")
    @PostMapping("/{pluginId}/import")
    public ResponseEntity<ApiResponse<PluginManagementService.PluginConfigResponse>> importPluginConfig(
            @Parameter(description = "插件ID") @PathVariable Long pluginId,
            @Valid @RequestBody ImportPluginConfigRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("导入插件配置: pluginId={}, userId={}", pluginId, userDetails.getUsername());

        PluginManagementService.ImportPluginConfigRequest importRequest =
                new PluginManagementService.ImportPluginConfigRequest();
        importRequest.setConfigData(request.getConfigData());
        importRequest.setFormat(request.getFormat());
        importRequest.setOverwrite(request.isOverwrite());

        PluginManagementService.PluginConfigResponse config = pluginManagementService.importPluginConfig(
                getUserId(userDetails), pluginId, importRequest);

        return ResponseEntity.ok(ApiResponse.success(config));
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
    public static class UpdatePluginConfigRequest {
        @NotNull(message = "配置数据不能为空")
        private Map<String, Object> configData;

        private String environment = "default";
        private String version;

        // Getters and setters
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getEnvironment() { return environment; }
        public void setEnvironment(String environment) { this.environment = environment; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }

    public static class BatchUpdatePluginConfigRequest {
        @NotNull(message = "配置列表不能为空")
        private List<PluginConfigUpdateItem> configs;

        // Getters and setters
        public List<PluginConfigUpdateItem> getConfigs() { return configs; }
        public void setConfigs(List<PluginConfigUpdateItem> configs) { this.configs = configs; }

        public static class PluginConfigUpdateItem {
            @NotNull(message = "插件ID不能为空")
            private Long pluginId;
            private Map<String, Object> configData;
            private String environment;

            // Getters and setters
            public Long getPluginId() { return pluginId; }
            public void setPluginId(Long pluginId) { this.pluginId = pluginId; }
            public Map<String, Object> getConfigData() { return configData; }
            public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
            public String getEnvironment() { return environment; }
            public void setEnvironment(String environment) { this.environment = environment; }
        }
    }

    public static class ImportPluginConfigRequest {
        @NotNull(message = "配置数据不能为空")
        private String configData;

        @NotBlank(message = "格式不能为空")
        private String format = "json";

        private boolean overwrite = false;

        // Getters and setters
        public String getConfigData() { return configData; }
        public void setConfigData(String configData) { this.configData = configData; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public boolean isOverwrite() { return overwrite; }
        public void setOverwrite(boolean overwrite) { this.overwrite = overwrite; }
    }
}
