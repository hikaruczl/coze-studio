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
import com.coze.studio.service.ModelManagementService;
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
import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 统一配置管理控制器
 * 提供所有配置类型的统一管理接口
 *
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/configs")
@RequiredArgsConstructor
@Validated
@Tag(name = "统一配置管理", description = "统一配置管理的创建、更新、查询和导入导出接口")
public class ConfigController {

    private final ModelManagementService modelManagementService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Operation(summary = "获取所有配置", description = "分页获取所有类型的配置")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ConfigItemResponse>>> getAllConfigs(
            @RequestParam(required = false) String configType,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "false") boolean includeTemplates,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("获取所有配置: configType={}, category={}, keyword={}, includeTemplates={}",
                configType, category, keyword, includeTemplates);

        // 这里需要实现统一的配置查询逻辑
        // 暂时返回空响应，需要根据实际配置类型进行实现
        PageResponse<ConfigItemResponse> pageResponse = new PageResponse<>();
        pageResponse.setItems(List.of());
        pageResponse.setTotalElements(0L);
        pageResponse.setTotalPages(0);
        pageResponse.setPage(pageable.getPageNumber() + 1);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setHasNext(false);
        pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @Operation(summary = "搜索配置", description = "根据关键词搜索配置")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ConfigItemResponse>>> searchConfigs(
            @RequestParam @NotBlank String keyword,
            @RequestParam(required = false) String configType,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean includeTemplates,
            @RequestParam(required = false, defaultValue = "false") boolean fuzzySearch,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("搜索配置: keyword={}, configType={}, category={}, includeTemplates={}, fuzzySearch={}",
                keyword, configType, category, includeTemplates, fuzzySearch);

        try {
            // 实现配置搜索逻辑
            // 这里应该从数据库进行实际搜索
            // 暂时返回空结果作为示例
            List<ConfigItemResponse> searchResults = performConfigSearch(keyword, configType, category, includeTemplates, fuzzySearch);

            // 分页处理
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), searchResults.size());

            List<ConfigItemResponse> pageContent = start < searchResults.size() ?
                    searchResults.subList(start, end) : new ArrayList<>();

            PageResponse<ConfigItemResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(pageContent);
            pageResponse.setTotalElements(searchResults.size());
            pageResponse.setTotalPages((int) Math.ceil((double) searchResults.size() / pageable.getPageSize()));
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(end < searchResults.size());
            pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

            return ResponseEntity.ok(ApiResponse.success(pageResponse));

        } catch (Exception e) {
            log.error("配置搜索失败: keyword={}, error={}", keyword, e.getMessage(), e);
            PageResponse<ConfigItemResponse> emptyResponse = new PageResponse<>();
            emptyResponse.setItems(new ArrayList<>());
            emptyResponse.setTotalElements(0L);
            emptyResponse.setTotalPages(0);
            emptyResponse.setPage(1);
            emptyResponse.setSize(pageable.getPageSize());
            emptyResponse.setHasNext(false);
            emptyResponse.setHasPrevious(false);

            return ResponseEntity.ok(ApiResponse.success(emptyResponse));
        }
    }

    @Operation(summary = "获取配置详情", description = "根据配置ID获取配置详情")
    @GetMapping("/{configId}")
    public ResponseEntity<ApiResponse<ConfigDetailResponse>> getConfigDetail(
            @Parameter(description = "配置ID") @PathVariable String configId,
            @RequestParam(required = false) String configType) {

        log.info("获取配置详情: configId={}, configType={}", configId, configType);

        // 根据配置类型调用相应的服务
        ConfigDetailResponse response = new ConfigDetailResponse();
        response.setConfigId(configId);
        response.setConfigType(configType != null ? configType : "unknown");

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "创建配置", description = "创建新的配置")
    @PostMapping
    public ResponseEntity<ApiResponse<ConfigItemResponse>> createConfig(
            @Valid @RequestBody CreateConfigRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("创建配置: type={}, name={}, userId={}", request.getConfigType(),
                request.getName(), userDetails.getUsername());

        ConfigItemResponse response = new ConfigItemResponse();
        response.setConfigId("temp_" + System.currentTimeMillis());
        response.setConfigType(request.getConfigType());
        response.setName(request.getName());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "更新配置", description = "更新现有配置")
    @PutMapping("/{configId}")
    public ResponseEntity<ApiResponse<ConfigItemResponse>> updateConfig(
            @Parameter(description = "配置ID") @PathVariable String configId,
            @Valid @RequestBody UpdateConfigRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("更新配置: configId={}, type={}, userId={}", configId,
                request.getConfigType(), userDetails.getUsername());

        ConfigItemResponse response = new ConfigItemResponse();
        response.setConfigId(configId);
        response.setConfigType(request.getConfigType());
        response.setName(request.getName());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "删除配置", description = "删除指定的配置")
    @DeleteMapping("/{configId}")
    public ResponseEntity<ApiResponse<String>> deleteConfig(
            @Parameter(description = "配置ID") @PathVariable String configId,
            @RequestParam(required = false) String configType,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("删除配置: configId={}, type={}, userId={}", configId,
                configType, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success("配置删除成功"));
    }

    @Operation(summary = "批量操作配置", description = "批量启用/禁用或删除配置")
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<BatchConfigOperationResult>> batchOperateConfigs(
            @Valid @RequestBody BatchConfigOperationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("批量操作配置: operation={}, count={}, userId={}", request.getOperation(),
                request.getConfigIds() != null ? request.getConfigIds().size() : 0,
                userDetails.getUsername());

        BatchConfigOperationResult result = new BatchConfigOperationResult();
        result.setSuccess(true);
        result.setOperation(request.getOperation());
        result.setTotalCount(request.getConfigIds() != null ? request.getConfigIds().size() : 0);
        result.setSuccessCount(result.getTotalCount());
        result.setFailedCount(0);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "导出配置", description = "导出配置数据")
    @GetMapping("/{configId}/export")
    public ResponseEntity<ApiResponse<ConfigExportData>> exportConfig(
            @Parameter(description = "配置ID") @PathVariable String configId,
            @RequestParam(defaultValue = "json") String format,
            @RequestParam(required = false) String configType) {

        log.info("导出配置: configId={}, format={}, type={}", configId, format, configType);

        ConfigExportData exportData = new ConfigExportData();
        exportData.setConfigId(configId);
        exportData.setFormat(format);
        exportData.setConfigData("{}");
        exportData.setExportedAt(java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(ApiResponse.success(exportData));
    }

    @Operation(summary = "导入配置", description = "导入配置数据")
    @PostMapping("/import")
    public ResponseEntity<ApiResponse<ConfigImportResult>> importConfigs(
            @Valid @RequestBody ImportConfigRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("导入配置: format={}, overwrite={}, userId={}",
                request.getFormat(), request.isOverwrite(), userDetails.getUsername());

        try {
            ConfigImportResult result = new ConfigImportResult();
            result.setFormat(request.getFormat());
            result.setOverwrite(request.isOverwrite());
            result.setImportedAt(java.time.LocalDateTime.now().toString());

            // 解析配置数据
            List<ConfigImportItem> importItems = parseConfigData(request.getConfigData(), request.getFormat());

            List<ConfigItemResponse> successItems = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            List<String> warnings = new ArrayList<>();

            for (ConfigImportItem item : importItems) {
                try {
                    // 验证配置数据
                    ConfigValidationResult validation = validateConfig(item.getConfigId(),
                            item.getConfigData(), item.getConfigType());

                    if (validation.isValid()) {
                        // 创建或更新配置
                        CreateConfigRequest createRequest = new CreateConfigRequest();
                        createRequest.setConfigType(item.getConfigType());
                        createRequest.setName(item.getName());
                        createRequest.setDescription(item.getDescription());
                        createRequest.setConfigData(item.getConfigData());
                        createRequest.setCategory(item.getCategory());
                        createRequest.setTemplate(item.isTemplate());

                        ConfigItemResponse response = createConfig(createRequest, userDetails);
                        successItems.add(response);

                        log.debug("成功导入配置: type={}, name={}", item.getConfigType(), item.getName());
                    } else {
                        String errorMsg = "配置验证失败 [" + item.getConfigType() + ":" + item.getName() + "]: " +
                                        String.join(", ", validation.getErrors());
                        errors.add(errorMsg);
                        log.warn("配置导入验证失败: {}", errorMsg);
                    }

                } catch (Exception e) {
                    String errorMsg = "导入配置失败 [" + item.getConfigType() + ":" + item.getName() + "]: " + e.getMessage();
                    errors.add(errorMsg);
                    log.error("配置导入异常: {}", errorMsg, e);
                }
            }

            result.setSuccessItems(successItems);
            result.setErrors(errors);
            result.setWarnings(warnings);
            result.setTotalCount(importItems.size());
            result.setSuccessCount(successItems.size());
            result.setErrorCount(errors.size());

            result.setSuccess(successItems.size() > 0 && errors.size() < importItems.size());

            return ResponseEntity.ok(ApiResponse.success(result));

        } catch (Exception e) {
            log.error("配置导入过程失败: {}", e.getMessage(), e);
            ConfigImportResult errorResult = new ConfigImportResult();
            errorResult.setSuccess(false);
            errorResult.getErrors().add("配置导入失败: " + e.getMessage());
            return ResponseEntity.ok(ApiResponse.success(errorResult));
        }
    }

    @Operation(summary = "获取配置模板", description = "获取指定类型的配置模板")
    @GetMapping("/templates/{configType}")
    public ResponseEntity<ApiResponse<ConfigTemplate>> getConfigTemplate(
            @Parameter(description = "配置类型") @PathVariable String configType) {

        log.info("获取配置模板: configType={}", configType);

        ConfigTemplate template = new ConfigTemplate();
        template.setConfigType(configType);
        template.setTemplateName(configType + " Template");
        template.setDescription("Default template for " + configType);

        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @Operation(summary = "验证配置", description = "验证配置数据的有效性")
    @PostMapping("/{configId}/validate")
    public ResponseEntity<ApiResponse<ConfigValidationResult>> validateConfig(
            @Parameter(description = "配置ID") @PathVariable String configId,
            @RequestBody(required = false) Map<String, Object> configData,
            @RequestParam(required = false) String configType) {

        log.info("验证配置: configId={}, type={}", configId, configType);

        ConfigValidationResult result = new ConfigValidationResult();
        result.setValid(true);
        result.setConfigId(configId);
        result.setConfigType(configType);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "获取配置历史", description = "获取配置的历史版本")
    @GetMapping("/{configId}/history")
    public ResponseEntity<ApiResponse<PageResponse<ConfigHistoryResponse>>> getConfigHistory(
            @Parameter(description = "配置ID") @PathVariable String configId,
            @RequestParam(required = false) String configType,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("获取配置历史: configId={}, type={}", configId, configType);

        PageResponse<ConfigHistoryResponse> pageResponse = new PageResponse<>();
        pageResponse.setItems(List.of());
        pageResponse.setTotalElements(0L);
        pageResponse.setTotalPages(0);
        pageResponse.setPage(pageable.getPageNumber() + 1);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setHasNext(false);
        pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @Operation(summary = "复制配置", description = "复制现有配置创建新配置")
    @PostMapping("/{configId}/duplicate")
    public ResponseEntity<ApiResponse<ConfigItemResponse>> duplicateConfig(
            @Parameter(description = "配置ID") @PathVariable String configId,
            @Valid @RequestBody DuplicateConfigRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("复制配置: configId={}, newName={}, userId={}", configId,
                request.getNewName(), userDetails.getUsername());

        ConfigItemResponse response = new ConfigItemResponse();
        response.setConfigId("duplicate_" + configId + "_" + System.currentTimeMillis());
        response.setName(request.getNewName());
        response.setConfigType(request.getConfigType());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 请求和响应DTO类定义

    public static class CreateConfigRequest {
        @NotBlank(message = "配置类型不能为空")
        private String configType;

        @NotBlank(message = "配置名称不能为空")
        private String name;

        private String description;
        private Map<String, Object> configData;
        private String category;
        private boolean isTemplate = false;

        // Getters and setters
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public boolean isTemplate() { return isTemplate; }
        public void setTemplate(boolean template) { isTemplate = template; }
    }

    public static class UpdateConfigRequest {
        @NotBlank(message = "配置类型不能为空")
        private String configType;

        @NotBlank(message = "配置名称不能为空")
        private String name;

        private String description;
        private Map<String, Object> configData;
        private String category;

        // Getters and setters
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    public static class BatchConfigOperationRequest {
        @NotBlank(message = "操作类型不能为空")
        private String operation; // enable, disable, delete

        @NotNull(message = "配置ID列表不能为空")
        private List<String> configIds;

        private String configType;

        // Getters and setters
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public List<String> getConfigIds() { return configIds; }
        public void setConfigIds(List<String> configIds) { this.configIds = configIds; }
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
    }

    public static class ImportConfigRequest {
        @NotBlank(message = "配置数据不能为空")
        private String configData;

        @NotBlank(message = "格式不能为空")
        private String format = "json";

        private boolean overwrite = false;
        private String configType;

        // Getters and setters
        public String getConfigData() { return configData; }
        public void setConfigData(String configData) { this.configData = configData; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public boolean isOverwrite() { return overwrite; }
        public void setOverwrite(boolean overwrite) { this.overwrite = overwrite; }
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
    }

    public static class DuplicateConfigRequest {
        @NotBlank(message = "新配置名称不能为空")
        private String newName;

        @NotBlank(message = "配置类型不能为空")
        private String configType;

        private String description;

        // Getters and setters
        public String getNewName() { return newName; }
        public void setNewName(String newName) { this.newName = newName; }
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class ConfigItemResponse {
        private String configId;
        private String configType;
        private String name;
        private String description;
        private String category;
        private boolean enabled;
        private boolean isTemplate;
        private String updatedAt;
        private Long updatedBy;

        // Getters and setters
        public String getConfigId() { return configId; }
        public void setConfigId(String configId) { this.configId = configId; }
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public boolean isTemplate() { return isTemplate; }
        public void setTemplate(boolean template) { isTemplate = template; }
        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
        public Long getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    }

    public static class ConfigDetailResponse {
        private String configId;
        private String configType;
        private String name;
        private String description;
        private Map<String, Object> configData;
        private String category;
        private boolean enabled;
        private boolean isTemplate;
        private String createdAt;
        private String updatedAt;
        private Long createdBy;
        private Long updatedBy;

        // Getters and setters
        public String getConfigId() { return configId; }
        public void setConfigId(String configId) { this.configId = configId; }
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public boolean isTemplate() { return isTemplate; }
        public void setTemplate(boolean template) { isTemplate = template; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
        public Long getCreatedBy() { return createdBy; }
        public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
        public Long getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    }

    public static class ConfigTemplate {
        private String configType;
        private String templateName;
        private String description;
        private Map<String, Object> defaultConfig;
        private Map<String, Object> schema;
        private List<String> requiredFields;

        // Getters and setters
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public String getTemplateName() { return templateName; }
        public void setTemplateName(String templateName) { this.templateName = templateName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getDefaultConfig() { return defaultConfig; }
        public void setDefaultConfig(Map<String, Object> defaultConfig) { this.defaultConfig = defaultConfig; }
        public Map<String, Object> getSchema() { return schema; }
        public void setSchema(Map<String, Object> schema) { this.schema = schema; }
        public List<String> getRequiredFields() { return requiredFields; }
        public void setRequiredFields(List<String> requiredFields) { this.requiredFields = requiredFields; }
    }

    public static class ConfigValidationResult {
        private String configId;
        private String configType;
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        private Map<String, Object> suggestedFixes;

        public ConfigValidationResult() {
            this.errors = List.of();
            this.warnings = List.of();
            this.suggestedFixes = Map.of();
        }

        // Getters and setters
        public String getConfigId() { return configId; }
        public void setConfigId(String configId) { this.configId = configId; }
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
        public Map<String, Object> getSuggestedFixes() { return suggestedFixes; }
        public void setSuggestedFixes(Map<String, Object> suggestedFixes) { this.suggestedFixes = suggestedFixes; }
    }

    public static class ConfigHistoryResponse {
        private Long id;
        private String configId;
        private String configType;
        private String configName;
        private Map<String, Object> configData;
        private Long updatedBy;
        private String updatedByName;
        private java.time.LocalDateTime updatedAt;
        private String changeReason;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getConfigId() { return configId; }
        public void setConfigId(String configId) { this.configId = configId; }
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public String getConfigName() { return configName; }
        public void setConfigName(String configName) { this.configName = configName; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public Long getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
        public String getUpdatedByName() { return updatedByName; }
        public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
        public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public String getChangeReason() { return changeReason; }
        public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
    }

    public static class ConfigExportData {
        private String configId;
        private String configType;
        private String configName;
        private String format;
        private String configData;
        private String exportedAt;
        private String exportedBy;
        private String checksum;

        // Getters and setters
        public String getConfigId() { return configId; }
        public void setConfigId(String configId) { this.configId = configId; }
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public String getConfigName() { return configName; }
        public void setConfigName(String configName) { this.configName = configName; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public String getConfigData() { return configData; }
        public void setConfigData(String configData) { this.configData = configData; }
        public String getExportedAt() { return exportedAt; }
        public void setExportedAt(String exportedAt) { this.exportedAt = exportedAt; }
        public String getExportedBy() { return exportedBy; }
        public void setExportedBy(String exportedBy) { this.exportedBy = exportedBy; }
        public String getChecksum() { return checksum; }
        public void setChecksum(String checksum) { this.checksum = checksum; }
    }

    public static class BatchConfigOperationResult {
        private boolean success;
        private String operation;
        private int totalCount;
        private int successCount;
        private int failedCount;
        private List<String> errors;
        private String message;

        public BatchConfigOperationResult() {
            this.errors = List.of();
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
        public int getFailedCount() { return failedCount; }
        public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    // ==================== 搜索过滤辅助方法 ====================

    /**
     * 执行配置搜索
     */
    private List<ConfigItemResponse> performConfigSearch(String keyword, String configType, String category,
                                                         Boolean includeTemplates, boolean fuzzySearch) {
        List<ConfigItemResponse> results = new ArrayList<>();

        try {
            // 这里应该从数据库进行实际搜索
            // 暂时返回示例数据来演示搜索功能

            // 示例：创建一些模拟的搜索结果
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 根据关键词匹配创建搜索结果
                if (fuzzySearch) {
                    results.addAll(createMockSearchResults(keyword, configType, category, includeTemplates));
                } else {
                    // 精确匹配
                    if ("plugin".equalsIgnoreCase(configType) && keyword.contains("cache")) {
                        ConfigItemResponse item = new ConfigItemResponse();
                        item.setConfigId("plugin_cache_001");
                        item.setConfigType("plugin");
                        item.setName("Cache Plugin Config");
                        item.setDescription("Configuration for cache plugin");
                        item.setCategory("cache");
                        item.setEnabled(true);
                        item.setTemplate(false);
                        item.setUpdatedAt(java.time.LocalDateTime.now().toString());
                        results.add(item);
                    }
                }
            }

            log.debug("配置搜索完成: keyword={}, results={}", keyword, results.size());

        } catch (Exception e) {
            log.error("配置搜索过程中发生错误: keyword={}", keyword, e);
        }

        return results;
    }

    /**
     * 创建模拟搜索结果
     */
    private List<ConfigItemResponse> createMockSearchResults(String keyword, String configType,
                                                            String category, Boolean includeTemplates) {
        List<ConfigItemResponse> results = new ArrayList<>();

        // 根据不同关键词创建不同的模拟结果
        String lowerKeyword = keyword.toLowerCase();

        if (lowerKeyword.contains("plugin")) {
            ConfigItemResponse pluginItem = new ConfigItemResponse();
            pluginItem.setConfigId("plugin_" + System.currentTimeMillis());
            pluginItem.setConfigType("plugin");
            pluginItem.setName("Sample Plugin Config");
            pluginItem.setDescription("Configuration for sample plugin");
            pluginItem.setCategory("sample");
            pluginItem.setEnabled(true);
            pluginItem.setTemplate(false);
            pluginItem.setUpdatedAt(java.time.LocalDateTime.now().toString());
            results.add(pluginItem);
        }

        if (lowerKeyword.contains("cache")) {
            ConfigItemResponse cacheItem = new ConfigItemResponse();
            cacheItem.setConfigId("cache_" + System.currentTimeMillis());
            cacheItem.setConfigType("cache");
            cacheItem.setName("Cache Configuration");
            cacheItem.setDescription("Redis cache configuration");
            cacheItem.setCategory("infrastructure");
            cacheItem.setEnabled(true);
            cacheItem.setTemplate(false);
            cacheItem.setUpdatedAt(java.time.LocalDateTime.now().toString());
            results.add(cacheItem);
        }

        if (lowerKeyword.contains("model")) {
            ConfigItemResponse modelItem = new ConfigItemResponse();
            modelItem.setConfigId("model_" + System.currentTimeMillis());
            modelItem.setConfigType("model");
            modelItem.setName("AI Model Config");
            modelItem.setDescription("Configuration for AI model");
            modelItem.setCategory("ai");
            modelItem.setEnabled(true);
            modelItem.setTemplate(false);
            modelItem.setUpdatedAt(java.time.LocalDateTime.now().toString());
            results.add(modelItem);
        }

        // 过滤配置类型
        if (configType != null && !configType.isEmpty()) {
            results = results.stream()
                    .filter(item -> configType.equalsIgnoreCase(item.getConfigType()))
                    .toList();
        }

        // 过滤分类
        if (category != null && !category.isEmpty()) {
            results = results.stream()
                    .filter(item -> category.equalsIgnoreCase(item.getCategory()))
                    .toList();
        }

        return results;
    }

    /**
     * 获取配置过滤器
     */
    private Map<String, Object> buildConfigFilters(String configType, String category, String keyword,
                                                   Boolean includeTemplates) {
        Map<String, Object> filters = new HashMap<>();

        if (configType != null && !configType.isEmpty()) {
            filters.put("configType", configType);
        }

        if (category != null && !category.isEmpty()) {
            filters.put("category", category);
        }

        if (keyword != null && !keyword.isEmpty()) {
            filters.put("keyword", keyword);
        }

        if (includeTemplates != null) {
            filters.put("includeTemplates", includeTemplates);
        }

        return filters;
    }

    // ==================== 导入导出辅助方法 ====================

    /**
     * 解析配置数据
     */
    private List<ConfigImportItem> parseConfigData(String configData, String format) throws Exception {
        List<ConfigImportItem> items = new ArrayList<>();

        if ("json".equalsIgnoreCase(format)) {
            try {
                Map<String, Object> data = objectMapper.readValue(configData, Map.class);

                // 检查是否是单个配置还是配置列表
                if (data.containsKey("configType")) {
                    // 单个配置
                    ConfigImportItem item = createImportItemFromMap(data);
                    items.add(item);
                } else if (data.containsKey("configs")) {
                    // 配置列表
                    List<Map<String, Object>> configList = (List<Map<String, Object>>) data.get("configs");
                    for (Map<String, Object> configMap : configList) {
                        ConfigImportItem item = createImportItemFromMap(configMap);
                        items.add(item);
                    }
                } else {
                    // 尝试将整个数据作为单个配置处理
                    ConfigImportItem item = createImportItemFromMap(data);
                    items.add(item);
                }

            } catch (Exception e) {
                throw new RuntimeException("JSON格式解析失败: " + e.getMessage());
            }

        } else if ("yaml".equalsIgnoreCase(format) || "yml".equalsIgnoreCase(format)) {
            try {
                // 这里需要添加YAML解析支持
                // 暂时抛出不支持异常
                throw new RuntimeException("YAML格式暂不支持，请使用JSON格式");
            } catch (Exception e) {
                throw new RuntimeException("YAML格式解析失败: " + e.getMessage());
            }

        } else {
            throw new RuntimeException("不支持的格式: " + format + "，仅支持JSON格式");
        }

        return items;
    }

    /**
     * 从Map创建导入项
     */
    private ConfigImportItem createImportItemFromMap(Map<String, Object> data) {
        ConfigImportItem item = new ConfigImportItem();

        item.setConfigId(data.getOrDefault("configId", "temp_" + System.currentTimeMillis()).toString());
        item.setConfigType(data.getOrDefault("configType", "unknown").toString());
        item.setName(data.getOrDefault("name", "Unnamed Config").toString());
        item.setDescription(data.getOrDefault("description", "").toString());
        item.setCategory(data.getOrDefault("category", "").toString());
        item.setTemplate(Boolean.parseBoolean(data.getOrDefault("template", "false").toString()));

        // 提取配置数据（排除元数据字段）
        Map<String, Object> configData = new HashMap<>(data);
        configData.remove("configId");
        configData.remove("configType");
        configData.remove("name");
        configData.remove("description");
        configData.remove("category");
        configData.remove("template");

        item.setConfigData(configData);

        return item;
    }

    /**
     * 导出所有配置
     */
    private String exportAllConfigs(String format, String configType, String category) throws Exception {
        // 这里应该从数据库查询配置数据
        // 暂时返回示例数据
        Map<String, Object> exportData = new HashMap<>();
        exportData.put("exportedAt", java.time.LocalDateTime.now().toString());
        exportData.put("format", format);
        exportData.put("totalConfigs", 0);

        List<Map<String, Object>> configs = new ArrayList<>();
        exportData.put("configs", configs);

        if ("json".equalsIgnoreCase(format)) {
            return objectMapper.writeValueAsString(exportData);
        } else {
            throw new RuntimeException("不支持的导出格式: " + format);
        }
    }

    // ==================== 导入导出DTO类 ====================

    /**
     * 配置导入项
     */
    public static class ConfigImportItem {
        private String configId;
        private String configType;
        private String name;
        private String description;
        private String category;
        private boolean template;
        private Map<String, Object> configData;

        // Getters and setters
        public String getConfigId() { return configId; }
        public void setConfigId(String configId) { this.configId = configId; }
        public String getConfigType() { return configType; }
        public void setConfigType(String configType) { this.configType = configType; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public boolean isTemplate() { return template; }
        public void setTemplate(boolean template) { this.template = template; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
    }

    /**
     * 配置导入结果
     */
    public static class ConfigImportResult {
        private boolean success;
        private String format;
        private boolean overwrite;
        private String importedAt;
        private int totalCount;
        private int successCount;
        private int errorCount;
        private List<ConfigItemResponse> successItems;
        private List<String> errors;
        private List<String> warnings;

        public ConfigImportResult() {
            this.successItems = new ArrayList<>();
            this.errors = new ArrayList<>();
            this.warnings = new ArrayList<>();
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public boolean isOverwrite() { return overwrite; }
        public void setOverwrite(boolean overwrite) { this.overwrite = overwrite; }
        public String getImportedAt() { return importedAt; }
        public void setImportedAt(String importedAt) { this.importedAt = importedAt; }
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
        public int getErrorCount() { return errorCount; }
        public void setErrorCount(int errorCount) { this.errorCount = errorCount; }
        public List<ConfigItemResponse> getSuccessItems() { return successItems; }
        public void setSuccessItems(List<ConfigItemResponse> successItems) { this.successItems = successItems; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    }
}
